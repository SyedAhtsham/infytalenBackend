package com.infosys.infybenchpro.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employee")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee { //TODO: create the employee one page profile maker

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String employeeId;   // display / source ID (e.g. 7-digit number)

    @Column(nullable = false)
    private String name;

    private String email;
    private String location;
    private String jobLevel;

    @Enumerated(EnumType.STRING)
    private EmployeeStatus status;

    private String rpl;
    private String employeeDU;

    /**
     * BENCH AGE — AUTO-INCREMENTING
     *
     * We do NOT store a plain integer that would go stale.
     * Instead we store benchStartDate (the calendar date the employee entered bench)
     * and compute daysOnBench live as (today - benchStartDate).
     *
     * Lombok getter/setter are suppressed for this field so we can provide
     * custom implementations below that keep benchStartDate in sync.
     */
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Integer daysOnBench; // kept in DB for legacy rows that predate benchStartDate

    /**
     * The date the employee was placed on bench.
     * Set automatically whenever daysOnBench is assigned (import or manual edit).
     * Nullable so existing rows without it gracefully fall back to stored daysOnBench.
     */
    private LocalDate benchStartDate;

    /**
     * Returns live bench age: if benchStartDate is recorded, compute days elapsed
     * from that anchor to today. Falls back to the stored integer for legacy rows
     * that were saved before this feature was added.
     */
    public Integer getDaysOnBench() {
        if (benchStartDate != null) {
            // Compute how many full days have passed since the employee entered bench.
            // This automatically increments by 1 each calendar day with no cron job needed.
            return (int) ChronoUnit.DAYS.between(benchStartDate, LocalDate.now());
        }
        // Legacy fallback: return whatever integer was stored directly (won't auto-increment)
        return daysOnBench;
    }

    /**
     * Called during import and manual saves.
     * Stores the raw value for legacy compatibility AND derives benchStartDate
     * so that future reads are always live-computed.
     *
     * Example: importing daysOnBench=13 on 2026-04-15 sets benchStartDate=2026-04-02.
     * The next day getDaysOnBench() returns 14 automatically.
     *
     * Note: we set benchStartDate unconditionally here (not checking status) because
     * during Jackson deserialization field order is undefined — status may not be set yet.
     * EmployeeService.recalibrateBenchStartDate() is called after the full object is
     * assembled to clear the anchor when the employee is not actually on bench.
     */
    public void setDaysOnBench(Integer days) {
        this.daysOnBench = days;
        if (days != null) {
            // Anchor: employee was placed on bench `days` days before today
            this.benchStartDate = LocalDate.now().minusDays(days);
        } else {
            // Days explicitly cleared — remove the anchor too
            this.benchStartDate = null;
        }
    }

    private Integer nonProdDays;
    private String baseCity;
    private String baseCountry;
    private String currentCity;
    private String currentCountry;
    private String tripCity;
    private String tripState;
    private String readyToRelocate;
    private String rto;
    private String benchSpoc;
    private String hubSpoc;
    private String actualStatus;
    private String remarks;
    private Long managerId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "employee_tech_stream", joinColumns = @JoinColumn(name = "employee_id"))
    @Column(name = "tech_stream")
    @Enumerated(EnumType.STRING)
    private List<TechStream> techStreams = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "employee_skill", joinColumns = @JoinColumn(name = "employee_id"))
    private List<Skill> skills = new ArrayList<>();
}
