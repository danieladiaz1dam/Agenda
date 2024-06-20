FROM maven:3.6.3-openjdk-17-slim AS build

# Copiar pom.xml
COPY pom.xml /home/app/pom.xml
RUN mvn -f /home/app/pom.xml verify clean --fail-never

# Copiar el código
COPY src /home/app/src

# Compilar
RUN mvn -X -f /home/app/pom.xml package

# Instalar Tomcat & java17
FROM tomcat:jdk17-temurin-jammy

# Copiar script para hacer Tomcat Web Manager accesible fuera del container
COPY docker/tomcat/enableManager.sh /home/app/enableManager.sh

EXPOSE 8080

# Configurar y reiniciar Tomcat
CMD /bin/bash /home/app/enableManager.sh

# Copiar la app compilada para que Tomcat la hostee
COPY --from=build /home/app/target/AgendaDaniela-1.0.war /usr/local/tomcat/webapps/