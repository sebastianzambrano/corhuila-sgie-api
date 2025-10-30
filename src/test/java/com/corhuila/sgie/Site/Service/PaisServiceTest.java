package com.corhuila.sgie.Site.Service;

import com.corhuila.sgie.Site.Entity.Pais;
import com.corhuila.sgie.Site.IRepository.IPaisRepository;
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
class PaisServiceTest {

    @Mock
    private IPaisRepository repository;

    @InjectMocks
    private PaisService service;

    @Test
    void saveDelegatesToRepository() throws Exception {
        Pais pais = new Pais();
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        service.save(pais);

        verify(repository).save(pais);
    }

    @Test
    void findByStateTrueFiltersOnlyActives() {
        Pais activo = new Pais();
        activo.setState(Boolean.TRUE);
        Pais inactivo = new Pais();
        inactivo.setState(Boolean.FALSE);

        when(repository.findAll()).thenReturn(List.of(activo, inactivo));

        assertThat(service.findByStateTrue()).containsExactly(activo);
    }
}
