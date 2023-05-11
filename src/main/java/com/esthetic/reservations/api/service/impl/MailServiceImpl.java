package com.esthetic.reservations.api.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

import com.esthetic.reservations.api.dto.UserEntityDTO;
import com.esthetic.reservations.api.exception.ResourceNotFoundException;
import com.esthetic.reservations.api.model.Appointment;
import com.esthetic.reservations.api.model.Branch;
import com.esthetic.reservations.api.model.Sale;
import com.esthetic.reservations.api.model.UserEntity;
import com.esthetic.reservations.api.service.MailService;
import com.esthetic.reservations.api.util.Util;

@Service
public class MailServiceImpl {
	
	@Autowired
	AppointmentServiceImpl appointmentServiceImpl;
	
	@Autowired
	SaleServiceImpl saleServiceImpl;
	
	@Autowired
	UserServiceImpl userServiceImpl;
		
	@Autowired
	BranchServiceImpl branchServiceImpl;
	
	@Autowired
	Util util;
	
	@Autowired
	MailService mailService;
	
	public Object sendMailAppoitment(Long id_usuario, Long id_branch, Long id_cita, boolean created) {
		Appointment cita = appointmentServiceImpl.mapToModel(appointmentServiceImpl.findById(id_cita));
        UserEntity usuario = userServiceImpl.mapToModel(userServiceImpl.findById(id_usuario));
        Branch sucursal = branchServiceImpl.mapToModel(branchServiceImpl.findById(id_branch));
        Map<String, String> map = new HashMap<String, String>();
        String message = util.typeEmail(created ? 1 : 2, usuario, sucursal, cita, null);
        try {
			mailService.sendMultiMail(usuario.getEmail(), message, id_cita);
        	map.put("message", "Correo Enviado Correctamente");
        	map.put("errorCode", "200");
            return new ResponseEntity<Object>(map, HttpStatus.OK);
        } catch (MessagingException | IOException e) {
        	map.put("message", e.getMessage());
        	return new ResponseEntity<Object>(map, HttpStatus.CONFLICT);
        }
	}
	
	public Object sendMailSale(Long id_usuario, Long id_branch, Long id_sale, boolean created) {
		Sale venta = saleServiceImpl.mapToModel(saleServiceImpl.findById(id_sale));
		UserEntity usuario = userServiceImpl.mapToModel(userServiceImpl.findById(id_usuario));
        Branch sucursal = branchServiceImpl.mapToModel(branchServiceImpl.findById(id_branch));
        Map<String, String> map = new HashMap<String, String>();
        String message = util.typeEmail(created ? 5 : 6, usuario, sucursal, null, venta);
        try {
			mailService.sendMultiMail(usuario.getEmail(), message, id_sale);
        	map.put("message", "Correo Enviado Correctamente");
        	map.put("errorCode", "200");
            return new ResponseEntity<Object>(map, HttpStatus.OK);
        } catch (MessagingException | IOException e) {
        	map.put("message", e.getMessage());
        	return new ResponseEntity<Object>(map, HttpStatus.CONFLICT);
        }
	}
	
	public Object sendMailReestablecer(String mail) {
		if (mail == "")
            new ResourceNotFoundException("Email", "Vacio");
        UserEntity usuario = userServiceImpl.mapToModel(userServiceImpl.findByEmail(mail));
        Map<String, String> map = new HashMap<String, String>();
        String message = util.typeEmail(3, usuario, null, null, null);
        try {
            mailService.sendMail(mail, message);
            map.put("message", "Correo Enviado Correctamente");
            map.put("errorCode", "200");
            return new ResponseEntity<Object>(map, HttpStatus.OK);
        } catch (MailException e) {
            map.put("message", e.getMessage());
            return new ResponseEntity<Object>(map, HttpStatus.CONFLICT);
        }
	}
}
