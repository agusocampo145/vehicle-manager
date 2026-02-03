package com.kavak.vehicle_manager.api.dto.vehiculo.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record VehiculoActualizarKilometrajeRequest(
        @NotNull @Min(0) Integer kilometrajeActual
) {}
