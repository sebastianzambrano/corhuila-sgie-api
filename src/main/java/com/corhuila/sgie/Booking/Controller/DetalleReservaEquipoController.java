package com.corhuila.sgie.Booking.Controller;

import com.corhuila.sgie.Booking.DTO.CerrarDetalleReservaEquipoDTO;
import com.corhuila.sgie.Booking.DTO.IReservaEquipoDTO;
import com.corhuila.sgie.Booking.DTO.IReservaInstalacionDTO;
import com.corhuila.sgie.Booking.Entity.DetalleReservaEquipo;
import com.corhuila.sgie.Booking.IService.IDetalleReservaEquipoService;
import com.corhuila.sgie.Booking.Service.DetalleReservaEquipoService;
import com.corhuila.sgie.common.BaseController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("v1/api/detalle-reserva-equipo")
public class DetalleReservaEquipoController extends BaseController<DetalleReservaEquipo, IDetalleReservaEquipoService> {

    private final DetalleReservaEquipoService detalleReservaEquipoService;

    public DetalleReservaEquipoController(IDetalleReservaEquipoService service, DetalleReservaEquipoService detalleReservaEquipoService) {
        super(service, "DETALLE_RESERVA_EQUIPO");
        this.detalleReservaEquipoService = detalleReservaEquipoService;
    }

    @PutMapping("/{idDetalle}/cerrar-detalle-reserva-equipo")
    //@PreAuthorize("@permissionEvaluator.hasPermission(authentication, this.entityName, 'ACTUALIZAR')")
    public ResponseEntity<DetalleReservaEquipo> cerrarDetalle(
            @PathVariable Long idDetalle,
            @RequestBody CerrarDetalleReservaEquipoDTO request) {

        DetalleReservaEquipo actualizado =
                detalleReservaEquipoService.cerrarDetalleReservaEquipo(idDetalle, request.getEntregaEquipo());

        return ResponseEntity.ok(actualizado);
    }

    @GetMapping("/reservas-equipos")
    //@PreAuthorize("@permissionEvaluator.hasPermission(authentication, this.entityName, 'CONSULTAR')")
    public ResponseEntity<List<IReservaEquipoDTO>> findReservasEquipoByNumeroIdentificacion(@RequestParam String numeroIdentificacionPersona) {
        List<IReservaEquipoDTO> reservasEquipos = service.findReservasEquipoByNumeroIdentificacion(numeroIdentificacionPersona);
        return ResponseEntity.ok(reservasEquipos);
    }

}
