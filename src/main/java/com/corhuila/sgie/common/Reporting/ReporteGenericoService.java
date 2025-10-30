package com.corhuila.sgie.common.Reporting;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Service
public class ReporteGenericoService {

    private final TransactionTemplate readOnlyTxTemplate;

    public ReporteGenericoService(PlatformTransactionManager transactionManager) {
        this.readOnlyTxTemplate = new TransactionTemplate(transactionManager);
        this.readOnlyTxTemplate.setReadOnly(true);
        this.readOnlyTxTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
    }

    public <T> GeneradorReporteUtil.GeneratedReport generar(ReportFormat format,
                                                            Collection<T> data,
                                                            Class<T> dtoType,
                                                            String fileName,
                                                            String title) {
        Objects.requireNonNull(dtoType, "El tipo de DTO es obligatorio");
        try {
            return GeneradorReporteUtil.generarEnMemoria(format, data, dtoType, fileName, title);
        } catch (Exception ex) {
            throw new IllegalStateException("Error generando el reporte", ex);
        }
    }

    public <T> GeneradorReporteUtil.GeneratedReport generar(ReportFormat format,
                                                            Stream<T> rows,
                                                            Class<T> dtoType,
                                                            String fileName,
                                                            String title) {
        Objects.requireNonNull(dtoType, "El tipo de DTO es obligatorio");
        try {
            return GeneradorReporteUtil.generarEnMemoria(format, rows, dtoType, fileName, title);
        } catch (Exception ex) {
            throw new IllegalStateException("Error generando el reporte", ex);
        }
    }

    public ResponseEntity<Resource> construirRespuesta(GeneradorReporteUtil.GeneratedReport reporte) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(reporte.mediaType());
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename(reporte.fileName())
                .build());
        try {
            long size = reporte.resource().contentLength();
            if (size >= 0) {
                headers.setContentLength(size);
            }
        } catch (Exception ignored) {
            // Se ignora la excepción al hacer flush del buffer porque la respuesta ya fue enviada
        }
        return ResponseEntity.ok()
                .headers(headers)
                .body(reporte.resource());
    }

    public <T> ResponseEntity<Resource> generarYResponder(ReportFormat format,
                                                          Collection<T> data,
                                                          Class<T> dtoType,
                                                          String fileName,
                                                          String title) {
        GeneradorReporteUtil.GeneratedReport reporte = generar(format, data, dtoType, fileName, title);
        return construirRespuesta(reporte);
    }


    public <T> ResponseEntity<StreamingResponseBody> generarStreaming(ReportFormat format,
                                                                      Supplier<Stream<T>> streamSupplier,
                                                                      Class<T> dtoType,
                                                                      String fileName,
                                                                      String title) {
        Objects.requireNonNull(streamSupplier, "El supplier del stream es obligatorio");
        ReportWriter writer = GeneradorReporteUtil.resolveWriter(format);
        String resolvedFileName = GeneradorReporteUtil.sugerirNombreArchivo(fileName, format);

        StreamingResponseBody body = outputStream ->
                readOnlyTxTemplate.execute(status -> {
                    try (Stream<T> stream = streamSupplier.get()) {
                        GeneradorReporteUtil.escribir(outputStream, format, dtoType, stream, title);
                        outputStream.flush();
                    } catch (Exception ex) {
                        throw new IllegalStateException("Error generando el reporte", ex);
                    }
                    return null;
                });

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(writer.contentType()));
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename(resolvedFileName)
                .build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(body);
    }

    /*
    public <T> ResponseEntity<StreamingResponseBody> generarStreaming(
            ReportFormat format,
            Supplier<Stream<T>> streamSupplier,
            Class<T> dtoType,
            String fileName,
            String title
    ) {
        Objects.requireNonNull(streamSupplier, "El supplier del stream es obligatorio");

        // 1) Genera el reporte DENTRO de la transacción y CIERRA el stream.
        //    Si algo falla aquí, aún NO se han enviado headers binarios y tu @ControllerAdvice
        //    podrá responder JSON sin romper el content-type.
        GeneradorReporteUtil.GeneratedReport reporte = readOnlyTxTemplate.execute(status -> {
            try (Stream<T> stream = streamSupplier.get()) {
                return GeneradorReporteUtil.generarEnMemoria(format, stream, dtoType, fileName, title);
            } catch (Exception ex) {
                throw new IllegalStateException("Error generando el reporte", ex);
            }
        });

        // 2) Prepara el cuerpo streaming leyendo del Resource ya generado en memoria.
        StreamingResponseBody body = os -> {
            try (var in = reporte.resource().getInputStream()) {
                in.transferTo(os);
                os.flush();
            }
        };

        // 3) Arma los headers binarios SOLO ahora que ya tienes el archivo listo.
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(reporte.mediaType());
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename(reporte.fileName())
                .build());
        try {
            long size = reporte.resource().contentLength();
            if (size >= 0) {
                headers.setContentLength(size);
            }
        } catch (Exception ignored) {
            // Si no puedes calcular el length, omítelo.
        }

        return ResponseEntity.ok()
                .headers(headers)
                .body(body);
    }
*/

    public <T> void escribirAlResponse(HttpServletResponse response,
                                       ReportFormat format,
                                       Supplier<Stream<T>> streamSupplier,
                                       Class<T> dtoType,
                                       String fileName,
                                       String title) {
        Objects.requireNonNull(response, "El HttpServletResponse es obligatorio");
        Objects.requireNonNull(streamSupplier, "El supplier del stream es obligatorio");

        ReportWriter writer = GeneradorReporteUtil.resolveWriter(format);
        String resolvedFileName = GeneradorReporteUtil.sugerirNombreArchivo(fileName, format);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(writer.contentType());
        ContentDisposition disposition = ContentDisposition.attachment()
                .filename(resolvedFileName)
                .build();
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, disposition.toString());

        final java.io.OutputStream outputStream;
        try {
            outputStream = response.getOutputStream();
        } catch (Exception e) {
            throw new IllegalStateException("No fue posible obtener el OutputStream de la respuesta", e);
        }

        readOnlyTxTemplate.executeWithoutResult(status -> {
            try (Stream<T> stream = streamSupplier.get()) {
                GeneradorReporteUtil.escribir(outputStream, format, dtoType, stream, title);
                outputStream.flush();
            } catch (Exception ex) {
                throw new IllegalStateException("Error generando el reporte", ex);
            }
        });

        try {
            response.flushBuffer();
        } catch (Exception ignored) {
            // Se ignora la excepción al hacer flush del buffer porque la respuesta ya fue enviada al cliente
        }
    }
}
