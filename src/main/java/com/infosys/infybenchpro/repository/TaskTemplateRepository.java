package com.infosys.infybenchpro.repository;

import com.infosys.infybenchpro.entity.TaskTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskTemplateRepository extends JpaRepository<TaskTemplate, Long> {
}
