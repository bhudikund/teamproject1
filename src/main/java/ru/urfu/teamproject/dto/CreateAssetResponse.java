package ru.urfu.teamproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateAssetResponse {
    private String result;
    private String inventory_number;
}