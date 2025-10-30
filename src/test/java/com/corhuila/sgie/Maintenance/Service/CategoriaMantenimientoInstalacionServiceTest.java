package com.corhuila.sgie.Maintenance.Service;

import com.corhuila.sgie.Maintenance.Entity.CategoriaMantenimientoInstalacion;
import com.corhuila.sgie.Maintenance.IRepository.ICategoriaMantenimientoInstalacionRepository;
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
class CategoriaMantenimientoInstalacionServiceTest {

    @Mock
    private ICategoriaMantenimientoInstalacionRepository repository;

    @InjectMocks
    private CategoriaMantenimientoInstalacionService service;

    @Test
    void saveDelegatesToRepository() throws Exception {
        CategoriaMantenimientoInstalacion categoria = new CategoriaMantenimientoInstalacion();
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        service.save(categoria);

        verify(repository).save(categoria);
    }

    @Test
    void findByStateTrueRetornaSoloActivos() {
        CategoriaMantenimientoInstalacion activo = new CategoriaMantenimientoInstalacion();
        activo.setState(Boolean.TRUE);
        CategoriaMantenimientoInstalacion inactivo = new CategoriaMantenimientoInstalacion();
        inactivo.setState(Boolean.FALSE);

        when(repository.findAll()).thenReturn(List.of(activo, inactivo));

        assertThat(service.findByStateTrue()).containsExactly(activo);
    }
}
