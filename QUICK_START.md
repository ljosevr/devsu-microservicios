# Quick Start Guide - Devsu Microservicios

## 🚀 Inicio Rápido (5 minutos)

### Requisitos
- Docker y Docker Compose instalados
- Java 17 (solo para desarrollo local)

### Opción 1: Despliegue Completo con Docker (Recomendado)

```bash
# 1. Clonar o ubicarse en el directorio del proyecto
cd "Devsu Project"

# 2. Ejecutar el script de despliegue
./deploy.sh
```

Esto compilará ambos microservicios y los desplegará en Docker automáticamente.

### Opción 2: Despliegue Manual

```bash
# 1. Compilar los microservicios
cd clientes && ./gradlew clean build -x test && cd ..
cd cuentas && ./gradlew clean build -x test && cd ..

# 2. Iniciar con Docker Compose
docker-compose up -d

# 3. Ver logs
docker-compose logs -f
```

### ✅ Verificación

Acceder a:
- **Clientes API**: http://localhost:8081/clientes
- **Cuentas API**: http://localhost:8082/cuentas
- **Swagger Clientes**: http://localhost:8081/swagger-ui.html
- **Swagger Cuentas**: http://localhost:8082/swagger-ui.html
- **RabbitMQ**: http://localhost:15672 (guest/guest)

### 📝 Probar con Postman

1. Importar la colección: `Devsu-Microservicios.postman_collection.json`
2. Ejecutar los requests en orden:
   - Crear clientes
   - Crear cuentas
   - Registrar movimientos
   - Generar reportes

### 🧪 Ejecutar Pruebas

```bash
./run-tests.sh
```

### 🛑 Detener Servicios

```bash
./stop.sh
```

## 📊 Flujo de Trabajo Típico

1. **Crear un cliente** (POST /clientes)
2. **Crear cuentas** para el cliente (POST /cuentas)
3. **Realizar movimientos** (POST /movimientos)
4. **Generar reporte** (GET /reportes)

## 🔍 Solución de Problemas

### Puerto ocupado
```bash
# Ver qué proceso usa el puerto
lsof -i :8081
# Matar el proceso
kill -9 <PID>
```

### Reiniciar todo
```bash
docker-compose down -v
./deploy.sh
```

### Ver logs de un servicio específico
```bash
docker-compose logs -f clientes-service
docker-compose logs -f cuentas-service
```

## 📚 Documentación Completa

Ver el archivo `README.md` para documentación completa.

