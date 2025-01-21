package com.cars.carSaleWebsite.repository;

import com.cars.carSaleWebsite.dto.RoleDto;
import com.cars.carSaleWebsite.models.entities.user.Role;
import com.cars.carSaleWebsite.models.entities.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

public interface UserEntityRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity>findByUsername(String username);
    @Query("SELECT u FROM UserEntity u WHERE u.username = :username")
    UserEntity findByUserByUsername(@Param("username") String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    @Query(value = "SELECT r.id, r.name FROM roles r JOIN user_roles ur ON r.id = ur.role_id WHERE ur.user_id = :userId", nativeQuery = true)
    HashSet<Object[]> findRolesByUserId(@Param("userId") UUID userId);
}
