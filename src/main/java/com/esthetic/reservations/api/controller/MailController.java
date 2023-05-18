package com.esthetic.reservations.api.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.esthetic.reservations.api.dto.UserEntityDTO;
import com.esthetic.reservations.api.exception.ResourceNotFoundException;
import com.esthetic.reservations.api.service.impl.MailServiceImpl;

@RestController
@RequestMapping("/api/mail")
public class MailController {
	
	@Autowired
	MailServiceImpl mailServiceImpl;
	
    @PostMapping("/new/appointment")
    public ResponseEntity<Object> sendMailNewAppointment(@RequestParam("mail") Long mail, @RequestParam("branch") Long id, @RequestParam("id") Long id_cita) {
    	return new ResponseEntity<Object>(mailServiceImpl.sendMailAppoitment(mail, id, id_cita, true), HttpStatus.ACCEPTED);
    }
    
    @PutMapping("/update/appointment")
    public ResponseEntity<Object> sendMailUpdateAppointment(@RequestParam("mail") Long mail, @RequestParam("branch") Long id, @RequestParam("id") Long id_cita) {
    	return new ResponseEntity<Object>(mailServiceImpl.sendMailAppoitment(mail, id, id_cita, false), HttpStatus.ACCEPTED);
    }
    
    @PostMapping("/new/sale")
    public ResponseEntity<Object> sendMailNewSale(@RequestParam("mail") Long mail, @RequestParam("branch") Long id, @RequestParam("id") Long id_sale) {
    	return new ResponseEntity<Object>(mailServiceImpl.sendMailSale(mail, id, id_sale, true), HttpStatus.ACCEPTED);
    }
    
    @PutMapping("/update/sale")
    public ResponseEntity<Object> sendMailUpdateSale(@RequestParam("mail") Long mail, @RequestParam("branch") Long id, @RequestParam("id") Long id_sale) {
    	return new ResponseEntity<Object>(mailServiceImpl.sendMailSale(mail, id, id_sale, false), HttpStatus.ACCEPTED);
    }
    
    @PostMapping("/reestablecer")
    public ResponseEntity<Object> sendsimpleMail(@RequestParam("mail") String mail) {
        return new ResponseEntity<Object>(mailServiceImpl.sendMailReestablecer(mail), HttpStatus.ACCEPTED);
    }
}
