package com.infosys.infybenchpro.service;

import com.infosys.infybenchpro.entity.*;
import com.infosys.infybenchpro.repository.TaskRepository;
import com.infosys.infybenchpro.repository.TaskTemplateRepository;
import com.infosys.infybenchpro.repository.UserTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TemplateService {

    private final TaskTemplateRepository templateRepo;
    private final TaskRepository taskRepo;
    private final UserTaskRepository userTaskRepo;

    public List<TaskTemplate> findAll() {
        return templateRepo.findAll();
    }

    public TaskTemplate findById(Long id) {
        return templateRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found: " + id));
    }

    public TaskTemplate save(TaskTemplate template) {
        return templateRepo.save(template);
    }

    public void delete(Long id) {
        templateRepo.deleteById(id);
    }

    @Transactional
    public void assignToEmployees(Long templateId, List<Long> employeeIds) {
        TaskTemplate template = findById(templateId);
        if (employeeIds == null || employeeIds.isEmpty()) return;

        LocalDate today = LocalDate.now();

        for (Long empId : employeeIds) {
            for (TemplateTask tmplTask : template.getTasks()) {
                Task sourceTask = taskRepo.findById(tmplTask.getTaskId()).orElse(null);
                if (sourceTask == null) continue;

                Task newTask = new Task();
                newTask.setTitle(sourceTask.getTitle());
                newTask.setDescription(sourceTask.getDescription());
                newTask.setPriority(sourceTask.getPriority());
                newTask.setAssignedEmployeeIds(List.of(empId));
                Task saved = taskRepo.save(newTask);

                UserTask ut = new UserTask();
                ut.setTask(saved);
                ut.setEmployeeId(empId);
                ut.setStatus(TaskStatus.IN_PROGRESS);
                ut.setDueDate(today.plusDays(tmplTask.getDueDaysFromNow()));
                userTaskRepo.save(ut);
            }
        }
    }
}
