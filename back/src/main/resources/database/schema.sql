create database if not exists p6;
use p6;

create table if not exists USERS
(
    id         int primary key,
    email      char(50)  not null,
    username   char(50)  not null,
    password   char(120) not null,
    created_at datetime  not null,
    updated_at datetime  null
);