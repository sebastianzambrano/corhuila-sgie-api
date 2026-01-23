package com.corhuila.sgie.Site.Controller;

import com.corhuila.sgie.Site.Entity.CategoriaInstalacion;
import com.corhuila.sgie.Site.IService.ICategoriaInstalacionService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaInstalacionControllerTest {

    @Mock
    private ICategoriaInstalacionService service;

    private CategoriaInstalacionController controller;

    @BeforeEach
    void setup() {
        controller = new CategoriaInstalacionController(service);
    }

    @Test
    void findByStateTrueDelegatesToService() {
        CategoriaInstalacion categoria = new CategoriaInstalacion();
        categoria.setNombre("Laboratorios");
        when(service.findByStateTrue()).thenReturn(List.of(categoria));

        ResponseEntity<ApiResponseDto<List<CategoriaInstalacion>>> response = controller.findByStateTrue();

        assertThat(response.getBody().getData()).containsExactly(categoria);
        verify(service).findByStateTrue();
    }

    @Test
    void saveInvocaServicio() throws Exception {
        CategoriaInstalacion categoria = new CategoriaInstalacion();
        when(service.save(categoria)).thenReturn(categoria);

        ResponseEntity<ApiResponseDto<CategoriaInstalacion>> response = controller.save(categoria);
        assertThat(response.getBody().getData()).isSameAs(categoria);
        verify(service).save(categoria);
    }

    @Test
    void cambiarEstadoPropagaValor() throws Exception {
        EstadoDTO estadoDTO = new EstadoDTO();
        estadoDTO.setEstado(Boolean.FALSE);

        controller.cambiarEstado(11L, estadoDTO);

        ArgumentCaptor<Boolean> captor = ArgumentCaptor.forClass(Boolean.class);
        verify(service).cambiarEstado(eq(11L), captor.capture());
        assertThat(captor.getValue()).isFalse();
    }
}
