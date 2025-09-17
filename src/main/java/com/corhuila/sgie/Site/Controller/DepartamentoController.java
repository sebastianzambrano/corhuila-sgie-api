package com.corhuila.sgie.Site.Controller;

import com.corhuila.sgie.Site.Entity.Departamento;
import com.corhuila.sgie.Site.IService.IDepartamentoService;
import com.corhuila.sgie.common.BaseController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("v1/api/departamento")
public class DepartamentoController extends BaseController<Departamento, IDepartamentoService> {
    public DepartamentoController(IDepartamentoService service) {
        super(service, "DEPARTAMENTO");
    }
}
