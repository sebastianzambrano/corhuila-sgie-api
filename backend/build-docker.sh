#!/bin/bash

# Script para construir y desplegar la aplicación con Docker
# Uso: ./build-docker.sh [--multistage]

set -e  # Salir si hay errores

echo "🔨 Construyendo aplicación SGIE..."

# Verificar si se debe usar multistage
MULTISTAGE=false
if [[ "$1" == "--multistage" ]]; then
    MULTISTAGE=true
fi

if [ "$MULTISTAGE" = true ]; then
    echo "📦 Usando Dockerfile multi-stage (compila dentro de Docker)..."
    docker build -f Dockerfile.multistage -t sgie-backend .
else
    echo "📦 Compilando con Maven..."
    mvn clean package -DskipTests

    echo "🐳 Construyendo imagen Docker..."
    docker build -t sgie-backend .
fi

echo ""
echo "✅ Imagen Docker creada exitosamente: sgie-backend"
echo ""
echo "🚀 Para iniciar la aplicación:"
echo "   docker-compose up -d"
echo ""
echo "📋 Para ver logs:"
echo "   docker-compose logs -f backend"
echo ""
echo "🛑 Para detener:"
echo "   docker-compose down"

