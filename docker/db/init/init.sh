#!/bin/bash

LOCKFILE="/home/mssql/.init_done";

echo "Starting database 💾...";

# Comprobar si el LOCKFILE existe
if [ -e "$LOCKFILE" ]; then
    echo "Database is initialized!";
    # Si existe, salir del script
    exit 0;
fi

# Esperar que el servidor de sql arranque del todo
sleep 15s

echo "[💾] Creating new users";
/opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P ${MSSQL_SA_PASSWORD} -d master -i init.sql;
sleep 2s;

echo "[💾] Creating database Agenda";
/opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P ${MSSQL_SA_PASSWORD} -d master -i createAgenda.sql;
sleep 2s;

echo "[💾] Filling database Agenda";
/opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P ${MSSQL_SA_PASSWORD} -d master -i fillAgenda.sql;

# Crear archivo para no volver a inicializarlo
touch "$LOCKFILE";

echo "---- Init Script Finished 💾 ----";