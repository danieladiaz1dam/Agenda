#!/bin/bash

echo "Starting Tomcat 🐯..."

# Backup webapps
mv /usr/local/tomcat/webapps /usr/local/tomcat/webapps2;

# Copiar webapps con el manager
mv /usr/local/tomcat/webapps.dist /usr/local/tomcat/webapps;

# Copiar el archivo war de webapps2
mv /usr/local/tomcat/webapps2/*.war /usr/local/tomcat/webapps

# Copiar los archivos de configuración
cp /tmp/context.xml /usr/local/tomcat/webapps/manager/META-INF/context.xml;

# Esperar que la base de datos termine de montarse
echo "[🐯] Waiting for database to finish 🧶🐅";

sleep 20s;

echo "[🐯] Starting Tomcat 🚀";

# Iniciar Tomcat
catalina.sh run;