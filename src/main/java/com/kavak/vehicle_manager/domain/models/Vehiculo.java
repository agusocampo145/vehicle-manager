package com.kavak.vehicle_manager.domain.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "vehiculos", uniqueConstraints = {@UniqueConstraint(name = "uk_vehiculos_patente", columnNames = "patente")})
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String patente;

    @Column(nullable = false)
    private String marca;

    @Column(nullable = false)
    private String modelo;

    @Column(nullable = false)
    private Integer anio;

    @Column(name = "kilometraje_actual", nullable = false)
    private Integer kilometrajeActual;

    public Vehiculo(String patente, String marca, String modelo, Integer anio, Integer kilometrajeActual) {
        this.patente = patente;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.kilometrajeActual = kilometrajeActual;
    }

    public void actualizarKilometraje(Integer nuevoKilometraje) {
        if (nuevoKilometraje < this.kilometrajeActual) {
            throw new IllegalArgumentException("El kilometraje no puede disminuir");
        }
        this.kilometrajeActual = nuevoKilometraje;
    }
}

