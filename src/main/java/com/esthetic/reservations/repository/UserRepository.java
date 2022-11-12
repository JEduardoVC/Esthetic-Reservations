package com.esthetic.reservations.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esthetic.reservations.model.UserEntity;


public interface UserRepository extends JpaRepository<UserEntity, Long> {

    public Optional<UserEntity> findByUsername(String username);

    public Boolean existsByUsername(String username);

    public Boolean existsByEmail(String email);

    public Optional<UserEntity> findByEmail(String email);

}
