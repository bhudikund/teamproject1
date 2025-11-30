package ru.urfu.teamproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.teamproject.Entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);

    Optional<User> findByFullName(String fullName);
}