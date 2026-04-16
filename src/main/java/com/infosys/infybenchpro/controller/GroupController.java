package com.infosys.infybenchpro.controller;

import com.infosys.infybenchpro.entity.EmployeeGroup;
import com.infosys.infybenchpro.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping
    public List<EmployeeGroup> getAll() {
        return groupService.findAll();
    }

    @GetMapping("/{id}")
    public EmployeeGroup getById(@PathVariable Long id) {
        return groupService.findById(id);
    }

    @PostMapping
    public ResponseEntity<EmployeeGroup> create(@RequestBody EmployeeGroup group) {
        return ResponseEntity.status(HttpStatus.CREATED).body(groupService.save(group));
    }

    @PutMapping("/{id}")
    public EmployeeGroup update(@PathVariable Long id, @RequestBody EmployeeGroup group) {
        group.setId(id);
        return groupService.save(group);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        groupService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
