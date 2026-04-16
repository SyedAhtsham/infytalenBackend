package com.infosys.infybenchpro.service;

import com.infosys.infybenchpro.entity.BenchSpocMapping;
import com.infosys.infybenchpro.entity.Employee;
import com.infosys.infybenchpro.repository.BenchSpocMappingRepository;
import com.infosys.infybenchpro.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BenchSpocMappingService {

    private final BenchSpocMappingRepository mappingRepo;
    private final EmployeeRepository employeeRepo;

    public List<BenchSpocMapping> findAll() {
        return mappingRepo.findAll();
    }

    public BenchSpocMapping findById(Long id) {
        return mappingRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("BenchSpocMapping not found: " + id));
    }

    /**
     * Save or update a mapping, then cascade the new managerId to all employees
     * who have this benchSpoc identifier.
     */
    @Transactional
    public BenchSpocMapping save(BenchSpocMapping mapping) {
        BenchSpocMapping saved = mappingRepo.save(mapping);
        cascadeManagerId(saved.getBenchSpoc(), saved.getManagerId());
        return saved;
    }

    @Transactional
    public BenchSpocMapping update(Long id, BenchSpocMapping updated) {
        BenchSpocMapping existing = findById(id);
        existing.setBenchSpoc(updated.getBenchSpoc());
        existing.setManagerId(updated.getManagerId());
        BenchSpocMapping saved = mappingRepo.save(existing);
        cascadeManagerId(saved.getBenchSpoc(), saved.getManagerId());
        return saved;
    }

    @Transactional
    public void delete(Long id) {
        mappingRepo.deleteById(id);
    }

    /**
     * Resolves the managerId for a given benchSpoc identifier.
     * Called when an employee's benchSpoc is set.
     */
    public Long resolveManagerId(String benchSpoc) {
        if (benchSpoc == null || benchSpoc.isBlank()) return null;
        return mappingRepo.findByBenchSpoc(benchSpoc)
                .map(BenchSpocMapping::getManagerId)
                .orElse(null);
    }

    /** Update managerId on all employees that have the given benchSpoc. */
    private void cascadeManagerId(String benchSpoc, Long managerId) {
        List<Employee> employees = employeeRepo.findAll();
        for (Employee emp : employees) {
            if (benchSpoc.equals(emp.getBenchSpoc())) {
                emp.setManagerId(managerId);
                employeeRepo.save(emp);
            }
        }
    }
}
