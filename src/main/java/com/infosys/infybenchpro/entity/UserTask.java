package com.infosys.infybenchpro.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "user_task",
       uniqueConstraints = @UniqueConstraint(columnNames = {"task_id", "employee_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTask { //TODO: add deadline to user task

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    private LocalDate dueDate;

    // Convenience getter for serialization
    public Long getTaskId() {
        return task != null ? task.getId() : null;
    }
}
