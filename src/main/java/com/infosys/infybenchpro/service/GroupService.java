package com.infosys.infybenchpro.service;

import com.infosys.infybenchpro.entity.EmployeeGroup;
import com.infosys.infybenchpro.repository.EmployeeGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final EmployeeGroupRepository groupRepo;

    public List<EmployeeGroup> findAll() {
        return groupRepo.findAll();
    }

    public EmployeeGroup findById(Long id) {
        return groupRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Group not found: " + id));
    }

    public EmployeeGroup save(EmployeeGroup group) {
        return groupRepo.save(group);
    }

    public void delete(Long id) {
        groupRepo.deleteById(id);
    }
}
