package com.infosys.infybenchpro.service;

import com.infosys.infybenchpro.entity.Task;
import com.infosys.infybenchpro.entity.TaskStatus;
import com.infosys.infybenchpro.entity.UserTask;
import com.infosys.infybenchpro.repository.TaskRepository;
import com.infosys.infybenchpro.repository.UserTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepo;
    private final UserTaskRepository userTaskRepo;

    public List<Task> findAll() {
        return taskRepo.findAll();
    }

    public Task findById(Long id) {
        return taskRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found: " + id));
    }

    @Transactional
    public Task create(Task task) {
        LocalDate defaultDueDate = task.getDueDate();
        Map<String, LocalDate> empDueDates = task.getEmployeeDueDates();

        Task saved = taskRepo.save(task);

        for (Long empId : task.getAssignedEmployeeIds()) {
            UserTask ut = new UserTask();
            ut.setTask(saved);
            ut.setEmployeeId(empId);
            ut.setStatus(TaskStatus.IN_PROGRESS);
            ut.setDueDate(resolveDueDate(empId, empDueDates, defaultDueDate));
            userTaskRepo.save(ut);
        }
        return saved;
    }

    @Transactional
    public Task update(Task updated) {
        Task existing = findById(updated.getId());

        LocalDate defaultDueDate = updated.getDueDate();
        Map<String, LocalDate> empDueDates = updated.getEmployeeDueDates();

        List<Long> oldIds = existing.getAssignedEmployeeIds();
        List<Long> newIds = updated.getAssignedEmployeeIds();

        // Remove UserTask for employees no longer assigned
        oldIds.stream()
            .filter(id -> !newIds.contains(id))
            .forEach(id -> userTaskRepo.findByTask_IdAndEmployeeId(existing.getId(), id)
                .ifPresent(userTaskRepo::delete));

        // Add UserTask for newly assigned employees
        newIds.stream()
            .filter(id -> !oldIds.contains(id))
            .filter(id -> userTaskRepo.findByTask_IdAndEmployeeId(existing.getId(), id).isEmpty())
            .forEach(id -> {
                UserTask ut = new UserTask();
                ut.setTask(existing);
                ut.setEmployeeId(id);
                ut.setStatus(TaskStatus.IN_PROGRESS);
                ut.setDueDate(resolveDueDate(id, empDueDates, defaultDueDate));
                userTaskRepo.save(ut);
            });

        // Update dueDate on existing UserTasks if a default was provided
        if (defaultDueDate != null || empDueDates != null) {
            newIds.stream()
                .filter(id -> oldIds.contains(id))
                .forEach(id -> {
                    LocalDate due = resolveDueDate(id, empDueDates, defaultDueDate);
                    if (due != null) {
                        userTaskRepo.findByTask_IdAndEmployeeId(existing.getId(), id)
                            .ifPresent(ut -> {
                                ut.setDueDate(due);
                                userTaskRepo.save(ut);
                            });
                    }
                });
        }

        existing.setTitle(updated.getTitle());
        existing.setDescription(updated.getDescription());
        existing.setPriority(updated.getPriority());
        existing.setAssignedEmployeeIds(newIds);
        return taskRepo.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        userTaskRepo.findByTask_Id(id).forEach(userTaskRepo::delete);
        taskRepo.deleteById(id);
    }

    private LocalDate resolveDueDate(Long empId, Map<String, LocalDate> empDueDates, LocalDate defaultDueDate) {
        if (empDueDates != null) {
            LocalDate specific = empDueDates.get(String.valueOf(empId));
            if (specific != null) return specific;
        }
        return defaultDueDate;
    }
}
