package com.infosys.infybenchpro.dto;

import lombok.Data;

@Data
public class BulkImportRowDto {
    private String employeeId;
    private String name;
    private String email;
    private String jobLevel;
    private String status;
    private String rpl;
    private String employeeDU;
    private Integer daysOnBench;
    private Integer nonProdDays;
    private String baseCity;
    private String baseCountry;
    private String currentCity;
    private String currentCountry;
    private String tripCity;
    private String tripState;
    private String skillCategory;
    private String skillSet;
}
