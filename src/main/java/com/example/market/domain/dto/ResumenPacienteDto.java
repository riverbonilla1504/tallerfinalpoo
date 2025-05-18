package com.example.market.domain.dto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResumenPacienteDto {
    private PacienteDto paciente;
    private int totalCitas;
    private int totalConsultas;
    private int totalPagos;
    private BigDecimal montoTotalPagos;
}