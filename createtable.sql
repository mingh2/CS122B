create database if not exists moviedb;

use moviedb;

create table if not exists movies(
	id integer not null primary key auto_increment,
    title varchar(100) not null,
    year int not null,
    director varchar(100) not null,
    banner_url varchar(200) default "",
    trailer_url varchar(200) default ""
);

create table if not exists stars(
	id integer not null primary key auto_increment,
	first_name varchar(50) not null,
	last_name varchar(50) not null,
	dob date,
	photo_url varchar(200) default ""
);

create table if not exists stars_in_movies(
	star_id integer not null,
    movie_id integer not null,
    foreign key (star_id) references stars(id),
    foreign key (movie_id) references movies(id)
);

create table if not exists genres(
	id integer not null primary key auto_increment,
	name varchar(32) not null
);

create table if not exists genres_in_movies(
	genre_id integer not null,
    movie_id integer not null,
    foreign key (genre_id) references genres(id),
    foreign key (movie_id) references movies(id)
);

create table if not exists creditcards(
	id varchar(20) not null primary key,
	first_name varchar(50) not null, 
	last_name varchar(50) not null,
	expiration date not null
);

create table if not exists customers(
	id integer not null primary key auto_increment,
    first_name varchar(50) not null,
    last_name varchar(50) not null,
    cc_id varchar(20) not null,
    address varchar(200) not null,
    email varchar(50) not null,
    password varchar(20) not null,
    foreign key (cc_id) references creditcards(id)
);

create table if not exists sales(
	id integer not null primary key auto_increment,
    customer_id integer not null,
    movie_id integer not null,
    sale_date date not null,
    foreign key (customer_id) references customers(id),
    foreign key (movie_id) references movies(id)
);



    