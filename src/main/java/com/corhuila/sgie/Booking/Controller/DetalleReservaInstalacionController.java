package com.corhuila.sgie.Booking.Controller;

import com.corhuila.sgie.Booking.DTO.ActualizarReservaDetalleInstalacionRequestDTO;
import com.corhuila.sgie.Booking.DTO.CerrarDetalleReservaInstalacionRequestDTO;
import com.corhuila.sgie.Booking.DTO.CerrarDetalleReservaInstalacionResponseDTO;
import com.corhuila.sgie.Booking.DTO.IReservaInstalacionDTO;
import com.corhuila.sgie.Booking.Entity.DetalleReservaInstalacion;
import com.corhuila.sgie.Booking.IService.IDetalleReservaInstalacionService;
import com.corhuila.sgie.Booking.Service.DetalleReservaInstalacionService;
import com.corhuila.sgie.common.BaseController;
import org.springframework.http.ResponseEntity;
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
        //@PreAuthorize("@permissionEvaluator.hasPermission(authentication, this.entityName, 'ACTUALIZAR')")
        public ResponseEntity<CerrarDetalleReservaInstalacionResponseDTO> cerrarDetalle(
                @PathVariable Long idDetalle,
                @RequestBody CerrarDetalleReservaInstalacionRequestDTO request) {

            CerrarDetalleReservaInstalacionResponseDTO actualizado =
                    detalleReservaInstalacionService.cerrarDetalleReservaInstalacion(idDetalle, request.getEntregaInstalacion());

            return ResponseEntity.ok(actualizado);
        }

    @GetMapping("/reservas-instalaciones")
    //@PreAuthorize("@permissionEvaluator.hasPermission(authentication, this.entityName, 'CONSULTAR')")
    public ResponseEntity<List<IReservaInstalacionDTO>> findReservaInstalacionByNumeroIdentificacion(@RequestParam String numeroIdentificacionPersona) {
        List<IReservaInstalacionDTO> reservasInstalaciones = service.findReservaInstalacionByNumeroIdentificacion(numeroIdentificacionPersona);
        return ResponseEntity.ok(reservasInstalaciones);
    }

    @PutMapping("/{idDetalle}/actualizar-detalle-reserva")
    public ResponseEntity<DetalleReservaInstalacion> actualizarDetalleInstalacion(
            @PathVariable Long idDetalle,
            @RequestBody ActualizarReservaDetalleInstalacionRequestDTO request) {

        DetalleReservaInstalacion actualizado =
                detalleReservaInstalacionService.actualizarDetalleReserva(idDetalle, request);

        return ResponseEntity.ok(actualizado);
    }

}
