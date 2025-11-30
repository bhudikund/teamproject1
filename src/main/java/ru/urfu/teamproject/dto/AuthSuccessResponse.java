package ru.urfu.teamproject.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthSuccessResponse {
    private String user;      // ФИО
    private Long user_id;     // идентификатор пользователя
}