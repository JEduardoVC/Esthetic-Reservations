package com.esthetic.reservations.api.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.esthetic.reservations.api.dto.LoginDTO;
import com.esthetic.reservations.api.dto.LoginResponseDTO;
import com.esthetic.reservations.api.dto.ResponseDTO;
import com.esthetic.reservations.api.dto.UserEntityDTO;
import com.esthetic.reservations.api.exception.BadRequestException;
import com.esthetic.reservations.api.exception.ConflictException;
import com.esthetic.reservations.api.exception.ResourceNotFoundException;
import com.esthetic.reservations.api.exception.UnauthorizedException;
import com.esthetic.reservations.api.model.UserEntity;
import com.esthetic.reservations.api.security.JwtUtil;
import com.esthetic.reservations.api.service.MailService;
import com.esthetic.reservations.api.service.impl.UserDetailsServiceImpl;
import com.esthetic.reservations.api.service.impl.UserServiceImpl;
import com.esthetic.reservations.api.util.AppConstants;
import com.esthetic.reservations.api.util.Util;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private UserDetailsServiceImpl userDetailsService;
    private UserServiceImpl userService;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;
    private Util util;

    @Autowired
    MailService mailService;

    @Autowired
    public AuthController(UserDetailsServiceImpl userDetailsService, AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder, UserServiceImpl userService, JwtUtil jwtUtil, Util util) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.util = util;
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
        UserEntity userEntity;
        Authentication authentication;
        try {
            userEntity = userService.mapToModel(userService.findByUsername(loginDTO.getUsername()));
        } catch (ResourceNotFoundException e) {
            throw new UnauthorizedException("Username", "no existe");
        }
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDTO.getUsername(), loginDTO.getPassword()));
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Contraseña", "incorrecta");
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getUsername());
        String token = this.jwtUtil.generateToken(userDetails);
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(token, userEntity.getUserRoles().subList(0, 1),
                userEntity.getId());
        return new ResponseEntity<>(loginResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/sendMail")
    public ResponseEntity<Object> sendMail(@RequestParam("mail") String mail) {
    	if(mail == "") new ResourceNotFoundException("Email", "Vacio");
        UserEntityDTO usuario = userService.findByEmail(mail);
        if(usuario == null) new BadRequestException("Email", "No existe");
        Map<String, String> map = new HashMap<String, String>();
        String message = "Correo enviado por Esthetic Reservation con el motivo de cambiar tu contraseña\n\n"
                + usuario.getName() + " " + usuario.getLastName() + "\n"
                + "Hacer click en el siguiente enlace para cambiar tu contraseña \n\n"
                + "http://localhost:5500/app/reestablecer/password/update/" + usuario.getId()
                + "\n\n"
                + "De no haber requerido este correo, favor de ignorarlo";
        try {
            mailService.sendMail("gevalencia99@gmail.com", mail, "Esthetic Reservation", message);
        	map.put("message", "Correo Enviado Correctamente");
            return new ResponseEntity<Object>(map, HttpStatus.OK);
        } catch (MailException e) {
        	map.put("message", e.getMessage());
        	return new ResponseEntity<Object>(map, HttpStatus.OK);
        }
    }
}
