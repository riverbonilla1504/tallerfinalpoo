package com.example.market.util;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.example.market.domain.dto.CitaDto;
import com.example.market.domain.dto.ConsultaDto;
import com.example.market.domain.dto.PagoDto;
import com.example.market.domain.dto.ResumenHistorialDto;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.BaseColor;

@Component
public class PdfGenerator {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // Colores personalizados
    private static final BaseColor PRIMARY_COLOR = new BaseColor(41, 128, 185); // Azul profesional
    private static final BaseColor SECONDARY_COLOR = new BaseColor(52, 152, 219); // Azul claro
    private static final BaseColor ACCENT_COLOR = new BaseColor(231, 76, 60); // Rojo suave
    private static final BaseColor TEXT_COLOR = new BaseColor(44, 62, 80); // Gris oscuro

    public byte[] generarResumenPdf(ResumenHistorialDto resumen) {
        Document document = new Document(PageSize.A4, 36, 36, 54, 36); // Márgenes personalizados
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Fuentes personalizadas
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, PRIMARY_COLOR);
            Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, SECONDARY_COLOR);
            Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, TEXT_COLOR);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, TEXT_COLOR);
            Font highlightFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, ACCENT_COLOR);

            // Encabezado
            Paragraph header = new Paragraph("HISTORIAL MÉDICO", titleFont);
            header.setAlignment(Element.ALIGN_CENTER);
            header.setSpacingAfter(20);
            document.add(header);

            // Línea divisoria
            LineSeparator ls = new LineSeparator(1, 100, PRIMARY_COLOR, Element.ALIGN_CENTER, -2);
            document.add(new Chunk(ls));
            document.add(Chunk.NEWLINE);

            // Datos del Paciente
            document.add(createSectionTitle("INFORMACIÓN DEL PACIENTE", sectionFont));
            PdfPTable patientTable = new PdfPTable(2);
            patientTable.setWidthPercentage(100);
            patientTable.setSpacingBefore(10);
            patientTable.setSpacingAfter(15);

            addTableRow(patientTable, "Nombre:",
                    resumen.getPaciente().getFirstName() + " " + resumen.getPaciente().getLastName(), normalFont);
            addTableRow(patientTable, "Documento:", resumen.getPaciente().getIdNumber(), normalFont);
            addTableRow(patientTable, "Email:", resumen.getPaciente().getEmail(), normalFont);
            addTableRow(patientTable, "Teléfono:", resumen.getPaciente().getPhone(), normalFont);
            document.add(patientTable);

            // Resumen General
            document.add(createSectionTitle("RESUMEN GENERAL", sectionFont));
            PdfPTable summaryTable = new PdfPTable(2);
            summaryTable.setWidthPercentage(100);
            summaryTable.setSpacingBefore(10);
            summaryTable.setSpacingAfter(15);

            addTableRow(summaryTable, "Total Citas:", String.valueOf(resumen.getTotalCitas()), normalFont);
            addTableRow(summaryTable, "Total Consultas:", String.valueOf(resumen.getTotalConsultas()), normalFont);
            addTableRow(summaryTable, "Total Pagos:", "$" + resumen.getTotalPagos(), highlightFont);
            addTableRow(summaryTable, "Promedio Calificaciones:",
                    String.format("%.1f", resumen.getPromedioCalificaciones()), normalFont);
            addTableRow(summaryTable, "Última Consulta:", resumen.getUltimaConsulta(), normalFont);
            addTableRow(summaryTable, "Próxima Cita:", resumen.getProximaCita(), highlightFont);
            document.add(summaryTable);

            // Diagnósticos Frecuentes
            if (resumen.getDiagnosticosFrecuentes() != null && !resumen.getDiagnosticosFrecuentes().isEmpty()) {
                document.add(createSectionTitle("DIAGNÓSTICOS FRECUENTES", sectionFont));
                PdfPTable diagTable = new PdfPTable(1);
                diagTable.setWidthPercentage(100);
                diagTable.setSpacingBefore(10);
                diagTable.setSpacingAfter(15);

                for (String diagnostico : resumen.getDiagnosticosFrecuentes()) {
                    PdfPCell cell = new PdfPCell(new Phrase("• " + diagnostico, normalFont));
                    cell.setBorder(0);
                    cell.setPadding(5);
                    diagTable.addCell(cell);
                }
                document.add(diagTable);
            }

            // Medicamentos Frecuentes
            if (resumen.getMedicamentosFrecuentes() != null && !resumen.getMedicamentosFrecuentes().isEmpty()) {
                document.add(createSectionTitle("MEDICAMENTOS FRECUENTES", sectionFont));
                PdfPTable medTable = new PdfPTable(1);
                medTable.setWidthPercentage(100);
                medTable.setSpacingBefore(10);
                medTable.setSpacingAfter(15);

                for (String medicamento : resumen.getMedicamentosFrecuentes()) {
                    PdfPCell cell = new PdfPCell(new Phrase("• " + medicamento, normalFont));
                    cell.setBorder(0);
                    cell.setPadding(5);
                    medTable.addCell(cell);
                }
                document.add(medTable);
            }

            // Historial de Consultas
            if (resumen.getConsultas() != null && !resumen.getConsultas().isEmpty()) {
                document.add(createSectionTitle("HISTORIAL DE CONSULTAS", sectionFont));
                for (ConsultaDto consulta : resumen.getConsultas()) {
                    PdfPTable consultaTable = new PdfPTable(1);
                    consultaTable.setWidthPercentage(100);
                    consultaTable.setSpacingBefore(10);
                    consultaTable.setSpacingAfter(15);

                    addTableRow(consultaTable, "Fecha:", consulta.getFecha().format(DATE_TIME_FORMATTER), normalFont);
                    addTableRow(consultaTable, "Médico:", consulta.getMedicoNombre(), normalFont);
                    addTableRow(consultaTable, "Diagnóstico:", consulta.getDiagnostico(), highlightFont);
                    addTableRow(consultaTable, "Prescripción:", consulta.getPrescripcion(), normalFont);
                    if (consulta.getObservaciones() != null && !consulta.getObservaciones().isEmpty()) {
                        addTableRow(consultaTable, "Observaciones:", consulta.getObservaciones(), normalFont);
                    }
                    document.add(consultaTable);
                    document.add(new Chunk(ls));
                }
            }

            // Historial de Citas
            if (resumen.getCitas() != null && !resumen.getCitas().isEmpty()) {
                document.add(createSectionTitle("HISTORIAL DE CITAS", sectionFont));
                for (CitaDto cita : resumen.getCitas()) {
                    PdfPTable citaTable = new PdfPTable(1);
                    citaTable.setWidthPercentage(100);
                    citaTable.setSpacingBefore(10);
                    citaTable.setSpacingAfter(15);

                    addTableRow(citaTable, "Fecha:", cita.getFechaHora().format(DATE_TIME_FORMATTER), normalFont);
                    addTableRow(citaTable, "Médico:", cita.getNombreMedico(), normalFont);
                    addTableRow(citaTable, "Especialidad:", cita.getEspecialidadMedico(), normalFont);
                    addTableRow(citaTable, "Motivo:", cita.getMotivo(), highlightFont);
                    addTableRow(citaTable, "Estado:", cita.getEstado(), normalFont);
                    addTableRow(citaTable, "Estado de Pago:", cita.getEstadoPago(), normalFont);
                    document.add(citaTable);
                    document.add(new Chunk(ls));
                }
            }

            // Historial de Pagos
            if (resumen.getPagos() != null && !resumen.getPagos().isEmpty()) {
                document.add(createSectionTitle("HISTORIAL DE PAGOS", sectionFont));
                PdfPTable pagosTable = new PdfPTable(4);
                pagosTable.setWidthPercentage(100);
                pagosTable.setSpacingBefore(10);
                pagosTable.setSpacingAfter(15);

                // Encabezados de la tabla
                String[] headers = { "Fecha", "Monto", "Método", "Estado" };
                for (String headerText : headers) {
                    PdfPCell cell = new PdfPCell(new Phrase(headerText, sectionFont));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setBackgroundColor(SECONDARY_COLOR);
                    cell.setPadding(5);
                    pagosTable.addCell(cell);
                }

                // Datos de pagos
                for (PagoDto pago : resumen.getPagos()) {
                    pagosTable.addCell(new Phrase(pago.getFechaPago().format(DATE_FORMATTER), normalFont));
                    pagosTable.addCell(new Phrase("$" + pago.getMonto(), highlightFont));
                    pagosTable.addCell(new Phrase(pago.getMetodoPago(), normalFont));
                    pagosTable.addCell(new Phrase(pago.getEstadoPago(), normalFont));
                }
                document.add(pagosTable);
            }

            // Pie de página
            Paragraph footer = new Paragraph("Documento generado el " + LocalDate.now().format(DATE_FORMATTER),
                    normalFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(20);
            document.add(footer);

            document.close();
            return out.toByteArray();
        } catch (DocumentException e) {
            throw new RuntimeException("Error al generar el PDF", e);
        }
    }

    private Paragraph createSectionTitle(String title, Font font) {
        Paragraph sectionTitle = new Paragraph(title, font);
        sectionTitle.setSpacingBefore(20);
        sectionTitle.setSpacingAfter(10);
        return sectionTitle;
    }

    private void addTableRow(PdfPTable table, String label, String value, Font font) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, font));
        labelCell.setBorder(0);
        labelCell.setPadding(5);
        labelCell.setBackgroundColor(new BaseColor(240, 240, 240));

        PdfPCell valueCell = new PdfPCell(new Phrase(value, font));
        valueCell.setBorder(0);
        valueCell.setPadding(5);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }
}
