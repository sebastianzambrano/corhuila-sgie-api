package com.corhuila.sgie.Site.Controller;

import com.corhuila.sgie.Site.Entity.Instalacion;
import com.corhuila.sgie.Site.IService.IInstalacionService;
import com.corhuila.sgie.common.BaseController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("v1/api/instalacion")
public class InstalacionController extends BaseController <Instalacion, IInstalacionService>{
    public InstalacionController(IInstalacionService service) {
        super(service, "INSTALACION");
    }
}
