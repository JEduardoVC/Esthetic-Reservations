package com.esthetic.reservations.api.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.esthetic.reservations.api.dto.RoleDTO;
import com.esthetic.reservations.api.exception.ResourceNotFoundException;
import com.esthetic.reservations.api.model.Role;
import com.esthetic.reservations.api.repository.RoleRepository;
import com.esthetic.reservations.api.service.RoleService;

@Service
public class RoleServiceImpl extends GenericServiceImpl<Role, RoleDTO> implements RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository repository, ModelMapper modelMapper, @Qualifier("RoleRepository") RoleRepository roleRepository) {
        super(repository, modelMapper, Role.class, RoleDTO.class);
        this.roleRepository = roleRepository;
    }

    @Override
    public RoleDTO findByName(String name) {
        Role role = roleRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Rol", "no encontrado", "nombre", name));
        return mapToDTO(role);
    }

}
