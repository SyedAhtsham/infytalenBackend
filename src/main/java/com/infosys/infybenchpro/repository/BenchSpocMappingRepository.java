package com.infosys.infybenchpro.repository;

import com.infosys.infybenchpro.entity.BenchSpocMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BenchSpocMappingRepository extends JpaRepository<BenchSpocMapping, Long> {
    Optional<BenchSpocMapping> findByBenchSpoc(String benchSpoc);
}
