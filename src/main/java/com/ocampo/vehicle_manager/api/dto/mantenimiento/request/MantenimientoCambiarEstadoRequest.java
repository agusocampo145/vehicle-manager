package com.ocampo.vehicle_manager.api.dto.mantenimiento.request;

import com.ocampo.vehicle_manager.domain.enums.EstadoMantenimiento;
import jakarta.validation.constraints.NotNull;

public record MantenimientoCambiarEstadoRequest(
        @NotNull EstadoMantenimiento estado
) {}

