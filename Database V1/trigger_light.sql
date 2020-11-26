-- Trigger that updates the light average -- 

-- change temporary the default delimiter in order to execute all the statements in the trigger --
DELIMITER $$
create trigger update_light after insert on sensor_to_readings for each row begin
update sensor
	set a_ambient_light= (SELECT AVG(readings.ambient_light)
	FROM sensor_to_readings, readings
	WHERE readings.reading_id = sensor_to_readings.reading_id AND sensor_to_readings.sensor_id = sensor.sensor_id)
WHERE sensor.sensor_id = new.sensor_id;
END$$
-- revert to the default delimiter -- 
DELIMITER ;


