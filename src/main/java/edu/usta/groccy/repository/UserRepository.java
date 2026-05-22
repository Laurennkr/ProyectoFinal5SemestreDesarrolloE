package edu.usta.groccy.repository;

import edu.usta.groccy.entity.User;
import edu.usta.groccy.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndStatus(String email, Status status);

    boolean existsByEmail(String email);
}