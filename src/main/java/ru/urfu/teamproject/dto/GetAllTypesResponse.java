package ru.urfu.teamproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetAllTypesResponse {

    private List<String> type_object;
}