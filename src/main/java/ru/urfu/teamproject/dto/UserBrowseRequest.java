package ru.urfu.teamproject.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserBrowseRequest {

    @NotBlank
    private String owner; // ФИО пользователя
}