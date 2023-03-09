package com.esthetic.reservations.api.service;

import java.io.File;
import java.io.IOException;
import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

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
    
    public void sendMultiMail(String to, String body, Long id) throws MessagingException, IOException {
    	File file = QRCode.from("http://localhost:5500/app/appointment/" + id).withSize(500, 500).to(ImageType.PNG).file();
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("gevalencia99@gmail.com");
        helper.setTo(to);
        helper.setSubject("Esthetic-Reservation");
        helper.setText(body);
        helper.addAttachment("Code Qr.png", file);
    	javaMailSender.send(message);
    }
}