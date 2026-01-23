package com.corhuila.sgie.Equipment.Service;

import com.corhuila.sgie.Booking.IRepository.IDetalleReservaEquipoRepository;
import com.corhuila.sgie.Equipment.DTO.HojaDeVidaEquipoDTO;
import com.corhuila.sgie.Equipment.Entity.CategoriaEquipo;
import com.corhuila.sgie.Equipment.Entity.Equipo;
import com.corhuila.sgie.Equipment.Entity.TipoEquipo;
import com.corhuila.sgie.Equipment.IRepository.IEquipoRepository;
import com.corhuila.sgie.Maintenance.IRepository.IMantenimientoEquipoRepository;
import com.corhuila.sgie.Site.Entity.Instalacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HojaDeVidaEquipoServiceTest {

    @Mock
    private IEquipoRepository equipoRepository;
    @Mock
    private IDetalleReservaEquipoRepository detalleReservaEquipoRepository;
    @Mock
    private IMantenimientoEquipoRepository mantenimientoEquipoRepository;

    @InjectMocks
    private HojaDeVidaEquipoService service;

    private Equipo equipo;

    @BeforeEach
    void setup() {
        CategoriaEquipo categoria = new CategoriaEquipo();
        categoria.setNombre("Multimedia");

        TipoEquipo tipo = new TipoEquipo();
        tipo.setNombre("Proyector");
        tipo.setDescripcion("Alta definición");
        tipo.setCategoriaEquipo(categoria);

        Instalacion instalacion = new Instalacion();
        instalacion.setNombre("Sala 101");

        equipo = new Equipo();
        equipo.setId(1L);
        equipo.setCodigo("EQ-1");
        equipo.setTipoEquipo(tipo);
        equipo.setInstalacion(instalacion);
        equipo.setState(true);
    }

    @Test
    void getHojaDeVidaEquipoConstruyeDTOCompleto() {
        when(equipoRepository.findById(1L)).thenReturn(Optional.of(equipo));

        List<Object[]> reservas = new ArrayList<>();
        reservas.add(new Object[]{Date.valueOf(LocalDate.now()), Time.valueOf("08:00:00"), Time.valueOf("10:00:00"), "Reserva", "Persona", "Programa"});
        when(detalleReservaEquipoRepository.findHistorialReservasByEquipo(1L)).thenReturn(reservas);

        List<Object[]> mantenimientos = new ArrayList<>();
        mantenimientos.add(new Object[]{Date.valueOf(LocalDate.now().plusDays(1)), "Preventivo", "Notas", ""});
        when(mantenimientoEquipoRepository.findHistorialMantenimientosByEquipo(1L)).thenReturn(mantenimientos);

        HojaDeVidaEquipoDTO dto = service.getHojaDeVidaEquipo(1L);

        assertThat(dto.getEquipo().getNombre()).isEqualTo("Proyector");
        assertThat(dto.getReservas()).hasSize(1);
        assertThat(dto.getMantenimientos()).hasSize(1);
        assertThat(dto.getEstadoActual()).isEqualTo("Mantenimiento programado");
    }

    @Test
    void getHojaDeVidaEquipoLanzaCuandoNoExiste() {
        when(equipoRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getHojaDeVidaEquipo(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Equipo no encontrado");
    }
}
