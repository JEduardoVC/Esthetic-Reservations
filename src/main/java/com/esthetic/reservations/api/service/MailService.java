package com.esthetic.reservations.api.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.OutputStream;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.swing.ImageIcon;

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
    
    public void sendMultiMail(String to, String body, MultipartFile qr) throws MessagingException, IOException {
    	BodyPart mensaje = new MimeBodyPart();
    	mensaje.setText(body);
    	BodyPart imagen = new MimeBodyPart();
		ByteArrayDataSource raw = new ByteArrayDataSource(qr.getBytes(), "image/png");
    	imagen.setDataHandler(new DataHandler(raw));
    	imagen.setFileName("QR.png");
    	MimeMultipart partes = new MimeMultipart();
    	partes.addBodyPart(mensaje);
    	partes.addBodyPart(imagen);
		MimeMessage message = javaMailSender.createMimeMessage();
    	message.setFrom("gevalencia99@gmail.com");
    	message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
    	message.setSubject("Esthetic Reservation");
    	message.setContent(partes);
    	javaMailSender.send(message);
    }
}