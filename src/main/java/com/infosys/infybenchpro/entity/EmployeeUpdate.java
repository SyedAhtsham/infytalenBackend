package com.infosys.infybenchpro.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "employee_update")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long employeeId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    private LocalDate date;
}
