use Agenda;

-- Insertar contactos
INSERT INTO Contact (name, birthday, favorite, lastEdit)
VALUES (N'Juan Pérez', '1990-05-15', 1, GETDATE()),
       (N'María García', '1985-08-20', 0, GETDATE()),
       (N'Carlos Martínez', '1995-03-10', 1, GETDATE());

-- Insertar números de teléfono
INSERT INTO Phone (idContact, countryCode, phone, category)
VALUES (1, '34', '123456789', 'Personal'),
       (1, '34', '987654321', 'Trabajo'),
       (2, '34', '111222333', 'Personal'),
       (3, '34', '555666777', 'Personal');

-- Insertar direcciones de correo electrónico
INSERT INTO Email (idContact, email, category)
VALUES (1, 'juan@example.com', 'Personal'),
       (1, 'juan.perez@trabajo.com', 'Trabajo'),
       (2, 'maria@example.com', 'Personal'),
       (3, 'carlos@example.com', 'Personal');

-- Insertar direcciones
INSERT INTO Address (idContact, street, houseNumber, city, zip, category)
VALUES (1, 'Calle Principal', '123', 'Ciudad', '12345', 'Personal'),
       (1, 'Avenida Central', '456', 'Ciudad', '54321', 'Trabajo'),
       (2, 'Calle Secundaria', '789', 'Pueblo', '98765', 'Personal'),
       (3, 'Calle Alternativa', '1011', 'Villa', '13579', 'Personal');

-- Insertar etiquetas
INSERT INTO Tag (name)
VALUES ('Amigo'),
       ('Familia'),
       ('Trabajo');

-- Asignar etiquetas a contactos
INSERT INTO Contact_Tag (idContact, idTag)
VALUES (1, 1), (1, 2),
       (2, 1),
       (3, 3);

-- Insertar grupos
INSERT INTO [Group] (name, description)
VALUES ('Grupo de Amigos', 'Amigos cercanos'),
       ('Grupo de Trabajo', 'CompaC1eros de trabajo');

-- Asignar contactos a grupos
INSERT INTO Contact_Group (idContact, idGroup)
VALUES (1, 1), (1, 2),
       (2, 1),
       (3, 2);

go

/* ------------ FUNCTIONS & PROCEDURES ------------ */

/*
 * Vacía la base de datos entera, para hacer pruebas de JUnit
 */
create or alter procedure emptyDatabase
as begin
    delete from Contact;
    delete from [Group];
    delete from [Tag];
end;
go

/*
 * Devuelve todos los contactos
 * Columnas: id | name | favorite | Email | Phone
 */
create or alter function getContacts ()
returns table
as return (
    select C.id, C.name, C.favorite,
    max(E.email) as 'Email',
    max(concat_ws(' ', P.countryCode, P.phone)) as 'Phone'
    from Contact C
    left join Phone P on C.ID = P.idContact
    left join Email E on C.ID = E.idContact
    group by C.ID, C.name, C.favorite
);
go


/*
 * Devuelve los detalles de un contacto en especifico
 * Columnas 1: name | birthday | creationDate | favorite | Tags
 * Columnas 2: phoneId | countryCode | phone | category
 * Columnas 3: emailID | email | category
 * Columnas 4: addressID | street | houseNumber | city | zip | category
 */
create or alter procedure getContactDetails (@ID int) as
begin
    select C.name, C.birthday, C.creationDate, C.favorite, string_agg(T.name, ',') 'Tags'
    from Contact C
             left join Contact_Tag CT on C.id = CT.idContact
             left join Tag T on CT.idTag = T.id
    where C.ID = @ID
    group by C.name, C.birthday, C.creationDate, C.favorite;

    select ID 'phoneID', countryCode, phone, category
    from Phone
    where idContact = @ID
    order by category, countryCode, phone;

    select ID 'emailID', email, category
    from Email where idContact = @ID
    order by category, email;

    select ID 'addressID', street, houseNumber, city, zip, category
    from Address
    where idContact = @ID
    order by concat_ws(' ', street, houseNumber, city, zip, category);
end;
go

/*
 * Elimina un contacto si existe
 * Parametros:
 *      - @ID: ID del contacto a eliminar
 * Devuelve: 'ok' si se elimino, 'not found' si no se encontró el contacto
 */
create or alter procedure removeContact (@ID int) as
begin
    if exists (select * from Contact where ID = @ID)
        begin
            delete from Contact where ID = @ID;
            select 'ok' 'res'
        end
    else
        select 'not found' 'res'
end;
go

/*
 * Elimina un grupo si existe
 * Parámetros:
 *      - @ID: ID del grupo a eliminar
 * Devuelve: 'ok' si se elimino, 'not found' si no se encontró el contacto
 */
create or alter procedure removeGroup (@ID int) as
begin
    if exists (select * from [Group] where ID = @ID)
        begin
            delete from [Group] where ID = @ID;
            select 'ok' 'res'
        end
    else
        select 'not found' 'res'
end;
go

/*
 * Devuelve los contactos filtrando por los Parámetros introducidos
 * Parámetros:
 *      - @name: Nombre del contacto
 *      - @phone: teléfono del contacto
 *      - @email: Email del contacto
 *      - @tags: Etiqueta(s) del contacto, separadas por comas
 * Uso:
 *      - select * from searchContact('John', '123456789', 'example@example.com', 'familia');
 *      - select * from searchContact('John', null, null, null);
 */
create or alter function searchContact (
    @name nvarchar(50) = NULL,
    @phone nvarchar(25) = NULL,
    @email nvarchar(120) = NULL,
    @tags nvarchar(MAX) = NULL
) returns table
    as return (
    select distinct C.ID, C.name, C.birthday, C.creationDate, C.favorite
    from Contact C
             left join Phone P on C.ID = P.idContact
             left join Email E on C.ID = E.idContact
             left join Contact_Tag CT on C.ID = CT.idContact
             left join Tag T on CT.idTag = T.ID
    where
        (@name is null or C.name like '%' + @name + '%') and
        (@phone is null or P.phone like '%' + @phone + '%') and
        (@email is null or E.email like '%' + @email + '%') and
        (@tags is null
            or exists (
                select 1
                from string_split(@tags, ',') as tag
                where T.name like '%' + trim(tag.value) + '%'
            )
            )
    );
go

/*
 * Devuelve una lista de los grupos y cuantos miembros hay en ellos
 * Columnas: id | name | description | Members
 */
create or alter procedure getGroups as
select G.id, G.name, G.description, count(CG.idContact) 'Members'
from [Group] G
         left join Contact_Group as CG on G.id = CG.idGroup
group by G.id, G.name, G.description;
go

/*
 * Devuelve una lista de los contactos que forman un grupo
 * Columnas: id | name | favorite | Email | Phone
 */
create or alter procedure getGroupMembers (@ID int) as
select * from dbo.getContacts()
where id in (
    select idContact
    from Contact_Group
    where idGroup = @ID
)
go

/*
 * Devuelve una lista de los contactos que NO forman un grupo
 * Columnas: id | name | favorite | Email | Phone
 */
create or alter procedure getGroupAvailableMembers(@ID int) as
select * from dbo.getContacts()
where id not in (
    select idContact
    from Contact_Group
    where idGroup = @ID
)
go

/*
 * Devuelve detalles de un un grupo
 * Columnas: id | name | description
 */
create or alter procedure getGroup (@ID int) as
select * from [Group] where id = @ID;
go

/*
 * Añade un nuevo grupo y añade miembros automáticamente
 */
create or alter procedure addGroup (
    @name nvarchar(50),
    @description nvarchar(256),
    @members varchar(max)
) as begin

    insert into [Group] (name, description) values (@name, @description);
    -- Recoger el id de la fila que acabamos de insertar
    declare @id int = scope_identity();
    insert into Contact_Group (idContact, idGroup) select value, @id from string_split(@members, ',');
end;
go

/*
 * Añade contactos a un grupo existente siempre que ambos existan
 */
create or alter procedure addGroupMembers (
    @groupID int,
    @members varchar(max)
) as begin
    if exists (select *  from [Group] where id = @groupID)
        begin
            insert into Contact_Group (idContact, idGroup)
            select value, @groupID
            from string_split(@members, ',')
            where value in (select id from Contact);
        end
end;
go


create or alter procedure removeGroupMember (
    @groupID int,
    @memberID int
) as begin
    if exists (select * from Contact_Group where idContact = @memberID and idGroup = @groupID)
        begin
            delete from Contact_Group where idContact = @memberID and idGroup = @groupID;
        end
end;
go

/*
 * Devuelva todas las tags existentes
 * Columnas: ID | name
 */
create or alter procedure getTags as
select * from Tag;
go

/*
 * Devuelve las tags filtrándolas por su nombre
 * Columnas: ID | name
 */
create or alter function getTagsByNames (
    @tags nvarchar(max)
) returns table
    as return (
    select * from Tag where name in (
        select trim(value) 'v' from string_split(@tags, ',')
    )
    );
go

/*
 * Devuelve las tags seleccionables para un contacto
 * Columnas: ID | name
 */
create or alter procedure getAvailableTags (@contactID int)
as begin
    select CT.idTag 'ID', T.name
    from Contact_Tag CT
             inner join Tag T on CT.idTag = T.ID
    where CT.idContact != @contactID;
end;
go

/*
 * Actualiza el campo lastEdit de un contacto tras actualizarlo
 */
create or alter trigger actualizarLastEdit
    on Contact
    after update
    as begin
    update Contact
    set lastEdit = getdate()
    from Contact c
    inner join inserted i on c.id = i.id;
end;
go

/*
 * Actualiza un contacto
 */
create or alter procedure updateContact (
    @ID int,
    @name nvarchar(50),
    @birthday date,
    @favorite bit,
    @deletedEmails varchar(max),
    @deletedPhones varchar(max),
    @deletedAddresses varchar(max)
) as begin
    -- Actualizar contacto
    update Contact
    set name = @name, birthday = @birthday, favorite = @favorite
    where ID = @ID;

    -- Borrar emails
    if @deletedEmails is not null and len(@deletedEmails) > 0
        begin
            delete from Email where idContact = @ID and ID in (select value from string_split(@deletedEmails, ','));
        end

    -- Borrar teléfonos
    if @deletedPhones is not null and len(@deletedPhones) > 0
        begin
            delete from Phone where idContact = @ID and ID in (select value from string_split(@deletedPhones, ','));
        end

    -- Borrar direcciones
    if @deletedAddresses is not null and len(@deletedAddresses) > 0
        begin
            delete from Address where idContact = @ID and ID in (select value from string_split(@deletedAddresses, ','));
        end
end;
go

/*
 * Actualiza un email de un contacto.
 * Si el ID = -1, se insertará un nuevo email
 */
create or alter procedure updateEmail (
    @contactID int,
    @ID int,
    @email nvarchar(120),
    @category nvarchar(25)
) as begin

    if @ID = -1
        begin
            insert into Email (idContact, email, category) values(@contactID, @email, @category)
        end
    else
        begin
            update Email
            set email = @email, category = @category
            where idContact = @contactID and ID = @ID
        end
end;
go

/*
 * Actualiza un número de teléfono de un contacto.
 * Si el ID = -1, se insertará un nuevo número de teléfono
 */
create or alter procedure updatePhone (
    @contactID int,
    @ID int,
    @countryCode nvarchar(5),
    @phone nvarchar(25),
    @category nvarchar(25)
) as begin
    if @ID = -1
        begin
            insert into Phone (idContact, countryCode, phone, category)
            values (@contactID, @countryCode, @phone, @category);
        end
    else
        begin
            update Phone
            set countryCode = @countryCode, phone = @phone, category = @category
            where idContact = @contactID and ID = @ID;
        end
end;
go

/*
 * Actualiza una dirección de un contacto.
 * Si el ID = -1, se insertará una nueva dirección
 */
create or alter procedure updateAddress (
    @contactID int,
    @ID int,
    @street nvarchar(100),
    @houseNumber nvarchar(4),
    @zip char(5),
    @city nvarchar(50),
    @category nvarchar(25)
) as begin
    if @ID = -1
        begin
            insert into Address (idContact, street, houseNumber, city, zip, category)
            values (@contactID, @street, @houseNumber, @city, @zip, @category);
        end
    else
        begin
            update Address
            set street = @street, houseNumber = @houseNumber, city = @city, zip = @zip, category = @category
            where idContact = @contactID and ID = @ID;
        end
end;
go

/*
 * Añade las tags nuevas a la base de datos
 */
create or alter procedure insertContactTags (
    @contactID int,
    @tags nvarchar(max)
) as
begin
    -- Tabla para guardar los IDs de las etiquetas que ya existen
    declare @tagList table (id int);

    -- Insertar etiquetas si no existen
    insert into Tag (name)
    output inserted.id into @tagList (id) -- Guardar IDs generados
    select trim(S.value) as name
    from string_split(@tags, ',') as S -- Divide nuevamente la cadena @tags en filas separadas por comas
    where not exists (
        select *
        from Tag
        where trim(S.value) = Tag.name -- Verifica si la etiqueta ya existe en la tabla Tag
    );

    -- Insertar los IDs de las etiquetas y el contactID
    insert into Contact_Tag (idContact, idTag)
    select @contactID, id
    from @tagList;
end;
go