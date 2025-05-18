package com.example.market.domain.dto;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class CitaDto {
    private Long id;
    private Long pacienteId;
    private Long medicoId;
    private LocalDateTime fechaHora;
    private String motivo;
    private String estado;
    private String observaciones;
    private String estadoPago;
    private Long pagoId;

    // Datos adicionales
    private String nombrePaciente;
    private String nombreMedico;
    private String especialidadMedico;
}