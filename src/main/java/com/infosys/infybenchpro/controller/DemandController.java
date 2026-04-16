package com.infosys.infybenchpro.controller;

import com.infosys.infybenchpro.entity.Demand;
import com.infosys.infybenchpro.service.DemandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/demands")
@RequiredArgsConstructor
public class DemandController {

    private final DemandService demandService;

    @GetMapping
    public List<Demand> getAll() {
        return demandService.findAll();
    }

    @GetMapping("/{id}")
    public Demand getById(@PathVariable Long id) {
        return demandService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Demand> create(@RequestBody Demand demand) {
        return ResponseEntity.status(HttpStatus.CREATED).body(demandService.save(demand));
    }

    @PutMapping("/{id}")
    public Demand update(@PathVariable Long id, @RequestBody Demand demand) {
        demand.setId(id);
        return demandService.save(demand);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        demandService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
