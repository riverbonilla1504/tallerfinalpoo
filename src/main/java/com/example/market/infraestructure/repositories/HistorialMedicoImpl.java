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
        // Inicia un stream para procesar la lista de pagos
        BigDecimal totalPagos = pagos.stream()
                // Transforma cada PagoDto en su monto (BigDecimal)
                .map(PagoDto::getMonto)
                // Suma todos los montos, comenzando desde cero
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Obtener diagnósticos frecuentes
        List<String> diagnosticosFrecuentes = consultaClient.getDiagnosticosFrecuentes().getBody();
        if (diagnosticosFrecuentes == null)
            diagnosticosFrecuentes = List.of();

        // Inicia un stream para procesar la lista de consultas
        List<String> medicamentosFrecuentes = consultas.stream()
                // Extrae la prescripción de cada consulta
                .map(ConsultaDto::getPrescripcion)
                // Elimina prescripciones duplicadas
                .distinct()
                // Toma solo los primeros 5 elementos
                .limit(5)
                // Convierte el stream resultante en una lista
                .collect(Collectors.toList());

        // Inicia un stream para procesar la lista de consultas
        String ultimaConsulta = consultas.stream()
                // Encuentra la consulta con la fecha más reciente
                .max(Comparator.comparing(ConsultaDto::getFecha))
                // Convierte la fecha a String
                .map(c -> c.getFecha().toString())
                // Si no hay consultas, retorna mensaje por defecto
                .orElse("No hay consultas registradas");

        // Inicia un stream para procesar la lista de citas
        String proximaCita = citas.stream()
                // Filtra solo las citas con fecha posterior a la actual
                .filter(c -> c.getFechaHora().isAfter(LocalDateTime.now()))
                // Encuentra la cita con la fecha más próxima
                .min(Comparator.comparing(CitaDto::getFechaHora))
                // Convierte la fecha a String
                .map(c -> c.getFechaHora().toString())
                // Si no hay citas futuras, retorna mensaje por defecto
                .orElse("No hay citas programadas");

        // Construir y retornar el resumen
        return ResumenHistorialDto.builder()
                .paciente(paciente)
                .citas(citas)
                .consultas(consultas)
                .pagos(pagos)
                .totalCitas(totalCitas)
                .totalConsultas(totalConsultas)
                .totalPagos(totalPagos)
                .diagnosticosFrecuentes(diagnosticosFrecuentes)
                .medicamentosFrecuentes(medicamentosFrecuentes)
                .ultimaConsulta(ultimaConsulta)
                .proximaCita(proximaCita)
                .build();
    }
}
