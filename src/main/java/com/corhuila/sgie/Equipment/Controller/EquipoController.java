package com.corhuila.sgie.Equipment.Controller;

import com.corhuila.sgie.Equipment.Entity.Equipo;
import com.corhuila.sgie.Equipment.IService.IEquipoService;
import com.corhuila.sgie.common.BaseController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("v1/api/equipo")
public class EquipoController extends BaseController <Equipo, IEquipoService>{
    public EquipoController(IEquipoService service) {
        super(service, "EQUIPO");
    }
}
