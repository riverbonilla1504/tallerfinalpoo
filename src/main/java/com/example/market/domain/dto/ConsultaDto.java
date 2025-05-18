package com.example.market.domain.dto;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class ConsultaDto {
    private Long id;
    private LocalDateTime fecha;
    private String diagnostico;
    private String prescripcion;
    private String observaciones;
    private Long medicoId;
    private String medicoNombre;
    private Long citaId;
    private Long pacienteId;
    private String pacienteNombre;
}