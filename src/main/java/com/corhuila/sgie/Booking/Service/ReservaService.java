package com.corhuila.sgie.Booking.Service;

import com.corhuila.sgie.Booking.DTO.HoraDisponibleDTO;
import com.corhuila.sgie.Booking.DTO.IReservaGeneralDTO;
import com.corhuila.sgie.Booking.DTO.ReservaGeneralReporteDTO;
import com.corhuila.sgie.Booking.Entity.Reserva;
import com.corhuila.sgie.Booking.IRepository.IReservaRepository;
import com.corhuila.sgie.Booking.IService.IReservaService;
import com.corhuila.sgie.common.BaseService;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Service
public class ReservaService extends BaseService<Reserva> implements IReservaService {

    private final IReservaRepository repository;

    public ReservaService(IReservaRepository repository) {
        this.repository = repository;
    }

    @Override
    protected IBaseRepository<Reserva, Long> getRepository() {
        return repository;
    }

    @Override
    public List<HoraDisponibleDTO> getHorasDisponiblesInstalacion(LocalDate fecha, Integer idInstalacion, Long idDetalle) {
        List<Object[]> results = repository.findHorasDisponiblesInstalacion(fecha, idInstalacion, idDetalle);

        return results.stream()
                .map(r -> new HoraDisponibleDTO(r[0].toString()))
                .toList();
    }

    @Override
    public List<HoraDisponibleDTO> getHorasDisponiblesEquipo(LocalDate fecha, Integer idEquipo, Long idDetalle) {
        List<Object[]> results = repository.findHorasDisponiblesEquipo(fecha, idEquipo, idDetalle);

        return results.stream()
                .map(r -> new HoraDisponibleDTO(r[0].toString()))
                .toList();
    }

    @Override
    public Reserva save(Reserva reserva) throws DataAccessException {
        List<Reserva> solapadas = repository.findReservasSolapadas(
                reserva.getFechaReserva(),
                reserva.getHoraInicio(),
                reserva.getHoraFin(),
                reserva.getTipoReserva().getId()
        );

        if (!solapadas.isEmpty()) {
            throw new IllegalStateException("La reserva se solapa con otra existente entre "
                    + solapadas.get(0).getHoraInicio() + " y " + solapadas.get(0).getHoraFin());
        }

        return super.save(reserva);
    }


    public List<IReservaGeneralDTO> findReservasYMantenimientosByNumeroIdentificacion(String numeroIdentificacion) {
        return repository.findReservasYMantenimientosByNumeroIdentificacion(numeroIdentificacion);
    }

    public Supplier<Stream<ReservaGeneralReporteDTO>> proveedorStream(String numeroIdentificacion) {
        return () -> repository.findReservasYMantenimientosByNumeroIdentificacionReport(numeroIdentificacion);
    }

    @Transactional(readOnly = true)
    public List<ReservaGeneralReporteDTO> obtenerDatosEnMemoria(String numeroIdentificacion) {
        Supplier<Stream<ReservaGeneralReporteDTO>> supplier = proveedorStream(numeroIdentificacion);
        try (Stream<ReservaGeneralReporteDTO> stream = supplier.get()) {
            return stream.toList();
        }
    }
}
