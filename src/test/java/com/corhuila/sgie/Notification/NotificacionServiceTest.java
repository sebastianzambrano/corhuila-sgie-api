package com.corhuila.sgie.Notification;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacionServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private NotificacionService service;

    @Test
    void noEnviaCorreoSiDestinatarioVacio() {
        service.enviarCorreoReserva(" ", "Asunto", "Cuerpo");
        verifyNoInteractions(mailSender);
    }

    @Test
    void enviaCorreoConMascara() {
        service.enviarCorreoReserva("user@mail.com", "Asunto", "Mensaje");

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());
        SimpleMailMessage message = captor.getValue();
        assertThat(message.getTo()).containsExactly("user@mail.com");
        assertThat(message.getSubject()).isEqualTo("Asunto");
    }
}
