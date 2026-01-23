package com.corhuila.sgie.Maintenance.Controller;

import com.corhuila.sgie.Maintenance.Entity.CategoriaMantenimientoEquipo;
import com.corhuila.sgie.Maintenance.Service.CategoriaMantenimientoEquipoService;
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
class CategoriaMantenimientoEquipoControllerTest {

    @Mock
    private CategoriaMantenimientoEquipoService service;

    private CategoriaMantenimientoEquipoController controller;

    @BeforeEach
    void setup() {
        controller = new CategoriaMantenimientoEquipoController(service);
    }

    @Test
    void findByStateTrueDevuelveDatos() {
        CategoriaMantenimientoEquipo categoria = new CategoriaMantenimientoEquipo();
        categoria.setNombre("Preventivo");
        when(service.findByStateTrue()).thenReturn(List.of(categoria));

        ResponseEntity<ApiResponseDto<List<CategoriaMantenimientoEquipo>>> response = controller.findByStateTrue();
        assertThat(response.getBody().getData()).containsExactly(categoria);
    }
}
