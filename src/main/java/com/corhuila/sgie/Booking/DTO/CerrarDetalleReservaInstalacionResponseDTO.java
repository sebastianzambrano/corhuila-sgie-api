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
public class CerrarDetalleReservaInstalacionResponseDTO {
    private Long id;
    private boolean state;
    private String entregaInstalacion;
    private LocalDateTime updatedAt;
    private Long idReserva;
}
