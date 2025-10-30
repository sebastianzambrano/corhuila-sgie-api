package com.corhuila.sgie.Maintenance.Service;

import com.corhuila.sgie.Maintenance.Entity.CategoriaMantenimientoEquipo;
import com.corhuila.sgie.Maintenance.IRepository.ICategoriaMantenimientoEquipoRepository;
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
class CategoriaMantenimientoEquipoServiceTest {

    @Mock
    private ICategoriaMantenimientoEquipoRepository repository;

    @InjectMocks
    private CategoriaMantenimientoEquipoService service;

    @Test
    void saveDelegatesToRepository() throws Exception {
        CategoriaMantenimientoEquipo categoria = new CategoriaMantenimientoEquipo();
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        service.save(categoria);

        verify(repository).save(categoria);
    }

    @Test
    void findByStateTrueFiltraActivos() {
        CategoriaMantenimientoEquipo activo = new CategoriaMantenimientoEquipo();
        activo.setState(Boolean.TRUE);
        CategoriaMantenimientoEquipo inactivo = new CategoriaMantenimientoEquipo();
        inactivo.setState(Boolean.FALSE);

        when(repository.findAll()).thenReturn(List.of(activo, inactivo));

        assertThat(service.findByStateTrue()).containsExactly(activo);
    }
}
