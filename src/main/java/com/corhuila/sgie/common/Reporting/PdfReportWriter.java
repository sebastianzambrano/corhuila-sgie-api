package com.corhuila.sgie.common.Reporting;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.*;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Stream;

public class PdfReportWriter implements ReportWriter {

    @Override
    public String contentType() {
        return "application/pdf";
    }

    @Override
    public String extension() {
        return ".pdf";
    }

    @Override
    public void write(OutputStream out, Class<?> dtoType, Stream<?> rows, String title) throws Exception {
        Document document = new Document(PageSize.A4.rotate(), 36, 36, 24, 24);
        PdfWriter writer = PdfWriter.getInstance(document, out);
        writer.setCloseStream(false);
        document.open();

        if (title != null && !title.isBlank()) {
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Paragraph paragraph = new Paragraph(title, titleFont);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            paragraph.setSpacingAfter(12f);
            document.add(paragraph);
        }

        List<BeanRowExtractor.ColumnMeta> metas = BeanRowExtractor.metas(dtoType);
        PdfPTable table = new PdfPTable(metas.size());
        table.setWidthPercentage(100f);
        table.setSplitRows(true);
        table.setHeaderRows(1);

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 9);

        for (BeanRowExtractor.ColumnMeta meta : metas) {
            PdfPCell cell = new PdfPCell(new Phrase(meta.header(), headerFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(6f);
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            table.addCell(cell);
        }

        rows.forEach(bean -> {
            List<Object> values = BeanRowExtractor.values(bean);
            try {
                for (Object value : values) {
                    String text = value == null ? "" : String.valueOf(value);
                    PdfPCell cell = new PdfPCell(new Phrase(text, bodyFont));
                    cell.setPadding(5f);
                    table.addCell(cell);
                }
            } catch (Exception ex) {
                throw new IllegalStateException("Error generando el PDF", ex);
            }
        });

        document.add(table);
        document.close();
    }
}
