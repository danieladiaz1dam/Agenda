# Run Microsoft SQl Server and initialization script (at the same time)
echo "entrypoint";
/home/mssql/init.sh & /opt/mssql/bin/sqlservr