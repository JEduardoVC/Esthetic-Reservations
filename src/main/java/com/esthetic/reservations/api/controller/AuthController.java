package com.esthetic.reservations.api.controller;

import java.util.ArrayList;
import java.util.List;
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
import com.esthetic.reservations.api.dto.RoleDTO;
import com.esthetic.reservations.api.dto.UserEntityDTO;
import com.esthetic.reservations.api.dto.UserRoleDTO;
import com.esthetic.reservations.api.exception.BadRequestException;
import com.esthetic.reservations.api.exception.ConflictException;
import com.esthetic.reservations.api.model.Role;
import com.esthetic.reservations.api.security.JwtUtil;
import com.esthetic.reservations.api.service.MailService;
import com.esthetic.reservations.api.service.impl.RoleServiceImpl;
import com.esthetic.reservations.api.dto.UserEntityDTO;
import com.esthetic.reservations.api.exception.BadRequestException;
import com.esthetic.reservations.api.exception.ConflictException;
import com.esthetic.reservations.api.security.JwtUtil;
import com.esthetic.reservations.api.service.impl.UserDetailsServiceImpl;
import com.esthetic.reservations.api.service.impl.UserServiceImpl;
import com.esthetic.reservations.api.util.AppConstants;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private UserDetailsServiceImpl userDetailsService;
    private UserServiceImpl userService;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;
    
    @Autowired
	MailService mailService;

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
    public ResponseEntity<ArrayList<Object>> login(@Valid @RequestBody LoginDTO loginDTO) {
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDTO.getUsername(), loginDTO.getPassword()));
        } catch (BadCredentialsException e) {
            // Test with email
            if (!userService.existsByUsername(loginDTO.getUsername())) {
                throw new BadCredentialsException("Username No Existe");
            } else if(!loginDTO.getPassword().equals(userService.findByUsername(loginDTO.getUsername()).getPassword())) {
            	throw new BadCredentialsException("Contraseña Incorrecta");
            if (!userService.existsByEmail(loginDTO.getUsername())) {
                throw new BadCredentialsException("Bad credentials");
            }
            loginDTO.setUsername(userService.findByEmail(loginDTO.getUsername()).getUsername());
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDTO.getUsername(), loginDTO.getPassword()));
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getUsername());
        List<Role> roles = userService.findByUsername(loginDTO.getUsername()).getUserRoles();
        String token = this.jwtUtil.generateToken(userDetails);
        ArrayList<Object> arr = new ArrayList<>();
        arr.add(new LoginResponseDTO(token));
        arr.add(roles.get(0));
        return ResponseEntity.ok(arr);
    }
    
	@PostMapping("/sendMail")
    public ResponseEntity<String> sendMail(@RequestParam("mail") String mail) {
		UserEntityDTO usuario = userService.findByEmail(mail);
        String message = "Correo enviado por Esthetic Reservation con el motivo de cambiar tu contraseña\n\n"
        		+ usuario.getName() + " " + usuario.getLastName() + "\n"
        		+ "Hacer click en el siguiente enlace para cambiar tu contraseña \n\n"
        		+ "De no haber requerido este correo, favor de ignorarlo";
        try {
        	mailService.sendMail("gevalencia99@gmail.com",mail,"Esthetic Reservation",message);
		} catch (MailException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
        return new ResponseEntity<String>("Enviado", HttpStatus.OK);
    }
        String token = this.jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

}
