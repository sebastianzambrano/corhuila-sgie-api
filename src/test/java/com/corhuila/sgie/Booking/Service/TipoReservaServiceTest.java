package com.corhuila.sgie.Booking.Service;

import com.corhuila.sgie.Booking.Entity.TipoReserva;
import com.corhuila.sgie.Booking.IRepository.ITipoReservaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TipoReservaServiceTest {

    @Mock
    private ITipoReservaRepository repository;

    @InjectMocks
    private TipoReservaService service;

    private TipoReserva tipoReserva;

    @BeforeEach
    void setup() {
        tipoReserva = new TipoReserva();
        tipoReserva.setId(1L);
        tipoReserva.setNombre("Equipo");
    }

    @Test
    void allDelegatesToRepository() {
        when(repository.findAll()).thenReturn(List.of(tipoReserva));
        assertThat(service.all()).containsExactly(tipoReserva);
    }

    @Test
    void findByIdDevuelveEntidad() throws Exception {
        when(repository.findById(1L)).thenReturn(Optional.of(tipoReserva));
        assertThat(service.findById(1L)).isEqualTo(tipoReserva);
    }

    @Test
    void findByIdLanzaExcepcionSiNoExiste() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.findById(99L)).isInstanceOf(Exception.class);
    }
}
