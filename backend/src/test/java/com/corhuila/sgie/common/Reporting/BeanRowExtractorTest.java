package com.corhuila.sgie.common.Reporting;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BeanRowExtractorTest {

    @Test
    void headersRespetanAnotaciones() {
        List<String> headers = BeanRowExtractor.headers(SampleDto.class);
        assertThat(headers).containsExactly("Nombre", "Valor");
    }

    @Test
    void valuesObtienenDatos() {
        SampleDto dto = new SampleDto("Demo", 5);
        assertThat(BeanRowExtractor.values(dto)).containsExactly("Demo", 5);
    }

    @Test
    void recordSeProcesaAutomaticamente() {
        List<String> headers = BeanRowExtractor.headers(RecordDto.class);
        assertThat(headers).contains("Texto");
    }

    private static class SampleDto {
        @ReportColumn(header = "Nombre", order = 0)
        private final String nombre;
        @ReportColumn(header = "Valor", order = 1)
        private final int valor;

        private SampleDto(String nombre, int valor) {
            this.nombre = nombre;
            this.valor = valor;
        }
    }

    private record RecordDto(String texto) {}
}
