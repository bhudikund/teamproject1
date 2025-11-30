package ru.urfu.teamproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.teamproject.Entity.Asset;
import ru.urfu.teamproject.Entity.User;
import ru.urfu.teamproject.Entity.UserActive;
import java.util.List;
import java.util.Optional;

public interface UserActiveRepository extends JpaRepository<UserActive, Long> {

    List<UserActive> findByUserAndIsApprovedTrue(User user);

    Optional<UserActive> findFirstByActiveAndIsApprovedTrue(Asset active);
}