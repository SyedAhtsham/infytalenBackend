package com.infosys.infybenchpro.controller;

import com.infosys.infybenchpro.entity.EmployeeUpdate;
import com.infosys.infybenchpro.service.EmployeeUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/updates")
@RequiredArgsConstructor
public class EmployeeUpdateController {

    private final EmployeeUpdateService updateService;

    @GetMapping
    public List<EmployeeUpdate> getAll() {
        return updateService.findAll();
    }

    @PostMapping
    public ResponseEntity<EmployeeUpdate> create(@RequestBody EmployeeUpdate update) {
        return ResponseEntity.status(HttpStatus.CREATED).body(updateService.save(update));
    }
}
