package com.corhuila.sgie.Equipment.Controller;

import com.corhuila.sgie.Equipment.Entity.TipoEquipo;
import com.corhuila.sgie.Equipment.IService.ITipoEquipoService;
import com.corhuila.sgie.common.ApiResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TipoEquipoControllerTest {

    @Mock
    private ITipoEquipoService service;

    private TipoEquipoController controller;

    @BeforeEach
    void setup() {
        controller = new TipoEquipoController(service);
    }

    @Test
    void findByStateTrueDevuelveDtos() {
        TipoEquipo tipo = new TipoEquipo();
        tipo.setNombre("Proyector");
        when(service.findByStateTrue()).thenReturn(List.of(tipo));

        ResponseEntity<ApiResponseDto<List<TipoEquipo>>> response = controller.findByStateTrue();
        assertThat(response.getBody().getData()).containsExactly(tipo);
        verify(service).findByStateTrue();
    }

    @Test
    void showConsultaPorId() throws Exception {
        TipoEquipo tipo = new TipoEquipo();
        tipo.setId(4L);
        when(service.findById(4L)).thenReturn(tipo);

        ResponseEntity<ApiResponseDto<TipoEquipo>> response = controller.show(4L);
        assertThat(response.getBody().getData()).isSameAs(tipo);
    }
}
