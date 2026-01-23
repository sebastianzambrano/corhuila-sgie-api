FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Crear usuario no-root para seguridad
RUN groupadd -r spring && useradd -r -g spring spring

# Copiar el JAR (debe estar construido antes con: mvn clean package -DskipTests)
COPY target/*.jar app.jar

# Cambiar permisos
RUN chown -R spring:spring /app
USER spring:spring

# Exponer puerto
EXPOSE 8080

# Variables de entorno por defecto
ENV JAVA_OPTS="-Xms512m -Xmx1024m"
ENV SPRING_PROFILES_ACTIVE=dev

# Ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
