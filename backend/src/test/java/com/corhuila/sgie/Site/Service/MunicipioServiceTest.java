package com.corhuila.sgie.Site.Service;

import com.corhuila.sgie.Site.Entity.Municipio;
import com.corhuila.sgie.Site.IRepository.IMunicipioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MunicipioServiceTest {

    @Mock
    private IMunicipioRepository repository;

    @InjectMocks
    private MunicipioService service;

    @Test
    void saveDelegatesToRepository() throws Exception {
        Municipio municipio = new Municipio();
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        service.save(municipio);

        verify(repository).save(municipio);
    }

    @Test
    void findByStateTrueReturnsActives() {
        Municipio activo = new Municipio();
        activo.setState(Boolean.TRUE);
        Municipio inactivo = new Municipio();
        inactivo.setState(Boolean.FALSE);

        when(repository.findAll()).thenReturn(List.of(activo, inactivo));

        assertThat(service.findByStateTrue()).containsExactly(activo);
    }
}
