create table vehiculos (
    id bigserial primary key,
    patente varchar(20) not null,
    marca varchar(100) not null,
    modelo varchar(100) not null,
    anio integer not null,
    kilometraje_actual integer not null,

    constraint uk_vehiculos_patente unique (patente)
);

create table mantenimientos (
    id bigserial primary key,
    vehiculo_id bigint not null,
    tipo_mantenimiento varchar(30) not null,
    descripcion text,
    fecha_creacion timestamp not null,
    estado varchar(20) not null,
    costo_estimado numeric(12,2) not null,
    costo_final numeric(12,2),

    constraint fk_mantenimientos_vehiculo
        foreign key (vehiculo_id)
        references vehiculos (id)
);

create index idx_mantenimientos_vehiculo
    on mantenimientos (vehiculo_id);

create index idx_mantenimientos_vehiculo_estado
    on mantenimientos (vehiculo_id, estado);
