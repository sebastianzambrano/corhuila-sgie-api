package com.corhuila.sgie.Site.Controller;

import com.corhuila.sgie.Site.Entity.CategoriaInstalacion;
import com.corhuila.sgie.Site.IService.ICategoriaInstalacionService;
import com.corhuila.sgie.common.BaseController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("v1/api/categoria-instalacion")
public class CategoriaInstalacionController extends BaseController<CategoriaInstalacion, ICategoriaInstalacionService> {
    public CategoriaInstalacionController(ICategoriaInstalacionService service) {
        super(service, "CategoriaInstalacion");
    }
}
