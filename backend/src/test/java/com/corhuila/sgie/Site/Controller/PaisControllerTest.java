package com.corhuila.sgie.Site.Controller;

import com.corhuila.sgie.Site.Entity.Pais;
import com.corhuila.sgie.Site.IRepository.IPaisRepository;
import com.corhuila.sgie.Site.IService.IPaisService;
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
class PaisControllerTest {

    @Mock
    private IPaisService service;
    @Mock
    private IPaisRepository repository;

    private PaisController controller;

    @BeforeEach
    void setup() {
        controller = new PaisController(service, repository);
        ReflectionTestUtils.setField(controller, "repository", repository);
    }

    @Test
    void findByStateTrueDelegatesToService() {
        Pais pais = new Pais();
        pais.setNombre("Colombia");
        when(service.findByStateTrue()).thenReturn(List.of(pais));

        ResponseEntity<ApiResponseDto<List<Pais>>> response = controller.findByStateTrue();

        assertThat(response.getBody().getData()).containsExactly(pais);
        verify(service).findByStateTrue();
    }

    @Test
    void saveInvocaServicio() throws Exception {
        Pais pais = new Pais();
        when(service.save(pais)).thenReturn(pais);

        ResponseEntity<ApiResponseDto<Pais>> response = controller.save(pais);

        assertThat(response.getBody().getData()).isSameAs(pais);
        verify(service).save(pais);
    }

    @Test
    void byContinenteConsultaRepositorio() {
        Pais pais = new Pais();
        when(repository.findByContinenteIdAndStateTrue(99L)).thenReturn(List.of(pais));

        ResponseEntity<ApiResponseDto<List<Pais>>> response = controller.byContinente(99L);

        assertThat(response.getBody().getData()).containsExactly(pais);
    }

    @Test
    void cambiarEstadoPropagaValor() throws Exception {
        EstadoDTO estadoDTO = new EstadoDTO();
        estadoDTO.setEstado(Boolean.FALSE);

        controller.cambiarEstado(4L, estadoDTO);

        ArgumentCaptor<Boolean> captor = ArgumentCaptor.forClass(Boolean.class);
        verify(service).cambiarEstado(eq(4L), captor.capture());
        assertThat(captor.getValue()).isFalse();
    }

    @Test
    void deleteInvocaService() throws Exception {
        controller.delete(7L);
        verify(service).delete(7L);
    }
}
