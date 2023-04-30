package com.esthetic.reservations.api.service;

import com.esthetic.reservations.api.dto.EmployeeDTO;
import com.esthetic.reservations.api.model.Employee;

public interface EmployeeService extends GenericService<Employee, EmployeeDTO> {

    public EmployeeDTO findByUserId(Long id);

    public EmployeeDTO findByUserUsername(String username);

    public EmployeeDTO findByUserEmail(String email);

    public Boolean existsByUserUsername(String username);

    public Boolean existsByUserEmail(String email);

    public boolean existsById(Long id);

    public boolean existsByUserId(Long id);

}
