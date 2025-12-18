-- Crear las bases de datos para los microservicios
CREATE DATABASE clientesdb;
CREATE DATABASE cuentasdb;

-- Conectar a clientesdb para crear las tablas
\c clientesdb;

-- Tabla Personas (Herencia)
CREATE TABLE IF NOT EXISTS personas (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    genero VARCHAR(20),
    edad INTEGER,
    identificacion VARCHAR(20) UNIQUE NOT NULL,
    direccion VARCHAR(200),
    telefono VARCHAR(20)
);

-- Tabla Clientes (Hereda de Personas)
CREATE TABLE IF NOT EXISTS clientes (
    id BIGINT PRIMARY KEY REFERENCES personas(id) ON DELETE CASCADE,
    cliente_id VARCHAR(50) UNIQUE NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE
);

-- Índices para mejorar el rendimiento
CREATE INDEX idx_clientes_cliente_id ON clientes(cliente_id);
CREATE INDEX idx_personas_identificacion ON personas(identificacion);

-- Conectar a cuentasdb para crear las tablas
\c cuentasdb;

-- Tabla Cuentas
CREATE TABLE IF NOT EXISTS cuentas (
    id BIGSERIAL PRIMARY KEY,
    numero_cuenta VARCHAR(20) UNIQUE NOT NULL,
    tipo_cuenta VARCHAR(50) NOT NULL,
    saldo_inicial DECIMAL(15,2) NOT NULL,
    saldo_actual DECIMAL(15,2) NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE,
    cliente_id VARCHAR(50) NOT NULL,
    cliente_nombre VARCHAR(100)
);

-- Tabla Movimientos
CREATE TABLE IF NOT EXISTS movimientos (
    id BIGSERIAL PRIMARY KEY,
    fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipo_movimiento VARCHAR(50) NOT NULL,
    valor DECIMAL(15,2) NOT NULL,
    saldo DECIMAL(15,2) NOT NULL,
    cuenta_id BIGINT NOT NULL,
    FOREIGN KEY (cuenta_id) REFERENCES cuentas(id) ON DELETE CASCADE
);

-- Índices para mejorar el rendimiento
CREATE INDEX idx_cuentas_numero_cuenta ON cuentas(numero_cuenta);
CREATE INDEX idx_cuentas_cliente_id ON cuentas(cliente_id);
CREATE INDEX idx_movimientos_cuenta_id ON movimientos(cuenta_id);
CREATE INDEX idx_movimientos_fecha ON movimientos(fecha);

-- Datos iniciales de prueba
\c clientesdb;

-- Insertar clientes de ejemplo
INSERT INTO personas (nombre, genero, edad, identificacion, direccion, telefono) VALUES
('Jose Lema', 'Masculino', 30, '1234567890', 'Otavalo sn y principal', '098254785'),
('Marianela Montalvo', 'Femenino', 28, '0987654321', 'Amazonas y NNUU', '097548965'),
('Juan Osorio', 'Masculino', 35, '1122334455', '13 junio y Equinoccial', '098874587')
ON CONFLICT (identificacion) DO NOTHING;

INSERT INTO clientes (id, cliente_id, contrasena, estado) VALUES
((SELECT id FROM personas WHERE identificacion = '1234567890'), 'CLI001', '1234', true),
((SELECT id FROM personas WHERE identificacion = '0987654321'), 'CLI002', '5678', true),
((SELECT id FROM personas WHERE identificacion = '1122334455'), 'CLI003', '1245', true)
ON CONFLICT (cliente_id) DO NOTHING;

\c cuentasdb;

-- Insertar cuentas de ejemplo
INSERT INTO cuentas (numero_cuenta, tipo_cuenta, saldo_inicial, saldo_actual, estado, cliente_id, cliente_nombre) VALUES
('478758', 'AHORROS', 2000.00, 2000.00, true, 'CLI001', 'Jose Lema'),
('225487', 'CORRIENTE', 100.00, 100.00, true, 'CLI001', 'Jose Lema'),
('495878', 'AHORROS', 0.00, 0.00, true, 'CLI002', 'Marianela Montalvo'),
('496825', 'AHORROS', 540.00, 540.00, true, 'CLI002', 'Marianela Montalvo'),
('585545', 'CORRIENTE', 1000.00, 1000.00, true, 'CLI003', 'Juan Osorio')
ON CONFLICT (numero_cuenta) DO NOTHING;

