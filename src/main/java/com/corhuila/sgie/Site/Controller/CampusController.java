package com.corhuila.sgie.Site.Controller;

import com.corhuila.sgie.Site.Entity.Campus;
import com.corhuila.sgie.Site.IRepository.ICampusRepository;
import com.corhuila.sgie.Site.IRepository.IMunicipioRepository;
import com.corhuila.sgie.Site.Service.CampusService;
import com.corhuila.sgie.common.ApiResponseDto;
import com.corhuila.sgie.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("v1/api/campus")
public class CampusController extends BaseController <Campus, CampusService> {

    @Autowired
    private ICampusRepository repository;

    public CampusController(CampusService service) {
        super(service, "CAMPUS");
    }

    @GetMapping("/por-municipio/{id}")
    @PreAuthorize("@permissionEvaluator.hasPermission(authentication, this.entityName, 'CONSULTAR')")
    public ResponseEntity<ApiResponseDto<List<Campus>>> byMunicipio(@PathVariable Long id) {
        List<Campus> data = repository.findByMunicipioIdAndStateTrue(id);
        return ResponseEntity.ok(new ApiResponseDto<>("Datos obtenidos", data, true));
    }
}
