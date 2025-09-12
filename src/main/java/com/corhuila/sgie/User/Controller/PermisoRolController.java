package com.corhuila.sgie.User.Controller;

import com.corhuila.sgie.User.Entity.PermisoRol;
import com.corhuila.sgie.User.IService.IPermisoRolService;
import com.corhuila.sgie.common.BaseController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("v1/api/permiso-rol")
public class PermisoRolController extends BaseController<PermisoRol, IPermisoRolService> {
    public PermisoRolController(IPermisoRolService service) {
        super(service, "PermisoRol");
    }

}
