package com.corhuila.sgie.Booking.Controller;

import com.corhuila.sgie.Booking.DTO.CerrarDetalleReservaInstalacionDTO;
import com.corhuila.sgie.Booking.Entity.DetalleReservaInstalacion;
import com.corhuila.sgie.Booking.IService.IDetalleReservaInstalacionService;
import com.corhuila.sgie.Booking.Service.DetalleReservaInstalacionService;
import com.corhuila.sgie.common.BaseController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("v1/api/detalle-reserva-instalacion")
public class DetalleReservaInstalacionController extends BaseController<DetalleReservaInstalacion,IDetalleReservaInstalacionService> {

    private final DetalleReservaInstalacionService detalleReservaInstalacionService;


    public DetalleReservaInstalacionController(IDetalleReservaInstalacionService service, DetalleReservaInstalacionService detalleReservaInstalacionService) {
        super(service, "DETALLE_RESERVA_INSTALACION");
        this.detalleReservaInstalacionService = detalleReservaInstalacionService;
    }


    @PutMapping("/{idDetalle}/cerrar-detalle-reserva-instalacion")
    //@PreAuthorize("@permissionEvaluator.hasPermission(authentication, this.entityName, 'ACTUALIZAR')")
    public ResponseEntity<DetalleReservaInstalacion> cerrarDetalle(
            @PathVariable Long idDetalle,
            @RequestBody CerrarDetalleReservaInstalacionDTO request) {

        DetalleReservaInstalacion actualizado =
                detalleReservaInstalacionService.cerrarDetalleReservaInstalacion(idDetalle, request.getEntregaInstalacion());

        return ResponseEntity.ok(actualizado);
    }
}
