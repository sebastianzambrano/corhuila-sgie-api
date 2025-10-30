package com.corhuila.sgie.Maintenance.Controller;

import com.corhuila.sgie.Maintenance.Entity.CategoriaMantenimientoInstalacion;
import com.corhuila.sgie.Maintenance.Service.CategoriaMantenimientoInstalacionService;
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
class CategoriaMantenimientoInstalacionControllerTest {

    @Mock
    private CategoriaMantenimientoInstalacionService service;

    private CategoriaMantenimientoInstalacionController controller;

    @BeforeEach
    void setup() {
        controller = new CategoriaMantenimientoInstalacionController(service);
    }

    @Test
    void findByStateTrueDevuelveLista() {
        CategoriaMantenimientoInstalacion categoria = new CategoriaMantenimientoInstalacion();
        categoria.setNombre("Correctivo");
        when(service.findByStateTrue()).thenReturn(List.of(categoria));

        ResponseEntity<ApiResponseDto<List<CategoriaMantenimientoInstalacion>>> response = controller.findByStateTrue();
        assertThat(response.getBody().getData()).containsExactly(categoria);
    }
}
