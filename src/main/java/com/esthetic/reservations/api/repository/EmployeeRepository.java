package com.esthetic.reservations.api.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import com.esthetic.reservations.api.model.Employee;
import com.esthetic.reservations.api.model.UserEntity;

@Transactional
@Component("EmployeeRepository")
public interface EmployeeRepository extends GenericRepository<Employee, Long> {

    public Optional<UserEntity> findByUserId(Long id);

    public Optional<UserEntity> findByUserUsername(String username);

    public Boolean existsByUserUsername(String username);

    public Boolean existsByUserEmail(String email);
    
    public boolean existsByUserId(Long id);

    public boolean existsById(Long id);

    public Optional<UserEntity> findByUserEmail(String email);

    public void deleteByUserId(Long id);

}
