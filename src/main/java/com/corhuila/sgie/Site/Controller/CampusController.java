package com.corhuila.sgie.Site.Controller;

import com.corhuila.sgie.Site.Entity.Campus;
import com.corhuila.sgie.Site.Service.CampusService;
import com.corhuila.sgie.common.BaseController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("v1/api/campus")
public class CampusController extends BaseController <Campus, CampusService> {
    public CampusController(CampusService service) {
        super(service, "Campus");
    }
}
