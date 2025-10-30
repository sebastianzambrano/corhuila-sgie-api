package com.corhuila.sgie.Booking.Controller;

import com.corhuila.sgie.Booking.DTO.*;
import com.corhuila.sgie.Booking.IService.IDetalleReservaInstalacionService;
import com.corhuila.sgie.Booking.Service.DetalleReservaInstalacionService;
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
class DetalleReservaInstalacionControllerTest {

    @Mock
    private IDetalleReservaInstalacionService detalleReservaInstalacionServiceFacade;
    @Mock
    private DetalleReservaInstalacionService detalleReservaInstalacionService;

    private DetalleReservaInstalacionController controller;

    @BeforeEach
    void setup() {
        controller = new DetalleReservaInstalacionController(detalleReservaInstalacionServiceFacade, detalleReservaInstalacionService);
    }

    @Test
    void cerrarDetalleReservaInstalacionDevuelveRespuesta() {
        CerrarDetalleReservaInstalacionRequestDTO request = new CerrarDetalleReservaInstalacionRequestDTO();
        request.setEntregaInstalacion("Devuelto");

        CerrarDetalleReservaInstalacionResponseDTO dto = new CerrarDetalleReservaInstalacionResponseDTO(1L, false, "Devuelto", LocalDateTime.now(), 2L);
        when(detalleReservaInstalacionService.cerrarDetalleReservaInstalacion(1L, "Devuelto"))
                .thenReturn(dto);

        ResponseEntity<CerrarDetalleReservaInstalacionResponseDTO> response = controller.cerrarDetalleReservaInstalacion(1L, request);
        assertThat(response.getBody()).isSameAs(dto);
    }

    @Test
    void actualizarDetalleReservaInstalacionDevuelveRespuesta() {
        ActualizarReservaDetalleInstalacionRequestDTO request = new ActualizarReservaDetalleInstalacionRequestDTO();
        request.setProgramaAcademico("Ingeniería");

        DetalleReservaInstalacionResponseDTO dto = new DetalleReservaInstalacionResponseDTO();
        dto.setProgramaAcademico("Ingeniería");
        when(detalleReservaInstalacionService.actualizarDetalleReservaInstalacion(2L, request)).thenReturn(dto);

        ResponseEntity<DetalleReservaInstalacionResponseDTO> response = controller.actualizarDetalleReservaInstalacion(2L, request);
        assertThat(response.getBody().getProgramaAcademico()).isEqualTo("Ingeniería");
    }

    @Test
    void findReservaInstalacionDelegatesToFacade() {
        IReservaInstalacionDTO dto = mock(IReservaInstalacionDTO.class);
        when(detalleReservaInstalacionServiceFacade.findReservaInstalacionByNumeroIdentificacion("123"))
                .thenReturn(List.of(dto));

        ResponseEntity<List<IReservaInstalacionDTO>> response = controller.findReservaInstalacionByNumeroIdentificacion("123");
        assertThat(response.getBody()).containsExactly(dto);
    }
}
