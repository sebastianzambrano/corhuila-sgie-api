package com.corhuila.sgie.Equipment.Controller;

import com.corhuila.sgie.Equipment.DTO.EquipoReporteDTO;
import com.corhuila.sgie.Equipment.DTO.HojaDeVidaEquipoDTO;
import com.corhuila.sgie.Equipment.DTO.IEquipoInstalacionDTO;
import com.corhuila.sgie.Equipment.IService.IEquipoService;
import com.corhuila.sgie.Equipment.Service.EquipoService;
import com.corhuila.sgie.Equipment.Service.HojaDeVidaEquipoService;
import com.corhuila.sgie.common.Reporting.GeneradorReporteUtil;
import com.corhuila.sgie.common.Reporting.ReportFormat;
import com.corhuila.sgie.common.Reporting.ReporteGenericoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EquipoControllerTest {

    @Mock
    private IEquipoService equipoBaseService;
    @Mock
    private HojaDeVidaEquipoService hojaDeVidaEquipoService;
    @Mock
    private ReporteGenericoService reporteGenericoService;
    @Mock
    private EquipoService equipoService;

    @InjectMocks
    private EquipoController controller;

    @BeforeEach
    void setup() {
        controller = new EquipoController(equipoBaseService, hojaDeVidaEquipoService, reporteGenericoService, equipoService);
    }

    @Test
    void findEquiposInstalacionesUsaServicioBase() {
        IEquipoInstalacionDTO dto = org.mockito.Mockito.mock(IEquipoInstalacionDTO.class);
        when(equipoBaseService.findEquiposInstalaciones(null, null)).thenReturn(List.of(dto));

        ResponseEntity<List<IEquipoInstalacionDTO>> response = controller.findEquiposInstalaciones(null, null);

        assertThat(response.getBody()).containsExactly(dto);
    }

    @Test
    void getHojaDeVidaEntregaDto() {
        HojaDeVidaEquipoDTO dto = new HojaDeVidaEquipoDTO();
        when(hojaDeVidaEquipoService.getHojaDeVidaEquipo(1L)).thenReturn(dto);

        ResponseEntity<HojaDeVidaEquipoDTO> response = controller.getHojaDeVida(1L);
        assertThat(response.getBody()).isSameAs(dto);
    }

    @Test
    void exportarEquiposModoMemoriaUtilizaReporteGenerico() {
        EquipoReporteDTO dto = new EquipoReporteDTO(1L, "EQ-1", "Proyector",
                true, "Sala 101", "Multimedia", "Campus Norte");
        when(equipoService.obtenerDatosEnMemoria()).thenReturn(List.of(dto));

        GeneradorReporteUtil.GeneratedReport report = new GeneradorReporteUtil.GeneratedReport(
                new ByteArrayResource("demo".getBytes()),
                MediaType.APPLICATION_OCTET_STREAM,
                "reporte.csv");
        when(reporteGenericoService.generar(eq(ReportFormat.CSV), anyList(), eq(EquipoReporteDTO.class), anyString(), anyString()))
                .thenReturn(report);

        ResponseEntity<StreamingResponseBody> response = controller.exportarEquipos("csv", "memoria");
        assertThat(response.getHeaders().getContentDisposition().getFilename()).isEqualTo("reporte.csv");
    }

    @Test
    void exportarEquiposStreamingDevuelveRespuestaDelServicio() {
        ResponseEntity<StreamingResponseBody> streaming = ResponseEntity.ok(outputStream -> {});
        when(equipoService.proveedorStream()).thenReturn(() -> Stream.of(
                new EquipoReporteDTO(1L, "EQ-1", "Proyector", true, "Sala 101", "Multimedia", "Campus Norte")
        ));
        when(reporteGenericoService.generarStreaming(eq(ReportFormat.PDF), any(), eq(EquipoReporteDTO.class), anyString(), anyString()))
                .thenReturn(streaming);

        ResponseEntity<StreamingResponseBody> response = controller.exportarEquipos("pdf", "stream");
        assertThat(response).isSameAs(streaming);
    }
}
