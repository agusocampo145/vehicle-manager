package com.ocampo.vehicle_manager.api.dto.vehiculo.response;

public record VehiculoResponse(
        Long id,
        String patente,
        String marca,
        String modelo,
        Integer anio,
        Integer kilometrajeActual
) {}

