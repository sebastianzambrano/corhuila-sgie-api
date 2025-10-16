package com.corhuila.sgie.Maintenance.Controller;

import com.corhuila.sgie.Maintenance.DTO.*;
import com.corhuila.sgie.Maintenance.Entity.MantenimientoInstalacion;
import com.corhuila.sgie.Maintenance.IService.IMantenimientoInstalacionService;
import com.corhuila.sgie.Maintenance.Service.MantenimientoInstalacionService;
import com.corhuila.sgie.common.BaseController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("v1/api/mantenimiento-instalacion")
public class MantenimientoInstalacionController extends BaseController<MantenimientoInstalacion, IMantenimientoInstalacionService> {

    private final MantenimientoInstalacionService mantenimientoInstalacionService;

    public MantenimientoInstalacionController(MantenimientoInstalacionService service, MantenimientoInstalacionService mantenimientoInstalacionService) {
        super(service, "MANTENIMIENTO_INSTALACION");
        this.mantenimientoInstalacionService = mantenimientoInstalacionService;
    }

    @PutMapping("/{idDetalle}/cerrar-mantenimiento-instalacion")
    @PreAuthorize("@permissionEvaluator.hasPermission(authentication, this.entityName, 'ACTUALIZAR')")
    public ResponseEntity<CerrarMantenimientoInstalacionResponseDTO> cerrarMantenimientoInstalacion(
            @PathVariable Long idDetalle,
            @RequestBody CerrarMantenimientoInstalacionDTO request) {

        CerrarMantenimientoInstalacionResponseDTO actualizado =
                mantenimientoInstalacionService.cerrarMantenimientoInstalacion(idDetalle, request.getFechaProximaMantenimiento(), request.getResultadoMantenimiento());

        return ResponseEntity.ok(actualizado);
    }

    @PutMapping("/{idMantenimiento}/actualizar-mantenimiento-instalacion")
    @PreAuthorize("@permissionEvaluator.hasPermission(authentication, this.entityName, 'ACTUALIZAR')")
    public ResponseEntity<MantenimientoInstalacionResponseDTO> actualizarMantenimientoInstalacion(
            @PathVariable Long idMantenimiento,
            @RequestBody ActualizarMantenimientoInstalacionRequestDTO request) {

        MantenimientoInstalacionResponseDTO actualizado = mantenimientoInstalacionService.actualizarMantenimientoInstalacion(idMantenimiento, request);
        return ResponseEntity.ok(actualizado);
    }

    @GetMapping("/mantenimientos-instalaciones")
    @PreAuthorize("@permissionEvaluator.hasPermission(authentication, this.entityName, 'CONSULTAR')")
    public ResponseEntity<List<IMantenimientoInstalacionDTO>> findMantenimientosInstalacionByNumeroIdentificacion(@RequestParam String numeroIdentificacion) {
        List<IMantenimientoInstalacionDTO> mantenimientosInstalaciones = service.findMantenimientosInstalacionByNumeroIdentificacion(numeroIdentificacion);
        return ResponseEntity.ok(mantenimientosInstalaciones);
    }
}
