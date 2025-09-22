package com.corhuila.sgie.Booking.Controller;

import com.corhuila.sgie.Booking.DTO.*;
import com.corhuila.sgie.Booking.Entity.DetalleReservaInstalacion;
import com.corhuila.sgie.Booking.IService.IDetalleReservaInstalacionService;
import com.corhuila.sgie.Booking.Service.DetalleReservaInstalacionService;
import com.corhuila.sgie.common.BaseController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        @PreAuthorize("@permissionEvaluator.hasPermission(authentication, this.entityName, 'ACTUALIZAR')")
        public ResponseEntity<CerrarDetalleReservaInstalacionResponseDTO> cerrarDetalleReservaInstalacion(
                @PathVariable Long idDetalle,
                @RequestBody CerrarDetalleReservaInstalacionRequestDTO request) {

            CerrarDetalleReservaInstalacionResponseDTO actualizado =
                    detalleReservaInstalacionService.cerrarDetalleReservaInstalacion(idDetalle, request.getEntregaInstalacion());

            return ResponseEntity.ok(actualizado);
        }

    @PutMapping("/{idDetalle}/actualizar-detalle-reserva")
    @PreAuthorize("@permissionEvaluator.hasPermission(authentication, this.entityName, 'ACTUALIZAR')")
    public ResponseEntity<DetalleReservaInstalacionResponseDTO> actualizarDetalleReservaInstalacion(
            @PathVariable Long idDetalle,
            @RequestBody ActualizarReservaDetalleInstalacionRequestDTO request) {

        DetalleReservaInstalacionResponseDTO actualizado =
                detalleReservaInstalacionService.actualizarDetalleReservaInstalacion(idDetalle, request);

        return ResponseEntity.ok(actualizado);
    }

    @GetMapping("/reservas-instalaciones")
    @PreAuthorize("@permissionEvaluator.hasPermission(authentication, this.entityName, 'CONSULTAR')")
    public ResponseEntity<List<IReservaInstalacionDTO>> findReservaInstalacionByNumeroIdentificacion(@RequestParam String numeroIdentificacionPersona) {
        List<IReservaInstalacionDTO> reservasInstalaciones = service.findReservaInstalacionByNumeroIdentificacion(numeroIdentificacionPersona);
        return ResponseEntity.ok(reservasInstalaciones);
    }



}
