package com.corhuila.sgie.common.Reporting;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class GeneradorReporteUtilTest {

    @Test
    void generarEnMemoriaCsvSanitizaNombre() throws Exception {
        GeneradorReporteUtil.GeneratedReport report = GeneradorReporteUtil.generarEnMemoria(
                ReportFormat.CSV,
                List.of(new SampleRow("Item", 5)),
                SampleRow.class,
                "reporte demo",
                "Titulo");

        assertThat(report.fileName()).isEqualTo("reporte_demo.csv");
        assertThat(report.mediaType().toString()).contains("csv");
        assertThat(report.resource().contentLength()).isGreaterThan(0);
    }

    @Test
    void generarEnMemoriaPdfProduceContenido() throws Exception {
        GeneradorReporteUtil.GeneratedReport report = GeneradorReporteUtil.generarEnMemoria(
                ReportFormat.PDF,
                Stream.of(new SampleRow("Item", 3)),
                SampleRow.class,
                "reporte",
                "Titulo");

        Resource resource = report.resource();
        assertThat(resource.contentLength()).isGreaterThan(0);
    }

    @Test
    void resolveWriterPermiteEscribirXlsx() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ReportWriter writer = GeneradorReporteUtil.resolveWriter(ReportFormat.XLSX);
        writer.write(baos, SampleRow.class, Stream.of(new SampleRow("Item", 1)), "Titulo");

        assertThat(baos.toByteArray()).isNotEmpty();
    }

    private record SampleRow(
            @ReportColumn(header = "Nombre", order = 0) String nombre,
            @ReportColumn(header = "Cantidad", order = 1) int cantidad) {
    }
}
