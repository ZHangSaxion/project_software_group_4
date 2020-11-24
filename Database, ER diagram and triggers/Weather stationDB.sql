 -- If database already exists then remove it --
DROP DATABASE IF EXISTS Wstation;

CREATE DATABASE Wstation;
USE Wstation;

 -- Create sensor table --
 
CREATE TABLE sensor (
sensor_id int auto_increment,
a_b_pressure decimal(5,1),
a_ambient_light decimal(5,1),
a_temperature decimal(3,1),
location varchar(30),
PRIMARY KEY (sensor_id)
);

 -- Create readings table --

CREATE TABLE readings (
reading_id int auto_increment,
b_pressure decimal(5,1),
ambient_light decimal(5,1),
temperature decimal(3,1),
date_read datetime,
PRIMARY KEY (reading_id)
);

 -- Create sensor_to_readings table --

CREATE TABLE sensor_to_readings (
sensor_id int,
reading_id int,
FOREIGN KEY (sensor_id) REFERENCES sensor(sensor_id),
FOREIGN KEY (reading_id) REFERENCES readings(reading_id)
);


