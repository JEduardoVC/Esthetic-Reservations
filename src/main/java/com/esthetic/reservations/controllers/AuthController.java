package com.esthetic.reservations.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esthetic.reservations.api.dto.LoginDTO;
import com.esthetic.reservations.api.dto.LoginResponseDTO;
import com.esthetic.reservations.api.dto.UserEntityDTO;
import com.esthetic.reservations.api.exception.BadRequestException;
import com.esthetic.reservations.api.exception.ConflictException;
import com.esthetic.reservations.api.security.JwtUtil;
import com.esthetic.reservations.api.util.AppConstants;
import com.esthetic.reservations.implemen.service.UserDetailsServiceImpl;
import com.esthetic.reservations.implemen.service.UserServiceImpl;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private UserDetailsServiceImpl userDetailsService;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private UserServiceImpl userService;
    private JwtUtil jwtUtil;

    @Autowired
    public AuthController(UserDetailsServiceImpl userDetailsService, AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder, UserServiceImpl userService, JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/{role}/register")
    public ResponseEntity<UserEntityDTO> register(@Valid @RequestBody UserEntityDTO userEntityDTO,
            @PathVariable(name = "role", required = true) String role) {
        if (!role.equalsIgnoreCase(AppConstants.ADMIN_ROLE_NAME)
                && !role.equalsIgnoreCase(AppConstants.OWNER_ROLE_NAME)
                && !role.equalsIgnoreCase(AppConstants.EMPLOYEE_ROLE_NAME)
                && !role.equalsIgnoreCase(AppConstants.CLIENT_ROLE_NAME)) {
            throw new BadRequestException("Rol", "no es válido", "nombre", role);

        }
        if (userService.existsByUsername(userEntityDTO.getUsername())) {
            throw new ConflictException("Usuario", "ya está siendo usado", "nombre de usuario",
                    userEntityDTO.getUsername());
        }
        if (userService.existsByEmail(userEntityDTO.getEmail())) {
            throw new ConflictException("Usuario", "ya está siendo usado", "correo electrónico",
                    userEntityDTO.getEmail());
        }
        userEntityDTO.setPassword(this.passwordEncoder.encode(userEntityDTO.getPassword()));
        return new ResponseEntity<>(userService.register(userEntityDTO, role), HttpStatus.CREATED);

    }

    @PostMapping("/user/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDTO.getUsername(), loginDTO.getPassword()));
        } catch (BadCredentialsException e) {
            // Test with email
            if (!userService.existsByEmail(loginDTO.getUsername())) {
                throw new BadCredentialsException("Bad credentials");
            }
            loginDTO.setUsername(userService.findByEmail(loginDTO.getUsername()).getUsername());
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDTO.getUsername(), loginDTO.getPassword()));
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getUsername());
        String token = this.jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

}
