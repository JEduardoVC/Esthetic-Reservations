package com.esthetic.reservations.app.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public ModelAndView registro(List<String> alertas, String tipo) {
        Map<String, List<String>> alerts = new HashMap<String, List<String>>();
        alerts.put(tipo, alertas);
        ModelAndView registro = new ModelAndView("Login/registro");
        registro.addObject("alertas", alerts);
        return registro;
    }

    public static Map<String, List<String>> formatearAlertas(List<String> alertas, String tipo) {
        Map<String, List<String>> alerts = new HashMap<String, List<String>>();
        alerts.put(tipo, alertas);
        return alerts;
    }

}
