package com.esthetic.reservations.service;

import com.esthetic.reservations.api.dto.ResponseDTO;
import com.esthetic.reservations.api.dto.RoleDTO;
import com.esthetic.reservations.model.Role;


public interface RoleService extends GenericService<Role, RoleDTO, ResponseDTO<RoleDTO>> {

    public RoleDTO findByName(String name);

}
