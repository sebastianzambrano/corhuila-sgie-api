package com.corhuila.sgie.Maintenance.Controller;

import com.corhuila.sgie.Maintenance.DTO.CerrarMantenimientoEquipoDTO;
import com.corhuila.sgie.Maintenance.Entity.MantenimientoEquipo;
import com.corhuila.sgie.Maintenance.Service.MantenimientoEquipoService;
import com.corhuila.sgie.common.BaseController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("v1/api/mantenimiento-equipo")
public class MantenimientoEquipoController extends BaseController<MantenimientoEquipo, MantenimientoEquipoService> {

    private final MantenimientoEquipoService mantenimientoEquipoService;

    public MantenimientoEquipoController(MantenimientoEquipoService service, MantenimientoEquipoService mantenimientoEquipoService) {
        super(service, "MANTENIMIENTO_EQUIPO");
        this.mantenimientoEquipoService = mantenimientoEquipoService;
    }


    @PutMapping("/{idDetalle}/cerrar-mantenimiento-equipo")
    @PreAuthorize("@permissionEvaluator.hasPermission(authentication, this.entityName, 'ACTUALIZAR')")
    public ResponseEntity<MantenimientoEquipo> cerrarDetalle(
            @PathVariable Long idDetalle,
            @RequestBody CerrarMantenimientoEquipoDTO request) {

        MantenimientoEquipo actualizado =
                mantenimientoEquipoService.cerrarMantenimientoEquipo(idDetalle,request.getFechaProximaMantenimiento(), request.getResultadoMantenimiento());

        return ResponseEntity.ok(actualizado);
    }

}