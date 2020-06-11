
create database if not exists adlister_db;

use adlister_db;

drop table if exists ads;
drop table if exists users;

create table users(
                      id int unsigned not null auto_increment,
                      username varchar(255) not null,
                      email varchar (255) not null,
                      password varchar (255) not null,
                      primary key (id)
);

create table ads(
                    id int unsigned not null auto_increment,
                    user_id int unsigned not null,
                    title varchar(255) not null,
                    description text not null,
                    PRIMARY KEY (id),
                    foreign key (user_id) references users(id)
                on delete cascade
);