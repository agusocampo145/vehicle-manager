package com.kavak.vehicle_manager.api.dto.mantenimiento.request;

import com.kavak.vehicle_manager.domain.enums.TipoMantenimiento;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record MantenimientoCrearRequest(
        @NotNull TipoMantenimiento tipoMantenimiento,
        String descripcion,
        @NotNull @DecimalMin(value = "0.0", inclusive = true) BigDecimal costoEstimado
) {}
