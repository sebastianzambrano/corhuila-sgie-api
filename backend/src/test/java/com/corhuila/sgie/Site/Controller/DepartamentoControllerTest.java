package com.corhuila.sgie.Site.Controller;

import com.corhuila.sgie.Site.Entity.Departamento;
import com.corhuila.sgie.Site.IRepository.IDepartamentoRepository;
import com.corhuila.sgie.Site.IService.IDepartamentoService;
import com.corhuila.sgie.common.ApiResponseDto;
import com.corhuila.sgie.common.EstadoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepartamentoControllerTest {

    @Mock
    private IDepartamentoService service;
    @Mock
    private IDepartamentoRepository repository;

    private DepartamentoController controller;

    @BeforeEach
    void setup() {
        controller = new DepartamentoController(service, repository);
        ReflectionTestUtils.setField(controller, "repository", repository);
    }

    @Test
    void findByStateTrueRetornaActivos() {
        Departamento departamento = new Departamento();
        departamento.setNombre("Huila");
        when(service.findByStateTrue()).thenReturn(List.of(departamento));

        ResponseEntity<ApiResponseDto<List<Departamento>>> response = controller.findByStateTrue();

        assertThat(response.getBody().getData()).containsExactly(departamento);
        verify(service).findByStateTrue();
    }

    @Test
    void byPaisConsultaRepositorio() {
        Departamento departamento = new Departamento();
        when(repository.findByPaisIdAndStateTrue(5L)).thenReturn(List.of(departamento));

        ResponseEntity<ApiResponseDto<List<Departamento>>> response = controller.byPais(5L);

        assertThat(response.getBody().getData()).containsExactly(departamento);
    }

    @Test
    void updateDelegatesToService() throws Exception {
        Departamento departamento = new Departamento();
        ResponseEntity<ApiResponseDto<Departamento>> response = controller.update(8L, departamento);
        assertThat(response.getBody().getStatus()).isTrue();
        verify(service).update(8L, departamento);
    }

    @Test
    void cambiarEstadoPropagaValor() throws Exception {
        EstadoDTO estadoDTO = new EstadoDTO();
        estadoDTO.setEstado(Boolean.TRUE);

        controller.cambiarEstado(3L, estadoDTO);

        ArgumentCaptor<Boolean> captor = ArgumentCaptor.forClass(Boolean.class);
        verify(service).cambiarEstado(eq(3L), captor.capture());
        assertThat(captor.getValue()).isTrue();
    }

    @Test
    void deleteInvocaServicio() throws Exception {
        controller.delete(2L);
        verify(service).delete(2L);
    }
}
