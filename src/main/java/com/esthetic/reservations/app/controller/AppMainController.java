package com.esthetic.reservations.app.controller;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class AppMainController {

    @GetMapping
    public void index(HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader("Location", "app/");
        httpServletResponse.setStatus(302);
    }

}
