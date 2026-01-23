package com.corhuila.sgie.Site.Controller;

import com.corhuila.sgie.Site.Entity.Continente;
import com.corhuila.sgie.Site.IService.IContinenteService;
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
class ContinenteControllerTest {

    @Mock
    private IContinenteService service;

    private ContinenteController controller;

    @BeforeEach
    void setup() {
        controller = new ContinenteController(service);
    }

    @Test
    void findByStateTrueDelegatesToService() {
        Continente continente = new Continente();
        continente.setNombre("América");
        when(service.findByStateTrue()).thenReturn(List.of(continente));

        ResponseEntity<ApiResponseDto<List<Continente>>> response = controller.findByStateTrue();

        assertThat(response.getBody().getData()).containsExactly(continente);
        verify(service).findByStateTrue();
    }

    @Test
    void saveInvocaServicio() throws Exception {
        Continente continente = new Continente();
        when(service.save(continente)).thenReturn(continente);

        ResponseEntity<ApiResponseDto<Continente>> response = controller.save(continente);
        assertThat(response.getBody().getData()).isSameAs(continente);
        verify(service).save(continente);
    }

    @Test
    void cambiarEstadoPropagaValor() throws Exception {
        EstadoDTO estadoDTO = new EstadoDTO();
        estadoDTO.setEstado(Boolean.TRUE);

        controller.cambiarEstado(1L, estadoDTO);

        ArgumentCaptor<Boolean> captor = ArgumentCaptor.forClass(Boolean.class);
        verify(service).cambiarEstado(eq(1L), captor.capture());
        assertThat(captor.getValue()).isTrue();
    }
}
