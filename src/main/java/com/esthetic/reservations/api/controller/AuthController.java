package com.esthetic.reservations.api.controller;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.esthetic.reservations.api.dto.LoginDTO;
import com.esthetic.reservations.api.dto.LoginResponseDTO;
import com.esthetic.reservations.api.dto.UserEntityDTO;
import com.esthetic.reservations.api.exception.BadRequestException;
import com.esthetic.reservations.api.exception.ConflictException;
import com.esthetic.reservations.api.exception.ResourceNotFoundException;
import com.esthetic.reservations.api.exception.UnauthorizedException;
import com.esthetic.reservations.api.model.UserEntity;
import com.esthetic.reservations.api.security.JwtUtil;
import com.esthetic.reservations.api.service.MailService;
import com.esthetic.reservations.api.service.impl.RoleServiceImpl;
import com.esthetic.reservations.api.service.impl.UserDetailsServiceImpl;
import com.esthetic.reservations.api.service.impl.UserServiceImpl;
import com.esthetic.reservations.api.util.Util;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private UserDetailsServiceImpl userDetailsService;
    private UserServiceImpl userService;
    private RoleServiceImpl roleService;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;
    private Util util;

    @Autowired
    MailService mailService;

    public AuthController(UserDetailsServiceImpl userDetailsService, AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder, UserServiceImpl userService, JwtUtil jwtUtil, Util util,
            RoleServiceImpl roleService) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.util = util;
        this.roleService = roleService;
    }

    @PostMapping("/{role}/register")
    public ResponseEntity<UserEntityDTO> register(@Valid @RequestBody UserEntityDTO userEntityDTO,
            @PathVariable(name = "role", required = true) String role) {
        try {
            Long roleId = Long.parseLong(role);
            if (userService.existsByUsername(userEntityDTO.getUsername())) {
                throw new ConflictException("Usuario", "ya está siendo usado", "nombre de usuario",
                        userEntityDTO.getUsername());
            }
            if (userService.existsByEmail(userEntityDTO.getEmail())) {
                throw new ConflictException("Usuario", "ya está siendo usado", "correo electrónico",
                        userEntityDTO.getEmail());
            }
            userEntityDTO.setPassword(this.passwordEncoder.encode(userEntityDTO.getPassword()));
            return new ResponseEntity<>(userService.register(userEntityDTO, roleId), HttpStatus.CREATED);
        } catch (NumberFormatException eFormatException) {
            if (!roleService.existsByName(role)) {
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
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(token, userEntity.getRoles(), userEntity.getId());
        return new ResponseEntity<>(loginResponseDTO, HttpStatus.OK);
    }

    @PutMapping("/update/password/{id}")
    public ResponseEntity<UserEntityDTO> updatePassword(@PathVariable("id") Long id, @RequestBody UserEntity usuario) {
        UserEntity user = userService.mapToModel(userService.findById(id));
        if (user == null)
            new ResourceNotFoundException("Usuario", "No existe el usuario");
        if (user.getEmail() != usuario.getEmail())
            new BadRequestException("Correo y usuario", "No coinciden");
        user.setPassword(this.passwordEncoder.encode(usuario.getPassword()));
        return new ResponseEntity<UserEntityDTO>(userService.save(userService.mapToDTO(user)), HttpStatus.ACCEPTED);
    }

    @PostMapping("/sendSimpleMail")
    public ResponseEntity<Object> sendsimpleMail(@RequestParam("mail") String mail) {
        if (mail == "")
            new ResourceNotFoundException("Email", "Vacio");
        UserEntityDTO usuario = userService.findByEmail(mail);
        Map<String, String> map = new HashMap<String, String>();
        String message = util.typeEmail(3, userService.mapToModel(usuario), null, null);
        try {
            mailService.sendMail(mail, message);
            map.put("message", "Correo Enviado Correctamente");
            map.put("errorCode", "200");
            return new ResponseEntity<Object>(map, HttpStatus.OK);
        } catch (MailException e) {
            map.put("message", e.getMessage());
            return new ResponseEntity<Object>(map, HttpStatus.CONFLICT);
        }
    }
}
