package com.esthetic.reservations.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.esthetic.reservations.api.repository.UserRepository;

@RestController
@RequestMapping("/app/client")
public class AppClientController {
	
	@Autowired
	UserRepository usuario;

    @GetMapping("/dashboard")
    public ModelAndView viewClientDashboard() {
        return new ModelAndView("client/dashboard");
    }
    @GetMapping("/location")
    public ModelAndView viewClientBranch() {
    	return new ModelAndView("client/branches");
    }
    @GetMapping("/services")
    public ModelAndView viewClientServices() {
        return new ModelAndView("client/services");
    }
    @GetMapping("/products")
    public ModelAndView viewClientProducts() {
        return new ModelAndView("client/products");
    }
    @GetMapping("/employees")
    public ModelAndView viewClientManager() {
    	return new ModelAndView("client/managers");
    }
    @GetMapping("/reservation")
    public ModelAndView viewClientAppointment() {
    	return new ModelAndView("client/appointment");
    }
    @GetMapping("/appointment/update")
    public ModelAndView viewClientAppointmentUpdate() {
    	return new ModelAndView("client/services");
    }
}
