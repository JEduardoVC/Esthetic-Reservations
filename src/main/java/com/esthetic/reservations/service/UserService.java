package com.esthetic.reservations.service;

import com.esthetic.reservations.api.dto.ResponseDTO;
import com.esthetic.reservations.api.dto.UserEntityDTO;
import com.esthetic.reservations.model.UserEntity;


public interface UserService extends GenericService<UserEntity, UserEntityDTO, ResponseDTO<UserEntityDTO>> {

    public UserEntityDTO register(UserEntityDTO userEntityDTO, String role);

    public UserEntityDTO findByUsername(String username);

    public UserEntityDTO findByEmail(String email);

    public Boolean existsByUsername(String username);

    public Boolean existsByEmail(String email);

    public UserEntityDTO grantRoleToUser(Long userId, String role);
    
    public UserEntityDTO revokeRoleToUser(Long userId, String role);
    
}
