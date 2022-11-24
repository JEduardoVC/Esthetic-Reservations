package com.esthetic.reservations.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.esthetic.reservations.api.service.impl.UserServiceImpl;

@RestController
@RequestMapping("/app")
public class AppLoginController {
	
	UserServiceImpl userServiceImpl;

    @GetMapping
    public ModelAndView index() {
        ModelAndView inicio = new ModelAndView("index");
        return inicio;
    }

    @GetMapping("/login")
    public ModelAndView viewLogin() {
        ModelAndView inicio = new ModelAndView("Login/login");
        return inicio;
    }

    @GetMapping("/registro")
    public ModelAndView Register() {
        return new ModelAndView("Login/registro");
    }
    
    @GetMapping("/reestablecer/password")
    public ModelAndView olvidePassword() {
    	return new ModelAndView("Login/olvidePassword");
    }
    
    @GetMapping("/reestablecer/password/update/{id}")
    public ModelAndView reestablecerPassword(@PathVariable("id") Long id_usuario) {
    	return new ModelAndView("Login/updatePassword").addObject("usuario", userServiceImpl.findById(id_usuario));
    }
}
