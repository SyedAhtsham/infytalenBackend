package com.infosys.infybenchpro.controller;

import com.infosys.infybenchpro.entity.TaskStatus;
import com.infosys.infybenchpro.entity.UserTask;
import com.infosys.infybenchpro.service.UserTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user-tasks")
@RequiredArgsConstructor
public class UserTaskController {

    private final UserTaskService userTaskService;

    @GetMapping
    public List<UserTask> getAll() {
        return userTaskService.findAll();
    }

    @PatchMapping("/{taskId}/{employeeId}")
    public UserTask updateStatus(@PathVariable Long taskId,
                                 @PathVariable Long employeeId,
                                 @RequestBody Map<String, String> body) {
        TaskStatus status = TaskStatus.from(body.get("status"));
        return userTaskService.updateStatus(taskId, employeeId, status);
    }
}
