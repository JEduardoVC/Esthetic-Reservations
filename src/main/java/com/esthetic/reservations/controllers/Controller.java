package com.esthetic.reservations.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.esthetic.reservations.model.Client;
import com.esthetic.reservations.repository.ClientRepository;

@RestController
@RequestMapping
public class Controller {
	
	private Client clienteLogueado;
	
	@Autowired
	ClientRepository clienteRepository;
	
	@GetMapping
	public ModelAndView Index() {
		ModelAndView inicio = new ModelAndView("Login/login");
		return inicio;
	}
	
	public ModelAndView Index(List<String> alertas, String tipo) {
		Map<String, List<String>> alerts = new HashMap<String, List<String>>();
		alerts.put(tipo, alertas);
		return new ModelAndView("Login/login").addObject("alertas", alerts);
	}
	
	@PostMapping("/")
	public ModelAndView Login(@ModelAttribute Client cliente) {
		Client client = clienteRepository.findByEmail(cliente.getEmail());
		List<String> alertas = new ArrayList<String>();
		if(client != null) {
			if(client.getPassword().equals(cliente.getPassword())) {
				clienteLogueado = client;
				return new ModelAndView("redirect:Dashboard");
			} else {
				alertas.add("Contraseña incorrecta");
			}
		} else {
			alertas.add("Usuario no existe");
		}
		return Index(alertas, "error");
	}
	
	@GetMapping("/registro")
	public ModelAndView Register() {
		return new ModelAndView("Login/registro");
	}
	
	public ModelAndView Registro(Map<String, String> alertas) {
		ModelAndView registro = new ModelAndView("Login/registro");
		registro.addObject("alertas", alertas);
		System.out.println(alertas.toString());
		return registro;
	}

	@PostMapping("/registro")
	public ModelAndView Registro(@ModelAttribute Client cliente,
			@RequestParam(name = "repeat_password") String repeat) {
		Map<String, String> alertas = new HashMap<String, String>();
		if (cliente.getFirst_name().equalsIgnoreCase("") ||
				cliente.getLast_name().equalsIgnoreCase("") ||
				cliente.getPhone_number().equalsIgnoreCase("") ||
				cliente.getEmail().equalsIgnoreCase("") ||
				cliente.getPassword().equalsIgnoreCase("")) {
			alertas.put("error", "Debes llenar todos los campos");
			return Registro(alertas);
		}
		int cont = 0;
		if (!repeat.equals(cliente.getPassword())) {
			alertas.put("error" + cont++, "Las contraseñas no coinciden");
		}
		if (cliente.getPhone_number().length() != 10) {
			alertas.put("error" + cont++, "El numero de telefono debe ser de 10 digitos");
		}
		if (!validateEmail(cliente.getEmail())) {
			alertas.put("error" + cont++, "Introduce un correo electronico valido");
		}
		if (!validatePassword(cliente.getPassword())) {
			alertas.put("error" + cont++, "Introduce una contraseña segura");
		}
		if (!alertas.isEmpty()) {
			return Registro(alertas);
		}
		List<Client> clients = (List<Client>) clienteRepository.findByEmail(cliente.getEmail());
		if (clients != null && clients.size() != 0) {
			alertas.put("error" + cont++, "Correo ya está siendo usado");
		} else {
			clienteRepository.save(cliente);
			alertas.put("ok", "Usuario creado con éxito.");
		}
		return Registro(alertas);
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

	@GetMapping("/Dashboard")
	public ModelAndView Dashboard() {
		return new ModelAndView("index");
	}
}
