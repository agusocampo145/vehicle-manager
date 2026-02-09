package com.ocampo.vehicle_manager.api.dto.vehiculo.mapper;

import com.ocampo.vehicle_manager.api.dto.vehiculo.request.VehiculoCrearRequest;
import com.ocampo.vehicle_manager.api.dto.vehiculo.response.VehiculoResponse;
import com.ocampo.vehicle_manager.domain.models.Vehiculo;

public class VehiculoMapper {

    private VehiculoMapper() {
    }

    public static Vehiculo toEntity(VehiculoCrearRequest request) {
        return new Vehiculo(
                request.patente(),
                request.marca(),
                request.modelo(),
                request.anio(),
                request.kilometrajeActual()
        );
    }

    public static VehiculoResponse toResponse(Vehiculo vehiculo) {
        return new VehiculoResponse(
                vehiculo.getId(),
                vehiculo.getPatente(),
                vehiculo.getMarca(),
                vehiculo.getModelo(),
                vehiculo.getAnio(),
                vehiculo.getKilometrajeActual()
        );
    }
}

