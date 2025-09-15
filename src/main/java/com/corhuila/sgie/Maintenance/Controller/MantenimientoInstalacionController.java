package com.corhuila.sgie.Maintenance.Controller;

import com.corhuila.sgie.Maintenance.DTO.CerrarMantenimientoInstalacionDTO;
import com.corhuila.sgie.Maintenance.Entity.MantenimientoInstalacion;
import com.corhuila.sgie.Maintenance.IService.IMantenimientoInstalacionService;
import com.corhuila.sgie.Maintenance.Service.MantenimientoInstalacionService;
import com.corhuila.sgie.common.BaseController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("v1/api/mantenimiento-instalacion")
public class MantenimientoInstalacionController extends BaseController<MantenimientoInstalacion, IMantenimientoInstalacionService> {

    private final MantenimientoInstalacionService mantenimientoInstalacionService;

    public MantenimientoInstalacionController(MantenimientoInstalacionService service, MantenimientoInstalacionService mantenimientoInstalacionService) {
        super(service, "Campus");
        this.mantenimientoInstalacionService = mantenimientoInstalacionService;
    }

    @PutMapping("/{idDetalle}/cerrar-mantenimiento-instalacion")
    public ResponseEntity<MantenimientoInstalacion> cerrarDetalle(
            @PathVariable Long idDetalle,
            @RequestBody CerrarMantenimientoInstalacionDTO request) {

        MantenimientoInstalacion actualizado =
                mantenimientoInstalacionService.cerrarMantenimientoInstalacion(idDetalle,request.getFechaProximaMantenimiento(), request.getResultadoMantenimiento());

        return ResponseEntity.ok(actualizado);
    }
}
