package com.corhuila.sgie.Booking.Controller;

import com.corhuila.sgie.Booking.Entity.DetalleReservaInstalacion;
import com.corhuila.sgie.Booking.IService.IDetalleReservaInstalacionService;
import com.corhuila.sgie.common.BaseController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("v1/api/detalle-reserva-instalacion")
public class DetalleReservaInstalacionController extends BaseController<DetalleReservaInstalacion,IDetalleReservaInstalacionService> {
    public DetalleReservaInstalacionController(IDetalleReservaInstalacionService service) {
        super(service, "DetalleReservaInstalacion");
    }
}
