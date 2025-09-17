package com.corhuila.sgie.User.Controller;

import com.corhuila.sgie.User.Entity.Persona;
import com.corhuila.sgie.User.IService.IPersonaService;
import com.corhuila.sgie.common.BaseController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("v1/api/persona")
public class PersonaController extends BaseController<Persona, IPersonaService> {
    public PersonaController(IPersonaService service) {
        super(service, "PERSONA");
    }
}
