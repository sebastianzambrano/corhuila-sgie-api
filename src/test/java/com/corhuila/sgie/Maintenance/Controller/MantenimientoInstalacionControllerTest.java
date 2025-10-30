package com.corhuila.sgie.Maintenance.Controller;

import com.corhuila.sgie.Maintenance.DTO.*;
import com.corhuila.sgie.Maintenance.Entity.MantenimientoInstalacion;
import com.corhuila.sgie.Maintenance.Service.MantenimientoInstalacionService;
import com.corhuila.sgie.common.EstadoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MantenimientoInstalacionControllerTest {

    @Mock
    private MantenimientoInstalacionService service;

    private MantenimientoInstalacionController controller;

    @BeforeEach
    void setup() {
        controller = new MantenimientoInstalacionController(service, service);
    }

    @Test
    void cerrarMantenimientoInstalacionDevuelveDto() {
        CerrarMantenimientoInstalacionDTO request = new CerrarMantenimientoInstalacionDTO();
        request.setFechaProximaMantenimiento(LocalDate.now().plusDays(5));
        request.setResultadoMantenimiento("OK");

        CerrarMantenimientoInstalacionResponseDTO dto = new CerrarMantenimientoInstalacionResponseDTO(1L, false,
                LocalDate.now().plusDays(5), "OK", LocalDateTime.now(), 7L);
        when(service.cerrarMantenimientoInstalacion(anyLong(), any(), any())).thenReturn(dto);

        ResponseEntity<CerrarMantenimientoInstalacionResponseDTO> response = controller.cerrarMantenimientoInstalacion(1L, request);
        assertThat(response.getBody()).isSameAs(dto);
    }

    @Test
    void actualizarMantenimientoInstalacionDevuelveDTO() {
        ActualizarMantenimientoInstalacionRequestDTO request = new ActualizarMantenimientoInstalacionRequestDTO();
        request.setDescripcion("Actualizar");

        MantenimientoInstalacionResponseDTO dto = new MantenimientoInstalacionResponseDTO();
        dto.setDescripcion("Actualizar");
        when(service.actualizarMantenimientoInstalacion(2L, request)).thenReturn(dto);

        ResponseEntity<MantenimientoInstalacionResponseDTO> response = controller.actualizarMantenimientoInstalacion(2L, request);
        assertThat(response.getBody().getDescripcion()).isEqualTo("Actualizar");
    }

    @Test
    void findMantenimientosInstalacionDelegates() {
        IMantenimientoInstalacionDTO dto = mock(IMantenimientoInstalacionDTO.class);
        when(service.findMantenimientosInstalacionByNumeroIdentificacion("123"))
                .thenReturn(List.of(dto));

        ResponseEntity<List<IMantenimientoInstalacionDTO>> response = controller.findMantenimientosInstalacionByNumeroIdentificacion("123");
        assertThat(response.getBody()).containsExactly(dto);
    }

    @Test
    void baseControllerOperaciones() throws Exception {
        MantenimientoInstalacion entity = new MantenimientoInstalacion();
        when(service.findByStateTrue()).thenReturn(List.of(entity));
        when(service.findById(3L)).thenReturn(entity);
        when(service.save(entity)).thenReturn(entity);

        assertThat(controller.findByStateTrue().getBody().getData()).containsExactly(entity);
        assertThat(controller.show(3L).getBody().getData()).isSameAs(entity);
        assertThat(controller.save(entity).getBody().getData()).isSameAs(entity);

        controller.update(3L, entity);
        verify(service).update(3L, entity);

        EstadoDTO estadoDTO = new EstadoDTO();
        estadoDTO.setEstado(false);
        controller.cambiarEstado(3L, estadoDTO);
        ArgumentCaptor<Boolean> captor = ArgumentCaptor.forClass(Boolean.class);
        verify(service).cambiarEstado(eq(3L), captor.capture());
        assertThat(captor.getValue()).isFalse();

        controller.delete(3L);
        verify(service).delete(3L);
    }
}
