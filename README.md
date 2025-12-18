# Devsu - Proyecto de Microservicios

Sistema de gestión bancaria implementado con arquitectura de microservicios utilizando Spring Boot, PostgreSQL y RabbitMQ.

## 📋 Tabla de Contenidos
- [Descripción](#descripción)
- [Arquitectura](#arquitectura)
- [Tecnologías Utilizadas](#tecnologías-utilizadas)
- [Requisitos Previos](#requisitos-previos)
- [Instalación y Configuración](#instalación-y-configuración)
- [Despliegue con Docker](#despliegue-con-docker)
- [Pruebas](#pruebas)
- [Endpoints de la API](#endpoints-de-la-api)
- [Colección de Postman](#colección-de-postman)

## 📖 Descripción

Sistema de microservicios que gestiona clientes y sus cuentas bancarias con las siguientes funcionalidades:

### Microservicio de Clientes
- Gestión completa (CRUD) de clientes y personas
- Herencia entre entidades Persona y Cliente
- Comunicación asíncrona con RabbitMQ

### Microservicio de Cuentas
- Gestión de cuentas bancarias (CRU)
- Registro de movimientos (depósitos y retiros)
- Validación de saldo disponible
- Generación de reportes de estado de cuenta

## 🏗️ Arquitectura

El proyecto implementa **Clean Architecture** con la siguiente estructura:

```
├── domain/              # Entidades, excepciones, repositorios
├── application/         # Casos de uso, DTOs, servicios
└── infrastructure/      # Controladores REST, configuraciones, mensajería
```

### Comunicación entre Microservicios
- **Asíncrona**: RabbitMQ para eventos de cambios en clientes
- **Base de datos**: PostgreSQL con bases de datos separadas

## 🛠️ Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.3.5**
- **Spring Data JPA**
- **PostgreSQL 15** (desarrollo y producción)
- **H2 Database** (solo para pruebas automatizadas)
- **RabbitMQ 3.12**
- **Docker & Docker Compose**
- **Gradle**
- **Lombok**
- **JUnit 5 & Mockito**
- **Swagger/OpenAPI**

## ✅ Requisitos Previos

- Java 17 o superior
- Docker y Docker Compose
- Gradle 8.x (o usar el wrapper incluido)
- Git

## 🚀 Instalación y Configuración

### 1. Clonar el repositorio

```bash
git clone <url-del-repositorio>
cd "Devsu Project"
```

### 2. Compilar los proyectos

#### Para el microservicio de Clientes:
```bash
cd clientes
./gradlew clean build
cd ..
```

#### Para el microservicio de Cuentas:
```bash
cd cuentas
./gradlew clean build
cd ..
```

### 3. Ejecutar en modo desarrollo (sin Docker)

#### Terminal 1 - Microservicio de Clientes:
```bash
cd clientes
./gradlew bootRun
```
Disponible en: http://localhost:8081

#### Terminal 2 - Microservicio de Cuentas:
```bash
cd cuentas
./gradlew bootRun
```
Disponible en: http://localhost:8082

**Nota:** En modo desarrollo se usa H2 Database en memoria. Para usar PostgreSQL local, configura las propiedades en `application.properties`.

## 🐳 Despliegue con Docker

### Opción 1: Despliegue Completo

```bash
# Compilar los proyectos
./gradlew clean build

# O compilar individualmente:
cd clientes && ./gradlew clean build && cd ..
cd cuentas && ./gradlew clean build && cd ..

# Iniciar todos los servicios
docker-compose up -d

# Ver logs
docker-compose logs -f
```

### Opción 2: Reconstruir y Desplegar

```bash
# Detener servicios existentes
docker-compose down

# Compilar y construir las imágenes
docker-compose up -d --build

# Verificar que los servicios estén corriendo
docker-compose ps
```

### Servicios Disponibles

| Servicio | Puerto | URL |
|----------|--------|-----|
| Clientes Service | 8081 | http://localhost:8081 |
| Cuentas Service | 8082 | http://localhost:8082 |
| PostgreSQL | 5432 | localhost:5432 |
| RabbitMQ Management | 15672 | http://localhost:15672 |
| RabbitMQ AMQP | 5672 | localhost:5672 |

### Credenciales

**PostgreSQL (ambas bases de datos):**
- Usuario: `devsu`
- Contraseña: `devsu123`
- Base de datos Clientes: `clientesdb`
- Base de datos Cuentas: `cuentasdb`

**RabbitMQ:**
- Usuario: `devsu`
- Contraseña: `devsu123`

### Detener los servicios

```bash
docker-compose down

# Para eliminar también los volúmenes (datos)
docker-compose down -v
```

## 🧪 Pruebas

### Ejecutar todas las pruebas

```bash
# Clientes
cd clientes
./gradlew test

# Cuentas
cd cuentas
./gradlew test
```

### Ejecutar pruebas con reporte de cobertura

```bash
./gradlew test jacocoTestReport
```

Los reportes se generan en:
- `clientes/build/reports/jacoco/test/html/index.html`
- `cuentas/build/reports/jacoco/test/html/index.html`

### Tipos de Pruebas Implementadas

1. **Pruebas Unitarias** (F5):
   - `ClienteServiceTest`: Pruebas completas del servicio de clientes

2. **Pruebas de Integración** (F6):
   - `CuentaMovimientoIntegrationTest`: Flujo completo de creación de cuenta y movimientos

## 📡 Endpoints de la API

### Microservicio de Clientes (Puerto 8081)

#### Clientes
- `POST /clientes` - Crear cliente
- `GET /clientes` - Listar todos los clientes
- `GET /clientes/{id}` - Obtener cliente por ID
- `GET /clientes/clienteId/{clienteId}` - Obtener cliente por clienteId
- `PUT /clientes/{id}` - Actualizar cliente
- `PATCH /clientes/{id}` - Actualizar parcialmente cliente
- `DELETE /clientes/{id}` - Eliminar cliente

### Microservicio de Cuentas (Puerto 8082)

#### Cuentas
- `POST /cuentas` - Crear cuenta
- `GET /cuentas` - Listar todas las cuentas
- `GET /cuentas/{id}` - Obtener cuenta por ID
- `GET /cuentas/numero/{numeroCuenta}` - Obtener cuenta por número
- `GET /cuentas/cliente/{clienteId}` - Obtener cuentas de un cliente
- `PUT /cuentas/{id}` - Actualizar cuenta
- `PATCH /cuentas/{id}` - Actualizar parcialmente cuenta

#### Movimientos
- `POST /movimientos` - Registrar movimiento
- `GET /movimientos` - Listar todos los movimientos
- `GET /movimientos/{id}` - Obtener movimiento por ID
- `GET /movimientos/cuenta/{numeroCuenta}` - Obtener movimientos de una cuenta

#### Reportes
- `GET /reportes?cliente={clienteId}&fechaInicio={yyyy-MM-dd}&fechaFin={yyyy-MM-dd}` - Estado de cuenta

### Documentación Swagger

- Clientes: http://localhost:8081/swagger-ui.html
- Cuentas: http://localhost:8082/swagger-ui.html

## 📮 Colección de Postman

Se incluye una colección de Postman en el archivo `Devsu-Microservicios.postman_collection.json` con todos los endpoints configurados.

### Ejemplos de Uso

#### 1. Crear Cliente
```json
POST http://localhost:8081/clientes
{
  "nombre": "Jose Lema",
  "genero": "Masculino",
  "edad": 30,
  "identificacion": "1234567890",
  "direccion": "Otavalo sn y principal",
  "telefono": "098254785",
  "clienteId": "CLI001",
  "contrasena": "1234",
  "estado": true
}
```

#### 2. Crear Cuenta
```json
POST http://localhost:8082/cuentas
{
  "numeroCuenta": "478758",
  "tipoCuenta": "AHORROS",
  "saldoInicial": 2000.00,
  "estado": true,
  "clienteId": "CLI001"
}
```

#### 3. Registrar Depósito
```json
POST http://localhost:8082/movimientos
{
  "numeroCuenta": "478758",
  "tipoMovimiento": "DEPOSITO",
  "valor": 600.00
}
```

#### 4. Registrar Retiro
```json
POST http://localhost:8082/movimientos
{
  "numeroCuenta": "478758",
  "tipoMovimiento": "RETIRO",
  "valor": 575.00
}
```

#### 5. Generar Reporte
```
GET http://localhost:8082/reportes?cliente=CLI001&fechaInicio=2024-01-01&fechaFin=2024-12-31
```

## 📊 Datos de Prueba

El sistema se inicializa con los siguientes datos (ver `init-databases.sql`):

### Clientes
1. **Jose Lema** (CLI001)
   - Identificación: 1234567890
   - Cuentas: 478758 (Ahorros, $2000), 225487 (Corriente, $100)

2. **Marianela Montalvo** (CLI002)
   - Identificación: 0987654321
   - Cuentas: 495878 (Ahorros, $0), 496825 (Ahorros, $540)

3. **Juan Osorio** (CLI003)
   - Identificación: 1122334455
   - Cuentas: 585545 (Corriente, $1000)

### Movimientos de Prueba a Realizar
1. Retiro de $575 en cuenta 478758
2. Depósito de $600 en cuenta 225487
3. Depósito de $150 en cuenta 495878
4. Retiro de $540 en cuenta 496825

## 🏆 Funcionalidades Implementadas

- ✅ **F1**: CRUD completo de Clientes, CRU de Cuentas y Movimientos
- ✅ **F2**: Registro de movimientos con actualización de saldo
- ✅ **F3**: Validación de saldo disponible con mensaje "Saldo no disponible"
- ✅ **F4**: Reporte de estado de cuenta con rango de fechas
- ✅ **F5**: Pruebas unitarias para la entidad Cliente
- ✅ **F6**: Pruebas de integración
- ✅ **F7**: Despliegue en contenedores Docker

## 🔒 Consideraciones de Escalabilidad y Resiliencia

1. **Comunicación Asíncrona**: RabbitMQ para desacoplar microservicios
2. **Health Checks**: Implementados en Docker Compose
3. **Retry Policy**: Configurado en dependencias de servicios
4. **Bases de Datos Separadas**: Cada microservicio tiene su propia BD
5. **Índices en BD**: Para mejorar el rendimiento de consultas
6. **Transacciones**: Garantizan la consistencia de datos

## 📝 Buenas Prácticas Implementadas

- ✅ Clean Architecture (Dominio, Aplicación, Infraestructura)
- ✅ Patrón Repository
- ✅ DTOs para transferencia de datos
- ✅ Manejo centralizado de excepciones
- ✅ Validación de datos con Bean Validation
- ✅ Logging estructurado
- ✅ Documentación con Swagger/OpenAPI
- ✅ Pruebas unitarias y de integración
- ✅ Code coverage con Jacoco

## 🐛 Troubleshooting

### Error: Puerto ya en uso
```bash
# Ver qué proceso usa el puerto
lsof -i :8081
# Matar el proceso
kill -9 <PID>
```

### Error: RabbitMQ no se conecta
```bash
# Verificar que RabbitMQ esté corriendo
docker-compose ps rabbitmq
# Ver logs de RabbitMQ
docker-compose logs rabbitmq
```

### Error: Base de datos no se crea
```bash
# Eliminar volúmenes y recrear
docker-compose down -v
docker-compose up -d
```

## 👨‍💻 Autor

Desarrollado como parte de la prueba técnica para Devsu

## 📄 Licencia

Este proyecto es de uso educativo y de evaluación técnica.

