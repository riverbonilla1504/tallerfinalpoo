package com.example.market.domain.dto;

import java.util.List;
import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResumenHistorialDto {
    private PacienteDto paciente;
    private List<CitaDto> citas;
    private List<ConsultaDto> consultas;
    private List<PagoDto> pagos;

    // Estadísticas
    private int totalCitas;
    private int totalConsultas;
    private BigDecimal totalPagos;
    private double promedioCalificaciones;

    // Resumen médico
    private List<String> diagnosticosFrecuentes;
    private List<String> medicamentosFrecuentes;
    private String ultimaConsulta;
    private String proximaCita;
}