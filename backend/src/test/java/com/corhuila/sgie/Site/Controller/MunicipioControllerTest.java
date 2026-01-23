package com.corhuila.sgie.Site.Controller;

import com.corhuila.sgie.Site.Entity.Municipio;
import com.corhuila.sgie.Site.IRepository.IMunicipioRepository;
import com.corhuila.sgie.Site.IService.IMunicipioService;
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
class MunicipioControllerTest {

    @Mock
    private IMunicipioService service;
    @Mock
    private IMunicipioRepository repository;

    private MunicipioController controller;

    @BeforeEach
    void setup() {
        controller = new MunicipioController(service, repository);
        ReflectionTestUtils.setField(controller, "repository", repository);
    }

    @Test
    void findByStateTrueDelegatesToService() {
        Municipio municipio = new Municipio();
        municipio.setNombre("Neiva");
        when(service.findByStateTrue()).thenReturn(List.of(municipio));

        ResponseEntity<ApiResponseDto<List<Municipio>>> response = controller.findByStateTrue();

        assertThat(response.getBody().getData()).containsExactly(municipio);
        verify(service).findByStateTrue();
    }

    @Test
    void byDepartamentoConsultaRepositorio() {
        Municipio municipio = new Municipio();
        when(repository.findByDepartamentoIdAndStateTrue(7L)).thenReturn(List.of(municipio));

        ResponseEntity<ApiResponseDto<List<Municipio>>> response = controller.byDepartamento(7L);

        assertThat(response.getBody().getData()).containsExactly(municipio);
    }

    @Test
    void saveInvocaService() throws Exception {
        Municipio municipio = new Municipio();
        when(service.save(municipio)).thenReturn(municipio);

        ResponseEntity<ApiResponseDto<Municipio>> response = controller.save(municipio);

        assertThat(response.getBody().getData()).isSameAs(municipio);
        verify(service).save(municipio);
    }

    @Test
    void cambiarEstadoPropagaValor() throws Exception {
        EstadoDTO estadoDTO = new EstadoDTO();
        estadoDTO.setEstado(Boolean.FALSE);

        controller.cambiarEstado(9L, estadoDTO);

        ArgumentCaptor<Boolean> captor = ArgumentCaptor.forClass(Boolean.class);
        verify(service).cambiarEstado(eq(9L), captor.capture());
        assertThat(captor.getValue()).isFalse();
    }

    @Test
    void deleteInvocaServicio() throws Exception {
        controller.delete(6L);
        verify(service).delete(6L);
    }
}
