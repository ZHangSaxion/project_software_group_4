package sample;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Parser {
    private static final String readingUrl = "https://wsgroup4.herokuapp.com/weather_station_java_api/all_readings";
    private static final String sensorUrl = "https://wsgroup4.herokuapp.com/weather_station_java_api/all_sensor";
    private List<Reading> readings;
    private List<Sensor> sensors;

    /** creates a parser that gets readings and sensor data from an API through predefined URL's
     * the function updateFromURL() is already run once during object creation
     *
     */
    public Parser() {
        readings = new ArrayList<>();
        sensors = new ArrayList<>();
        updateFromURL();
    }

    /** function to update the readings and sensors from predefined URL's.
     * this function is already run once with object creation
     *
     */
    public void updateFromURL() {
        try {
            var readingStream = new BufferedReader(new InputStreamReader((new URL(readingUrl))
                    .openConnection()
                    .getInputStream()));
            var sensorStream = new BufferedReader(new InputStreamReader((new URL(sensorUrl))
                    .openConnection()
                    .getInputStream()));
            readings.clear();
            sensors.clear();
            var gson = new Gson();
            (new JsonParser())
                    .parse(readingStream.lines().collect(Collectors.joining()))
                    .getAsJsonArray()
                    .forEach(e -> readings.add(gson.fromJson(e, Reading.class)));
            (new JsonParser())
                    .parse(sensorStream.lines().collect(Collectors.joining()))
                    .getAsJsonArray()
                    .forEach(e -> sensors.add(gson.fromJson(e, Sensor.class)));
            readings = readings.stream()
                    .sorted(Comparator.comparing(Reading::getDate))
                    .collect(Collectors.toList());
            sensors = sensors.stream()
                    .sorted(Comparator.comparing(Sensor::getId))
                    .collect(Collectors.toList());
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /** function that returns all readings ever recorded
     *
     * @return returns a list with all readings recorded sorted chronologically
     */
    public List<Reading> getReadings() {
        return readings;
    }

    /** function that returns all readings between two time points
     * hint: use "LocalDateTime.now()" and concatenated functions like "minusHours(long hours)"
     * if a specific time point is needed the input could look something like:
     * "LocalDateTime.parse("2020-12-05 14:26:09", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))"
     * where the first string is the time point and the DateTimeFormatter the format of it
     *
     * @param begin readings with a datetime before this will not be returned
     *              example: "LocalDateTime.now().minusDays(3)"
     * @param end readings with a datetime after this will not be returned
     *            example: "LocalDateTime.now().plusMinutes(1)"
     * @return returns all the stored readings with a datetime after begin and before end sorted chronologically
     */
    public List<Reading> getReadings(LocalDateTime begin, LocalDateTime end) {
        return readings.stream()
                .filter(o -> o.getDate().isAfter(begin) && o.getDate().isBefore(end))
                .collect(Collectors.toList());
    }

    /** function that returns all sensors sorted by id
     *
     * @return a list of sensors sorted by id
     */
    public List<Sensor> getSensors() {
        return sensors;
    }
}
