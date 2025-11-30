package ru.urfu.teamproject.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GetAllStatusRequest {

    @NotBlank
    private String type_object;
}