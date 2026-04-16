package com.infosys.infybenchpro.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "task")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Not persisted — used only to propagate a default due date to UserTasks on create/update
    @Transient
    private LocalDate dueDate;

    // Per-employee due dates overrides (key = employeeId as string, value = due date)
    // Also @Transient — used to pass per-employee dates from the request
    @Transient
    private Map<String, LocalDate> employeeDueDates;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "task_assigned_employee", joinColumns = @JoinColumn(name = "task_id"))
    @Column(name = "employee_id")
    private List<Long> assignedEmployeeIds = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserTask> userTasks = new ArrayList<>();
}
