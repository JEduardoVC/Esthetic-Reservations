package com.esthetic.reservations.api.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.esthetic.reservations.api.dto.ResponseDTO;
import com.esthetic.reservations.api.dto.UserEntityDTO;
import com.esthetic.reservations.api.exception.EstheticAppException;
import com.esthetic.reservations.api.service.impl.UserServiceImpl;
import com.esthetic.reservations.api.util.AppConstants;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserServiceImpl userService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserServiceImpl userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ResponseEntity<UserEntityDTO> findBy(@RequestParam(value = "by", required = true) String filterBy,
            @RequestParam(value = "filterTo", required = true) String filterTo) {
        switch (filterBy) {
            case "username":
                return ResponseEntity.ok(userService.findByUsername(filterTo));
            case "email":
                return ResponseEntity.ok(userService.findByEmail(filterTo));
            default:
                throw new EstheticAppException(HttpStatus.BAD_REQUEST, "Campo no válido");
        }
    }

    @GetMapping("/all")
    public ResponseDTO<UserEntityDTO> findAll(
            @RequestParam(value = "pageNum", defaultValue = AppConstants.PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir) {
        return userService.findAll(pageNumber, pageSize, sortBy, sortDir);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserEntityDTO> findById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    
    @PutMapping("/{id}/grant/{role}")
    public ResponseEntity<UserEntityDTO> grantRoleToUser(@PathVariable(name = "id", required = true) Long userId, @PathVariable(name = "role", required = true) String role){
        return new ResponseEntity<>(userService.grantRoleToUser(userId, role), HttpStatus.OK);
    }

    @PutMapping("/{id}/revoke/{role}")
    public ResponseEntity<UserEntityDTO> revokeRoleToUser(@PathVariable(name = "id", required = true) Long userId, @PathVariable(name = "role", required = true) String role){
        return new ResponseEntity<>(userService.revokeRoleToUser(userId, role), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserEntityDTO> update(@Valid @RequestBody UserEntityDTO userDTO,
            @PathVariable(name = "id") Long id) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        UserEntityDTO userReponse = userService.update(userDTO, id);
        return new ResponseEntity<>(userReponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable(name = "id") Long id) {
        userService.delete(id);
        return new ResponseEntity<>("Usuario eliminado", HttpStatus.OK);
    }

}
