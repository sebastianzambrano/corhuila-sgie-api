package com.corhuila.sgie.Equipment.Controller;

import com.corhuila.sgie.Equipment.DTO.HojaDeVidaEquipoDTO;
import com.corhuila.sgie.Equipment.Service.HojaDeVidaEquipoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("v1/api/hoja-de-vida-equipo")
public class HojaDeVidaEquipoController {
    private final HojaDeVidaEquipoService hojaDeVidaEquipoService;

    public HojaDeVidaEquipoController(HojaDeVidaEquipoService hojaDeVidaEquipoService) {
        this.hojaDeVidaEquipoService = hojaDeVidaEquipoService;
    }

    @GetMapping("/{idEquipo}")
    public ResponseEntity<HojaDeVidaEquipoDTO> getHojaDeVida(@PathVariable Long idEquipo) {
        HojaDeVidaEquipoDTO hoja = hojaDeVidaEquipoService.getHojaDeVidaEquipo(idEquipo);
        return ResponseEntity.ok(hoja);
    }
}
