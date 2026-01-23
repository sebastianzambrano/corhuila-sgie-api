package com.corhuila.sgie.Site.Service;

import com.corhuila.sgie.Site.Entity.Continente;
import com.corhuila.sgie.Site.IRepository.IContinenteRepository;
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
class ContinenteServiceTest {

    @Mock
    private IContinenteRepository repository;

    @InjectMocks
    private ContinenteService service;

    @Test
    void saveDelegatesToRepository() throws Exception {
        Continente continente = new Continente();
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        service.save(continente);

        verify(repository).save(continente);
    }

    @Test
    void findByStateTrueFiltersActive() {
        Continente activo = new Continente();
        activo.setState(Boolean.TRUE);
        Continente inactivo = new Continente();
        inactivo.setState(Boolean.FALSE);

        when(repository.findAll()).thenReturn(List.of(activo, inactivo));

        assertThat(service.findByStateTrue()).containsExactly(activo);
    }
}
