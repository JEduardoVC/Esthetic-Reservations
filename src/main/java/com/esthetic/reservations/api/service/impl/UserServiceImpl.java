package com.esthetic.reservations.api.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.esthetic.reservations.api.dto.ResponseDTO;
import com.esthetic.reservations.api.dto.UserEntityDTO;
import com.esthetic.reservations.api.dto.UserEntityEditDTO;
import com.esthetic.reservations.api.dto.UserEntityEditRolesDTO;
import com.esthetic.reservations.api.dto.UserEntityRolesDTO;
import com.esthetic.reservations.api.exception.BadRequestException;
import com.esthetic.reservations.api.exception.ConflictException;
import com.esthetic.reservations.api.exception.ResourceNotFoundException;
import com.esthetic.reservations.api.model.Branch;
import com.esthetic.reservations.api.model.Employee;
import com.esthetic.reservations.api.model.Role;
import com.esthetic.reservations.api.model.UserEntity;
import com.esthetic.reservations.api.repository.BranchRepository;
import com.esthetic.reservations.api.repository.EmployeeRepository;
import com.esthetic.reservations.api.repository.RoleRepository;
import com.esthetic.reservations.api.repository.UserRepository;
import com.esthetic.reservations.api.service.UserService;
import com.esthetic.reservations.api.util.AppConstants;
import com.esthetic.reservations.api.util.Util;

@Service
public class UserServiceImpl extends GenericServiceImpl<UserEntity, UserEntityDTO>
        implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private EmployeeRepository employeeRepository;
    private BranchRepository branchRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repository, ModelMapper modelMapper,
            @Qualifier("RoleRepository") RoleRepository roleRepository, EmployeeRepository employeeRepository,
            BranchRepository branchRepository, PasswordEncoder passwordEncoder) {
        super(repository, modelMapper, UserEntity.class, UserEntityDTO.class);
        this.userRepository = repository;
        this.roleRepository = roleRepository;
        this.employeeRepository = employeeRepository;
        this.branchRepository = branchRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserEntityDTO findByUsername(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Usuario", "no encontrado", "nombre de usuario", username));
        return mapToDTO(user);
    }

    @Override
    public UserEntityDTO findByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Usuario", "no encontrado", "correo electrónico", email));
        return mapToDTO(user);
    }

    @Override
    public UserEntityDTO register(UserEntityDTO userEntityDTO, String roleName) {
        // Check if exists
        /*
         * if (!userRepository.findByUsername(userEntityDTO.getUsername()).isEmpty()) {
         * throw new ConflictException("Usuario", "ya esta siendo usado",
         * "nombre de usuario",
         * userEntityDTO.getUsername());
         * }
         * if (!userRepository.findByEmail(userEntityDTO.getEmail()).isEmpty()) {
         * throw new ConflictException("Usuario", "ya esta siendo usado",
         * "correo electrónico",
         * userEntityDTO.getEmail());
         * }
         */
        // Not exists
        // Convert DTO to Model
        UserEntity user = mapToModel(userEntityDTO);

        // Default role
        Role role = roleRepository.findByName(roleName).orElseThrow(
                () -> new ResourceNotFoundException("Rol", "no encontrado", "nombre", roleName));
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        UserEntity newUser = userRepository.save(user); // Database

        // Convert Model to DTO
        UserEntityDTO userResponse = mapToDTO(newUser);

        return userResponse;
    }

    @Override
    public UserEntityDTO register(UserEntityDTO userEntityDTO, Long roleId) {
        // Check if exists
        /*
         * if (!userRepository.findByUsername(userEntityDTO.getUsername()).isEmpty()) {
         * throw new ConflictException("Usuario", "ya esta siendo usado",
         * "nombre de usuario",
         * userEntityDTO.getUsername());
         * }
         * if (!userRepository.findByEmail(userEntityDTO.getEmail()).isEmpty()) {
         * throw new ConflictException("Usuario", "ya esta siendo usado",
         * "correo electrónico",
         * userEntityDTO.getEmail());
         * }
         */
        // Not exists
        // Convert DTO to Model
        UserEntity user = mapToModel(userEntityDTO);

        // Default role
        Role role = roleRepository.findById(roleId).orElseThrow(
                () -> new ResourceNotFoundException("Rol", "no encontrado", "id", String.valueOf(roleId)));
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        UserEntity newUser = userRepository.save(user); // Database

        // Convert Model to DTO
        UserEntityDTO userResponse = mapToDTO(newUser);

        return userResponse;
    }

    @Override
    public UserEntityDTO register(UserEntityDTO userEntityDTO) {
        // Convert DTO to Model
        UserEntity user = mapToModel(userEntityDTO);

        // Exists admin ?
        Role adminRole = roleRepository.findByName(AppConstants.ADMIN_ROLE_NAME).orElseThrow(() -> new ResourceNotFoundException("Rol", "no encontrado", "nombre", AppConstants.ADMIN_ROLE_NAME));
        Role roleToBe = adminRole;
        if(userRepository.existsByRolesIn(Arrays.asList(adminRole))){
            Role clientRole = roleRepository.findByName(AppConstants.CLIENT_ROLE_NAME).orElseThrow(() -> new ResourceNotFoundException("Rol", "no encontrado", "nombre", AppConstants.CLIENT_ROLE_NAME));
            roleToBe = clientRole;
        }
        // Default role
        Set<Role> roles = new HashSet<>();
        roles.add(roleToBe);
        user.setRoles(roles);
        UserEntity newUser = userRepository.save(user); // Database
        // Convert Model to DTO
        UserEntityDTO userResponse = mapToDTO(newUser);
        return userResponse;
    }

    @Override
    @Transactional
    public UserEntityDTO register(UserEntityRolesDTO userEntityRolesDTO) {
        Role employeeRole = this.roleRepository.findByName(AppConstants.EMPLOYEE_ROLE_NAME).orElseThrow(
                () -> new ResourceNotFoundException("Rol", "no encontrado", "nombre", AppConstants.EMPLOYEE_ROLE_NAME));
        if (userEntityRolesDTO.getRolesIds().contains(employeeRole.getId())
                && (userEntityRolesDTO.getWorkingBranchesIds() == null
                        || userEntityRolesDTO.getWorkingBranchesIds().size() == 0)) {
            throw new BadRequestException("Registro con roles", "necesita las sucursales de trabajo para el empleado");
        }
        UserEntity user = mapToModel(userEntityRolesDTO);
        UserEntity newUser = userRepository.save(user); // Database
        Set<Role> roles = new HashSet<>();
        for (Long roleId : userEntityRolesDTO.getRolesIds()) {
            Role newRole = this.roleRepository.findById(roleId).orElseThrow(
                    () -> new ResourceNotFoundException("Rol", "no encontrado", "id", String.valueOf(roleId)));
            if (newRole.getName().equals(AppConstants.EMPLOYEE_ROLE_NAME)) {
                Employee employee = new Employee();
                employee.setUser(newUser);
                Set<Branch> branches = new HashSet<>();
                for (Long branchId : userEntityRolesDTO.getWorkingBranchesIds()) {
                    Branch branch = this.branchRepository.findById(branchId)
                            .orElseThrow(() -> new ResourceNotFoundException("Sucursal", "no encontrada", "id",
                                    String.valueOf(branchId)));
                    branches.add(branch);
                }
                employee.setWorkingBranches(branches);
                Employee savedEmployee = this.employeeRepository.save(employee);
                for (Long branchId : userEntityRolesDTO.getWorkingBranchesIds()) {
                    Branch branch = this.branchRepository.getReferenceById(branchId);
                    branch.getEmployees().add(savedEmployee);
                    this.branchRepository.save(branch);
                }
            }
            roles.add(newRole);
        }
        newUser.setRoles(roles);
        newUser = userRepository.save(newUser);
        return mapToDTO(userRepository.findById(newUser.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Usuario", "no encontrado al terminar el registro con roles")));
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserEntityDTO grantRoleToUser(Long userId, String role) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("Usuario", "no encontrado", "id", String.valueOf(userId)));
        Role roleEntity = null;
        try {
            Long roleId = Long.parseLong(role);
            roleEntity = roleRepository.findById(roleId).get();
        } catch (NumberFormatException e) {
            // TODO: handle exception
            role = AppConstants.ROLE_PREFIX + role;
            final String roleName = role;
            roleEntity = roleRepository.findByName(roleName).orElseThrow(
                    () -> new ResourceNotFoundException("Rol", "no encontrado", "nombre", roleName));
        }
        if (userEntity.getRoles().contains(roleEntity)) {
            throw new ConflictException("Usuario", "ya tiene", "rol", roleEntity.getName());
        }
        Set<Role> newRoles = userEntity.getRoles();
        newRoles.add(roleEntity);
        userEntity.setRoles(newRoles);
        return mapToDTO(userRepository.save(userEntity));
    }

    @Transactional
    public UserEntityDTO grantRoleToUser(Long userId, String role, List<Long> workingBranchesIds) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("Usuario", "no encontrado", "id", String.valueOf(userId)));
        Role roleEntity = null;
        try {
            Long roleId = Long.parseLong(role);
            roleEntity = roleRepository.findById(roleId).get();
        } catch (NumberFormatException e) {
            // TODO: handle exception
            role = AppConstants.ROLE_PREFIX + role;
            final String roleName = role;
            roleEntity = roleRepository.findByName(roleName).orElseThrow(
                    () -> new ResourceNotFoundException("Rol", "no encontrado", "nombre", roleName));
        }
        if (userEntity.getRoles().contains(roleEntity)) {
            throw new ConflictException("Usuario", "ya tiene", "rol", roleEntity.getName());
        }
        Set<Role> newRoles = userEntity.getRoles();
        newRoles.add(roleEntity);
        userEntity.setRoles(newRoles);
        UserEntity newUser = userRepository.save(userEntity);
        UserEntityDTO userEntityDTO = mapToDTO(newUser);
        if (roleEntity.getName().equals(AppConstants.EMPLOYEE_ROLE_NAME)) {
            Employee employee = new Employee();
            employee.setUser(newUser);
            for (Long branchId : workingBranchesIds) {
                Branch workingBranch = this.branchRepository.findById(branchId)
                        .orElseThrow(() -> new ResourceNotFoundException("Sucursal", "no encontrada", "id",
                                String.valueOf(branchId)));
                employee.getWorkingBranches().add(workingBranch);
            }
            this.employeeRepository.save(employee);
        }
        return userEntityDTO;
    }

    @Override
    @Transactional
    public UserEntityDTO revokeRoleToUser(Long userId, String role) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("Usuario", "no encontrado", "id", String.valueOf(userId)));
        Role roleEntity = null;
        try {
            Long roleId = Long.parseLong(role);
            roleEntity = roleRepository.findById(roleId).get();
        } catch (NumberFormatException e) {
            // TODO: handle exception
            role = AppConstants.ROLE_PREFIX + role;
            final String roleName = role;
            roleEntity = roleRepository.findByName(roleName).orElseThrow(
                    () -> new ResourceNotFoundException("Rol", "no encontrado", "nombre", roleName));
        }
        if (!userEntity.getRoles().contains(roleEntity)) {
            throw new ConflictException("Usuario", "no es", "rol", roleEntity.getName());
        }
        if (roleEntity.getName().equals(AppConstants.EMPLOYEE_ROLE_NAME)) {
            this.employeeRepository.deleteByUserId(userEntity.getId());
        }
        Set<Role> newRoles = userEntity.getRoles();
        newRoles.remove(roleEntity);
        userEntity.setRoles(newRoles);
        return mapToDTO(userRepository.save(userEntity));
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public boolean validOwnerId(Long id) {
        UserEntity owner = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Usuario", "no encontrado", "id", String.valueOf(id)));
        Role ownerRole = roleRepository.findByName(AppConstants.OWNER_ROLE_NAME).orElseThrow(
                () -> new ResourceNotFoundException("Rol", "no encontrado", "nombre",
                        String.valueOf(AppConstants.OWNER_ROLE_NAME)));
        boolean aver = owner.getRoles().contains(ownerRole);
        System.out.println(aver);
        return aver;
    }

    @Override
    @Transactional
    public UserEntityDTO update(UserEntityEditRolesDTO dto, Long id) {
        // The current entity
        UserEntity currentEntity = this.userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "no encontrado", "id", String.valueOf(id)));
        Role employeeRole = this.roleRepository.findByName(AppConstants.EMPLOYEE_ROLE_NAME).orElseThrow(
                () -> new ResourceNotFoundException("Rol", "no encontrado", "nombre", AppConstants.EMPLOYEE_ROLE_NAME));
        Boolean wasEmployee = currentEntity.getRoles().contains(employeeRole);
        if (dto.getPassword() != null) {
            // Update with new password
            if (!Util.isValidPassword(dto.getPassword())) {
                throw new BadRequestException("Contraseña.", AppConstants.INVALID_PASSWORD_MSG);
            }
            dto.setPassword(this.passwordEncoder.encode(dto.getPassword())); // Update password
        } else {
            dto.setPassword(currentEntity.getPassword()); // The already hashed password
        }
        // The edited entity from the request
        UserEntity editedEntity = new UserEntity(dto.getId(), dto.getUsername(), dto.getName(), dto.getLastName(),
                dto.getPhoneNumber(), dto.getAddress(), dto.getEmail(), dto.getPassword(), dto.getUserRoles());
        // Does the request sent new roles to set?
        if (dto.getRolesIds() != null) {
            // use those roles
            if (dto.getRolesIds().isEmpty()) {
                throw new BadRequestException("Lista de nuevos roles", "no puede estar vacía");
            }
            Set<Role> newRoles = new HashSet<>();
            for (Long roleId : dto.getRolesIds()) {
                Role newRole = this.roleRepository.findById(roleId).orElseThrow(
                        () -> new ResourceNotFoundException("Rol", "no encontrado", "id", String.valueOf(roleId)));
                newRoles.add(newRole);
            }
            editedEntity.setRoles(newRoles);
        } else {
            // use the same roles
            editedEntity.setRoles(currentEntity.getRoles());
        }
        // Lets save with edited values
        currentEntity.copy(editedEntity);
        UserEntity editedUser = this.userRepository.save(currentEntity);
        // Is a new employee?
        Boolean isEmployee = editedUser.getRoles().contains(employeeRole);
        if (isEmployee && dto.getWorkingBranchesIds() == null) {
            throw new BadRequestException("Editar usuario con un nuevo rol de empleado",
                    "requiere las sucursales donde trabaja.");
        }
        if (!wasEmployee && isEmployee || isEmployee && !this.employeeRepository.existsByUserId(editedUser.getId())) {
            Employee newEmployee = new Employee();
            newEmployee.setUser(editedUser);
            Set<Branch> workingBranches = new HashSet<>();
            for (Long branchId : dto.getWorkingBranchesIds()) {
                Branch workingBranch = this.branchRepository.findById(branchId)
                        .orElseThrow(() -> new ResourceNotFoundException("Sucursal", "no encontrada", "id",
                                String.valueOf(branchId)));
                workingBranches.add(workingBranch);
            }
            newEmployee.setWorkingBranches(workingBranches);
            Employee savedEmployee = this.employeeRepository.save(newEmployee);
            for (Branch branch : savedEmployee.getWorkingBranches()) {
                Branch refBranch = this.branchRepository.getReferenceById(branch.getId());
                refBranch.getEmployees().add(savedEmployee);
                this.branchRepository.save(refBranch);
            }
        }
        if (wasEmployee && isEmployee) {
            Employee employee = this.employeeRepository.findByUserId(editedUser.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Empleado", "no encontrado", "id de usuario",
                            String.valueOf(editedUser.getId())));
            Set<Branch> formerBranches = new HashSet<>();
            formerBranches.addAll(employee.getWorkingBranches());
            // El dto dice cuales son las sucursales
            Set<Branch> workingBranches = new HashSet<>();
            for (Long branchId : dto.getWorkingBranchesIds()) {
                Branch workingBranch = this.branchRepository.findById(branchId)
                        .orElseThrow(() -> new ResourceNotFoundException("Sucursal", "no encontrada", "id",
                                String.valueOf(branchId)));
                workingBranches.add(workingBranch);
            }
            for(Branch branch : formerBranches){
                if(!workingBranches.contains(branch)){
                    branch.getEmployees().remove(employee);
                    this.branchRepository.save(branch);
                }
            }
            employee.setWorkingBranches(workingBranches);
            Employee savedEmployee = this.employeeRepository.save(employee);
            for (Branch branch : savedEmployee.getWorkingBranches()) {
                Branch refBranch = this.branchRepository.getReferenceById(branch.getId());
                refBranch.getEmployees().add(savedEmployee);
                this.branchRepository.save(refBranch);
            }
        }
        // Used to be an employee but not anymore?
        if (wasEmployee && !isEmployee) {
            this.employeeRepository.deleteByUserId(editedUser.getId());
        }
        return mapToDTO(editedUser);
    }

    // @Override
    // public UserEntityDTO updateNoPassword(UserEntityEditDTO dto, Long id) {
    // UserEntity entity = getRepository().findById(id)
    // .orElseThrow(() -> new ResourceNotFoundException(getType().getSimpleName(),
    // "no encontrado", "id",
    // String.valueOf(id)));
    // dto.setPassword(entity.getPassword());
    // UserEntity editedEntity = new UserEntity(0l, dto.getUsername(),
    // dto.getName(), dto.getLastName(),
    // dto.getPhoneNumber(), dto.getAddress(),
    // dto.getEmail(), dto.getPassword());
    // entity.copy(editedEntity);
    // getRepository().save(entity);
    // return mapToDTO(entity);
    // }

    // @Override
    // @Transactional
    // public UserEntityDTO updateNoPassword(UserEntityEditRolesDTO dto, Long id) {
    // // Current entity
    // UserEntity entity = getRepository().findById(id)
    // .orElseThrow(() -> new ResourceNotFoundException(getType().getSimpleName(),
    // "no encontrado", "id",
    // String.valueOf(id)));
    // dto.setPassword(entity.getPassword());
    // UserEntity editedEntity = new UserEntity(0l, dto.getUsername(),
    // dto.getName(), dto.getLastName(),
    // dto.getPhoneNumber(), dto.getAddress(),
    // dto.getEmail(), dto.getPassword());
    // // the roles setted in edit request
    // List<Long> rolesIds = dto.getRolesIds();
    // Set<Role> establishedRoles = new HashSet<>();
    // for (Long roleId : rolesIds) {
    // Role role = this.roleRepository.findById(roleId).orElseThrow(
    // () -> new ResourceNotFoundException("Rol", "no encontrado", "id",
    // String.valueOf(roleId)));
    // establishedRoles.add(role);
    // }
    // Role employeeRole =
    // this.roleRepository.findByName(AppConstants.EMPLOYEE_ROLE_NAME).orElseThrow(
    // () -> new ResourceNotFoundException("Rol", "no encontrado", "nombre",
    // AppConstants.EMPLOYEE_ROLE_NAME));
    // // used to have employee role but now it dont?
    // if (entity.getRoles().contains(employeeRole) &&
    // !establishedRoles.contains(employeeRole)) {
    // // Delete employee relation
    // this.employeeRepository.deleteByUserId(entity.getId());
    // }
    // // Set the new values and save
    // entity.copy(editedEntity);
    // entity.setRoles(establishedRoles);
    // getRepository().save(entity);
    // return mapToDTO(entity);
    // }

    @Override
    public ResponseDTO<UserEntityDTO> findAllByRole(int pageNumber, int pageSize, String sortBy, String sortDir,
            String roleName) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Role userRole = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Rol", "no encontrado", "nombre", roleName));
        Page<UserEntity> entities = userRepository.findByRolesIn(Arrays.asList(userRole), pageable);
        ArrayList<UserEntity> entitiesList = new ArrayList<>(entities.getContent());
        // To JSON list
        ArrayList<UserEntityDTO> content = entitiesList.stream().map(entity -> mapToDTO(entity))
                .collect(Collectors.toCollection(ArrayList::new));
        ResponseDTO<UserEntityDTO> userResponseDTO = new ResponseDTO<>();
        userResponseDTO.setContent(content);
        userResponseDTO.setPageNumber(pageNumber);
        userResponseDTO.setPageSize(pageSize);
        userResponseDTO.setCount(entities.getTotalElements());
        userResponseDTO.setTotalPages(entities.getTotalPages());
        userResponseDTO.setLast(entities.isLast());
        return userResponseDTO;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Long id) {
        UserEntity user = this.userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "no encontrado", "id", String.valueOf(id)));

        // Get owner's branches
        if (this.validOwnerId(user.getId())) {
            String sortDir = AppConstants.DEFAULT_SORT_DIR;
            String sortBy = AppConstants.DEFAULT_SORT_ORDER;
            Integer pageNumber = Integer.parseInt(AppConstants.PAGE_NUMBER);
            Integer pageSize = Integer.parseInt(AppConstants.PAGE_SIZE);
            Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();
            Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
            Page<Branch> branchesPage = branchRepository.findAllByOwnerId(user.getId(), pageable);
            ArrayList<Branch> branches = new ArrayList<>(branchesPage.getContent());
            for (Branch branch : branches) {
                branch.setOwner(null);
                this.branchRepository.delete(branch);
            }
        }

        this.userRepository.delete(user);

    }

}
