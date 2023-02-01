package com.esthetic.reservations.api.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.esthetic.reservations.api.model.Role;

@Repository
@Transactional
@Component("RoleRepository")
public interface RoleRepository extends GenericRepository<Role, Long> {

    public Optional<Role> findByName(String name);

    public Boolean existsByName(String name);

}
