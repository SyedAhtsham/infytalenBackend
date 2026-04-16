package com.infosys.infybenchpro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BulkImportResult {
    private int added;
    private int updated;
}
