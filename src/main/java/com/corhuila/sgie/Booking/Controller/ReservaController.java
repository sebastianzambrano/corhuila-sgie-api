package com.corhuila.sgie.Booking.Controller;

import com.corhuila.sgie.Booking.DTO.HoraDisponibleDTO;
import com.corhuila.sgie.Booking.DTO.IReservaGeneralDTO;
import com.corhuila.sgie.Booking.DTO.ReservaGeneralReporteDTO;
import com.corhuila.sgie.Booking.Entity.Reserva;
import com.corhuila.sgie.Booking.IService.IReservaService;
import com.corhuila.sgie.Booking.Service.ReservaService;
import com.corhuila.sgie.common.BaseController;
import com.corhuila.sgie.common.Reporting.GeneradorReporteUtil;
import com.corhuila.sgie.common.Reporting.ReportFormat;
import com.corhuila.sgie.common.Reporting.ReporteGenericoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.corhuila.sgie.common.Reporting.HelperUtils.buildHeaders;
import static com.corhuila.sgie.common.Reporting.HelperUtils.isStreaming;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("v1/api/reserva")
public class ReservaController extends BaseController<Reserva, IReservaService> {

    private final ReporteGenericoService reporteGenericoService;
    private final ReservaService reservaService;

    public ReservaController(IReservaService service, ReporteGenericoService reporteGenericoService, ReservaService reservaService) {
        super(service, "RESERVA");
        this.reporteGenericoService = reporteGenericoService;
        this.reservaService = reservaService;
    }

    @GetMapping("/horas-disponibles-instalacion")
    @PreAuthorize("@permissionEvaluator.hasPermission(authentication, this.entityName, 'CONSULTAR')")
    public List<HoraDisponibleDTO> getHorasDisponiblesInstalacion(
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam("idInstalacion") Integer idInstalacion,
            @RequestParam(value = "idDetalle", required = false) Long idDetalle) {

        return service.getHorasDisponiblesInstalacion(fecha, idInstalacion, idDetalle);
    }

    @GetMapping("/horas-disponibles-equipo")
    @PreAuthorize("@permissionEvaluator.hasPermission(authentication, this.entityName, 'CONSULTAR')")
    public List<HoraDisponibleDTO> getHorasDisponiblesEquipo(
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam("idEquipo") Integer idEquipo,
            @RequestParam(value = "idDetalle", required = false) Long idDetalle) {

        return service.getHorasDisponiblesEquipo(fecha, idEquipo, idDetalle);
    }

    @GetMapping("/reservas-mantenimientos")
    @PreAuthorize("@permissionEvaluator.hasPermission(authentication, this.entityName, 'CONSULTAR')")
    public ResponseEntity<List<IReservaGeneralDTO>> findReservasYMantenimientosByNumeroIdentificacion(@RequestParam(required = false) String numeroIdentificacion) {
        List<IReservaGeneralDTO> reservas = service.findReservasYMantenimientosByNumeroIdentificacion(numeroIdentificacion);
        return ResponseEntity.ok(reservas);
    }

    @GetMapping(value = "/reporte/{formato}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @PreAuthorize("@permissionEvaluator.hasPermission(authentication, this.entityName, 'CONSULTAR')")
    @Transactional(readOnly = true)
    public ResponseEntity<StreamingResponseBody> exportarReservas(@PathVariable("formato") String formato, @RequestParam(defaultValue = "stream") String modo, @RequestParam(required = false) String numeroIdentificacion) {
        ReportFormat format = ReportFormat.fromName(formato);
        Supplier<Stream<ReservaGeneralReporteDTO>> supplier = reservaService.proveedorStream(numeroIdentificacion);

        if (!isStreaming(modo)) {
            List<ReservaGeneralReporteDTO> datos = reservaService.obtenerDatosEnMemoria(numeroIdentificacion);
            GeneradorReporteUtil.GeneratedReport reporte = reporteGenericoService.generar(
                    format,
                    datos,
                    ReservaGeneralReporteDTO.class,
                    "reporte_reservas",
                    "Reporte de reservas"
            );

            StreamingResponseBody body = outputStream -> {
                try (var input = reporte.resource().getInputStream()) {
                    input.transferTo(outputStream);
                }
            };

            HttpHeaders headers = buildHeaders(reporte);
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(body);
        }

        return reporteGenericoService.generarStreaming(
                format,
                supplier,
                ReservaGeneralReporteDTO.class,
                "reporte_reservas",
                "Reporte de reservas"
        );
    }
}
