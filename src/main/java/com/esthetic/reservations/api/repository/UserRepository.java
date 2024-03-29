package com.esthetic.reservations.api.repository;

import java.util.Collection;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.esthetic.reservations.api.model.Role;
import com.esthetic.reservations.api.model.UserEntity;

@Transactional
@Component("UserRepository")
public interface UserRepository extends GenericRepository<UserEntity, Long> {

    public Optional<UserEntity> findByUsername(String username);

    public Boolean existsByUsername(String username);

    public Boolean existsByEmail(String email);
    
    public boolean existsById(Long id);

    public Optional<UserEntity> findByEmail(String email);

    public Page<UserEntity> findByRolesIn(Collection<Role> roles, Pageable pageable);

    public Boolean existsByRolesIn(Collection<Role> roles);

}
