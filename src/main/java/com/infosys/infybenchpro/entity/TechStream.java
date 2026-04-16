package com.infosys.infybenchpro.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TechStream { //TODO: edit to have correct streams
    JAVA("Java"),
    DOTNET(".NET"),
    BUSINESS_ANALYST("Business Analyst"),
    DATA_SCIENCE("Data Science"),
    MAINFRAME("Mainframe");

    private final String displayName;

    TechStream(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static TechStream from(String value) {
        if (value == null) return null;
        for (TechStream t : values()) {
            if (t.displayName.equalsIgnoreCase(value) || t.name().equalsIgnoreCase(value)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Unknown TechStream: " + value);
    }
}
