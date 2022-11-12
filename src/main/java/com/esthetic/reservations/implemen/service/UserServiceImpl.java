package com.esthetic.reservations.implemen.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.esthetic.reservations.api.dto.ResponseDTO;
import com.esthetic.reservations.api.dto.UserEntityDTO;
import com.esthetic.reservations.api.exception.ConflictException;
import com.esthetic.reservations.api.exception.ResourceNotFoundException;
import com.esthetic.reservations.api.util.AppConstants;
import com.esthetic.reservations.model.Role;
import com.esthetic.reservations.model.UserEntity;
import com.esthetic.reservations.repository.RoleRepository;
import com.esthetic.reservations.repository.UserRepository;
import com.esthetic.reservations.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private RoleRepository roleRepository;

    private UserRepository userRepository;

    private ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(RoleRepository roleRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    private UserEntityDTO mapToDTO(UserEntity user) {
        return modelMapper.map(user, UserEntityDTO.class);
    }

    private UserEntity mapToModel(UserEntityDTO userDTO) {
        return modelMapper.map(userDTO, UserEntity.class);
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
    public ResponseDTO<UserEntityDTO> findAll(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<UserEntity> users = userRepository.findAll(pageable);
        ArrayList<UserEntity> usersList = new ArrayList<>(users.getContent());
        // To JSON list
        ArrayList<UserEntityDTO> content = usersList.stream().map(user -> mapToDTO(user))
                .collect(Collectors.toCollection(ArrayList::new));
        ResponseDTO<UserEntityDTO> userResponseDTO = new ResponseDTO<>();
        userResponseDTO.setContent(content);
        userResponseDTO.setPageNumber(pageNumber);
        userResponseDTO.setPageSize(pageSize);
        userResponseDTO.setCount(users.getTotalElements());
        userResponseDTO.setTotalPages(users.getTotalPages());
        userResponseDTO.setLast(users.isLast());
        return userResponseDTO;
    }

    @Override
    public UserEntityDTO findById(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "no encontrado", "id", String.valueOf(id)));
        return mapToDTO(user);
    }

    @Override
    public UserEntityDTO save(UserEntityDTO userDTO) {
        // Check if exists
        if (!userRepository.findByUsername(userDTO.getUsername()).isEmpty()) {
            throw new ConflictException("Usuario", "ya esta siendo usado", "nombre de usuario", userDTO.getUsername());
        }
        if (!userRepository.findByEmail(userDTO.getEmail()).isEmpty()) {
            throw new ConflictException("Usuario", "ya esta siendo usado", "correo electrónico", userDTO.getEmail());
        }
        // Not exists
        // Convert DTO to Model
        UserEntity user = mapToModel(userDTO);

        // Default role
        Role role = roleRepository.findByName(AppConstants.OWNER_ROLE_NAME).get();
        user.setUserRoles(Collections.singletonList(role));

        UserEntity newUser = userRepository.save(user); // Database

        // Convert Model to DTO
        UserEntityDTO userResponse = mapToDTO(newUser);

        return userResponse;
    }

    @Override
    public UserEntityDTO register(UserEntityDTO userEntityDTO, String role) {
        // Check if exists
        if (!userRepository.findByUsername(userEntityDTO.getUsername()).isEmpty()) {
            throw new ConflictException("Usuario", "ya esta siendo usado", "nombre de usuario",
                    userEntityDTO.getUsername());
        }
        if (!userRepository.findByEmail(userEntityDTO.getEmail()).isEmpty()) {
            throw new ConflictException("Usuario", "ya esta siendo usado", "correo electrónico",
                    userEntityDTO.getEmail());
        }
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
    public UserEntityDTO update(UserEntityDTO userDTO, Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "no encontrado", "id", String.valueOf(id)));
        UserEntity editedUser = mapToModel(userDTO);
        user.copy(editedUser);
        userRepository.save(user);
        return mapToDTO(user);
    }

    @Override
    public void delete(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "no encontrado", "id", String.valueOf(id)));
        // userRoleRepository.deleteByUserUserId(user.getUserId());
        userRepository.delete(user);
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
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Usuario", "no encontrado", "id", String.valueOf(userId)));
        Role roleEntity = roleRepository.findByName(role).orElseThrow(() -> new ResourceNotFoundException("Rol", "no encontrado", "nombre",role));
        if(userEntity.getUserRoles().contains(roleEntity)){
            throw new ConflictException("Usuario", "ya tiene","rol", role);
        }
        List<Role> newRoles = userEntity.getUserRoles();
        newRoles.add(roleEntity);
        userEntity.setUserRoles(newRoles);
        return mapToDTO(userRepository.save(userEntity));
    }

    @Override
    public UserEntityDTO revokeRoleToUser(Long userId, String role) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Usuario", "no encontrado", "id", String.valueOf(userId)));
        Role roleEntity = roleRepository.findByName(role).orElseThrow(() -> new ResourceNotFoundException("Rol", "no encontrado", "nombre",role));
        if(!userEntity.getUserRoles().contains(roleEntity)){
            throw new ConflictException("Usuario", "no es","rol", role);
        }
        List<Role> newRoles = userEntity.getUserRoles();
        newRoles.remove(roleEntity);
        userEntity.setUserRoles(newRoles);
        return mapToDTO(userRepository.save(userEntity));
    }

}
