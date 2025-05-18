package com.example.market.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.market.domain.dto.PacienteDto;

@FeignClient(name = "pacientes-service", url = "${services.pacientes.url}")
public interface PacienteClient {

    @GetMapping("/pacientes/{id}")
    ResponseEntity<PacienteDto> getPacienteById(@PathVariable Long id);
}