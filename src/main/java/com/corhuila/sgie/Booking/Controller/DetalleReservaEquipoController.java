package com.corhuila.sgie.Booking.Controller;

import com.corhuila.sgie.Booking.DTO.CerrarDetalleReservaEquipoDTO;
import com.corhuila.sgie.Booking.Entity.DetalleReservaEquipo;
import com.corhuila.sgie.Booking.IService.IDetalleReservaEquipoService;
import com.corhuila.sgie.Booking.Service.DetalleReservaEquipoService;
import com.corhuila.sgie.common.BaseController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("v1/api/detalle-reserva-equipo")
public class DetalleReservaEquipoController extends BaseController<DetalleReservaEquipo, IDetalleReservaEquipoService> {

    private final DetalleReservaEquipoService detalleReservaEquipoService;

    public DetalleReservaEquipoController(IDetalleReservaEquipoService service, DetalleReservaEquipoService detalleReservaEquipoService) {
        super(service, "DetalleReservaEquipo");
        this.detalleReservaEquipoService = detalleReservaEquipoService;
    }

    @PutMapping("/{idDetalle}/cerrar-detalle-reserva-equipo")
    public ResponseEntity<DetalleReservaEquipo> cerrarDetalle(
            @PathVariable Long idDetalle,
            @RequestBody CerrarDetalleReservaEquipoDTO request) {

        DetalleReservaEquipo actualizado =
                detalleReservaEquipoService.cerrarDetalleReservaEquipo(idDetalle, request.getEntregaEquipo());

        return ResponseEntity.ok(actualizado);
    }
}
