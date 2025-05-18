package com.example.market.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.market.domain.dto.ResumenHistorialDto;
import com.example.market.domain.dto.ResumenPacienteDto;
import com.example.market.domain.service.HistorialMedicoService;
import com.example.market.util.PdfGenerator;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/historial")
@RequiredArgsConstructor
public class HistorialController {

    @Autowired
    private HistorialMedicoService historialService;
    @Autowired
    private PdfGenerator pdfGenerator;

    @GetMapping("/paciente/{id}")
    public ResponseEntity<ResumenPacienteDto> obtenerResumenPaciente(@PathVariable Long id) {
        return ResponseEntity.ok(historialService.obtenerResumenBasicoPaciente(id));
    }

    @GetMapping("/resumen/{id}")
    public ResponseEntity<ResumenHistorialDto> obtenerResumen(@PathVariable Long id) {
        ResumenHistorialDto resumen = historialService.obtenerResumenPorPacienteId(id);
        return ResponseEntity.ok(resumen);
    }

    @GetMapping("/resumen/{id}/pdf")
    public ResponseEntity<ByteArrayResource> obtenerResumenPdf(@PathVariable Long id) {
        ResumenHistorialDto resumen = historialService.obtenerResumenPorPacienteId(id);
        byte[] pdfBytes = pdfGenerator.generarResumenPdf(resumen);

        ByteArrayResource resource = new ByteArrayResource(pdfBytes);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=historial_paciente_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdfBytes.length)
                .body(resource);
    }
}