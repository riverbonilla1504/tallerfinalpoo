package com.example.market.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.market.domain.dto.ResumenHistorialDto;
import com.example.market.domain.dto.ResumenPacienteDto;
import com.example.market.infraestructure.repositories.HistorialMedicoImpl;

@Service
public class HistorialMedicoService {

        @Autowired
        private HistorialMedicoImpl historialMedico;

        public ResumenPacienteDto obtenerResumenBasicoPaciente(Long pacienteId) {
                return historialMedico.obtenerResumenBasicoPaciente(pacienteId);
        }

        public ResumenHistorialDto obtenerResumenPorPacienteId(Long pacienteId) {
                return historialMedico.obtenerResumenPorPacienteId(pacienteId);
        }
}