package org.exampl.backend.repo;

import org.exampl.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByNic(String nic);
    Optional<User> findById(String id);
    User findTopByOrderByIdDesc();



}
