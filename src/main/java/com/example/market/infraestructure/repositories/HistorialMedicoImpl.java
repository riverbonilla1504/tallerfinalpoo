package com.example.market.infraestructure.repositories;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.market.client.CitaClient;
import com.example.market.client.ConsultaClient;
import com.example.market.client.PacienteClient;
import com.example.market.client.PagoClient;
import com.example.market.domain.dto.CitaDto;
import com.example.market.domain.dto.ConsultaDto;
import com.example.market.domain.dto.PacienteDto;
import com.example.market.domain.dto.PagoDto;
import com.example.market.domain.dto.ResumenHistorialDto;
import com.example.market.domain.dto.ResumenPacienteDto;

@Repository
public class HistorialMedicoImpl {

    @Autowired
    private PacienteClient pacienteClient;

    @Autowired
    private CitaClient citaClient;

    @Autowired
    private ConsultaClient consultaClient;

    @Autowired
    private PagoClient pagoClient;

    public ResumenPacienteDto obtenerResumenBasicoPaciente(Long pacienteId) {
        // Obtener datos del paciente
        PacienteDto paciente = pacienteClient.getPacienteById(pacienteId).getBody();
        if (paciente == null) {
            throw new RuntimeException("Paciente no encontrado con ID: " + pacienteId);
        }

        // Obtener citas del paciente
        List<CitaDto> citas = citaClient.getCitasByPacienteId(pacienteId).getBody();
        if (citas == null)
            citas = List.of();

        // Obtener consultas del paciente
        List<ConsultaDto> consultas = consultaClient.getConsultasByPacienteId(pacienteId).getBody();
        if (consultas == null)
            consultas = List.of();

        // Obtener pagos del paciente
        List<PagoDto> pagos = pagoClient.getPagosByPacienteId(pacienteId).getBody();
        if (pagos == null)
            pagos = List.of();

        // Calcular totales
        int totalCitas = citas.size();
        int totalConsultas = consultas.size();
        int totalPagos = pagos.size();
        BigDecimal montoTotalPagos = pagos.stream()
                .map(PagoDto::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return ResumenPacienteDto.builder()
                .paciente(paciente)
                .totalCitas(totalCitas)
                .totalConsultas(totalConsultas)
                .totalPagos(totalPagos)
                .montoTotalPagos(montoTotalPagos)
                .build();
    }

    public ResumenHistorialDto obtenerResumenPorPacienteId(Long pacienteId) {
        // Obtener datos del paciente
        PacienteDto paciente = pacienteClient.getPacienteById(pacienteId).getBody();
        if (paciente == null) {
            throw new RuntimeException("Paciente no encontrado con ID: " + pacienteId);
        }

        // Obtener citas del paciente
        List<CitaDto> citas = citaClient.getCitasByPacienteId(pacienteId).getBody();
        if (citas == null)
            citas = List.of();

        // Obtener consultas del paciente
        List<ConsultaDto> consultas = consultaClient.getConsultasByPacienteId(pacienteId).getBody();
        if (consultas == null)
            consultas = List.of();

        // Obtener pagos del paciente
        List<PagoDto> pagos = pagoClient.getPagosByPacienteId(pacienteId).getBody();
        if (pagos == null)
            pagos = List.of();

        // Calcular estadísticas
        int totalCitas = citas.size();
        int totalConsultas = consultas.size();
        BigDecimal totalPagos = pagos.stream()
                .map(PagoDto::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Obtener diagnósticos frecuentes
        List<String> diagnosticosFrecuentes = consultaClient.getDiagnosticosFrecuentes().getBody();
        if (diagnosticosFrecuentes == null)
            diagnosticosFrecuentes = List.of();

        // Obtener medicamentos frecuentes (extraídos de las prescripciones)
        List<String> medicamentosFrecuentes = consultas.stream()
                .map(ConsultaDto::getPrescripcion)
                .distinct()
                .limit(5)
                .collect(Collectors.toList());

        // Obtener última consulta y próxima cita
        String ultimaConsulta = consultas.stream()
                .max(Comparator.comparing(ConsultaDto::getFecha))
                .map(c -> c.getFecha().toString())
                .orElse("No hay consultas registradas");

        String proximaCita = citas.stream()
                .filter(c -> c.getFechaHora().isAfter(LocalDateTime.now()))
                .min(Comparator.comparing(CitaDto::getFechaHora))
                .map(c -> c.getFechaHora().toString())
                .orElse("No hay citas programadas");

        // Calcular promedio de calificaciones (si existe en el sistema)
        double promedioCalificaciones = 0.0; // Implementar cuando se agregue el sistema de calificaciones

        // Construir y retornar el resumen
        return ResumenHistorialDto.builder()
                .paciente(paciente)
                .citas(citas)
                .consultas(consultas)
                .pagos(pagos)
                .totalCitas(totalCitas)
                .totalConsultas(totalConsultas)
                .totalPagos(totalPagos)
                .promedioCalificaciones(promedioCalificaciones)
                .diagnosticosFrecuentes(diagnosticosFrecuentes)
                .medicamentosFrecuentes(medicamentosFrecuentes)
                .ultimaConsulta(ultimaConsulta)
                .proximaCita(proximaCita)
                .build();
    }
}
