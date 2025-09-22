package com.corhuila.sgie.Site.Controller;

import com.corhuila.sgie.Site.DTO.IInstalacionCampusDTO;
import com.corhuila.sgie.Site.Entity.Instalacion;
import com.corhuila.sgie.Site.IService.IInstalacionService;
import com.corhuila.sgie.User.DTO.IPermisoPorPersonaDTO;
import com.corhuila.sgie.common.BaseController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("v1/api/instalacion")
public class InstalacionController extends BaseController <Instalacion, IInstalacionService>{
    public InstalacionController(IInstalacionService service) {
        super(service, "INSTALACION");
    }

    @GetMapping("/instalacion-campus")
    @PreAuthorize("@permissionEvaluator.hasPermission(authentication, this.entityName, 'CONSULTAR')")
    public ResponseEntity<List<IInstalacionCampusDTO>> findInstalacionesCampus(@RequestParam String nombreInstalacion, @RequestParam String nombreCampus) {
        List<IInstalacionCampusDTO> instalacionesCampus = service.findInstalacionesCampus(nombreInstalacion,nombreCampus);
        return ResponseEntity.ok(instalacionesCampus);
    }
}
