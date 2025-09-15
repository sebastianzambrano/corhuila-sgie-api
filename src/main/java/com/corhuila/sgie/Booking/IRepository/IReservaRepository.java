package com.corhuila.sgie.Booking.IRepository;

import com.corhuila.sgie.Booking.Entity.Reserva;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface IReservaRepository extends IBaseRepository<Reserva,Long> {

    @Query(value = "SELECT * FROM horas_disponibles_instalacion(:fecha, :idInstalacion)", nativeQuery = true)
    List<Object[]> findHorasDisponiblesInstalacion(@Param("fecha") LocalDate fecha,
                                        @Param("idInstalacion") Integer idInstalacion);

    @Query(value = "SELECT * FROM horas_disponibles_equipo(:fecha, :idEquipo)", nativeQuery = true)
    List<Object[]> findHorasDisponiblesEquipo(@Param("fecha") LocalDate fecha,
                                        @Param("idEquipo") Integer idEquipo);
}
