package com.esthetic.reservations.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/unauthorized")
    public ModelAndView unauthorizedView() {
        ModelAndView model = new ModelAndView("/unauthorized");
        return model;
    }

    @GetMapping("/forbidden")
    public ModelAndView forbiddenView(@ModelAttribute("home") String home) {
        ModelAndView model = new ModelAndView("/forbidden");
        model.addObject("home", home);
        return model;
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
        return new ModelAndView("Login/updatePassword");
    }

    @GetMapping("/cerrar")
    public ModelAndView cerrarSesion() {
        return new ModelAndView("redirect: ");
    }
}
