package com.corhuila.sgie.Site.Controller;

import com.corhuila.sgie.Site.Entity.Pais;
import com.corhuila.sgie.Site.IService.IPaisService;
import com.corhuila.sgie.common.BaseController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("v1/api/pais")
public class PaisController extends BaseController<Pais, IPaisService> {
    public PaisController(IPaisService service) {
        super(service, "PAIS");
    }
}
