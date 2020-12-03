package main.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/weather_station_java_api")
public class APIController {
    @Autowired
    private SensorsRepository sensorsRepository;
    @Autowired
    private ReadingsRepository readingsRepository;
    @Autowired
    private SensorToReadingsRepository sensorToReadingsRepository;

    @GetMapping(path = "/all_sensor")
    public @ResponseBody Iterable<Sensor> getAllSensors() {
        return sensorsRepository.findAll();
    }

    @GetMapping(path = "/all_readings")
    public @ResponseBody Iterable<Readings> getAllReadings() {
        return readingsRepository.findAll();
    }

    @GetMapping(path = "/all_sensor_to_readings")
    public @ResponseBody Iterable<SensorToReadings> getAllSensorToReadings() {
        return sensorToReadingsRepository.findAll();
    }

    @GetMapping(path = "/now_sensor") // request time + last readings for each sensor
    public @ResponseBody String getReadingsByTime() {
        StringBuffer result = new StringBuffer();
        result.append("{\n\t\"time_requested\":");
        String sql = "SELECT DISTINCT sensor.sensor_id as location, readings.date_read as time, readings.temperature, readings.ambient_light, readings.b_pressure " +
                "FROM readings " +
                "INNER JOIN sensor_to_readings ON readings.reading_id = sensor_to_readings.reading_id " +
                "INNER JOIN sensor ON sensor.sensor_id = sensor_to_readings.sensor_id" +
                "ORDER BY date_read DESC " +
                "LIMIT BY 1";
        return result.toString();
    }
}
