package com.esthetic.reservations.api.service.impl;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.esthetic.reservations.api.dto.UserEntityDTO;
import com.esthetic.reservations.api.dto.UserEntityEditDTO;
import com.esthetic.reservations.api.exception.ConflictException;
import com.esthetic.reservations.api.exception.ResourceNotFoundException;
import com.esthetic.reservations.api.model.Role;
import com.esthetic.reservations.api.model.UserEntity;
import com.esthetic.reservations.api.repository.RoleRepository;
import com.esthetic.reservations.api.repository.UserRepository;
import com.esthetic.reservations.api.service.UserService;
import com.esthetic.reservations.api.util.AppConstants;

@Service
public class UserServiceImpl extends GenericServiceImpl<UserEntity, UserEntityDTO>
        implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository repository, ModelMapper modelMapper,
            @Qualifier("RoleRepository") RoleRepository roleRepository) {
        super(repository, modelMapper, UserEntity.class, UserEntityDTO.class);
        this.userRepository = repository;
        this.roleRepository = roleRepository;
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
    public UserEntityDTO register(UserEntityDTO userEntityDTO, String role) {
        // Check if exists
        /*if (!userRepository.findByUsername(userEntityDTO.getUsername()).isEmpty()) {
            throw new ConflictException("Usuario", "ya esta siendo usado", "nombre de usuario",
                    userEntityDTO.getUsername());
        }
        if (!userRepository.findByEmail(userEntityDTO.getEmail()).isEmpty()) {
            throw new ConflictException("Usuario", "ya esta siendo usado", "correo electrónico",
                    userEntityDTO.getEmail());
        }*/
        // Not exists
        // Convert DTO to Model
        UserEntity user = mapToModel(userEntityDTO);

        // Default role
        Role roles = roleRepository.findByName(role).get();
        user.setUserRoles(Collections.singletonList(roles));

        UserEntity newUser = userRepository.save(user); // Database

        // Convert Model to DTO
        UserEntityDTO userResponse = mapToDTO(newUser);

        return userResponse;
    }

    @Override
    public UserEntityDTO register(UserEntityDTO userEntityDTO, Long role) {
        // Check if exists
        /*if (!userRepository.findByUsername(userEntityDTO.getUsername()).isEmpty()) {
            throw new ConflictException("Usuario", "ya esta siendo usado", "nombre de usuario",
                    userEntityDTO.getUsername());
        }
        if (!userRepository.findByEmail(userEntityDTO.getEmail()).isEmpty()) {
            throw new ConflictException("Usuario", "ya esta siendo usado", "correo electrónico",
                    userEntityDTO.getEmail());
        }*/
        // Not exists
        // Convert DTO to Model
        UserEntity user = mapToModel(userEntityDTO);

        // Default role
        Role roles = roleRepository.findById(role).get();
        user.setUserRoles(Collections.singletonList(roles));

        UserEntity newUser = userRepository.save(user); // Database

        // Convert Model to DTO
        UserEntityDTO userResponse = mapToDTO(newUser);

        return userResponse;
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
            roleEntity = roleRepository.findByName(role).get();
        }
        if (userEntity.getUserRoles().contains(roleEntity)) {
            throw new ConflictException("Usuario", "ya tiene", "rol", roleEntity.getName());
        }
        List<Role> newRoles = userEntity.getUserRoles();
        newRoles.add(roleEntity);
        userEntity.setUserRoles(newRoles);
        return mapToDTO(userRepository.save(userEntity));
    }

    @Override
    public UserEntityDTO revokeRoleToUser(Long userId, String role) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("Usuario", "no encontrado", "id", String.valueOf(userId)));
        Role roleEntity = null;
        try {
            Long roleId = Long.parseLong(role);
            roleEntity = roleRepository.findById(roleId).get();
        } catch (NumberFormatException e) {
            // TODO: handle exception
            roleEntity = roleRepository.findByName(role).get();
        }
        if (!userEntity.getUserRoles().contains(roleEntity)) {
            throw new ConflictException("Usuario", "no es", "rol", roleEntity.getName());
        }
        List<Role> newRoles = userEntity.getUserRoles();
        newRoles.remove(roleEntity);
        userEntity.setUserRoles(newRoles);
        return mapToDTO(userRepository.save(userEntity));
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public boolean validOwnerId(Long id) {
        UserEntity owner = userRepository.findById(id).get();
        Role ownerRole = roleRepository.findByName(AppConstants.OWNER_ROLE_NAME).get();
        boolean aver = owner.getUserRoles().contains(ownerRole);
        System.out.println(aver);
        return aver;
    }

    @Override
    public UserEntityDTO updateNoPassword(UserEntityEditDTO dto, Long id) {
        UserEntity entity = getRepository().findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getType().getSimpleName(), "no encontrado", "id",
                        String.valueOf(id)));
        dto.setPassword(entity.getPassword());
        UserEntity editedEntity = new UserEntity(0l, dto.getUsername(), dto.getName(), dto.getLastName(),
                dto.getPhoneNumber(), dto.getAddress(),
                dto.getEmail(),dto.getPassword());
        entity.copy(editedEntity);
        getRepository().save(entity);
        return mapToDTO(entity);
    }

}
