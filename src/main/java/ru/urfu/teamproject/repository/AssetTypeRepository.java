package ru.urfu.teamproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.teamproject.Entity.AssetType;

import java.util.Optional;

public interface AssetTypeRepository extends JpaRepository<AssetType, Long> {

    Optional<AssetType> findByName(String name);
}