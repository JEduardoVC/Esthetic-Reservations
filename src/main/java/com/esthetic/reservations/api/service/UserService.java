package com.esthetic.reservations.api.service;

import com.esthetic.reservations.api.dto.UserEntityDTO;
import com.esthetic.reservations.api.dto.UserEntityEditDTO;
import com.esthetic.reservations.api.model.UserEntity;

public interface UserService extends GenericService<UserEntity, UserEntityDTO> {

    public UserEntityDTO register(UserEntityDTO userEntityDTO, String role);
    
    public UserEntityDTO register(UserEntityDTO userEntityDTO, Long role);

    public UserEntityDTO updateNoPassword(UserEntityEditDTO dto, Long id);

    public UserEntityDTO findByUsername(String username);

    public UserEntityDTO findByEmail(String email);

    public Boolean existsByUsername(String username);

    public Boolean existsByEmail(String email);
    
    public boolean existsById(Long id);

    public boolean validOwnerId(Long id);

    public UserEntityDTO grantRoleToUser(Long userId, String role);

    public UserEntityDTO revokeRoleToUser(Long userId, String role);

}
