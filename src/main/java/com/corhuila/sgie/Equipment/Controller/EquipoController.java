package com.corhuila.sgie.Equipment.Controller;

import com.corhuila.sgie.Equipment.DTO.IEquipoInstalacionDTO;
import com.corhuila.sgie.Equipment.Entity.Equipo;
import com.corhuila.sgie.Equipment.IService.IEquipoService;
import com.corhuila.sgie.common.BaseController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("v1/api/equipo")
public class EquipoController extends BaseController <Equipo, IEquipoService>{
    public EquipoController(IEquipoService service) {
        super(service, "EQUIPO");
    }

    @GetMapping("/equipo-instalacion")
    //@PreAuthorize("@permissionEvaluator.hasPermission(authentication, this.entityName, 'CONSULTAR')")
    public ResponseEntity<List<IEquipoInstalacionDTO>> findEquiposInstalaciones(@RequestParam String codigoEquipo, @RequestParam String nombreInstalacion ) {
        List<IEquipoInstalacionDTO> equiposInstalaciones = service.findEquiposInstalaciones(codigoEquipo, nombreInstalacion);
        return ResponseEntity.ok(equiposInstalaciones);
    }

}
