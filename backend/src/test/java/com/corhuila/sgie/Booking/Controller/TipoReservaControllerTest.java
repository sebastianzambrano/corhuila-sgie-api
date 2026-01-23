package com.corhuila.sgie.Booking.Controller;

import com.corhuila.sgie.Booking.Entity.TipoReserva;
import com.corhuila.sgie.Booking.IService.ITipoReservaService;
import com.corhuila.sgie.common.ApiResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TipoReservaControllerTest {

    @Mock
    private ITipoReservaService service;

    private TipoReservaController controller;

    @BeforeEach
    void setup() {
        controller = new TipoReservaController(service);
    }

    @Test
    void findByStateTrueDevuelveRespuesta() {
        TipoReserva tipo = new TipoReserva();
        tipo.setNombre("Equipo");
        when(service.findByStateTrue()).thenReturn(List.of(tipo));

        ResponseEntity<ApiResponseDto<List<TipoReserva>>> response = controller.findByStateTrue();
        assertThat(response.getBody().getData()).containsExactly(tipo);
    }
}
