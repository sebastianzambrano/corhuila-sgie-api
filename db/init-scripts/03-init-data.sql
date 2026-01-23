--Crear Rol ADMINISTRADOR
INSERT INTO rol (
    id, nombre, descripcion, state, fecha_creacion, fecha_modificacion, usuario_creacion, usuario_modificacion, usuario_eliminacion, fecha_eliminacion
) VALUES (
    1, 'ADMINISTRADOR', 'Rol con acceso total al sistema', TRUE, NOW(), NOW(), 1, NULL, NULL, NULL
);

--Crear Rol COORDINADOR DE RESERVAS
INSERT INTO rol (
    id, nombre, descripcion, state, fecha_creacion, fecha_modificacion, usuario_creacion, usuario_modificacion, usuario_eliminacion, fecha_eliminacion
) VALUES (
    2, 'COORDINADOR_RESERVAS', 'Rol con acceso requerido para gestionar reservas de instalaciones, equipos y reportes', TRUE, NOW(), NOW(), 1, NULL, NULL, NULL
);

--Crear Rol DOCENTE
INSERT INTO rol (
    id, nombre, descripcion, state, fecha_creacion, fecha_modificacion, usuario_creacion, usuario_modificacion, usuario_eliminacion, fecha_eliminacion
) VALUES (
    3, 'DOCENTE', 'Rol con acceso requerido para gestionar reservas de instalaciones y equipos', TRUE, NOW(), NOW(), 1, NULL, NULL, NULL
);

--Crear Rol ESTUDIANTE
INSERT INTO rol (
    id, nombre, descripcion, state, fecha_creacion, fecha_modificacion, usuario_creacion, usuario_modificacion, usuario_eliminacion, fecha_eliminacion
) VALUES (
    4, 'ESTUDIANTE', 'Rol con acceso requerido para gestionar reservas de instalaciones y equipos', TRUE, NOW(), NOW(), 1, NULL, NULL, NULL
);

--Crear Permisos (CRUD)
INSERT INTO permiso (
    id, nombre, descripcion, state, fecha_creacion, fecha_modificacion, usuario_creacion, usuario_modificacion, usuario_eliminacion, fecha_eliminacion
) VALUES
(1, 'CREAR', 'Permite crear registros', TRUE, NOW(), NOW(), 1, NULL, NULL, NULL),
(2, 'CONSULTAR', 'Permite consultar registros', TRUE, NOW(), NOW(), 1, NULL, NULL, NULL),
(3, 'ACTUALIZAR', 'Permite actualizar registros', TRUE, NOW(), NOW(), 1, NULL, NULL, NULL),
(4, 'ELIMINAR', 'Permite eliminar registros', TRUE, NOW(), NOW(), 1, NULL, NULL, NULL);

--Crear Entidades
INSERT INTO entidad (
    id, nombre, state, fecha_creacion, fecha_modificacion, usuario_creacion, usuario_modificacion, usuario_eliminacion, fecha_eliminacion
) VALUES
-- MODULO USER
(1, 'ENTIDAD', TRUE, NOW(), NOW(), 1, NULL, NULL, NULL),
(2, 'PERMISO', TRUE, NOW(), NOW(), 1, NULL, NULL, NULL),
(3, 'ROL', TRUE, NOW(), NOW(), 1, NULL, NULL, NULL),
(4, 'PERSONA', TRUE, NOW(), NOW(), 1, NULL, NULL, NULL),
(5, 'USUARIO', TRUE, NOW(), NOW(), 1, NULL, NULL, NULL),
(6, 'PERMISO_ROL_ENTIDAD', TRUE, NOW(), NOW(), 1, NULL, NULL, NULL),

-- MODULO SITE
(7, 'CONTINENTE', TRUE, NOW(), NOW(), 1, NULL, NULL, NULL),
(8, 'PAIS', TRUE, NOW(), NOW(), 1, NULL, NULL, NULL),
(9, 'DEPARTAMENTO', TRUE, NOW(), NOW(), 1, NULL, NULL, NULL),
(10, 'MUNICIPIO', TRUE, NOW(), NOW(), 1, NULL, NULL, NULL),
(11, 'CAMPUS', TRUE, NOW(), NOW(), 1, NULL, NULL, NULL),
(12, 'CATEGORIA_INSTALACION', TRUE, NOW(), NOW(), 1, NULL, NULL, NULL),
(13, 'INSTALACION', TRUE, NOW(), NOW(), 1, NULL, NULL, NULL),

-- MODULO MAINTENANCE
(14, 'CATEGORIA_MANTENIMIENTO_EQUIPO', TRUE, NOW(), NOW(), 1, NULL, NULL, NULL),
(15, 'CATEGORIA_MANTENIMIENTO_INSTALACION', TRUE, NOW(), NOW(), 1, NULL, NULL, NULL),
(16, 'MANTENIMIENTO_EQUIPO', TRUE, NOW(), NOW(), 1, NULL, NULL, NULL),
(17, 'MANTENIMIENTO_INSTALACION', TRUE, NOW(), NOW(), 1, NULL, NULL, NULL),

-- MODULO EQUIPMENT
(18, 'CATEGORIA_EQUIPO', TRUE, NOW(), NOW(), 1, NULL, NULL, NULL),
(19, 'EQUIPO', TRUE, NOW(), NOW(), 1, NULL, NULL, NULL),
(20, 'TIPO_EQUIPO',TRUE, NOW(), NOW(), 1, NULL, NULL, NULL),

-- MODULO BOOKING
(21, 'TIPO_RESERVA', TRUE, NOW(), NOW(), 1, NULL, NULL, NULL),
(22, 'RESERVA', TRUE, NOW(), NOW(), 1, NULL, NULL, NULL),
(23, 'DETALLE_RESERVA_EQUIPO', TRUE, NOW(), NOW(), 1, NULL, NULL, NULL),
(24, 'DETALLE_RESERVA_INSTALACION', TRUE, NOW(), NOW(), 1, NULL, NULL, NULL),

(25, 'TIPO_EQUIPO', TRUE, NOW(), NOW(), 1, NULL, NULL, NULL);


--Crear Persona (Administrador principal)
INSERT INTO persona (
    id, nombres, apellidos, tipo_documento, numero_identificacion, telefono_movil, id_rol,
    state, fecha_creacion, fecha_modificacion, usuario_creacion, usuario_modificacion, usuario_eliminacion, fecha_eliminacion
) VALUES (
    1, 'ADMIN', 'PRINCIPAL', 'CÉDULA DE CIUDADANÍA','1234567890', '3001234567', 1,
    TRUE, NOW(), NOW(), 1, NULL, NULL, NULL
);

--Crear Usuario
--contraseña Admin123
INSERT INTO usuario (
    id, email, password, id_persona,
    state, fecha_creacion, fecha_modificacion, usuario_creacion, usuario_modificacion, usuario_eliminacion, fecha_eliminacion
) VALUES (
    1, 'admin@system.com', '$2b$12$CKG0.blMMABlrdrbo4wo4OfYEJELvQqtzxMzKS8KFT3PbYEFBg7hS', 1,
    TRUE, NOW(), NOW(), 1, NULL, NULL, NULL
);

---crear categoria mantenimiento equipo
INSERT INTO categoria_mantenimiento_equipo (
    nombre,
    descripcion,
    state,
    fecha_creacion,
    fecha_modificacion,
    usuario_creacion,
    usuario_modificacion
) VALUES
    ('PREVENTIVO', 'Mantenimiento programado para evitar fallas o averías.', TRUE, NOW(), NOW(), 1, 1),
    ('CORRECTIVO', 'Mantenimiento realizado para reparar fallas detectadas.', TRUE, NOW(), NOW(), 1, 1),
    ('PREDICTIVO', 'Mantenimiento basado en el análisis de condiciones para predecir fallas.', TRUE, NOW(), NOW(), 1, 1);

---crear categoria mantenimiento instalacion
INSERT INTO categoria_mantenimiento_instalacion (
    nombre,
    descripcion,
    state,
    fecha_creacion,
    fecha_modificacion,
    usuario_creacion,
    usuario_modificacion
) VALUES
    ('Preventivo', 'Mantenimiento programado para evitar fallas o averías.', TRUE, NOW(), NOW(), 1, 1),
    ('Correctivo', 'Mantenimiento realizado para reparar fallas detectadas.', TRUE, NOW(), NOW(), 1, 1),
    ('Predictivo', 'Mantenimiento basado en el análisis de condiciones para predecir fallas.', TRUE, NOW(), NOW(), 1, 1);


  
--Crear Permisos-Rol-Entidad (todas las combinaciones)
DO $$
DECLARE
    e RECORD;
    p RECORD;
BEGIN
    FOR e IN SELECT id FROM entidad LOOP
        FOR p IN SELECT id FROM permiso LOOP
            INSERT INTO permiso_rol_entidad (
                id_rol, id_entidad, id_permiso,
                state, fecha_creacion, fecha_modificacion, usuario_creacion, usuario_modificacion, usuario_eliminacion, fecha_eliminacion
            )
            VALUES (
                1, e.id, p.id,
                TRUE, NOW(), NOW(), 1, NULL, NULL, NULL
            );
        END LOOP;
    END LOOP;
END$$;

--continente
INSERT INTO continente (id, nombre, descripcion, state, fecha_creacion, fecha_modificacion, usuario_creacion)
VALUES
(1, 'África', 'Continente africano', TRUE, NOW(), NOW(), 1),
(2, 'América del Norte', 'Continente norteamericano', TRUE, NOW(), NOW(), 1),
(3, 'América Central', 'Continente centroamericano', TRUE, NOW(), NOW(), 1),
(4, 'América del Sur', 'Continente sudamericano', TRUE, NOW(), NOW(), 1),
(5, 'Antártida', 'Continente antártico', TRUE, NOW(), NOW(), 1),
(6, 'Asia', 'Continente asiático', TRUE, NOW(), NOW(), 1),
(7, 'Europa', 'Continente europeo', TRUE, NOW(), NOW(), 1),
(8, 'Oceanía', 'Continente oceánico', TRUE, NOW(), NOW(), 1);

--pais
INSERT INTO pais (id, nombre, descripcion, id_continente, state, fecha_creacion, fecha_modificacion, usuario_creacion)
VALUES
(1, 'Colombia', 'País en América del Sur', 4, TRUE, NOW(), NOW(), 1);

--departamentos
INSERT INTO departamento (id, nombre, descripcion, id_pais, state, fecha_creacion, fecha_modificacion, usuario_creacion)
VALUES
(1, 'Amazonas', 'Departamento de Amazonas', 1, TRUE, NOW(), NOW(), 1),
(2, 'Antioquia', 'Departamento de Antioquia', 1, TRUE, NOW(), NOW(), 1),
(3, 'Atlántico', 'Departamento de Atlántico', 1, TRUE, NOW(), NOW(), 1),
(4, 'Bolívar', 'Departamento de Bolívar', 1, TRUE, NOW(), NOW(), 1),
(5, 'Boyacá', 'Departamento de Boyacá', 1, TRUE, NOW(), NOW(), 1),
(6, 'Caldas', 'Departamento de Caldas', 1, TRUE, NOW(), NOW(), 1),
(7, 'Cauca', 'Departamento de Cauca', 1, TRUE, NOW(), NOW(), 1),
(8, 'Cundinamarca', 'Departamento de Cundinamarca', 1, TRUE, NOW(), NOW(), 1),
(9, 'Huila', 'Departamento de Huila', 1, TRUE, NOW(), NOW(), 1),
(10, 'Valle del Cauca', 'Departamento de Valle del Cauca', 1, TRUE, NOW(), NOW(), 1);

--municipio
INSERT INTO municipio (id, nombre, descripcion, id_departamento, state, fecha_creacion, fecha_modificacion, usuario_creacion)
VALUES
-- Amazonas
(1, 'Leticia', 'Capital del Amazonas', 1, TRUE, NOW(), NOW(), 1),

-- Antioquia
(2, 'Medellín', 'Capital de Antioquia', 2, TRUE, NOW(), NOW(), 1),

-- Atlántico
(3, 'Barranquilla', 'Capital del Atlántico', 3, TRUE, NOW(), NOW(), 1),

-- Bolívar
(4, 'Cartagena de Indias', 'Capital de Bolívar', 4, TRUE, NOW(), NOW(), 1),

-- Boyacá
(5, 'Tunja', 'Capital de Boyacá', 5, TRUE, NOW(), NOW(), 1),

-- Caldas
(6, 'Manizales', 'Capital de Caldas', 6, TRUE, NOW(), NOW(), 1),

-- Cauca
(7, 'Popayán', 'Capital de Cauca', 7, TRUE, NOW(), NOW(), 1),

-- Cundinamarca
(8, 'Bogotá', 'Capital de Cundinamarca y del país', 8, TRUE, NOW(), NOW(), 1),

-- Huila (capital + municipios adicionales)
(9, 'Neiva', 'Capital del Huila', 9, TRUE, NOW(), NOW(), 1),
(10, 'Pitalito', 'Segundo municipio más poblado del Huila', 9, TRUE, NOW(), NOW(), 1),
(11, 'Garzón', 'Municipio del centro del Huila', 9, TRUE, NOW(), NOW(), 1),
(12, 'La Plata', 'Municipio del occidente del Huila', 9, TRUE, NOW(), NOW(), 1),
(13, 'Campoalegre', 'Municipio agrícola del Huila', 9, TRUE, NOW(), NOW(), 1),
(14, 'San Agustín', 'Municipio arqueológico del Huila', 9, TRUE, NOW(), NOW(), 1),

-- Valle del Cauca
(15, 'Cali', 'Capital de Valle del Cauca', 10, TRUE, NOW(), NOW(), 1);

-- Insertar categoria instalacion
INSERT INTO categoria_instalacion (
    id, nombre, descripcion,
    state, fecha_creacion, fecha_modificacion, usuario_creacion, usuario_modificacion, usuario_eliminacion, fecha_eliminacion
) VALUES (
    1, 'Laboratorio', 'Instalaciones destinadas a prácticas de laboratorio',
    TRUE, NOW(), NOW(), 1, NULL, NULL, NULL
);

INSERT INTO categoria_instalacion (
    id, nombre, descripcion,
    state, fecha_creacion, fecha_modificacion, usuario_creacion, usuario_modificacion, usuario_eliminacion, fecha_eliminacion
) VALUES (
    2, 'aulas', 'Instalaciones destinadas para dar clases',
    TRUE, NOW(), NOW(), 1, NULL, NULL, NULL
);

INSERT INTO categoria_instalacion (
    id, nombre, descripcion,
    state, fecha_creacion, fecha_modificacion, usuario_creacion, usuario_modificacion, usuario_eliminacion, fecha_eliminacion
) VALUES (
    3, 'auditorio', 'Instalaciones destinadas a eventos masivos',
    TRUE, NOW(), NOW(), 1, NULL, NULL, NULL
);

INSERT INTO categoria_instalacion (
    id, nombre, descripcion,
    state, fecha_creacion, fecha_modificacion, usuario_creacion, usuario_modificacion, usuario_eliminacion, fecha_eliminacion
) VALUES (
    4, 'bodega', 'Instalaciones destinadas para almacenamiento de equipos y materiales academicos',
    TRUE, NOW(), NOW(), 1, NULL, NULL, NULL
);

-- Insertar CAMPUS
INSERT INTO campus ( id, nombre, descripcion, id_municipio, state, fecha_creacion, fecha_modificacion, usuario_creacion, usuario_modificacion, usuario_eliminacion, fecha_eliminacion
) VALUES (1, 'Campus quirinal', 'Sede de administracion de empresas, negocios internacionales y MVZ', 1, TRUE, NOW(), NOW(), 1, NULL, NULL, NULL);
INSERT INTO campus ( id, nombre, descripcion, id_municipio,state, fecha_creacion, fecha_modificacion,usuario_creacion, usuario_modificacion, usuario_eliminacion, fecha_eliminacion
) VALUES (2, 'Campus prado alto', 'Sede de ingenieria', 1, TRUE, NOW(), NOW(),1, NULL, NULL, NULL);

-- Insertar INSTALACIONES
 INSERT INTO instalacion (
    id, nombre, descripcion, id_campus, id_categoria_instalacion,
    state, fecha_creacion, fecha_modificacion,
    usuario_creacion, usuario_modificacion, usuario_eliminacion, fecha_eliminacion
) VALUES (
    1, 'Laboratorio de informatica ', 'Instalación equipada para prácticas tecnologicas', 2, 1,
    TRUE, NOW(), NOW(),
    1, NULL, NULL, NULL
);
 INSERT INTO instalacion (
    id, nombre, descripcion, id_campus, id_categoria_instalacion,
    state, fecha_creacion, fecha_modificacion,
    usuario_creacion, usuario_modificacion, usuario_eliminacion, fecha_eliminacion
) VALUES (
    2, 'salon c-201', 'salon bloque c', 2, 2,
    TRUE, NOW(), NOW(),
    1, NULL, NULL, NULL
);
 INSERT INTO instalacion (
    id, nombre, descripcion, id_campus, id_categoria_instalacion,
    state, fecha_creacion, fecha_modificacion,
    usuario_creacion, usuario_modificacion, usuario_eliminacion, fecha_eliminacion
) VALUES (
    3, 'salon c-202', 'salon bloque c', 2, 2,
    TRUE, NOW(), NOW(),
    1, NULL, NULL, NULL
);
 INSERT INTO instalacion (
    id, nombre, descripcion, id_campus, id_categoria_instalacion,
    state, fecha_creacion, fecha_modificacion,
    usuario_creacion, usuario_modificacion, usuario_eliminacion, fecha_eliminacion
) VALUES (
    4, 'salon c-203', 'salon bloque c', 2, 2,
    TRUE, NOW(), NOW(),
    1, NULL, NULL, NULL
);

 INSERT INTO instalacion (
    id, nombre, descripcion, id_campus, id_categoria_instalacion,
    state, fecha_creacion, fecha_modificacion,
    usuario_creacion, usuario_modificacion, usuario_eliminacion, fecha_eliminacion
) VALUES (
    5, 'bodega', 'instalacion destinada para almacenar equipos tecnologicos', 2, 2,
    TRUE, NOW(), NOW(),
    1, NULL, NULL, NULL
);
-- Insertar categoria equipo
INSERT INTO categoria_equipo (
    id, nombre, descripcion,
    state, fecha_creacion, fecha_modificacion,
    usuario_creacion, usuario_modificacion, usuario_eliminacion, fecha_eliminacion
) VALUES (
    1, 'Equipos de computo', 'Categoría para equipos computo',
    TRUE, NOW(), NOW(),
    1, NULL, NULL, NULL
);
INSERT INTO categoria_equipo (
    id, nombre, descripcion,
    state, fecha_creacion, fecha_modificacion,
    usuario_creacion, usuario_modificacion, usuario_eliminacion, fecha_eliminacion
) VALUES (
    2, 'Equipos de telematica', 'Categoría para equipos usados en prácticas y experimentos de telematica',
    TRUE, NOW(), NOW(),
    1, NULL, NULL, NULL
);
INSERT INTO categoria_equipo (
    id, nombre, descripcion,
    state, fecha_creacion, fecha_modificacion,
    usuario_creacion, usuario_modificacion, usuario_eliminacion, fecha_eliminacion
) VALUES (
    3, 'Equipos audivisuales', 'Categoría para equipos usados para actividades audivisuales',
    TRUE, NOW(), NOW(),
    1, NULL, NULL, NULL
);
INSERT INTO categoria_equipo (
    id, nombre, descripcion,
    state, fecha_creacion, fecha_modificacion,
    usuario_creacion, usuario_modificacion, usuario_eliminacion, fecha_eliminacion
) VALUES (
    4, 'Equipos de Laboratorio', 'Categoría para equipos usados en prácticas y experimentos',
    TRUE, NOW(), NOW(),
    1, NULL, NULL, NULL
);

-- Insertar tipo equipo

INSERT INTO tipo_equipo (
    id, nombre, descripcion, id_categoria_equipo,
    state, fecha_creacion, fecha_modificacion,
    usuario_creacion, usuario_modificacion, usuario_eliminacion, fecha_eliminacion
) VALUES (
    1, 'Microscopio', 'Equipo óptico para observación de muestras', 4,
    TRUE, NOW(), NOW(),
    1, NULL, NULL, NULL
);

INSERT INTO tipo_equipo (
    id, nombre, descripcion, id_categoria_equipo,
    state, fecha_creacion, fecha_modificacion,
    usuario_creacion, usuario_modificacion, usuario_eliminacion, fecha_eliminacion
) VALUES (
    2, 'computador de escritorio Asus', 'Equipo de computo con procesador I7, memoria ram de 32, mouse y teclado inhalambrico y disco duro de 512 gb SSD', 1,
    TRUE, NOW(), NOW(),
    1, NULL, NULL, NULL
);

INSERT INTO tipo_equipo (
    id, nombre, descripcion, id_categoria_equipo,
    state, fecha_creacion, fecha_modificacion,
    usuario_creacion, usuario_modificacion, usuario_eliminacion, fecha_eliminacion
) VALUES (
    3, 'proyectos Epson', 'Equipo para la reproducción de contenido audiovisual', 3,
    TRUE, NOW(), NOW(),
    1, NULL, NULL, NULL
);

-- Insertar equipo

INSERT INTO equipo (
    id, codigo, id_instalacion, id_tipo_equipo,
    state, fecha_creacion, fecha_modificacion,
    usuario_creacion, usuario_modificacion, usuario_eliminacion, fecha_eliminacion
) VALUES (
    1, 'EQ-0001', 1, 1,
    TRUE, NOW(), NOW(),
    1, NULL, NULL, NULL
);
INSERT INTO equipo (
    id, codigo, id_instalacion, id_tipo_equipo,
    state, fecha_creacion, fecha_modificacion,
    usuario_creacion, usuario_modificacion, usuario_eliminacion, fecha_eliminacion
) VALUES (
    1, 'EQ-0001', 1, 1,
    TRUE, NOW(), NOW(),
    1, NULL, NULL, NULL
);
INSERT INTO equipo (
    id, codigo, id_instalacion, id_tipo_equipo,
    state, fecha_creacion, fecha_modificacion,
    usuario_creacion, usuario_modificacion, usuario_eliminacion, fecha_eliminacion
) VALUES (
    1, 'EQ-0001', 1, 1,
    TRUE, NOW(), NOW(),
    1, NULL, NULL, NULL
);




-- Insertar Tipos de Reserva
INSERT INTO tipo_reserva (
    id, nombre, descripcion,
    state, fecha_creacion, fecha_modificacion, usuario_creacion, usuario_modificacion, usuario_eliminacion, fecha_eliminacion
) VALUES
    (1, 'MANTENIMIENTO INSTALACION', 'Reserva para mantenimiento preventivo o correctivo de instalaciones', TRUE, NOW(), NOW(), 1, NULL, NULL, NULL),
    (2, 'MANTENIMIENTO EQUIPO', 'Reserva para mantenimiento preventivo o correctivo de equipos', TRUE, NOW(), NOW(), 1, NULL, NULL, NULL),
    (3, 'RESERVA EQUIPO', 'Reserva para uso de equipos por parte de los usuarios', TRUE, NOW(), NOW(), 1, NULL, NULL, NULL),
    (4, 'RESERVA INSTALACION', 'Reserva para uso de instalaciones por parte de los usuarios', TRUE, NOW(), NOW(), 1, NULL, NULL, NULL);


----------------------------------------------------------------------
--NUEVAS FUNCIONES

-- Borrar la función vieja
DROP FUNCTION IF EXISTS horas_disponibles_instalacion(DATE, INT, BIGINT);

--OPCION A

CREATE FUNCTION horas_disponibles_instalacion(
    p_fecha DATE, 
    p_idInstalacion INT,
    p_idDetalle BIGINT DEFAULT NULL
)
RETURNS TABLE (hora TIME) AS
$$
BEGIN
    RETURN QUERY
    WITH horas AS (
        SELECT generate_series(
            (p_fecha::timestamp + time '05:00'),
            (p_fecha::timestamp + time '21:00'),
            interval '1 hour'
        ) AS hora
    ),
    ocupadas AS (
        -- Reservas activas
        SELECT r.hora_inicio, r.hora_fin, di.id AS id_detalle
        FROM reserva r
        INNER JOIN detalle_reserva_instalacion di ON r.id = di.id_reserva
        WHERE r.fecha_reserva = p_fecha
          AND di.id_instalacion = p_idInstalacion
		  AND COALESCE(r.state, false) = true
          AND COALESCE(di.state, false) = true
        
        UNION
        
        -- Mantenimientos
        --SELECT r.hora_inicio, r.hora_fin
		SELECT r.hora_inicio, r.hora_fin, mi.id AS id_detalle
        FROM reserva r
        INNER JOIN mantenimiento_instalacion mi ON r.id = mi.id_reserva
        WHERE r.fecha_reserva = p_fecha
          AND mi.id_instalacion = p_idInstalacion
          -- Si quieres ignorar un detalle de mantenimiento específico, puedes hacer algo similar
		  AND COALESCE(r.state, false) = true
          AND COALESCE(mi.state, false) = true
    )
    SELECT h.hora::time
    FROM horas h
    WHERE NOT EXISTS (
        SELECT 1
        FROM ocupadas o
        WHERE h.hora::time >= o.hora_inicio
          AND h.hora::time < o.hora_fin
		  AND (p_idDetalle IS NULL OR o.id_detalle IS DISTINCT FROM p_idDetalle)
    )
    ORDER BY h.hora;
END;
$$ LANGUAGE plpgsql STABLE;

--OPCION B

CREATE OR REPLACE FUNCTION horas_disponibles_instalacion(
    p_fecha DATE,
    p_idInstalacion INT,
    p_idDetalle BIGINT DEFAULT NULL,
    p_origen TEXT DEFAULT NULL  -- 'RESERVA' o 'MANTENIMIENTO'
)
RETURNS TABLE (hora TIME) AS
$$
BEGIN
    RETURN QUERY
    WITH horas AS (
        SELECT generate_series(
            (p_fecha::timestamp + time '05:00'),
            (p_fecha::timestamp + time '21:00'),
            interval '1 hour'
        ) AS hora
    ),
    ocupadas AS (
        SELECT r.hora_inicio,
               r.hora_fin,
               di.id      AS id_detalle,
               'RESERVA'  AS origen
        FROM reserva r
        INNER JOIN detalle_reserva_instalacion di ON r.id = di.id_reserva
        WHERE r.fecha_reserva = p_fecha
          AND di.id_instalacion = p_idInstalacion
          AND COALESCE(r.state, false) = true
          AND COALESCE(di.state, false) = true

        UNION ALL

        SELECT r.hora_inicio,
               r.hora_fin,
               mi.id      AS id_detalle,
               'MANTENIMIENTO' AS origen
        FROM reserva r
        INNER JOIN mantenimiento_instalacion mi ON r.id = mi.id_reserva
        WHERE r.fecha_reserva = p_fecha
          AND mi.id_instalacion = p_idInstalacion
          AND COALESCE(r.state, false) = true
          AND COALESCE(mi.state, false) = true
    )
    SELECT h.hora::time
    FROM horas h
    WHERE NOT EXISTS (
        SELECT 1
        FROM ocupadas o
        WHERE h.hora::time >= o.hora_inicio
          AND h.hora::time < o.hora_fin
          AND NOT (
              p_idDetalle IS NOT NULL
              AND o.id_detalle = p_idDetalle
			  AND (p_origen IS NULL OR o.origen = p_origen)
			  --AND p_origen IS NOT NULL
              --AND o.origen = p_origen
			  
          )
    )
    ORDER BY h.hora;
END;
$$ LANGUAGE plpgsql STABLE;

-- Borrar la función vieja
DROP FUNCTION IF EXISTS horas_disponibles_equipo(DATE, INT, BIGINT);

-- Crear la nueva función con el parámetro opcional p_idDetalle

--OPCION A

CREATE OR REPLACE FUNCTION horas_disponibles_equipo(
    p_fecha DATE, 
    p_idEquipo INT, 
    p_idDetalle BIGINT DEFAULT NULL
)
RETURNS TABLE (hora TIME) AS
$$
BEGIN
    RETURN QUERY
    WITH horas AS (
        SELECT generate_series(
            (p_fecha::timestamp + time '05:00'),
            (p_fecha::timestamp + time '21:00'),
            interval '1 hour'
        ) AS hora
    ),
    ocupadas AS (
        -- Reservas activas
        SELECT r.hora_inicio, r.hora_fin, di.id AS id_detalle
        FROM reserva r
        INNER JOIN detalle_reserva_equipo di ON r.id = di.id_reserva
        WHERE r.fecha_reserva = p_fecha
          AND di.id_equipo = p_idEquipo
		  AND COALESCE(r.state, false) = true
          AND COALESCE(di.state, false) = true
        
        UNION
        
        -- Mantenimientos (👈 ahora devuelve el id del mantenimiento)
        SELECT r.hora_inicio, r.hora_fin, mi.id AS id_detalle
        FROM reserva r
        INNER JOIN mantenimiento_equipo mi ON r.id = mi.id_reserva
        WHERE r.fecha_reserva = p_fecha
          AND mi.id_equipo = p_idEquipo
		  AND COALESCE(r.state, false) = true
          AND COALESCE(mi.state, false) = true
    )
    SELECT h.hora::time
    FROM horas h
    WHERE NOT EXISTS (
        SELECT 1
        FROM ocupadas o
        WHERE h.hora::time >= o.hora_inicio
          AND h.hora::time < o.hora_fin
          AND (p_idDetalle IS NULL OR o.id_detalle IS DISTINCT FROM p_idDetalle)
    )
    ORDER BY h.hora;
END;
$$ LANGUAGE plpgsql STABLE;

--OPCION B
CREATE OR REPLACE FUNCTION horas_disponibles_equipo(
    p_fecha DATE,
    p_idEquipo INT,
    p_idDetalle BIGINT DEFAULT NULL,
    p_origen TEXT DEFAULT NULL  -- 'RESERVA' o 'MANTENIMIENTO'
)
RETURNS TABLE (hora TIME) AS
$$
BEGIN
    RETURN QUERY
    WITH horas AS (
        SELECT generate_series(
            (p_fecha::timestamp + time '05:00'),
            (p_fecha::timestamp + time '21:00'),   -- si quieres último inicio válido, cámbialo a '20:00'
            interval '1 hour'
        ) AS hora
    ),
    ocupadas AS (
        -- Reservas activas (equipo)
        SELECT r.hora_inicio,
               r.hora_fin,
               de.id       AS id_detalle,
               'RESERVA'   AS origen
        FROM reserva r
        INNER JOIN detalle_reserva_equipo de ON r.id = de.id_reserva
        WHERE r.fecha_reserva = p_fecha
          AND de.id_equipo = p_idEquipo
          AND COALESCE(r.state, false) = true
          AND COALESCE(de.state, false) = true

        UNION ALL

        -- Mantenimientos activos (equipo)
        SELECT r.hora_inicio,
               r.hora_fin,
               me.id       AS id_detalle,
               'MANTENIMIENTO' AS origen
        FROM reserva r
        INNER JOIN mantenimiento_equipo me ON r.id = me.id_reserva
        WHERE r.fecha_reserva = p_fecha
          AND me.id_equipo = p_idEquipo
          AND COALESCE(r.state, false) = true
          AND COALESCE(me.state, false) = true
    )
    SELECT h.hora::time
    FROM horas h
    WHERE NOT EXISTS (
        SELECT 1
        FROM ocupadas o
        WHERE h.hora::time >= o.hora_inicio
          AND h.hora::time <  o.hora_fin
          AND NOT (
              p_idDetalle IS NOT NULL
              AND o.id_detalle = p_idDetalle
              AND (p_origen IS NULL OR o.origen = p_origen)
          )
    )
    ORDER BY h.hora;
END;
$$ LANGUAGE plpgsql STABLE;