package com.szs.repository;

import com.szs.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findOneByUserIdIgnoreCase(String userId);

    Optional<User> findOneByRegNo(String regNo);
}
