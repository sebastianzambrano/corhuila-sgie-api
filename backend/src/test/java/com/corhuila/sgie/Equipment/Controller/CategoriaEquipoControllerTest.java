package com.corhuila.sgie.Equipment.Controller;

import com.corhuila.sgie.Equipment.Entity.CategoriaEquipo;
import com.corhuila.sgie.Equipment.IService.ICategoriaEquipoService;
import com.corhuila.sgie.common.ApiResponseDto;
import com.corhuila.sgie.common.EstadoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaEquipoControllerTest {

    @Mock
    private ICategoriaEquipoService service;

    private CategoriaEquipoController controller;

    @BeforeEach
    void setup() {
        controller = new CategoriaEquipoController(service);
    }

    @Test
    void findByStateTrueDevuelveDatos() {
        CategoriaEquipo categoria = new CategoriaEquipo();
        categoria.setNombre("Audio");
        when(service.findByStateTrue()).thenReturn(List.of(categoria));

        ResponseEntity<ApiResponseDto<List<CategoriaEquipo>>> response = controller.findByStateTrue();

        assertThat(response.getBody().getData()).containsExactly(categoria);
        verify(service).findByStateTrue();
    }

    @Test
    void showObtienePorId() throws Exception {
        CategoriaEquipo categoria = new CategoriaEquipo();
        categoria.setId(5L);
        when(service.findById(5L)).thenReturn(categoria);

        ResponseEntity<ApiResponseDto<CategoriaEquipo>> response = controller.show(5L);
        assertThat(response.getBody().getData()).isSameAs(categoria);
    }

    @Test
    void saveDelegatesToService() throws Exception {
        CategoriaEquipo categoria = new CategoriaEquipo();
        when(service.save(categoria)).thenReturn(categoria);

        ResponseEntity<ApiResponseDto<CategoriaEquipo>> response = controller.save(categoria);
        assertThat(response.getBody().getData()).isSameAs(categoria);
        verify(service).save(categoria);
    }

    @Test
    void updateInvocaService() throws Exception {
        CategoriaEquipo categoria = new CategoriaEquipo();
        ResponseEntity<ApiResponseDto<CategoriaEquipo>> response = controller.update(3L, categoria);
        assertThat(response.getBody().getStatus()).isTrue();
        verify(service).update(3L, categoria);
    }

    @Test
    void cambiarEstadoPropagaEstadoDto() throws Exception {
        EstadoDTO estadoDTO = new EstadoDTO();
        estadoDTO.setEstado(false);

        controller.cambiarEstado(2L, estadoDTO);

        ArgumentCaptor<Boolean> captor = ArgumentCaptor.forClass(Boolean.class);
        verify(service).cambiarEstado(eq(2L), captor.capture());
        assertThat(captor.getValue()).isFalse();
    }

    @Test
    void deleteInvocaServiceDelete() throws Exception {
        controller.delete(7L);
        verify(service).delete(7L);
    }
}
