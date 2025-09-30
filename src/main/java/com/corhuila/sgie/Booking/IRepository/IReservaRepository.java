package com.corhuila.sgie.Booking.IRepository;

import com.corhuila.sgie.Booking.Entity.Reserva;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IReservaRepository extends IBaseRepository<Reserva,Long> {

    @Query(value = "SELECT * FROM horas_disponibles_instalacion(:fecha, :idInstalacion)", nativeQuery = true)
    List<Object[]> findHorasDisponiblesInstalacion(@Param("fecha") LocalDate fecha,
                                        @Param("idInstalacion") Integer idInstalacion,
                                        @Param("idDetalle") Long idDetalle);

    @Query(value = "SELECT * FROM horas_disponibles_equipo(:fecha, :idEquipo, :idDetalle)", nativeQuery = true)
    List<Object[]> findHorasDisponiblesEquipo(@Param("fecha") LocalDate fecha,
                                              @Param("idEquipo") Integer idEquipo,
                                              @Param("idDetalle") Long idDetalle);
    @Query("""
        SELECT r 
        FROM Reserva r
        WHERE r.fechaReserva = :fecha
          AND r.state = true
          AND (
              (r.horaInicio < :horaFin AND r.horaFin > :horaInicio)
          )
          AND (
              ( :tipoReserva IN (1,3) AND r.tipoReserva.id IN (1,3) )
              OR
              ( :tipoReserva IN (2,4) AND r.tipoReserva.id IN (2,4) )
          )
    """)
    List<Reserva> findReservasSolapadas(
            @Param("fecha") LocalDate fecha,
            @Param("horaInicio") LocalTime horaInicio,
            @Param("horaFin") LocalTime horaFin,
            @Param("tipoReserva") Long tipoReserva
    );

    @EntityGraph(attributePaths = {"persona", "persona.usuario"})
    Optional<Reserva> findWithPersonaAndUsuarioById(Long id);

    List<Reserva> findByFechaReservaAndStateTrue(LocalDate fecha);
}
