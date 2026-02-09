package com.ocampo.vehicle_manager.api.dto.mantenimiento.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record MantenimientoCompletarRequest(
        @NotNull @DecimalMin(value = "0.0", inclusive = true) BigDecimal costoFinal
) {}

