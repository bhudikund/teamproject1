package ru.urfu.teamproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.teamproject.Entity.AssetStatus;
import java.util.List;
import java.util.Optional;

public interface AssetStatusRepository extends JpaRepository<AssetStatus, Long> {

    List<AssetStatus> findByIsSoft(boolean isSoft);

    Optional<AssetStatus> findByNameAndIsSoft(String name, boolean isSoft);
}