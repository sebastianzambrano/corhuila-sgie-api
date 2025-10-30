package com.corhuila.sgie.Site.Service;

import com.corhuila.sgie.Site.Entity.Departamento;
import com.corhuila.sgie.Site.IRepository.IDepartamentoRepository;
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
class DepartamentoServiceTest {

    @Mock
    private IDepartamentoRepository repository;

    @InjectMocks
    private DepartamentoService service;

    @Test
    void saveDelegatesToRepository() throws Exception {
        Departamento departamento = new Departamento();
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        service.save(departamento);

        verify(repository).save(departamento);
    }

    @Test
    void findByStateTrueReturnsOnlyActives() {
        Departamento activo = new Departamento();
        activo.setState(Boolean.TRUE);
        Departamento inactivo = new Departamento();
        inactivo.setState(Boolean.FALSE);

        when(repository.findAll()).thenReturn(List.of(activo, inactivo));

        assertThat(service.findByStateTrue()).containsExactly(activo);
    }
}
