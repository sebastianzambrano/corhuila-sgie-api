package com.corhuila.sgie.Site.Controller;

import com.corhuila.sgie.Site.Entity.Municipio;
import com.corhuila.sgie.Site.IRepository.IMunicipioRepository;
import com.corhuila.sgie.Site.IService.IMunicipioService;
import com.corhuila.sgie.common.ApiResponseDto;
import com.corhuila.sgie.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("v1/api/municipio")
public class MunicipioController extends BaseController<Municipio, IMunicipioService> {
    @Autowired
    private IMunicipioRepository repository;

    public MunicipioController(IMunicipioService service) {
        super(service, "MUNICIPIO");
    }

    @GetMapping("/por-departamento/{id}")
    @PreAuthorize("@permissionEvaluator.hasPermission(authentication, this.entityName, 'CONSULTAR')")
    public ResponseEntity<ApiResponseDto<List<Municipio>>> byDepartamento(@PathVariable Long id) {
        List<Municipio> data = repository.findByDepartamentoIdAndStateTrue(id);
        return ResponseEntity.ok(new ApiResponseDto<>("Datos obtenidos", data, true));
    }
}
