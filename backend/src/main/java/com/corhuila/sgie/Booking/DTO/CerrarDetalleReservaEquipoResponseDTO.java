package com.corhuila.sgie.Booking.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CerrarDetalleReservaEquipoResponseDTO {
    private Long id;
    private Boolean state;
    private String entregaEquipo;
    private LocalDateTime updatedAt;
    private Long idReserva;
}
