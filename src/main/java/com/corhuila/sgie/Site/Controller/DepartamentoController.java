package com.corhuila.sgie.Site.Controller;

import com.corhuila.sgie.Site.Entity.Departamento;
import com.corhuila.sgie.Site.IRepository.IDepartamentoRepository;
import com.corhuila.sgie.Site.IRepository.IPaisRepository;
import com.corhuila.sgie.Site.IService.IDepartamentoService;
import com.corhuila.sgie.common.ApiResponseDto;
import com.corhuila.sgie.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("v1/api/departamento")
public class DepartamentoController extends BaseController<Departamento, IDepartamentoService> {

    @Autowired
    private IDepartamentoRepository repository;

    public DepartamentoController(IDepartamentoService service) {
        super(service, "DEPARTAMENTO");
    }

    @GetMapping("/por-pais/{id}")
    @PreAuthorize("@permissionEvaluator.hasPermission(authentication, this.entityName, 'CONSULTAR')")
    public ResponseEntity<ApiResponseDto<List<Departamento>>> byPais(@PathVariable Long id) {
        List<Departamento> data = repository.findByPaisIdAndStateTrue(id);
        return ResponseEntity.ok(new ApiResponseDto<>("Datos obtenidos", data, true));
    }

}
