package com.warehousekeeper.root.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
@Data
public class StorageDto {

    @NotEmpty(message = "Row should not be empty")
    private String row;
    /**
     * This is int field where store number of storage
     */

    @NotNull(message = "Number should not be empty")
    private int number;
    /**
     * This is int field where store size
     */

    @NotNull(message = "Size should not be empty")
    private int size;
}
