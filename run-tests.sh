#!/bin/bash

# Script para ejecutar las pruebas de los microservicios

set -e

echo "========================================="
echo "EJECUCIÓN DE PRUEBAS"
echo "========================================="

# Pruebas del microservicio de clientes
echo ""
echo "1. Ejecutando pruebas de Clientes..."
cd clientes
./gradlew test jacocoTestReport
echo "✓ Pruebas de Clientes completadas"
echo "  Reporte de cobertura: clientes/build/reports/jacoco/test/html/index.html"
cd ..

# Pruebas del microservicio de cuentas
echo ""
echo "2. Ejecutando pruebas de Cuentas..."
cd cuentas
./gradlew test jacocoTestReport
echo "✓ Pruebas de Cuentas completadas"
echo "  Reporte de cobertura: cuentas/build/reports/jacoco/test/html/index.html"
cd ..

echo ""
echo "========================================="
echo "¡PRUEBAS COMPLETADAS!"
echo "========================================="
echo ""
echo "Abrir reportes de cobertura:"
echo "  open clientes/build/reports/jacoco/test/html/index.html"
echo "  open cuentas/build/reports/jacoco/test/html/index.html"
echo ""

