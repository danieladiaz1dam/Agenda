USE [master];
GO

-- Crear usuario para la aplicación de Tomcat
IF NOT EXISTS (SELECT * FROM sys.sql_logins WHERE name = 'usuario')
    BEGIN
        CREATE LOGIN [usuario] WITH PASSWORD = 'P@ssw0rd', CHECK_POLICY = OFF;
        ALTER SERVER ROLE [sysadmin] ADD MEMBER [usuario];
    END;
GO
