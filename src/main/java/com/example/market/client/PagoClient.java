package com.example.market.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.market.domain.dto.PagoDto;

import java.util.List;

@FeignClient(name = "pagos-service", url = "${services.pagos.url}")
public interface PagoClient {

    @GetMapping("/pagos/{id}")
    ResponseEntity<PagoDto> getPagoById(@PathVariable Long id);

    @GetMapping("/pagos/cita/{citaId}")
    ResponseEntity<List<PagoDto>> getPagosByCitaId(@PathVariable Long citaId);

    @GetMapping("/pagos/paciente/{pacienteId}")
    ResponseEntity<List<PagoDto>> getPagosByPacienteId(@PathVariable Long pacienteId);
}