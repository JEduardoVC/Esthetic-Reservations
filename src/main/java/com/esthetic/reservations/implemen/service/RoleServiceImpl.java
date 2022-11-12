package com.esthetic.reservations.implemen.service;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.esthetic.reservations.api.dto.ResponseDTO;
import com.esthetic.reservations.api.dto.RoleDTO;
import com.esthetic.reservations.api.exception.ResourceNotFoundException;
import com.esthetic.reservations.model.Role;
import com.esthetic.reservations.repository.RoleRepository;
import com.esthetic.reservations.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    ModelMapper modelMapper;
    RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(ModelMapper modelMapper, RoleRepository roleRepository) {
        this.modelMapper = modelMapper;
        this.roleRepository = roleRepository;
    }

    private RoleDTO mapToDTO(Role role) {
        return modelMapper.map(role, RoleDTO.class);
    }

    private Role mapToModel(RoleDTO roleDTO) {
        return modelMapper.map(roleDTO, Role.class);
    }

    @Override
    public ResponseDTO<RoleDTO> findAll(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Role> roles = roleRepository.findAll(pageable);
        ArrayList<Role> rolesList = new ArrayList<>(roles.getContent());
        // To JSON list
        ArrayList<RoleDTO> content = rolesList.stream().map(role -> mapToDTO(role))
                .collect(Collectors.toCollection(ArrayList::new));
        ResponseDTO<RoleDTO> roleResponseDTO = new ResponseDTO<>();
        roleResponseDTO.setContent(content);
        roleResponseDTO.setPageNumber(pageNumber);
        roleResponseDTO.setPageSize(pageSize);
        roleResponseDTO.setCount(roles.getTotalElements());
        roleResponseDTO.setTotalPages(roles.getTotalPages());
        roleResponseDTO.setLast(roles.isLast());
        return roleResponseDTO;
    }

    @Override
    public RoleDTO save(RoleDTO roleDTO) {
        // Convert DTO to Model
        Role role = mapToModel(roleDTO);

        Role newRole = roleRepository.save(role); // Database

        // Convert Model to DTO
        RoleDTO roleResponse = mapToDTO(newRole);

        return roleResponse;
    }

    @Override
    public RoleDTO findById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol", "no encontrado", "id", String.valueOf(id)));
        return mapToDTO(role);
    }

    @Override
    public RoleDTO update(RoleDTO roleDTO, Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol", "no encontrado", "id", String.valueOf(id)));
        Role editedRole = mapToModel(roleDTO);
        role.copy(editedRole);
        roleRepository.save(role);
        return mapToDTO(role);
    }

    @Override
    public void delete(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol", "no encontrado", "id", String.valueOf(id)));
        roleRepository.delete(role);
    }

    @Override
    public RoleDTO findByName(String name) {
        Role role = roleRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Rol", "no encontrado", "nombre", name));
        return mapToDTO(role);
    }

}
