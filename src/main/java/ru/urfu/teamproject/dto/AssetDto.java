package ru.urfu.teamproject.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssetDto {
    private String name;
    private String status;
    private String date_create;
    private String owner;
    private String inventory_number;
    private String type_object;
    private String serial_number;
}