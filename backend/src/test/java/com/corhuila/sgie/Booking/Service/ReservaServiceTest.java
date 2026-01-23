package com.corhuila.sgie.Booking.Service;

import com.corhuila.sgie.Booking.DTO.HoraDisponibleDTO;
import com.corhuila.sgie.Booking.DTO.IReservaGeneralDTO;
import com.corhuila.sgie.Booking.DTO.ReservaGeneralReporteDTO;
import com.corhuila.sgie.Booking.Entity.Reserva;
import com.corhuila.sgie.Booking.Entity.TipoReserva;
import com.corhuila.sgie.Booking.IRepository.IReservaRepository;
import com.corhuila.sgie.User.Entity.Persona;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock
    private IReservaRepository reservaRepository;

    @InjectMocks
    private ReservaService reservaService;

    private Reserva reserva;

    @BeforeEach
    void setup() {
        Persona persona = new Persona();
        persona.setId(1L);
        persona.setNumeroIdentificacion("123");

        TipoReserva tipo = new TipoReserva();
        tipo.setId(2L);
        tipo.setNombre("Equipo");

        reserva = new Reserva();
        reserva.setId(10L);
        reserva.setPersona(persona);
        reserva.setTipoReserva(tipo);
        reserva.setFechaReserva(LocalDate.now());
        reserva.setHoraInicio(LocalTime.of(8, 0));
        reserva.setHoraFin(LocalTime.of(10, 0));
        reserva.setNombre("Reserva demo");
    }

    @Test
    void getHorasDisponiblesInstalacionMapeaResultados() {
        List<Object[]> datos = new ArrayList<>();
        datos.add(new Object[]{"08:00"});
        datos.add(new Object[]{"09:00"});
        when(reservaRepository.findHorasDisponiblesInstalacion(any(), anyInt(), anyLong(), any())).thenReturn(datos);

        List<HoraDisponibleDTO> horas = reservaService.getHorasDisponiblesInstalacion(LocalDate.now(), 1, 1L, null);
        assertThat(horas).extracting(HoraDisponibleDTO::getHora).containsExactly("08:00", "09:00");
    }

    @Test
    void getHorasDisponiblesEquipoMapeaResultados() {
        List<Object[]> datos = new ArrayList<>();
        datos.add(new Object[]{"10:00"});
        when(reservaRepository.findHorasDisponiblesEquipo(any(), anyInt(), anyLong(), any())).thenReturn(datos);

        List<HoraDisponibleDTO> horas = reservaService.getHorasDisponiblesEquipo(LocalDate.now(), 1, 1L, null);
        assertThat(horas).extracting(HoraDisponibleDTO::getHora).containsExactly("10:00");
    }

    @Test
    void saveValidaSolapamiento() {
        when(reservaRepository.findReservasSolapadas(any(), any(), any(), anyLong()))
                .thenReturn(List.of(reserva));

        assertThatThrownBy(() -> reservaService.save(reserva))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("se solapa");
    }

    @Test
    void savePersisteCuandoNoHayConflictos() throws Exception {
        when(reservaRepository.findReservasSolapadas(any(), any(), any(), anyLong()))
                .thenReturn(Collections.emptyList());
        when(reservaRepository.save(reserva)).thenReturn(reserva);

        Reserva saved = reservaService.save(reserva);
        assertThat(saved).isNotNull();
    }

    @Test
    void proveedorStreamDevuelveSupplier() {
        ReservaGeneralReporteDTO dto = sampleReporte();
        when(reservaRepository.findReservasYMantenimientosByNumeroIdentificacionReport("123"))
                .thenReturn(Stream.of(dto));

        Supplier<Stream<ReservaGeneralReporteDTO>> supplier = reservaService.proveedorStream("123");
        try (Stream<ReservaGeneralReporteDTO> stream = supplier.get()) {
            assertThat(stream).containsExactly(dto);
        }
    }

    @Test
    void obtenerDatosEnMemoriaRecolectaStream() {
        ReservaGeneralReporteDTO dto = sampleReporte();
        when(reservaRepository.findReservasYMantenimientosByNumeroIdentificacionReport("123"))
                .thenReturn(Stream.of(dto));

        List<ReservaGeneralReporteDTO> datos = reservaService.obtenerDatosEnMemoria("123");
        assertThat(datos).containsExactly(dto);
    }

    @Test
    void findReservasYMantenimientosDelegatesToRepository() {
        IReservaGeneralDTO dto = org.mockito.Mockito.mock(IReservaGeneralDTO.class);
        when(reservaRepository.findReservasYMantenimientosByNumeroIdentificacion("123"))
                .thenReturn(List.of(dto));

        List<IReservaGeneralDTO> result = reservaService.findReservasYMantenimientosByNumeroIdentificacion("123");
        assertThat(result).containsExactly(dto);
    }

    private ReservaGeneralReporteDTO sampleReporte() {
        return new ReservaGeneralReporteDTO(
                "Reserva Equipo",
                "Reserva demo",
                "Descripcion",
                java.sql.Date.valueOf(LocalDate.now()),
                java.sql.Time.valueOf(LocalTime.of(8, 0)),
                java.sql.Time.valueOf(LocalTime.of(9, 0)),
                "Persona Demo",
                "123",
                "Instalacion",
                "Equipo",
                "Programa",
                20,
                "Tipo Mant",
                "Detalle",
                "Activa"
        );
    }
}
