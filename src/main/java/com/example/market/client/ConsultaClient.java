package com.example.market.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.market.domain.dto.ConsultaDto;

import java.util.List;

@FeignClient(name = "consultas-service", url = "${services.consultas.url}")
public interface ConsultaClient {

    @GetMapping("/consulta/{id}")
    ResponseEntity<ConsultaDto> getConsultaById(@PathVariable Long id);

    @GetMapping("/consulta/paciente/{id}")
    ResponseEntity<List<ConsultaDto>> getConsultasByPacienteId(@PathVariable Long id);

    @GetMapping("/consulta/diagnostico-frecuente")
    ResponseEntity<List<String>> getDiagnosticosFrecuentes();
}