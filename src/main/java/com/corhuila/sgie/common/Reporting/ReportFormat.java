package com.corhuila.sgie.common.Reporting;

import java.util.Arrays;
import java.util.Locale;

public enum ReportFormat {
    CSV,
    XLSX,
    PDF;

    public static ReportFormat fromName(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El formato de reporte es obligatorio");
        }
        String normalized = value.trim().toUpperCase(Locale.ROOT);
        return Arrays.stream(values())
                .filter(f -> f.name().equalsIgnoreCase(normalized))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Formato de reporte no soportado: " + value));
    }

    public String lowerCase() {
        return name().toLowerCase(Locale.ROOT);
    }
}
