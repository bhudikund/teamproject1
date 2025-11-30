package ru.urfu.teamproject.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateAssetRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String type_object;     // имя типа актива (например, "Монитор")

    @NotBlank
    private String status;          // имя статуса (например, "На складе")

    @NotBlank
    private String inventory_number;

    private String serial_number;
    private String description;
    private String address;

    // ФИО владельца (опционально). Если не указано — актив без владельца.
    private String owner;
}