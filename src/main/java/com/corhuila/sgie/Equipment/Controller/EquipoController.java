package com.corhuila.sgie.Equipment.Controller;

import com.corhuila.sgie.Equipment.DTO.HojaDeVidaEquipoDTO;
import com.corhuila.sgie.Equipment.DTO.IEquipoInstalacionDTO;
import com.corhuila.sgie.Equipment.Entity.Equipo;
import com.corhuila.sgie.Equipment.IService.IEquipoService;
import com.corhuila.sgie.Equipment.Service.HojaDeVidaEquipoService;
import com.corhuila.sgie.common.BaseController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("v1/api/equipo")
public class EquipoController extends BaseController <Equipo, IEquipoService>{

    private final HojaDeVidaEquipoService hojaDeVidaEquipoService;

    public EquipoController(IEquipoService service, HojaDeVidaEquipoService hojaDeVidaEquipoService) {
        super(service, "EQUIPO");
        this.hojaDeVidaEquipoService = hojaDeVidaEquipoService;
    }

    @GetMapping("/equipo-instalacion")
    @PreAuthorize("@permissionEvaluator.hasPermission(authentication, this.entityName, 'CONSULTAR')")
    public ResponseEntity<List<IEquipoInstalacionDTO>> findEquiposInstalaciones(@RequestParam String codigoEquipo, @RequestParam String nombreInstalacion ) {
        List<IEquipoInstalacionDTO> equiposInstalaciones = service.findEquiposInstalaciones(codigoEquipo, nombreInstalacion);
        return ResponseEntity.ok(equiposInstalaciones);
    }

    @GetMapping("/hoja-vida-equipo/{idEquipo}")
    @PreAuthorize("@permissionEvaluator.hasPermission(authentication, this.entityName, 'CONSULTAR')")
    public ResponseEntity<HojaDeVidaEquipoDTO> getHojaDeVida(@PathVariable Long idEquipo) {
        HojaDeVidaEquipoDTO hoja = hojaDeVidaEquipoService.getHojaDeVidaEquipo(idEquipo);
        return ResponseEntity.ok(hoja);
    }

}
