package org.exampl.backend.repo;

import org.exampl.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDataRepository extends JpaRepository<User,String> {
    User findByUsername(String username);

}
