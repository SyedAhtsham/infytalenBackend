package com.infosys.infybenchpro.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EmployeeStatus {
    BENCH("Bench"),
    LEAVE("Leave"),
    OVERHEAD("Overhead"),
    PRE_PRODUCTION("Pre-Production"),
    PRODUCTION("Production"),
    PRODUCT_DEVELOPMENT("Product Development"),
    TRAINING("Training"),
    SALES_SUPPORT("Sales Support");

    private final String displayName;

    EmployeeStatus(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static EmployeeStatus from(String value) {
        if (value == null) return null;
        for (EmployeeStatus s : values()) {
            if (s.displayName.equalsIgnoreCase(value) || s.name().equalsIgnoreCase(value)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown EmployeeStatus: " + value);
    }
}
