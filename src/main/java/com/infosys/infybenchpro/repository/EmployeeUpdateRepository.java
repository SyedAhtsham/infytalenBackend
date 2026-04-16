package com.infosys.infybenchpro.repository;

import com.infosys.infybenchpro.entity.EmployeeUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeUpdateRepository extends JpaRepository<EmployeeUpdate, Long> {
    List<EmployeeUpdate> findByEmployeeId(Long employeeId);
}
