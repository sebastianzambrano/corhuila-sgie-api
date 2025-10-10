package com.corhuila.sgie.Site.Controller;

import com.corhuila.sgie.Site.Entity.Pais;
import com.corhuila.sgie.Site.IRepository.IPaisRepository;
import com.corhuila.sgie.Site.IService.IPaisService;
import com.corhuila.sgie.common.ApiResponseDto;
import com.corhuila.sgie.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("v1/api/pais")
public class PaisController extends BaseController<Pais, IPaisService> {
    @Autowired
    private IPaisRepository repository;

    public PaisController(IPaisService service) {
        super(service, "PAIS");
    }

    @GetMapping("/por-continente/{id}")
    @PreAuthorize("@permissionEvaluator.hasPermission(authentication, this.entityName, 'CONSULTAR')")
    public ResponseEntity<ApiResponseDto<List<Pais>>> byContinente(@PathVariable Long id) {
        List<Pais> data = repository.findByContinenteIdAndStateTrue(id);
        return ResponseEntity.ok(new ApiResponseDto<>("Datos obtenidos", data, true));}
}
