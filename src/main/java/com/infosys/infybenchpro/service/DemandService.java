package com.infosys.infybenchpro.service;

import com.infosys.infybenchpro.entity.Demand;
import com.infosys.infybenchpro.repository.DemandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DemandService {

    private final DemandRepository demandRepo;

    public List<Demand> findAll() {
        return demandRepo.findAll();
    }

    public Demand findById(Long id) {
        return demandRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Demand not found: " + id));
    }

    @Transactional
    public Demand save(Demand demand) {
        return demandRepo.save(demand);
    }

    @Transactional
    public void delete(Long id) {
        demandRepo.deleteById(id);
    }
}
