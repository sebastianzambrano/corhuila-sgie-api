package com.corhuila.sgie.Notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificacionService {

    private static final Logger log = LoggerFactory.getLogger(NotificacionService.class);

    private final JavaMailSender mailSender;

    public NotificacionService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarCorreoReserva(String destinatario, String asunto, String cuerpo) {
        if (destinatario == null || destinatario.isBlank()) {
            log.warn("Notificación omitida: destinatario vacío");
            return;
        }

        String maskedEmail = maskEmail(destinatario);
        log.info("Preparando envío de correo a {}", maskedEmail);

        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setTo(destinatario);
            mensaje.setSubject(asunto);
            mensaje.setText(cuerpo);
            mensaje.setFrom("jszambrano@corhuila.edu.co");

            mailSender.send(mensaje);
            log.info("Correo enviado correctamente a {}", maskedEmail);
        } catch (Exception e) {
            log.error("Error enviando correo a {}: {}", maskedEmail, e.getMessage(), e);
        }

    }

    private String maskEmail(String email) {
        int atIndex = email.indexOf("@");
        if (atIndex <= 2) {
            return "***" + email.substring(atIndex);
        }
        return email.substring(0, 2) + "***" + email.substring(atIndex);
    }
}
