package com.corhuila.sgie.Site.Controller;

import com.corhuila.sgie.Site.DTO.CampusReporteDTO;
import com.corhuila.sgie.Site.Entity.Campus;
import com.corhuila.sgie.Site.IRepository.ICampusRepository;
import com.corhuila.sgie.Site.Service.CampusService;
import com.corhuila.sgie.common.ApiResponseDto;
import com.corhuila.sgie.common.Reporting.GeneradorReporteUtil;
import com.corhuila.sgie.common.Reporting.ReportFormat;
import com.corhuila.sgie.common.Reporting.ReporteGenericoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CampusControllerTest {

    @Mock
    private CampusService campusService;
    @Mock
    private ReporteGenericoService reporteGenericoService;
    @Mock
    private ICampusRepository campusRepository;

    private CampusController controller;

    @BeforeEach
    void setup() {
        controller = new CampusController(campusService, reporteGenericoService, campusRepository);
        ReflectionTestUtils.setField(controller, "repository", campusRepository);
        ReflectionTestUtils.setField(controller, "campusService", campusService);
    }

    @Test
    void byMunicipioDevuelveLista() {
        Campus campus = new Campus();
        campus.setId(1L);
        campus.setNombre("Principal");
        when(campusRepository.findByMunicipioIdAndStateTrue(2L)).thenReturn(List.of(campus));

        ResponseEntity<ApiResponseDto<List<Campus>>> response = controller.byMunicipio(2L);
        assertThat(response.getBody().getData()).containsExactly(campus);
    }

    @Test
    void exportarCampusMemoriaGeneraReporte() {
        CampusReporteDTO dto = new CampusReporteDTO(1L, "America", "Colombia",
                "Huila", "Neiva", "Principal");
        when(campusService.obtenerDatosEnMemoria()).thenReturn(List.of(dto));
        GeneradorReporteUtil.GeneratedReport report = new GeneradorReporteUtil.GeneratedReport(
                new ByteArrayResource("demo".getBytes()),
                MediaType.APPLICATION_OCTET_STREAM,
                "campus.csv");
        when(reporteGenericoService.generar(eq(ReportFormat.CSV), anyList(), eq(CampusReporteDTO.class), anyString(), anyString()))
                .thenReturn(report);

        ResponseEntity<StreamingResponseBody> response = controller.exportarCampus("csv", "memoria");
        assertThat(response.getHeaders().getContentDisposition().getFilename()).isEqualTo("campus.csv");
    }

    @Test
    void exportarCampusStreamingRetornaRespuesta() {
        ResponseEntity<StreamingResponseBody> streaming = ResponseEntity.ok(outputStream -> {
        });
        when(campusService.proveedorStream()).thenReturn(() -> Stream.of(
                new CampusReporteDTO(1L, "America", "Colombia", "Huila", "Neiva", "Principal")
        ));
        when(reporteGenericoService.generarStreaming(eq(ReportFormat.PDF), any(), eq(CampusReporteDTO.class), anyString(), anyString()))
                .thenReturn(streaming);

        ResponseEntity<StreamingResponseBody> response = controller.exportarCampus("pdf", "stream");
        assertThat(response).isSameAs(streaming);
    }
}
