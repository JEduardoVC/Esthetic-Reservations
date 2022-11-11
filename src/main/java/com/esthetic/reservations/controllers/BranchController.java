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
public class BranchController {
	@Autowired
	OwnerRepository ownerRepository;
	@Autowired
	BranchRepository branchRepository;
	
	@GetMapping("/sucursal")
	public ModelAndView Sucursal() {
		return new ModelAndView("index");
	}
	
	@GetMapping("/sucursal/registro")
	public ModelAndView viewRegistroSucursal() {
		return new ModelAndView("admin/sucursal/agregar-sucursal").addObject("owners", ownerRepository.findAll());
	}
	
	public ModelAndView viewRegistroSucursal(List<String> alertas, String tipo) {
		return new ModelAndView("admin/sucursal/agregar-sucursal").addObject("alertas", formatearAlertas(alertas, tipo)).addObject("owners", ownerRepository.findAll());
	}
	
	@PostMapping("/sucursal/registro")
	public ModelAndView registroSucursal(@ModelAttribute Branch branch) {
		List<String> alertas = new ArrayList<String>();
		Branch bran = branchRepository.findByName(branch.getName());
		if(bran.getLocation().equals(branch.getLocation())) alertas.add("Sucursal ya registrada");
		if(branch.getName().equals("")) alertas.add("Nombre vacio");
		if(branch.getLocation().equals("")) alertas.add("Ubicación Vacia");
		if(branch.getId_owner() == null) alertas.add("Falto seleccionar dueño");
		if(alertas.isEmpty()) {
			branchRepository.save(branch);
			return new ModelAndView("redirect:/sucursal");
		} else {
			return viewRegistroSucursal(alertas, "error");
		}
	}
	
	@PostMapping("/sucursal/actualizar")
	public ModelAndView viewActualizarSucursal(@RequestParam(name = "id_branch") Integer id_branch) {
		return new ModelAndView("sucursal/actualizar").addObject("owners", ownerRepository.findAll()).addObject("branch", branchRepository.findById(id_branch).orElse(null));
	}
	
	@PostMapping("/sucursal/actualizando")
	public ModelAndView actualizarSucursal(@ModelAttribute Branch branch) {
		List<String> alertas = new ArrayList<String>();
		if(branch.getName().equals("")) alertas.add("Nombre vacio");
		if(branch.getLocation().equals("")) alertas.add("Ubicación Vacia");
		if(branch.getId_owner() == null) alertas.add("Falto seleccionar dueño");
		if(alertas.isEmpty()) {
			branchRepository.save(branch);
			return new ModelAndView("redirect:/sucursal");
		} else {
			return viewRegistroSucursal(alertas, "error");
		}
	}
	
	@PostMapping("/sucursal/eliminar")
	public ModelAndView eliminarSucursal(@RequestParam(name = "id_branch") int id_branch) {
		List<String> alertas = new ArrayList<String>();
		if(branchRepository.findById(id_branch).orElse(null) == null) alertas.add("Sucursal no existe");
		if(alertas.isEmpty()) {
			formatearAlertas(alertas, "Error");
			return new ModelAndView("redirect:/surcursal");
		} else {			
			branchRepository.deleteById(id_branch);
			return new ModelAndView("redirect:/sucursal");
		}
	}
	
	@PostMapping("/api/surcursal/obtener")
	public ResponseEntity<String> obtenerSucursal() {
		JSONObject json = new JSONObject();
		for (Branch sucursal : branchRepository.findAll()) {
			JSONObject jsonSucursal = new JSONObject();
			jsonSucursal.append("name", sucursal.getName());
			jsonSucursal.append("location", sucursal.getLocation());
			json.append("sucursal", jsonSucursal);
		}
		return new ResponseEntity<String>(String.valueOf(json), HttpStatus.OK);
	}
	
	@GetMapping("/sucursal/dashboard")
	public ModelAndView sucursal() {
		return new ModelAndView("client/client");
	}
	
	public static Map<String, List<String>> formatearAlertas(List<String> alertas, String tipo) {
		Map<String, List<String>> alerts = new HashMap<String, List<String>>();
		alerts.put(tipo, alertas);
		return alerts;
	}
}
