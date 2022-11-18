package com.esthetic.reservations.app.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.esthetic.reservations.api.service.impl.UserServiceImpl;

@RestController
@RequestMapping("/app/client")
public class AppClientController {
	
	private UserServiceImpl usuario;

    @PostMapping()
    public ModelAndView viewClient(@RequestParam(name = "username") String username) {
        return new ModelAndView("client/client").addObject("usuario", usuario.findByUsername(username));
    }
}
