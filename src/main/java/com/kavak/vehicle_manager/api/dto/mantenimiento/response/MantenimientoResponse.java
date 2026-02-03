package com.kavak.vehicle_manager.api.dto.mantenimiento.response;

import com.kavak.vehicle_manager.domain.enums.EstadoMantenimiento;
import com.kavak.vehicle_manager.domain.enums.TipoMantenimiento;

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

