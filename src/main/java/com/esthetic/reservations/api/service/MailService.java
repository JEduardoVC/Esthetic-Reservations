package com.esthetic.reservations.api.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMail(String to, String body) {

        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setFrom("gevalencia99@gmail.com");
        mail.setTo(to);
        mail.setSubject("Esthetic Reservation");
        mail.setText(body);

        javaMailSender.send(mail);
    }
    
    public void sendMultiMail(String to, String body, MultipartFile qr) throws MessagingException {
    	MimeMessage message = javaMailSender.createMimeMessage();
    	MimeMessageHelper helper = new MimeMessageHelper(message, true);
    	helper.setFrom("gevalencia99@gmail.com");
    	helper.setTo(to);
    	helper.setSubject("Esthetic Reservation");
    	helper.setText(body);
    	helper.addAttachment("Código QR", qr);
    	
    	javaMailSender.send(message);
    }
}