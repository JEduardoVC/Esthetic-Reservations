package com.esthetic.reservations.api.service;

import com.esthetic.reservations.api.dto.RoleDTO;
import com.esthetic.reservations.api.model.Role;

public interface RoleService extends GenericService<Role, RoleDTO> {

    public RoleDTO findByName(String name);
    
    public Boolean existsByName(String name);

}
