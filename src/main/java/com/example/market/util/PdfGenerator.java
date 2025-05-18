package com.example.market.util;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.example.market.domain.dto.CitaDto;
import com.example.market.domain.dto.ConsultaDto;
import com.example.market.domain.dto.PagoDto;
import com.example.market.domain.dto.ResumenHistorialDto;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

@Component
public class PdfGenerator {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public byte[] generarResumenPdf(ResumenHistorialDto resumen) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            // Título
            document.add(new Paragraph("Historial Médico Completo", titleFont));
            document.add(new Paragraph("\n"));

            // Datos del Paciente
            document.add(new Paragraph("Datos del Paciente:", subtitleFont));
            document.add(new Paragraph(
                    "Nombre: " + resumen.getPaciente().getFirstName() + " " + resumen.getPaciente().getLastName(),
                    normalFont));
            document.add(new Paragraph("Documento: " + resumen.getPaciente().getIdNumber(), normalFont));
            document.add(new Paragraph("Email: " + resumen.getPaciente().getEmail(), normalFont));
            document.add(new Paragraph("Teléfono: " + resumen.getPaciente().getPhone(), normalFont));
            document.add(new Paragraph("\n"));

            // Resumen General
            document.add(new Paragraph("Resumen General:", subtitleFont));
            document.add(new Paragraph("Total Citas: " + resumen.getTotalCitas(), normalFont));
            document.add(new Paragraph("Total Consultas: " + resumen.getTotalConsultas(), normalFont));
            document.add(new Paragraph("Total Pagos: $" + resumen.getTotalPagos(), normalFont));
            document.add(new Paragraph(
                    "Promedio Calificaciones: " + String.format("%.1f", resumen.getPromedioCalificaciones()),
                    normalFont));
            document.add(new Paragraph("Última Consulta: " + resumen.getUltimaConsulta(), normalFont));
            document.add(new Paragraph("Próxima Cita: " + resumen.getProximaCita(), normalFont));
            document.add(new Paragraph("\n"));

            // Diagnósticos Frecuentes
            if (resumen.getDiagnosticosFrecuentes() != null && !resumen.getDiagnosticosFrecuentes().isEmpty()) {
                document.add(new Paragraph("Diagnósticos Frecuentes:", subtitleFont));
                for (String diagnostico : resumen.getDiagnosticosFrecuentes()) {
                    document.add(new Paragraph("• " + diagnostico, normalFont));
                }
                document.add(new Paragraph("\n"));
            }

            // Medicamentos Frecuentes
            if (resumen.getMedicamentosFrecuentes() != null && !resumen.getMedicamentosFrecuentes().isEmpty()) {
                document.add(new Paragraph("Medicamentos Frecuentes:", subtitleFont));
                for (String medicamento : resumen.getMedicamentosFrecuentes()) {
                    document.add(new Paragraph("• " + medicamento, normalFont));
                }
                document.add(new Paragraph("\n"));
            }

            // Consultas
            if (resumen.getConsultas() != null && !resumen.getConsultas().isEmpty()) {
                document.add(new Paragraph("Historial de Consultas:", subtitleFont));
                for (ConsultaDto consulta : resumen.getConsultas()) {
                    document.add(
                            new Paragraph("Fecha: " + consulta.getFecha().format(DATE_TIME_FORMATTER), normalFont));
                    document.add(new Paragraph("Médico: " + consulta.getMedicoNombre(), normalFont));
                    document.add(new Paragraph("Diagnóstico: " + consulta.getDiagnostico(), normalFont));
                    document.add(new Paragraph("Prescripción: " + consulta.getPrescripcion(), normalFont));
                    if (consulta.getObservaciones() != null && !consulta.getObservaciones().isEmpty()) {
                        document.add(new Paragraph("Observaciones: " + consulta.getObservaciones(), normalFont));
                    }
                    document.add(new Paragraph("-------------------"));
                }
                document.add(new Paragraph("\n"));
            }

            // Citas
            if (resumen.getCitas() != null && !resumen.getCitas().isEmpty()) {
                document.add(new Paragraph("Historial de Citas:", subtitleFont));
                for (CitaDto cita : resumen.getCitas()) {
                    document.add(
                            new Paragraph("Fecha: " + cita.getFechaHora().format(DATE_TIME_FORMATTER), normalFont));
                    document.add(new Paragraph("Médico: " + cita.getNombreMedico(), normalFont));
                    document.add(new Paragraph("Especialidad: " + cita.getEspecialidadMedico(), normalFont));
                    document.add(new Paragraph("Motivo: " + cita.getMotivo(), normalFont));
                    document.add(new Paragraph("Estado: " + cita.getEstado(), normalFont));
                    document.add(new Paragraph("Estado de Pago: " + cita.getEstadoPago(), normalFont));
                    document.add(new Paragraph("-------------------"));
                }
                document.add(new Paragraph("\n"));
            }

            // Pagos
            if (resumen.getPagos() != null && !resumen.getPagos().isEmpty()) {
                document.add(new Paragraph("Historial de Pagos:", subtitleFont));
                for (PagoDto pago : resumen.getPagos()) {
                    document.add(new Paragraph("Fecha: " + pago.getFechaPago().format(DATE_FORMATTER), normalFont));
                    document.add(new Paragraph("Monto: $" + pago.getMonto(), normalFont));
                    document.add(new Paragraph("Método: " + pago.getMetodoPago(), normalFont));
                    document.add(new Paragraph("Estado: " + pago.getEstadoPago(), normalFont));
                    document.add(new Paragraph("-------------------"));
                }
            }

            document.close();
            return out.toByteArray();
        } catch (DocumentException e) {
            throw new RuntimeException("Error al generar el PDF", e);
        }
    }
}
