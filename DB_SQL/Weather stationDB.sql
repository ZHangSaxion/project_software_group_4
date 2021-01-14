 -- If database already exists then remove it --
DROP DATABASE IF EXISTS Wstation;

CREATE DATABASE Wstation;
USE Wstation;

 -- Create sensor table --
 
CREATE TABLE sensor (
sensor_id int primary key auto_increment,
a_b_pressure decimal(5,1) DEFAULT 0,
a_ambient_light decimal(5,1) DEFAULT 0,
a_temperature decimal(3,1) DEFAULT 0,
location varchar(30) NOT NULL,
date_added datetime DEFAULT current_timestamp
);

 -- Create readings table --

CREATE TABLE readings (
reading_id int primary key auto_increment,
b_pressure decimal(5,1) NOT NULL,
ambient_light decimal(5,1) NOT NULL,
temperature decimal(3,1) NOT NULL,
date_read datetime DEFAULT current_timestamp
);

 -- Create sensor_to_readings table --

CREATE TABLE sensor_to_readings (
sensor_id int NOT NULL,
reading_id int NOT NULL,
FOREIGN KEY (sensor_id) REFERENCES sensor(sensor_id),
FOREIGN KEY (reading_id) REFERENCES readings(reading_id)
);



