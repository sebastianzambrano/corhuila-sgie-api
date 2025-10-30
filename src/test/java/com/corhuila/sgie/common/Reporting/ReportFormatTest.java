package com.corhuila.sgie.common.Reporting;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReportFormatTest {

    @Test
    void fromNameReconoceDistintasMayusculas() {
        assertThat(ReportFormat.fromName("csv")).isEqualTo(ReportFormat.CSV);
        assertThat(ReportFormat.fromName("PDF")).isEqualTo(ReportFormat.PDF);
    }

    @Test
    void fromNameLanzaCuandoNoExiste() {
        assertThatThrownBy(() -> ReportFormat.fromName("xls"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
