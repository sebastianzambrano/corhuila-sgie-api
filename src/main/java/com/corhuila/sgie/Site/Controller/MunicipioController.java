package com.corhuila.sgie.Site.Controller;

import com.corhuila.sgie.Site.Entity.Municipio;
import com.corhuila.sgie.Site.IService.IMunicipioService;
import com.corhuila.sgie.common.BaseController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("v1/api/municipio")
public class MunicipioController extends BaseController <Municipio, IMunicipioService>{
    public MunicipioController(IMunicipioService service) {
        super(service, "Municipio");
    }
}
