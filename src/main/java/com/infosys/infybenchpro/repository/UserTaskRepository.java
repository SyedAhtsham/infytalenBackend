package com.infosys.infybenchpro.repository;

import com.infosys.infybenchpro.entity.UserTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserTaskRepository extends JpaRepository<UserTask, Long> {
    List<UserTask> findByTask_Id(Long taskId);
    List<UserTask> findByEmployeeId(Long employeeId);
    Optional<UserTask> findByTask_IdAndEmployeeId(Long taskId, Long employeeId);
    void deleteByTask_IdAndEmployeeId(Long taskId, Long employeeId);
}
