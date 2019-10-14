package com.example.repos;

import com.example.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Интерфейс, реализующий доступ к БД через репозиторий Spring JPA
 */

public interface UserRepo extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByActivationCode(String code);
}
