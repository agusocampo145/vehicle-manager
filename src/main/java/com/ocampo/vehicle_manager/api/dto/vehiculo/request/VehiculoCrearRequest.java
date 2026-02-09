package com.ocampo.vehicle_manager.api.dto.vehiculo.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record VehiculoCrearRequest(
        @NotBlank @Size(max = 20) String patente,
        @NotBlank String marca,
        @NotBlank String modelo,
        @NotNull @Min(1900) Integer anio,
        @NotNull @Min(0) Integer kilometrajeActual
) {}
