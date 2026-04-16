package com.infosys.infybenchpro.repository;

import com.infosys.infybenchpro.entity.Demand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemandRepository extends JpaRepository<Demand, Long> {
}
