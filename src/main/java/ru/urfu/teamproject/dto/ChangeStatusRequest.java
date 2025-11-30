package ru.urfu.teamproject.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangeStatusRequest {

    @NotBlank
    private String inventory_number;

    @NotBlank
    private String new_status;
}