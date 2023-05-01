package com.esthetic.reservations.api.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.esthetic.reservations.api.dto.EmployeeDTO;
import com.esthetic.reservations.api.dto.ResponseDTO;
import com.esthetic.reservations.api.exception.ConflictException;
import com.esthetic.reservations.api.exception.EstheticAppException;
import com.esthetic.reservations.api.exception.ResourceNotFoundException;
import com.esthetic.reservations.api.model.Branch;
import com.esthetic.reservations.api.model.Employee;
import com.esthetic.reservations.api.model.Role;
import com.esthetic.reservations.api.model.UserEntity;
import com.esthetic.reservations.api.repository.EmployeeRepository;
import com.esthetic.reservations.api.repository.RoleRepository;
import com.esthetic.reservations.api.repository.UserRepository;
import com.esthetic.reservations.api.service.EmployeeService;
import com.esthetic.reservations.api.util.AppConstants;

@Service
public class EmployeeServiceImpl extends GenericServiceImpl<Employee, EmployeeDTO>
        implements EmployeeService {

    private EmployeeRepository employeeRepository;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BranchServiceImpl branchService;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, ModelMapper modelMapper, UserRepository userRepository, RoleRepository roleRepository, BranchServiceImpl branchService) {
        super(employeeRepository, modelMapper, Employee.class, EmployeeDTO.class);
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.branchService = branchService;
    }

    @Override
    public ResponseDTO<EmployeeDTO> findAllByBranchId(int pageNumber, int pageSize, String sortBy, String sortDir,
            String roleName, Long branchId) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Branch branch = this.branchService.mapToModel(this.branchService.findById(branchId));
        Page<Employee> entities = this.employeeRepository.findAllByWorkingBranchesIn(Arrays.asList(branch), pageable);
        ArrayList<Employee> entitiesList = new ArrayList<>(entities.getContent());
        // To JSON list
        ArrayList<EmployeeDTO> content = entitiesList.stream().map(entity -> mapToDTO(entity))
                .collect(Collectors.toCollection(ArrayList::new));
        ResponseDTO<EmployeeDTO> userResponseDTO = new ResponseDTO<>();
        userResponseDTO.setContent(content);
        userResponseDTO.setPageNumber(pageNumber);
        userResponseDTO.setPageSize(pageSize);
        userResponseDTO.setCount(entities.getTotalElements());
        userResponseDTO.setTotalPages(entities.getTotalPages());
        userResponseDTO.setLast(entities.isLast());
        return userResponseDTO;
    }

    @Override
    public EmployeeDTO findByUserId(Long id) {
        UserEntity user = this.userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario", "no encontrado", "id", String.valueOf(id)));
        Role employeeRole = this.roleRepository.findByName(AppConstants.EMPLOYEE_ROLE_NAME).orElseThrow(() -> new ResourceNotFoundException("Rol", "no encontrado", "nombre", AppConstants.EMPLOYEE_ROLE_NAME));
        Employee employee = this.employeeRepository.findByUserId(id).orElse(null);
        if(user.getRoles().contains(employeeRole) && employee == null){
            throw new ConflictException("Empleado", "no tiene sucursales donde trabajar", ", empleado con id de usario", String.valueOf(id));
        }
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
