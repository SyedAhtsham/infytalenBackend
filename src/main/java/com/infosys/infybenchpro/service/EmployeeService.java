package com.infosys.infybenchpro.service;

import com.infosys.infybenchpro.dto.BulkImportResult;
import com.infosys.infybenchpro.dto.BulkImportRowDto;
import com.infosys.infybenchpro.entity.AppUser;
import com.infosys.infybenchpro.entity.Employee;
import com.infosys.infybenchpro.entity.EmployeeStatus;
import com.infosys.infybenchpro.entity.Role;
import com.infosys.infybenchpro.repository.AppUserRepository;
import com.infosys.infybenchpro.repository.BenchSpocMappingRepository;
import com.infosys.infybenchpro.repository.EmployeeGroupRepository;
import com.infosys.infybenchpro.repository.EmployeeRepository;
import com.infosys.infybenchpro.repository.TaskRepository;
import com.infosys.infybenchpro.repository.UserTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepo;
    private final TaskRepository taskRepo;
    private final UserTaskRepository userTaskRepo;
    private final EmployeeGroupRepository groupRepo;
    private final BenchSpocMappingRepository benchSpocMappingRepo;
    private final AppUserRepository appUserRepo;
    private final PasswordEncoder passwordEncoder;

    public List<Employee> findAll() {
        return employeeRepo.findAll();
    }

    public Employee findById(Long id) {
        return employeeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found: " + id));
    }

    @Transactional
    public Employee save(Employee employee) {
        boolean isNew = (employee.getId() == null);

        // Auto-assign managerId based on benchSpoc mapping
        if (employee.getBenchSpoc() != null && !employee.getBenchSpoc().isBlank()) {
            benchSpocMappingRepo.findByBenchSpoc(employee.getBenchSpoc())
                .ifPresent(mapping -> employee.setManagerId(mapping.getManagerId()));
        }

        // Ensure bench anchor is consistent with current status.
        // setDaysOnBench() sets benchStartDate unconditionally (because Jackson field order
        // is undefined), so we clear it here when the employee is not actually on bench.
        recalibrateBenchStartDate(employee);

        Employee saved = employeeRepo.save(employee);

        // Auto-create an AppUser account for new employees
        if (isNew && saved.getEmail() != null && !saved.getEmail().isBlank()) {
            createAccountIfAbsent(saved);
        }

        return saved;
    }

    @Transactional
    public void delete(Long id) {
        // Remove from task assignments
        taskRepo.findAll().forEach(task -> {
            if (task.getAssignedEmployeeIds().contains(id)) {
                task.getAssignedEmployeeIds().remove(id);
                taskRepo.save(task);
            }
        });
        // Remove user tasks
        userTaskRepo.findByEmployeeId(id).forEach(userTaskRepo::delete);
        // Remove from groups
        groupRepo.findAll().forEach(group -> {
            if (group.getEmployeeIds().contains(id)) {
                group.getEmployeeIds().remove(id);
                groupRepo.save(group);
            }
        });
        employeeRepo.deleteById(id);
    }

    @Transactional
    public BulkImportResult bulkImport(List<BulkImportRowDto> rows) {
        int added = 0, updated = 0;

        for (BulkImportRowDto row : rows) {
            var existing = employeeRepo.findByEmployeeId(row.getEmployeeId());
            if (existing.isPresent()) {
                Employee e = existing.get();
                if (row.getName() != null && !row.getName().isBlank())     e.setName(row.getName());
                if (row.getEmail() != null && !row.getEmail().isBlank())   e.setEmail(row.getEmail());
                if (row.getJobLevel() != null)                              e.setJobLevel(row.getJobLevel());
                if (row.getStatus() != null)                                e.setStatus(EmployeeStatus.from(row.getStatus()));
                e.setRpl(row.getRpl());
                e.setEmployeeDU(row.getEmployeeDU());
                e.setDaysOnBench(row.getDaysOnBench());
                e.setNonProdDays(row.getNonProdDays());
                e.setBaseCity(row.getBaseCity());
                e.setBaseCountry(row.getBaseCountry());
                e.setCurrentCity(row.getCurrentCity());
                e.setCurrentCountry(row.getCurrentCountry());
                if (row.getTripCity() != null)  e.setTripCity(row.getTripCity());
                if (row.getTripState() != null) e.setTripState(row.getTripState());
                // Re-resolve managerId if benchSpoc is present
                if (e.getBenchSpoc() != null) {
                    benchSpocMappingRepo.findByBenchSpoc(e.getBenchSpoc())
                        .ifPresent(m -> e.setManagerId(m.getManagerId()));
                }
                // Reconcile benchStartDate with final status after all fields are set
                recalibrateBenchStartDate(e);
                employeeRepo.save(e);
                updated++;
            } else {
                Employee e = new Employee();
                e.setEmployeeId(row.getEmployeeId());
                e.setName(row.getName() != null ? row.getName() : "");
                e.setEmail(row.getEmail() != null ? row.getEmail() : "");
                e.setJobLevel(row.getJobLevel() != null ? row.getJobLevel() : "");
                e.setStatus(row.getStatus() != null ? EmployeeStatus.from(row.getStatus()) : EmployeeStatus.BENCH);
                e.setLocation(buildLocation(row));
                e.setRpl(row.getRpl());
                e.setEmployeeDU(row.getEmployeeDU());
                e.setDaysOnBench(row.getDaysOnBench());
                e.setNonProdDays(row.getNonProdDays());
                e.setBaseCity(row.getBaseCity());
                e.setBaseCountry(row.getBaseCountry());
                e.setCurrentCity(row.getCurrentCity());
                e.setCurrentCountry(row.getCurrentCountry());
                e.setTripCity(row.getTripCity());
                e.setTripState(row.getTripState());
                e.setTechStreams(new ArrayList<>());
                e.setSkills(new ArrayList<>());
                // Reconcile benchStartDate with final status after all fields are set
                recalibrateBenchStartDate(e);
                Employee saved = employeeRepo.save(e);

                // Auto-create account for the new employee
                if (saved.getEmail() != null && !saved.getEmail().isBlank()) {
                    createAccountIfAbsent(saved);
                }
                added++;
            }
        }
        return new BulkImportResult(added, updated);
    }

    /**
     * Creates an AppUser for the given employee if one doesn't already exist.
     * Username = employee email (lowercased)
     * Temporary password = employeeId
     * requiresPasswordChange = true
     */
    private void createAccountIfAbsent(Employee employee) {
        String username = employee.getEmail().toLowerCase().trim();
        if (appUserRepo.existsByUsername(username)) return;

        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(employee.getEmployeeId()));
        user.setRole(Role.EMPLOYEE);
        user.setEmployeeId(employee.getId());
        user.setRequiresPasswordChange(true);
        appUserRepo.save(user);
    }

    /**
     * BENCH START DATE RECONCILIATION
     *
     * setDaysOnBench() eagerly writes benchStartDate whenever days is non-null,
     * even when status hasn't been set yet (Jackson deserialization order is undefined).
     * This method is called after the full Employee object is assembled to correct that:
     *
     *  - If status is BENCH and daysOnBench is set → ensure benchStartDate is anchored.
     *    (It likely already is, but we recompute to be safe.)
     *  - If status is NOT BENCH → clear benchStartDate so getDaysOnBench() returns null
     *    and the employee no longer appears to be accumulating bench days.
     */
    private void recalibrateBenchStartDate(Employee e) {
        if (e.getStatus() == EmployeeStatus.BENCH && e.getDaysOnBench() != null) {
            // Re-anchor to today so the computed value stays correct from this moment on.
            // getDaysOnBench() will return the stored value here (benchStartDate may already
            // be set), so we read the raw stored days via the backing field indirectly
            // by recomputing: anchor = today - current computed days.
            e.setBenchStartDate(LocalDate.now().minusDays(e.getDaysOnBench()));
        } else if (e.getStatus() != EmployeeStatus.BENCH) {
            // Not on bench — clear any stale anchor so bench days don't accumulate
            e.setBenchStartDate(null);
        }
    }

    private String buildLocation(BulkImportRowDto row) {
        String city    = row.getCurrentCity() != null ? row.getCurrentCity() : row.getBaseCity();
        String country = row.getCurrentCountry() != null ? row.getCurrentCountry() : row.getBaseCountry();
        List<String> parts = new ArrayList<>();
        if (city != null)    parts.add(city);
        if (country != null) parts.add(country);
        return String.join(", ", parts);
    }
}
