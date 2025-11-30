package ru.urfu.teamproject.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserBrowseResponse {
    private List<AssetDto> assets;
}