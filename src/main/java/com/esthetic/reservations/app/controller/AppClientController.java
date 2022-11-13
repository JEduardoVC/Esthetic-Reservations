package com.esthetic.reservations.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/app/client")
public class AppClientController {

    @GetMapping("/client")
    public ModelAndView viewClient() {
        return new ModelAndView("client/client");
    }

}
