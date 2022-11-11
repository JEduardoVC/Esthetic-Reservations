package com.esthetic.reservations.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.esthetic.reservations.model.Branch;
import com.esthetic.reservations.model.Client;
import com.esthetic.reservations.model.Owner;
import com.esthetic.reservations.repository.BranchRepository;
import com.esthetic.reservations.repository.ClientRepository;
import com.esthetic.reservations.repository.OwnerRepository;

@RestController
@RequestMapping
public class LoginController {

	private Client clienteLogueado;

	@Autowired
	ClientRepository clienteRepository;
	@Autowired
	OwnerRepository ownerRepository;
	@Autowired
	BranchRepository branchRepository;

	@GetMapping
	public ModelAndView index() {
		ModelAndView inicio = new ModelAndView("Login/login");
		return inicio;
	}

	public ModelAndView index(List<String> alertas, String tipo) {
		return new ModelAndView("Login/login").addObject("alertas", formatearAlertas(alertas, tipo));
	}

	@PostMapping("/")
	public ModelAndView login(@ModelAttribute Client cliente) {
		Client client = clienteRepository.findByEmail(cliente.getEmail());
		List<String> alertas = new ArrayList<String>();
		if (client != null) {
			if (client.getPassword().equals(cliente.getPassword())) {
				clienteLogueado = client;
				return new ModelAndView("redirect:sucursal");
			} else {
				alertas.add("Contraseña incorrecta");
			}
		} else {
			alertas.add("Usuario no existe");
		}
		return index(alertas, "error");
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

	@PostMapping("/registro")
	public ModelAndView registro(@ModelAttribute Client cliente,
			@RequestParam(name = "repeat_password") String repeat) {
		List<String> alertas = new ArrayList<String>();
		if (cliente.getFirst_name().equalsIgnoreCase("") || cliente.getLast_name().equalsIgnoreCase("")
				|| cliente.getPhone_number().equalsIgnoreCase("") || cliente.getEmail().equalsIgnoreCase("")
				|| cliente.getPassword().equalsIgnoreCase("")) {
			alertas.add("Debes llenar todos los campos");
			return registro(alertas, "error");
		}
		if (!repeat.equals(cliente.getPassword())) {
			alertas.add("Las contraseñas no coinciden");
		}
		if (cliente.getPhone_number().length() != 10) {
			alertas.add("El numero de telefono debe ser de 10 digitos");
		}
		if (!validateEmail(cliente.getEmail())) {
			alertas.add("Introduce un correo electronico valido");
		}
		if (!validatePassword(cliente.getPassword())) {
			alertas.add("Introduce una contraseña segura");
		}
		if (!alertas.isEmpty()) {
			return registro(alertas, "error");
		}
		Client client = clienteRepository.findByEmail(cliente.getEmail());
		if (client != null) {
			alertas.add("El correo ya está siendo usado");
			return registro(alertas, "error");
		} else {
			clienteRepository.save(cliente);
			alertas.add("Usuario creado con éxito.");
		}
		return registro(alertas, "successful");
	}

	public static boolean validateEmail(String emailStr) {
		Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
		return matcher.find();
	}

	public static boolean validatePassword(String passwordStr) {
		Pattern VALID_PASSWORD_REGEX = Pattern
				.compile("\\A(?=\\S*?[0-9])(?=\\S*?[a-z])(?=\\S*?[A-Z])(?=\\S*?[@#$%^&+=!])\\S{8,}\\z");
		Matcher matcher = VALID_PASSWORD_REGEX.matcher(passwordStr);
		return matcher.find();
	}

	public static Map<String, List<String>> formatearAlertas(List<String> alertas, String tipo) {
		Map<String, List<String>> alerts = new HashMap<String, List<String>>();
		alerts.put(tipo, alertas);
		return alerts;
	}
}
