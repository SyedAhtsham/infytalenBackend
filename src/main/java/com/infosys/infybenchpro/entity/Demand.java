package com.infosys.infybenchpro.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "demand")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Demand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Short title for the open position, e.g. "Senior Java Developer" */
    @Column(nullable = false)
    private String title;

    /** Client or account name, e.g. "Bank of America" */
    @Column(nullable = false)
    private String clientName;

    /** Date the demand was posted/opened */
    private LocalDate postDate;

    /** "Onsite", "Remote", or "Hybrid" */
    private String workType;

    /** City/state/country or region */
    private String location;

    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * Free-text skill names required for this demand, e.g. ["Java", "Spring Boot", "AWS"].
     * Stored in a separate collection table — one row per skill.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "demand_required_skill", joinColumns = @JoinColumn(name = "demand_id"))
    @Column(name = "skill")
    private List<String> requiredSkills = new ArrayList<>();
}
