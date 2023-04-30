package com.esthetic.reservations.api.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.esthetic.reservations.api.dto.EmployeeDTO;
import com.esthetic.reservations.api.exception.ResourceNotFoundException;
import com.esthetic.reservations.api.model.Employee;
import com.esthetic.reservations.api.repository.EmployeeRepository;
import com.esthetic.reservations.api.service.EmployeeService;

@Service
public class EmployeeServiceImpl extends GenericServiceImpl<Employee, EmployeeDTO>
        implements EmployeeService {

    private EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, ModelMapper modelMapper) {
        super(employeeRepository, modelMapper, Employee.class, EmployeeDTO.class);
        this.employeeRepository = employeeRepository;
    }

    @Override
    public EmployeeDTO findByUserId(Long id) {
        Employee employee = this.employeeRepository.findByUserId(id).orElseThrow(() -> new ResourceNotFoundException("Empleado", "no encontrado", "id de usario", String.valueOf(id)));
        return mapToDTO(employee);
    }

    @Override
    public EmployeeDTO findByUserUsername(String username) {
        Employee employee = this.employeeRepository.findByUserUsername(username).orElseThrow(() -> new ResourceNotFoundException("Empleado", "no encontrado", "nombre de usario", username));
        return mapToDTO(employee);
    }

    @Override
    public EmployeeDTO findByUserEmail(String email) {
        Employee employee = this.employeeRepository.findByUserEmail(email).orElseThrow(() -> new ResourceNotFoundException("Empleado", "no encontrado", "correo de usuario", email));
        return mapToDTO(employee);
    }

    @Override
    public Boolean existsByUserUsername(String username) {
        return this.employeeRepository.existsByUserUsername(username);
    }

    @Override
    public Boolean existsByUserEmail(String email) {
        return this.employeeRepository.existsByUserEmail(email);
    }

    @Override
    public boolean existsById(Long id) {
        return this.employeeRepository.existsById(id);
    }

    @Override
    public boolean existsByUserId(Long id) {
        return this.employeeRepository.existsByUserId(id);
    }
    
}
