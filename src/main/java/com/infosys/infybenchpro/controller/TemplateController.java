package com.infosys.infybenchpro.controller;

import com.infosys.infybenchpro.entity.TaskTemplate;
import com.infosys.infybenchpro.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/templates")
@RequiredArgsConstructor
public class TemplateController {

    private final TemplateService templateService;

    @GetMapping
    public List<TaskTemplate> getAll() {
        return templateService.findAll();
    }

    @GetMapping("/{id}")
    public TaskTemplate getById(@PathVariable Long id) {
        return templateService.findById(id);
    }

    @PostMapping
    public ResponseEntity<TaskTemplate> create(@RequestBody TaskTemplate template) {
        return ResponseEntity.status(HttpStatus.CREATED).body(templateService.save(template));
    }

    @PutMapping("/{id}")
    public TaskTemplate update(@PathVariable Long id, @RequestBody TaskTemplate template) {
        template.setId(id);
        return templateService.save(template);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        templateService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/assign")
    public ResponseEntity<Void> assign(@PathVariable Long id,
                                       @RequestBody Map<String, List<Long>> body) {
        templateService.assignToEmployees(id, body.get("employeeIds"));
        return ResponseEntity.ok().build();
    }
}
