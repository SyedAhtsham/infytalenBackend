package com.infosys.infybenchpro.service;

import com.infosys.infybenchpro.entity.EmployeeUpdate;
import com.infosys.infybenchpro.repository.EmployeeUpdateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeUpdateService {

    private final EmployeeUpdateRepository updateRepo;

    public List<EmployeeUpdate> findAll() {
        return updateRepo.findAll();
    }

    public EmployeeUpdate save(EmployeeUpdate update) {
        return updateRepo.save(update);
    }
}
