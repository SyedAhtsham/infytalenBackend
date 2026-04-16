package com.infosys.infybenchpro.controller;

import com.infosys.infybenchpro.entity.BenchSpocMapping;
import com.infosys.infybenchpro.service.BenchSpocMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bench-spoc-mappings")
@RequiredArgsConstructor
public class BenchSpocMappingController {

    private final BenchSpocMappingService service;

    @GetMapping
    public List<BenchSpocMapping> getAll() {
        return service.findAll();
    }

    @PostMapping
    public BenchSpocMapping create(@RequestBody BenchSpocMapping mapping) {
        return service.save(mapping);
    }

    @PutMapping("/{id}")
    public BenchSpocMapping update(@PathVariable Long id, @RequestBody BenchSpocMapping mapping) {
        return service.update(id, mapping);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
