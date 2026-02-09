package com.ocampo.vehicle_manager.api.dto.mantenimiento.mapper;

import com.ocampo.vehicle_manager.api.dto.mantenimiento.response.MantenimientoResponse;
import com.ocampo.vehicle_manager.domain.models.Mantenimiento;

public class MantenimientoMapper {

    private MantenimientoMapper() {
    }

    public static MantenimientoResponse toResponse(Mantenimiento mantenimiento) {
        return new MantenimientoResponse(
                mantenimiento.getId(),
                mantenimiento.getVehiculo().getId(),
                mantenimiento.getTipoMantenimiento(),
                mantenimiento.getDescripcion(),
                mantenimiento.getFechaCreacion(),
                mantenimiento.getEstado(),
                mantenimiento.getCostoEstimado(),
                mantenimiento.getCostoFinal()
        );
    }
}

