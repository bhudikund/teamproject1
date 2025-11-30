package ru.urfu.teamproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.teamproject.Entity.Asset;
import ru.urfu.teamproject.Entity.User;

import java.util.List;
import java.util.Optional;

public interface AssetRepository extends JpaRepository<Asset, Long> {

    Optional<Asset> findByInventoryNumber(String inventoryNumber);
}