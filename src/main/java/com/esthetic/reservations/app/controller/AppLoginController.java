package com.esthetic.reservations.app.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.esthetic.reservations.api.service.MailService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/app")
public class AppLoginController {

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

    public ModelAndView viewLogin(List<String> alertas, String tipo) {
        return new ModelAndView("Login/login").addObject("alertas", formatearAlertas(alertas, tipo));
    }

    @GetMapping("/registro")
    public ModelAndView Register() {
        return new ModelAndView("Login/registro");
    }
    
    @GetMapping("/reestablecer/password")
    public ModelAndView olvidePassword() {
    	return new ModelAndView("Login/olvide");
    }
}
