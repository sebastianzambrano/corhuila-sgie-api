package com.corhuila.sgie.common.Reporting;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public final class GeneradorReporteUtil {

    private static final Map<ReportFormat, ReportWriter> WRITERS = new EnumMap<>(ReportFormat.class);

    static {
        WRITERS.put(ReportFormat.CSV, new CsvReportWriter());
        WRITERS.put(ReportFormat.XLSX, new XlsxReportWriter());
        WRITERS.put(ReportFormat.PDF, new PdfReportWriter());
    }

    private GeneradorReporteUtil() {
    }

    public static void escribir(OutputStream out,
                                ReportFormat format,
                                Class<?> dtoType,
                                Stream<?> rows,
                                String title) throws Exception {
        Objects.requireNonNull(out, "El OutputStream es obligatorio");
        Objects.requireNonNull(format, "El formato es obligatorio");
        Objects.requireNonNull(dtoType, "El tipo de DTO es obligatorio");
        ReportWriter writer = resolveWriter(format);
        writer.write(out, dtoType, rows, title);
    }

    public static GeneratedReport generarEnMemoria(ReportFormat format,
                                                   Collection<?> data,
                                                   Class<?> dtoType,
                                                   String fileName,
                                                   String title) throws Exception {
        Stream<?> stream = data == null ? Stream.empty() : data.stream();
        return generarEnMemoria(format, stream, dtoType, fileName, title);
    }

    public static GeneratedReport generarEnMemoria(ReportFormat format,
                                                   Stream<?> rows,
                                                   Class<?> dtoType,
                                                   String fileName,
                                                   String title) throws Exception {
        Objects.requireNonNull(rows, "El stream de datos es obligatorio");
        ReportWriter writer = resolveWriter(format);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            writer.write(baos, dtoType, rows, title);
            String filename = buildFileName(fileName, writer);
            Resource resource = new ByteArrayResource(baos.toByteArray());
            MediaType mediaType = MediaType.parseMediaType(writer.contentType());
            return new GeneratedReport(resource, mediaType, filename);
        }
    }

    public static ReportWriter resolveWriter(ReportFormat format) {
        ReportWriter writer = WRITERS.get(format);
        if (writer == null) {
            throw new IllegalArgumentException("No existe writer configurado para el formato " + format);
        }
        return writer;
    }

    public static String sugerirNombreArchivo(String baseName, ReportFormat format) {
        return buildFileName(baseName, resolveWriter(format));
    }

    private static String buildFileName(String baseName, ReportWriter writer) {
        String sanitized = sanitize(baseName);
        if (sanitized.isEmpty()) {
            sanitized = "reporte";
        }
        if (!sanitized.toLowerCase(Locale.ROOT).endsWith(writer.extension())) {
            sanitized = sanitized + writer.extension();
        }
        return sanitized;
    }

    private static String sanitize(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        String cleaned = normalized.replaceAll("[^a-zA-Z0-9-_\\.]", "_");
        byte[] bytes = cleaned.getBytes(StandardCharsets.UTF_8);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public record GeneratedReport(Resource resource, MediaType mediaType, String fileName) {
    }
}
