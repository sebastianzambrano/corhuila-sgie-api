package com.corhuila.sgie.Booking.Controller;

import com.corhuila.sgie.Booking.DTO.HoraDisponibleDTO;
import com.corhuila.sgie.Booking.Entity.Reserva;
import com.corhuila.sgie.Booking.IService.IReservaService;
import com.corhuila.sgie.common.BaseController;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("v1/api/reserva")
public class ReservaController extends BaseController<Reserva,IReservaService> {

    public ReservaController(IReservaService service) {
        super(service, "Reserva");
    }

    @GetMapping("/horas-disponibles-instalacion")
    public List<HoraDisponibleDTO> getHorasDisponiblesInstalacion(
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam("idInstalacion") Integer idInstalacion) {

        return service.getHorasDisponiblesInstalacion(fecha, idInstalacion);
    }

    @GetMapping("/horas-disponibles-equipo")
    public List<HoraDisponibleDTO> getHorasDisponiblesEquipo(
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam("idEquipo") Integer idEquipo) {

        return service.getHorasDisponiblesEquipo(fecha, idEquipo);
    }
}
