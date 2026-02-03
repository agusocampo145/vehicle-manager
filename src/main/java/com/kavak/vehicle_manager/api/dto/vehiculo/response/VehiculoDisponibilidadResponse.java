package com.kavak.vehicle_manager.api.dto.vehiculo.response;

public record VehiculoDisponibilidadResponse(
        Long vehiculoId,
        boolean disponible
) {}

