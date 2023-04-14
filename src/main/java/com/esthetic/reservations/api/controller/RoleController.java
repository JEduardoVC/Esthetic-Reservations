package com.esthetic.reservations.api.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.esthetic.reservations.api.dto.ResponseDTO;
import com.esthetic.reservations.api.dto.RoleDTO;
import com.esthetic.reservations.api.exception.EstheticAppException;
import com.esthetic.reservations.api.service.impl.RoleServiceImpl;
import com.esthetic.reservations.api.util.AppConstants;

@RestController
@RequestMapping("/api/role")
public class RoleController {

    private RoleServiceImpl roleService;

    public RoleController(RoleServiceImpl roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/all")
    public ResponseDTO<RoleDTO> findAll(
            @RequestParam(value = "pageNum", defaultValue = AppConstants.PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir) {
        return roleService.findAll(pageNumber, pageSize, sortBy, sortDir);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> findById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(roleService.findById(id));
    }

    @GetMapping
    public ResponseEntity<RoleDTO> findBy(@RequestParam(value = "by", required = true) String filterBy,
            @RequestParam(value = "filterTo", required = true) String filterTo) {
        switch (filterBy) {
            case "name":
                return ResponseEntity.ok(roleService.findByName(filterTo));
            default:
                throw new EstheticAppException(HttpStatus.BAD_REQUEST, "Campo no válido");
        }
    }

    @PostMapping
    public ResponseEntity<RoleDTO> save(@Valid @RequestBody RoleDTO roleDTO) {
        return new ResponseEntity<>(roleService.save(roleDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDTO> update(@Valid @RequestBody RoleDTO roleDTO,
            @PathVariable(name = "id") Long id) {
        RoleDTO roleReponse = roleService.update(roleDTO, id);
        return new ResponseEntity<>(roleReponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable(name = "id") Long id) {
        roleService.delete(id);
        return new ResponseEntity<>("Rol eliminado", HttpStatus.OK);
    }

}
