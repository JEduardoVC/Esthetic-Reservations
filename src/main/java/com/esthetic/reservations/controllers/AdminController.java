package com.esthetic.reservations.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.esthetic.reservations.model.Branch;
import com.esthetic.reservations.repository.BranchRepository;
import com.esthetic.reservations.repository.OwnerRepository;

@RestController
@RequestMapping
public class AdminController {
	
	@Autowired
	OwnerRepository ownerRepository;
	@Autowired
	BranchRepository branchRepository;
	
	@GetMapping("/admin")
	public ModelAndView viewAdmin() {
		return new ModelAndView("admin/admin");
	}
	
	@GetMapping("/admin/sucursales")
	public ModelAndView viewUsuarios() {
		return new ModelAndView("admin/branch/sucursales");
	}
}