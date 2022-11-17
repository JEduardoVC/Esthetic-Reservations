package com.esthetic.reservations.api.service;

import com.esthetic.reservations.api.dto.RoleDTO;

public interface RoleService {

    public RoleDTO findByName(String name);

}
