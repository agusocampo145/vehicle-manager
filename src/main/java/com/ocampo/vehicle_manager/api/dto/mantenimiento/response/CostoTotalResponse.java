package com.ocampo.vehicle_manager.api.dto.mantenimiento.response;

import java.math.BigDecimal;

public record CostoTotalResponse(
        Long vehiculoId,
        BigDecimal costoTotal
) {}

