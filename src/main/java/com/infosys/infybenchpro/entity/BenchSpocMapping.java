package com.infosys.infybenchpro.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "bench_spoc_mapping")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BenchSpocMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String benchSpoc;

    // The employee ID (database PK) of the manager assigned to this benchSpoc
    private Long managerId;
}
