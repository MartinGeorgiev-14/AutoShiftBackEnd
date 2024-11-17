package com.cars.carSaleWebsite.repository;

import com.cars.carSaleWebsite.models.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity>findByUsername(String username);
    Boolean existsByUsername(String username);
}
