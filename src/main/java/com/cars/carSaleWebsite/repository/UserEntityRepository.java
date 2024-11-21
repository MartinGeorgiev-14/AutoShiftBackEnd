package com.cars.carSaleWebsite.repository;

import com.cars.carSaleWebsite.models.entities.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserEntityRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity>findByUsername(String username);
    @Query("SELECT u FROM UserEntity u WHERE u.username = :username")
    UserEntity findByUserByUsername(@Param("username") String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    // Stava nqkakvi problemi s toq shibanoto repository
}
