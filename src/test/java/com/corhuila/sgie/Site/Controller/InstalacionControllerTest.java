package com.corhuila.sgie.Site.Controller;

import com.corhuila.sgie.Site.DTO.IInstalacionCampusDTO;
import com.corhuila.sgie.Site.DTO.InstalacionReporteDTO;
import com.corhuila.sgie.Site.Entity.Instalacion;
import com.corhuila.sgie.Site.IService.IInstalacionService;
import com.corhuila.sgie.Site.Service.InstalacionService;
import com.corhuila.sgie.common.ApiResponseDto;
import com.corhuila.sgie.common.EstadoDTO;
import com.corhuila.sgie.common.Reporting.GeneradorReporteUtil;
import com.corhuila.sgie.common.Reporting.ReportFormat;
import com.corhuila.sgie.common.Reporting.ReporteGenericoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InstalacionControllerTest {

    @Mock
    private IInstalacionService baseService;
    @Mock
    private ReporteGenericoService reporteGenericoService;
    @Mock
    private InstalacionService instalacionService;

    private InstalacionController controller;

    @BeforeEach
    void setup() {
        controller = new InstalacionController(baseService, reporteGenericoService, instalacionService);
    }

    @Test
    void findByStateTrueUsaServicioBase() {
        Instalacion instalacion = new Instalacion();
        instalacion.setNombre("Bloque A");
        when(baseService.findByStateTrue()).thenReturn(List.of(instalacion));

        ResponseEntity<ApiResponseDto<List<Instalacion>>> response = controller.findByStateTrue();

        assertThat(response.getBody().getData()).containsExactly(instalacion);
        verify(baseService).findByStateTrue();
    }

    @Test
    void saveDelegatesToService() throws Exception {
        Instalacion instalacion = new Instalacion();
        when(baseService.save(instalacion)).thenReturn(instalacion);

        ResponseEntity<ApiResponseDto<Instalacion>> response = controller.save(instalacion);
        assertThat(response.getBody().getData()).isSameAs(instalacion);
        verify(baseService).save(instalacion);
    }

    @Test
    void findInstalacionesCampusRetornaLista() {
        IInstalacionCampusDTO dto = mock(IInstalacionCampusDTO.class);
        when(baseService.findInstalacionesCampus("lab", "campus")).thenReturn(List.of(dto));

        ResponseEntity<List<IInstalacionCampusDTO>> response = controller.findInstalacionesCampus("lab", "campus");

        assertThat(response.getBody()).containsExactly(dto);
    }

    @Test
    void exportarInstalacionesMemoriaGeneraArchivo() {
        InstalacionReporteDTO dto = new InstalacionReporteDTO(
                1L,
                "América",
                "Colombia",
                "Huila",
                "Neiva",
                "Principal",
                "Laboratorio",
                "Categoría");
        when(instalacionService.obtenerDatosEnMemoria()).thenReturn(List.of(dto));
        GeneradorReporteUtil.GeneratedReport report = new GeneradorReporteUtil.GeneratedReport(
                new ByteArrayResource("demo".getBytes()),
                MediaType.APPLICATION_OCTET_STREAM,
                "instalaciones.csv");
        when(reporteGenericoService.generar(eq(ReportFormat.CSV), anyList(), eq(InstalacionReporteDTO.class), anyString(), anyString()))
                .thenReturn(report);

        ResponseEntity<StreamingResponseBody> response = controller.exportarInstalaciones("csv", "memoria");

        assertThat(response.getHeaders().getContentDisposition().getFilename()).isEqualTo("instalaciones.csv");
    }

    @Test
    void exportarInstalacionesStreamingRetornaRespuesta() {
        ResponseEntity<StreamingResponseBody> streaming = ResponseEntity.ok(outputStream -> {});
        when(instalacionService.proveedorStream()).thenReturn(() -> Stream.of(
                new InstalacionReporteDTO(
                        1L,
                        "América",
                        "Colombia",
                        "Huila",
                        "Neiva",
                        "Principal",
                        "Laboratorio",
                        "Categoría")
        ));
        when(reporteGenericoService.generarStreaming(eq(ReportFormat.PDF), any(), eq(InstalacionReporteDTO.class), anyString(), anyString()))
                .thenReturn(streaming);

        ResponseEntity<StreamingResponseBody> response = controller.exportarInstalaciones("pdf", "stream");
        assertThat(response).isSameAs(streaming);
    }

    @Test
    void updateInvocaServiceBase() throws Exception {
        Instalacion instalacion = new Instalacion();
        ResponseEntity<ApiResponseDto<Instalacion>> response = controller.update(10L, instalacion);
        assertThat(response.getBody().getStatus()).isTrue();
        verify(baseService).update(10L, instalacion);
    }

    @Test
    void cambiarEstadoPropagaDto() throws Exception {
        EstadoDTO estadoDTO = new EstadoDTO();
        estadoDTO.setEstado(Boolean.FALSE);

        controller.cambiarEstado(12L, estadoDTO);

        ArgumentCaptor<Boolean> captor = ArgumentCaptor.forClass(Boolean.class);
        verify(baseService).cambiarEstado(eq(12L), captor.capture());
        assertThat(captor.getValue()).isFalse();
    }

    @Test
    void deleteInvocaServicio() throws Exception {
        controller.delete(8L);
        verify(baseService).delete(8L);
    }
}
