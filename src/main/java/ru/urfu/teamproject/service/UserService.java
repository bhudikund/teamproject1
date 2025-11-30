package ru.urfu.teamproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.urfu.teamproject.Entity.User;
import ru.urfu.teamproject.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> authenticate(String login, String passwordMd5) {
        return userRepository.findByLogin(login)
                .filter(u -> u.getPasswordHash().equalsIgnoreCase(passwordMd5));
    }

    public Optional<User> findByFullName(String fullName) {
        return userRepository.findByFullName(fullName);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}