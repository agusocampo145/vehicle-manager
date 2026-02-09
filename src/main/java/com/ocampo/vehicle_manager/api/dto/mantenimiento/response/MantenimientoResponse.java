package com.ocampo.vehicle_manager.api.dto.mantenimiento.response;

import com.ocampo.vehicle_manager.domain.enums.EstadoMantenimiento;
import com.ocampo.vehicle_manager.domain.enums.TipoMantenimiento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MantenimientoResponse(
        Long id,
        Long vehiculoId,
        TipoMantenimiento tipoMantenimiento,
        String descripcion,
        LocalDateTime fechaCreacion,
        EstadoMantenimiento estado,
        BigDecimal costoEstimado,
        BigDecimal costoFinal
) {}

