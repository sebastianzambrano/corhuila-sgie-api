package com.corhuila.sgie.Site.Service;

import com.corhuila.sgie.Site.Entity.CategoriaInstalacion;
import com.corhuila.sgie.Site.IRepository.ICategoriaInstalacionRepository;
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
class CategoriaInstalacionServiceTest {

    @Mock
    private ICategoriaInstalacionRepository repository;

    @InjectMocks
    private CategoriaInstalacionService service;

    @Test
    void saveDelegatesToRepository() throws Exception {
        CategoriaInstalacion categoria = new CategoriaInstalacion();
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        service.save(categoria);

        verify(repository).save(categoria);
    }

    @Test
    void findByStateTrueReturnsActiveOnly() {
        CategoriaInstalacion activo = new CategoriaInstalacion();
        activo.setState(Boolean.TRUE);
        CategoriaInstalacion inactivo = new CategoriaInstalacion();
        inactivo.setState(Boolean.FALSE);

        when(repository.findAll()).thenReturn(List.of(activo, inactivo));

        assertThat(service.findByStateTrue()).containsExactly(activo);
    }
}
