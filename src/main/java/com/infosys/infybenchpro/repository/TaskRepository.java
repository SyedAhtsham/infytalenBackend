package com.infosys.infybenchpro.repository;

import com.infosys.infybenchpro.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
