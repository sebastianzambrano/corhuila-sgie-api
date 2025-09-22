package com.corhuila.sgie.Booking.Controller;

import com.corhuila.sgie.Booking.DTO.HoraDisponibleDTO;
import com.corhuila.sgie.Booking.Entity.Reserva;
import com.corhuila.sgie.Booking.IService.IReservaService;
import com.corhuila.sgie.common.BaseController;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("v1/api/reserva")
public class ReservaController extends BaseController<Reserva,IReservaService> {

    public ReservaController(IReservaService service) {
        super(service, "RESERVA");
    }

    @GetMapping("/horas-disponibles-instalacion")
    @PreAuthorize("@permissionEvaluator.hasPermission(authentication, this.entityName, 'CONSULTAR')")
    public List<HoraDisponibleDTO> getHorasDisponiblesInstalacion(
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam("idInstalacion") Integer idInstalacion,
            @RequestParam(value = "idDetalle", required = false) Long idDetalle) {

        return service.getHorasDisponiblesInstalacion(fecha, idInstalacion, idDetalle);
    }

    @GetMapping("/horas-disponibles-equipo")
    @PreAuthorize("@permissionEvaluator.hasPermission(authentication, this.entityName, 'CONSULTAR')")
    public List<HoraDisponibleDTO> getHorasDisponiblesEquipo(
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam("idEquipo") Integer idEquipo,
            @RequestParam(value = "idDetalle", required = false) Long idDetalle) {

        return service.getHorasDisponiblesEquipo(fecha, idEquipo, idDetalle);
    }
}
