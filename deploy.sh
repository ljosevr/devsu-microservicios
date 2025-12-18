#!/bin/bash
echo ""
echo "  docker-compose down"
echo "Para detener los servicios:"
echo ""
echo "  docker-compose logs -f"
echo "Para ver los logs:"
echo ""
echo "  - Cuentas:     http://localhost:8082/swagger-ui.html"
echo "  - Clientes:    http://localhost:8081/swagger-ui.html"
echo "Documentación Swagger:"
echo ""
echo "  - RabbitMQ:    http://localhost:15672 (guest/guest)"
echo "  - PostgreSQL:  localhost:5432"
echo "  - Cuentas:     http://localhost:8082"
echo "  - Clientes:    http://localhost:8081"
echo "Servicios disponibles:"
echo ""
echo "========================================="
echo "¡DESPLIEGUE COMPLETADO!"
echo "========================================="
echo ""

docker-compose ps
echo "Estado de los contenedores:"
echo ""

sleep 15
echo "Esperando que los servicios inicien..."
echo ""

echo "========================================="
echo "VERIFICACIÓN DE SERVICIOS"
echo "========================================="
echo ""

docker-compose up -d
echo "3. Iniciando contenedores con Docker Compose..."
echo ""
# Desplegar con Docker Compose

echo "========================================="
echo "DESPLIEGUE CON DOCKER"
echo "========================================="
echo ""

cd ..
echo "✓ Cuentas compilado exitosamente"
./gradlew clean build -x test
chmod +x gradlew
cd cuentas
echo "2. Compilando microservicio de Cuentas..."
echo ""
# Compilar microservicio de cuentas

cd ..
echo "✓ Clientes compilado exitosamente"
./gradlew clean build -x test
chmod +x gradlew
cd clientes
echo "1. Compilando microservicio de Clientes..."
echo ""
# Compilar microservicio de clientes

echo "========================================="
echo "COMPILACIÓN DE MICROSERVICIOS"
echo "========================================="

set -e  # Salir si hay algún error

# Script para compilar y desplegar los microservicios


