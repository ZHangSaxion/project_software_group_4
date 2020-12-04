package main.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping(value = "/weather_station_java_api")
public class APIController {
    private final String url = "jdbc:mysql://bd91kfdkf5spw0xzmzqv-mysql.services.clever-cloud.com:3306/bd91kfdkf5spw0xzmzqv";
    private final String username = "udwuiyqcaqjflruo";
    private final String password = "KKsTC3HXAALuBQp4CrUz";
//            final String url = "jdbc:mysql://localhost:3306/localdb";
//        final String username = "zhmysql";
//        final String password = "bios2AlieZ!";
    @Autowired
    private SensorsRepository sensorsRepository;
    @Autowired
    private ReadingsRepository readingsRepository;

    @GetMapping(path = "/all_sensor")
    public @ResponseBody
    Iterable<Sensor> getAllSensors() {
        return sensorsRepository.findAll();
    }

    @GetMapping(path = "/all_readings")
    public @ResponseBody
    Iterable<Readings> getAllReadings() {
        return readingsRepository.findAll();
    }


    @GetMapping(path = "/now_sensor") // request time + last readings for each sensor
    public @ResponseBody
    String getReadingsByTime() {
        StringBuffer result = new StringBuffer();
        result.append("{\n\t\"time_requested\":");
        result.append(new Date() + ",\n");
        result.append("\"list\": [\n");
//        var newReadings = readingsRepository.findNowSensor();
//        for(Readings r : newReadings){
//            result.append(r);
//        }
        try {
            Connection conn = DriverManager.getConnection(url, username, password);

            String query = "SELECT DISTINCT sensor.location, readings.date_read, readings.temperature, readings.ambient_light, readings.b_pressure\n" +
                    "FROM readings\n" +
                    "INNER JOIN sensor\n" +
                    "ON readings.sensor_id = sensor.sensor_id\n" +
                    "GROUP BY readings.sensor_id\n" +
                    "ORDER BY readings.date_read DESC";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            // iterate through the java resultset
            while (rs.next()) {
                String location = rs.getString("sensor.location");
                Date time = rs.getDate("readings.date_read");
                double temperature = rs.getDouble("readings.temperature");
                double ambient_light = rs.getDouble("readings.ambient_light");
                double b_pressure = rs.getDouble("readings.b_pressure");

                result.append("{\n");
                result.append("\"location\": \"" + location + "\",\n");
                SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
                result.append("\"time\": \"" +ft.format(time) + "\",\n");
                result.append("\"temperature\": " + temperature + ",\n");
                result.append("\"ambient_light\": " + ambient_light + ",\n");
                result.append("\"b_pressure\": " + b_pressure + ",\n");
                result.append("},\n");
            }
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.append("\n]\n}");
        return result.toString();
    }
}
