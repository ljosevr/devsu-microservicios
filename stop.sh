#!/bin/bash

# Script para detener y limpiar los servicios Docker

echo "Deteniendo servicios..."
docker-compose down

echo ""
echo "¿Deseas eliminar también los volúmenes (datos)? (s/n)"
read -r response

if [[ "$response" =~ ^([sS][iI]|[sS])$ ]]; then
    echo "Eliminando volúmenes..."
    docker-compose down -v
    echo "✓ Volúmenes eliminados"
fi

echo ""
echo "✓ Servicios detenidos"

