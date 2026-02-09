package com.ocampo.vehicle_manager.controllers;

import com.ocampo.vehicle_manager.api.dto.vehiculo.mapper.VehiculoMapper;
import com.ocampo.vehicle_manager.api.dto.vehiculo.request.VehiculoActualizarKilometrajeRequest;
import com.ocampo.vehicle_manager.api.dto.vehiculo.request.VehiculoCrearRequest;
import com.ocampo.vehicle_manager.api.dto.vehiculo.response.VehiculoDisponibilidadResponse;
import com.ocampo.vehicle_manager.api.dto.vehiculo.response.VehiculoResponse;
import com.ocampo.vehicle_manager.domain.models.Vehiculo;
import com.ocampo.vehicle_manager.service.VehiculoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Vehículos", description = "Operaciones de registro, consulta y actualización de vehículos")
@RestController
@RequestMapping("/api/v1/vehiculos")
public class VehiculoController {

    private final VehiculoService vehiculoService;

    public VehiculoController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    @Operation(
            summary = "Registrar vehículo",
            description = "Crea un vehículo. La patente debe ser única."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Vehículo creado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o patente duplicada")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VehiculoResponse crear(@Valid @RequestBody VehiculoCrearRequest request) {
        Vehiculo vehiculoNuevo = VehiculoMapper.toEntity(request);
        Vehiculo guardado = vehiculoService.registrarVehiculo(vehiculoNuevo);
        return VehiculoMapper.toResponse(guardado);
    }

    @Operation(
            summary = "Obtener vehículo por id",
            description = "Devuelve los datos del vehículo si existe."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vehículo encontrado"),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
    })
    @GetMapping("/{id}")
    public VehiculoResponse obtenerPorId(
            @Parameter(description = "Id del vehículo", example = "1")
            @PathVariable Long id
    ) {
        return VehiculoMapper.toResponse(vehiculoService.obtenerPorId(id));
    }

    @Operation(
            summary = "Obtener vehículo por patente",
            description = "Devuelve los datos del vehículo asociado a la patente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vehículo encontrado"),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
    })
    @GetMapping("/patente/{patente}")
    public VehiculoResponse obtenerPorPatente(
            @Parameter(description = "Patente del vehículo", example = "AA123BB")
            @PathVariable String patente
    ) {
        return VehiculoMapper.toResponse(vehiculoService.obtenerPorPatente(patente));
    }

    @Operation(
            summary = "Actualizar kilometraje",
            description = "Actualiza el kilometraje del vehículo. No permite disminuir el kilometraje."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Kilometraje actualizado"),
            @ApiResponse(responseCode = "400", description = "Kilometraje inválido"),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
    })
    @PatchMapping("/{id}/kilometraje")
    public VehiculoResponse actualizarKilometraje(
            @Parameter(description = "Id del vehículo", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody VehiculoActualizarKilometrajeRequest request
    ) {
        Vehiculo actualizado = vehiculoService.actualizarKilometraje(id, request.kilometrajeActual());
        return VehiculoMapper.toResponse(actualizado);
    }

    @Operation(
            summary = "Consultar disponibilidad",
            description = "Un vehículo no está disponible si tiene al menos un mantenimiento en PENDIENTE o EN_PROCESO."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Disponibilidad calculada"),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
    })
    @GetMapping("/{id}/disponibilidad")
    public VehiculoDisponibilidadResponse disponibilidad(
            @Parameter(description = "Id del vehículo", example = "1")
            @PathVariable Long id
    ) {
        boolean disponible = vehiculoService.estaDisponible(id);
        return new VehiculoDisponibilidadResponse(id, disponible);
    }
}

