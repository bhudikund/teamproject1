package ru.urfu.teamproject.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank
    private String login;

    @NotBlank
    private String password; // md5 от пароля

    @NotBlank
    private String fullName; // ФИО одной строкой
}