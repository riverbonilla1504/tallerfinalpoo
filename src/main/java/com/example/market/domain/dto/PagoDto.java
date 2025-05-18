package com.example.market.domain.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class PagoDto {
    private Long id;
    private Long citaId;
    private Long pacienteId;
    private LocalDate fechaPago;
    private String metodoPago;
    private BigDecimal monto;
    private String estadoPago;
}