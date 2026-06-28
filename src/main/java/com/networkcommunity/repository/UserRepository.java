package com.networkcommunity.repository;
import java.util.List;
import com.networkcommunity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    List<User> findByNameContainingIgnoreCase(String name);
    Page<User> findByNameContainingIgnoreCase(String name, Pageable pageable);
}