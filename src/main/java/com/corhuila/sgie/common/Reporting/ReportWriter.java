package com.corhuila.sgie.common.Reporting;

import java.io.OutputStream;
import java.util.stream.Stream;

public interface ReportWriter {
    String contentType();
    String extension();
    void write(OutputStream out, Class<?> dtoType, Stream<?> rows, String title) throws Exception;
}
