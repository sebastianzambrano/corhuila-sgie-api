package com.corhuila.sgie.Booking.Controller;

import com.corhuila.sgie.Booking.DTO.*;
import com.corhuila.sgie.Booking.IService.IDetalleReservaEquipoService;
import com.corhuila.sgie.Booking.Service.DetalleReservaEquipoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DetalleReservaEquipoControllerTest {

    @Mock
    private IDetalleReservaEquipoService detalleReservaEquipoServiceFacade;
    @Mock
    private DetalleReservaEquipoService detalleReservaEquipoService;

    private DetalleReservaEquipoController controller;

    @BeforeEach
    void setup() {
        controller = new DetalleReservaEquipoController(detalleReservaEquipoServiceFacade, detalleReservaEquipoService);
    }

    @Test
    void cerrarDetalleReservaEquipoDevuelveRespuesta() {
        CerrarDetalleReservaEquipoDTO request = new CerrarDetalleReservaEquipoDTO();
        request.setEntregaEquipo("Devuelto");

        CerrarDetalleReservaEquipoResponseDTO responseDto = new CerrarDetalleReservaEquipoResponseDTO(1L, false, "Devuelto", LocalDateTime.now(), 2L);
        when(detalleReservaEquipoService.cerrarDetalleReservaEquipo(1L, "Devuelto")).thenReturn(responseDto);

        ResponseEntity<CerrarDetalleReservaEquipoResponseDTO> response = controller.cerrarDetalleReservaEquipo(1L, request);
        assertThat(response.getBody()).isSameAs(responseDto);
    }

    @Test
    void actualizarDetalleReservaEquipoDevuelveResultado() {
        ActualizarReservaDetalleEquipoRequestDTO request = new ActualizarReservaDetalleEquipoRequestDTO();
        request.setProgramaAcademico("Ingeniería");

        DetalleReservaEquipoResponseDTO dto = new DetalleReservaEquipoResponseDTO();
        dto.setProgramaAcademico("Ingeniería");
        when(detalleReservaEquipoService.actualizarDetalleReservaEquipo(2L, request)).thenReturn(dto);

        ResponseEntity<DetalleReservaEquipoResponseDTO> response = controller.actualizarDetalleReservaEquipo(2L, request);
        assertThat(response.getBody().getProgramaAcademico()).isEqualTo("Ingeniería");
    }

    @Test
    void findReservasEquipoByNumeroIdentificacionUsaFacade() {
        IReservaEquipoDTO dto = mock(IReservaEquipoDTO.class);
        when(detalleReservaEquipoServiceFacade.findReservasEquipoByNumeroIdentificacion("123"))
                .thenReturn(List.of(dto));

        ResponseEntity<List<IReservaEquipoDTO>> response = controller.findReservasEquipoByNumeroIdentificacion("123");
        assertThat(response.getBody()).containsExactly(dto);
    }
}
