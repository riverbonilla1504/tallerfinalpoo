package com.example.market.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.market.domain.dto.CitaDto;

import java.util.List;

@FeignClient(name = "citas-service", url = "${services.citas.url}")
public interface CitaClient {

    @GetMapping("/citas/{id}")
    ResponseEntity<CitaDto> getCitaById(@PathVariable Long id);

    @GetMapping("/citas/paciente/{pacienteId}")
    ResponseEntity<List<CitaDto>> getCitasByPacienteId(@PathVariable Long pacienteId);
}