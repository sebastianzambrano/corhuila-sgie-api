package com.corhuila.sgie.Booking.Controller;

import com.corhuila.sgie.Booking.Entity.DetalleReservaEquipo;
import com.corhuila.sgie.Booking.IService.IDetalleReservaEquipoService;
import com.corhuila.sgie.common.BaseController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("v1/api/detalle-reserva-equipo")
public class DetalleReservaEquipoController extends BaseController<DetalleReservaEquipo, IDetalleReservaEquipoService> {
    public DetalleReservaEquipoController(IDetalleReservaEquipoService service) {
        super(service, "DetalleReservaEquipo");
    }
}
