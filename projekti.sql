create database projekti;
use projekti;
create table users(
id integer auto_increment,
shfrytezuesi varchar(50) not null,
fjalekalimi varchar(100) not null,
salt varchar(50) not null,
primary key(id)
);

