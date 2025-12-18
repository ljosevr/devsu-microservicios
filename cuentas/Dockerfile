# Microservicio de Cuentas (Cuenta + Movimientos)
FROM amazoncorretto:17-alpine
LABEL maintainer="devsu"
LABEL service="cuentas-service"

WORKDIR /app

# Copiar el JAR generado
COPY build/libs/*.jar app.jar

# Exponer el puerto
EXPOSE 8082

# Variables de entorno por defecto
ENV SPRING_PROFILES_ACTIVE=docker
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Healthcheck
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8082/actuator/health || exit 1

# Ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

