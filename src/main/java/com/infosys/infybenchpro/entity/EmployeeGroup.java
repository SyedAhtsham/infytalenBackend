package com.infosys.infybenchpro.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employee_group")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "group_employee", joinColumns = @JoinColumn(name = "group_id"))
    @Column(name = "employee_id")
    private List<Long> employeeIds = new ArrayList<>();
}
