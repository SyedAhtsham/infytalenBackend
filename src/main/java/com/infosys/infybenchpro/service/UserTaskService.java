package com.infosys.infybenchpro.service;

import com.infosys.infybenchpro.entity.TaskStatus;
import com.infosys.infybenchpro.entity.UserTask;
import com.infosys.infybenchpro.repository.UserTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserTaskService {

    private final UserTaskRepository userTaskRepo;

    public List<UserTask> findAll() {
        return userTaskRepo.findAll();
    }

    public UserTask updateStatus(Long taskId, Long employeeId, TaskStatus status) {
        UserTask ut = userTaskRepo.findByTask_IdAndEmployeeId(taskId, employeeId)
                .orElseThrow(() -> new RuntimeException(
                        "UserTask not found for taskId=" + taskId + ", employeeId=" + employeeId));
        ut.setStatus(status);
        return userTaskRepo.save(ut);
    }
}
