FROM mcr.microsoft.com/mssql/server:2019-latest
WORKDIR /home/mssql
COPY init/* /home/mssql

ENV ACCEPT_EULA=Y
ENV MSSQL_DATA_DIR=/var/opt/sqlserver/data
ENV MSSQL_LOG_DIR=/var/opt/sqlserver/log
ENV MSSQL_BACKUP_DIR=/var/opt/sqlserver/backup
ENV MSSQL_SA_PASSWORD=SQLP@ssw0rd
ENV USUARIO_PASSWORD=P@ssw0rd

EXPOSE 1433

CMD /bin/bash ./entrypoint.sh