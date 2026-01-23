package com.corhuila.sgie.Equipment.Service;

import com.corhuila.sgie.Booking.IRepository.IDetalleReservaEquipoRepository;
import com.corhuila.sgie.Equipment.DTO.EquipoDTO;
import com.corhuila.sgie.Equipment.DTO.HojaDeVidaEquipoDTO;
import com.corhuila.sgie.Equipment.DTO.MantenimientoEquipoHistorialDTO;
import com.corhuila.sgie.Equipment.DTO.ReservaEquipoHistorialDTO;
import com.corhuila.sgie.Equipment.Entity.Equipo;
import com.corhuila.sgie.Equipment.IRepository.IEquipoRepository;
import com.corhuila.sgie.Maintenance.IRepository.IMantenimientoEquipoRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

@Service
public class HojaDeVidaEquipoService {
    private final IEquipoRepository equipoRepository;

    private final IDetalleReservaEquipoRepository detalleReservaEquipoRepository;

    private final IMantenimientoEquipoRepository mantenimientoEquipoRepository;

    public HojaDeVidaEquipoService(IEquipoRepository equipoRepository, IDetalleReservaEquipoRepository detalleReservaEquipoRepository, IMantenimientoEquipoRepository mantenimientoEquipoRepository) {
        this.equipoRepository = equipoRepository;
        this.detalleReservaEquipoRepository = detalleReservaEquipoRepository;
        this.mantenimientoEquipoRepository = mantenimientoEquipoRepository;
    }

    public HojaDeVidaEquipoDTO getHojaDeVidaEquipo(Long idEquipo) {
        Equipo equipo = equipoRepository.findById(idEquipo)
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado"));

        // Equipo DTO
        EquipoDTO equipoDTO = new EquipoDTO(
                /*
                equipo.getId(),
                equipo.getCodigo(),
                equipo.getNombre(),
                equipo.getDescripcion(),
                equipo.getInstalacion() != null ? equipo.getInstalacion().getNombre() : null,
                equipo.getCategoriaEquipo() != null ? equipo.getCategoriaEquipo().getNombre() : null
                */
                equipo.getId(),
                equipo.getCodigo(),
                equipo.getTipoEquipo() != null ? equipo.getTipoEquipo().getNombre() : null,
                equipo.getTipoEquipo() != null ? equipo.getTipoEquipo().getDescripcion() : null,
                equipo.getInstalacion() != null ? equipo.getInstalacion().getNombre() : null,
                equipo.getTipoEquipo() != null && equipo.getTipoEquipo().getCategoriaEquipo() != null
                        ? equipo.getTipoEquipo().getCategoriaEquipo().getNombre()
                        : null
        );

        // Reservas
        List<Object[]> reservasResult = detalleReservaEquipoRepository.findHistorialReservasByEquipo(idEquipo);
        List<ReservaEquipoHistorialDTO> reservas = reservasResult.stream()
                .map(r -> new ReservaEquipoHistorialDTO(
                        ((Date) r[0]).toLocalDate(),
                        ((Time) r[1]).toLocalTime(),
                        ((Time) r[2]).toLocalTime(),
                        (String) r[3],
                        (String) r[4],
                        (String) r[5]
                ))
                .toList();

        // Mantenimientos
        List<Object[]> mantenimientosResult = mantenimientoEquipoRepository.findHistorialMantenimientosByEquipo(idEquipo);
        List<MantenimientoEquipoHistorialDTO> mantenimientos = mantenimientosResult.stream()
                .map(m -> new MantenimientoEquipoHistorialDTO(
                        m[0] != null ? ((Date) m[0]).toLocalDate() : null,
                        (String) m[1],
                        (String) m[2],
                        (String) m[3]
                ))
                .toList();

        // Estado actual
        String estadoActual = "Operativo";
        boolean equipoActivo = Boolean.TRUE.equals(equipo.getState());

        if (!equipoActivo) {
            estadoActual = "Inactivo";
        } else if (!mantenimientos.isEmpty() && mantenimientos.get(0).getFechaProximaMantenimiento() != null
                && mantenimientos.get(0).getFechaProximaMantenimiento().isAfter(LocalDate.now())) {
            estadoActual = "Mantenimiento programado";
        }

        return new HojaDeVidaEquipoDTO(equipoDTO, reservas, mantenimientos, estadoActual);
    }
}
