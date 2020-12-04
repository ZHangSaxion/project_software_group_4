package main.java;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReadingsRepository extends CrudRepository<Readings, Long> {
//    @Query("SELECT DISTINCT sensor.location, readings.date_read, readings.temperature, readings.ambient_light, readings.b_pressure FROM readings INNER JOIN sensor ON readings.sensor_id = sensor.sensor_id GROUP BY readings.sensor_id ORDER BY readings.date_read DESC")
//    List<Readings> findNowSensor();
}