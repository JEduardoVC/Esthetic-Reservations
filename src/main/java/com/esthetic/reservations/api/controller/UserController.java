package com.esthetic.reservations.api.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.Valid;

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

import com.esthetic.reservations.api.dto.MessageDTO;
import com.esthetic.reservations.api.dto.ResponseDTO;
import com.esthetic.reservations.api.dto.UserEntityDTO;
import com.esthetic.reservations.api.dto.UserEntityEditDTO;
import com.esthetic.reservations.api.exception.BadRequestException;
import com.esthetic.reservations.api.exception.EstheticAppException;
import com.esthetic.reservations.api.service.impl.UserServiceImpl;
import com.esthetic.reservations.api.util.AppConstants;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserServiceImpl userService;
    private PasswordEncoder passwordEncoder;

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
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir,
            @RequestParam(value = "by", required = false, defaultValue = "na") String filterBy,
            @RequestParam(value = "filterTo", required = false) String filterTo) {
        switch (filterBy) {
            case "role":
                return userService.findAllByRole(pageNumber, pageSize, sortBy, sortDir, filterTo);
            default:
                return userService.findAll(pageNumber, pageSize, sortBy, sortDir);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserEntityDTO> findById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PutMapping("/{id}/grant/{role}")
    public ResponseEntity<UserEntityDTO> grantRoleToUser(@PathVariable(name = "id", required = true) Long userId,
            @PathVariable(name = "role", required = true) String role) {
        return new ResponseEntity<>(userService.grantRoleToUser(userId, role), HttpStatus.OK);
    }

    @PutMapping("/{id}/revoke/{role}")
    public ResponseEntity<UserEntityDTO> revokeRoleToUser(@PathVariable(name = "id", required = true) Long userId,
            @PathVariable(name = "role", required = true) String role) {
        return new ResponseEntity<>(userService.revokeRoleToUser(userId, role), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserEntityDTO> update(@Valid @RequestBody UserEntityEditDTO userDTO,
            @PathVariable(name = "id") Long id) {
        if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
            UserEntityDTO userReponse = userService.updateNoPassword(userDTO, id);
            return new ResponseEntity<>(userReponse, HttpStatus.OK);
        } else {
            @Valid UserEntityDTO editUserDTO = new @Valid UserEntityDTO(0l, userDTO.getUsername(), userDTO.getName(),
                    userDTO.getLastName(), userDTO.getPhoneNumber(), userDTO.getAddress(),
                    userDTO.getEmail(), userDTO.getPassword(), null);
            return updatePassword(editUserDTO, id);
        }
    }

    private ResponseEntity<UserEntityDTO> updatePassword(@Valid UserEntityDTO userDTO, Long id) {
        if(!isValidPassword(userDTO.getPassword())){
            throw new BadRequestException("Contraseña", "inválida. La contraseña debe contener al menos 8 caracteres, un número, una mayúsucula y un símbolo especial (@#$%^&+=!)");
        }
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        UserEntityDTO userReponse = userService.update(userDTO, id);
        return new ResponseEntity<>(userReponse, HttpStatus.OK);
    }

    private Boolean isValidPassword(String password){
        final String PATTERN = "\\A(?=\\S*?[0-9])(?=\\S*?[a-z])(?=\\S*?[A-Z])(?=\\S*?[@#$%^&+=!])\\S{8,}\\z";
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDTO> delete(@PathVariable(name = "id") Long id) {
        userService.delete(id);
        return new ResponseEntity<>(new MessageDTO("Usuario eliminado"), HttpStatus.OK);
    }

}
