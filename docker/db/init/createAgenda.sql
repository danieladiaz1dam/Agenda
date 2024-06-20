use master;
go
drop database if exists Agenda;
create database Agenda;
go
use Agenda
go

create table Contact
(
    ID           int primary key identity,
    name         nvarchar(50) not null,
    birthday     date,
    favorite     bit  default 0,
    creationDate date default getdate(),
    lastEdit     date default null
);

create sequence PhoneSequence start with 1 increment by 1;
create table Phone
(
    ID          int          default next value for PhoneSequence,
    idContact   int         not null,
    countryCode nvarchar(5) not null,
    phone       nvarchar(25),
    category    nvarchar(25) default N'Sin categoría',

    constraint FK_PHONE_CONTACT foreign key (idContact) references Contact (ID) on delete cascade on update cascade,
    constraint PK_PHONE primary key (idContact, ID)
);

create sequence EmailSequence start with 1 increment by 1;
create table Email
(
    ID        int          default next value for EmailSequence,
    idContact int           not null,
    email     nvarchar(120) not null,
    category  nvarchar(25) default N'Sin categoría',

    constraint FK_EMAIL_CONTACT foreign key (idContact) references Contact (ID) on delete cascade on update cascade,
    constraint PK_EMAIL primary key (idContact, ID)
);

create sequence AddressSequence start with 1 increment by 1;
create table Address
(
    ID          int          default next value for AddressSequence,
    idContact   int           not null,
    street      nvarchar(100) not null,
    houseNumber nvarchar(4)   not null,
    city        nvarchar(50)  not null,
    zip         char(5)       not null,
    category    nvarchar(25) default N'Sin categoría',

    constraint FK_ADDRESS_CONTACT foreign key (idContact) references Contact (ID) on delete cascade on update cascade,
    constraint PK_ADDRESS primary key (idContact, ID)
);

create table Tag
(
    ID   int primary key identity,
    name nvarchar(50) not null
);

create table Contact_Tag
(
    idContact int,
    idTag     int,

    constraint FK_CONTACT_TAG_CONTACT foreign key (idContact) references Contact (ID) on delete cascade on update cascade,
    constraint FK_CONTACT_TAG_TAG foreign key (idTag) references Tag (ID) on delete cascade on update cascade,
    constraint PK_CONTACT_TAG primary key (idContact, idTag)
);

create table [Group]
(
    ID          int primary key identity,
    name        nvarchar(50) not null,
    description nvarchar(256) default N'Sin descripción'
);

create table Contact_Group
(
    idContact int,
    idGroup   int,

    constraint FK_CONTACT_GROUP_CONTACT foreign key (idContact) references Contact (ID) on delete cascade on update cascade,
    constraint FK_CONTACT_GROUP_GROUP foreign key (idGroup) references [Group] (ID) on delete cascade on update cascade,
    constraint PK_CONTACT_GROUP primary key (idContact, idGroup)
);
go