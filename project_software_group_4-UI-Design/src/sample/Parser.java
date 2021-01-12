package sample;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Parser {
    private static final String baseUrl = "https://wsgroup4.herokuapp.com/weather_station_java_api/";
    private static final String key = "key=saxion_group_4";
    private static int retryCounter = 0;

    /**
     * returns a raw json string from a url
     *
     * @param url the url to get the data from
     * @return the string returned by the url or an ampty one if something went wrong
     */
    private static String getJsonFromURL(String url) {
        try {
            return new BufferedReader(new InputStreamReader(new URL(url)
                    .openConnection()
                    .getInputStream()))
                    .lines()
                    .collect(Collectors.joining());
        } catch(IOException e) {
            e.printStackTrace();
            try {
                Thread.sleep(200);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            if(retryCounter < 5) {
                retryCounter += 1;
                getJsonFromURL(url);
            }
            retryCounter = 0;
            return "";
        }
    }

    /**
     * constructs a list of the specified class objects from the json returned by the url
     *
     * @param url the url to get the json from
     * @param classOfT the type of object to construct the list with
     * @param <T> same as classOfT?
     * @return a list of objects extracted from the json
     */
    private static <T> List<T> getClassObjects(String url, Class<T> classOfT) {
        var objects = new ArrayList<T>();
        try {
            var gson = new Gson();
            var json = new JsonParser().parse(getJsonFromURL(url));
            if (json.isJsonArray()) {
                json.getAsJsonArray()
                        .forEach(jsonElement -> objects.add(gson.fromJson(jsonElement, classOfT)));
            } else if (json.getAsJsonObject().has("list")) {
                json.getAsJsonObject()
                        .get("list")
                        .getAsJsonArray()
                        .forEach(jsonElement -> objects.add(gson.fromJson(jsonElement, classOfT)));
            }
        } catch(JsonSyntaxException e) {
            e.printStackTrace();
        }
        return objects;
    }

    /**
     * returns the difference in days between LocalDateTime.now() and other
     * @param other the LocalDateTime to compare to
     * @return the amount of difference in days
     */
    private static long dayDifference(LocalDateTime other) {
        var now = LocalDateTime.now();
        return ChronoUnit.DAYS.between(other, now);
    }

    /**
     * function that returns all sensors sorted by id
     *
     * @return a list of sensors sorted by id, if none are returned the list is empty
     */
    public static List<Sensor> getSensors() {
        var url = baseUrl + "all_sensor";
        return getClassObjects(url, Sensor.class).stream()
                .sorted(Comparator.comparing(Sensor::getId))
                .collect(Collectors.toList());
    }

    /**
     * similar to getSensors() but now only constructs objects with id and location (name)
     *
     * @return a list of sensors sorted by id, only location and id are stored
     */
    public static List<Sensor> getLocations() {
        var url = baseUrl + "all_location?" + key;
        return getClassObjects(url, Sensor.class).stream()
                .sorted(Comparator.comparing(Sensor::getId))
                .collect(Collectors.toList());
    }

    /**
     * function that returns all readings between two time points
     * hint: use "LocalDateTime.now()" and concatenated functions like ".minusHours(long hours)"
     *
     * @param begin readings with a datetime before this will not be returned
     *              example: "LocalDateTime.now().minusDays(3)"
     * @param end readings with a datetime after this will not be returned
     *            example: "LocalDateTime.now().plusMinutes(1)"
     * @return returns all the stored readings with a datetime after begin and before end sorted chronologically
     */
    public static List<Reading> getReadings(LocalDateTime begin, LocalDateTime end) {
        var url = baseUrl + "readings?day_from=" +
                (dayDifference(begin) + 1) + "&day_to=" + dayDifference(end) + "&id=-1&req_type=0&" + key;
        return getClassObjects(url, Reading.class).parallelStream()
                .filter(reading -> reading.getDate().isAfter(begin) && reading.getDate().isBefore(end))
                .sorted(Comparator.comparing(Reading::getDate))
                .collect(Collectors.toList());
    }

    /**
     * similar to getReadings(LocalDateTime begin, LocalDateTime end) but limited to a single sensor
     * @param begin readings with a datetime before this will not be returned
     * @param end readings with a datetime after this will not be returned
     * @param sensorId a unique sensor id
     * @return returns all the stored readings between the time points that came from the specified sensor
     */
    public static List<Reading> getReadings(LocalDateTime begin, LocalDateTime end, int sensorId) {
        var url = baseUrl + "readings?day_from=" +
                (dayDifference(begin) + 1) + "&day_to=" + dayDifference(end) + "&id=" + sensorId + "&req_type=0&" + key;
        return getClassObjects(url, Reading.class).parallelStream()
                .filter(reading -> reading.getDate().isAfter(begin) && reading.getDate().isBefore(end))
                .sorted(Comparator.comparing(Reading::getDate))
                .collect(Collectors.toList());
    }

    /**
     * returns readings with only the temperatures between begin and end
     *
     * @param begin readings with a datetime before this will not be returned
     * @param end readings with a datetime after this will not be returned
     * @return returns readings with only temperatures and a datetime after begin and before end sorted chronologically
     */
    public static List<Reading> getTemperatures(LocalDateTime begin, LocalDateTime end) {
        var url = baseUrl + "readings?day_from=" +
                (dayDifference(begin) + 1) + "&day_to=" + dayDifference(end) + "&id=-1&req_type=2&" + key;
        return getClassObjects(url, Reading.class).parallelStream()
                .filter(temperature -> temperature.getDate().isAfter(begin) && temperature.getDate().isBefore(end))
                .sorted(Comparator.comparing(Reading::getDate))
                .collect(Collectors.toList());
    }

    /**
     * returns readings with only the temperatures between begin and end from a specific sensor
     *
     * @param begin readings with a datetime before this will not be returned
     * @param end readings with a datetime after this will not be returned
     * @param sensorId a unique sensor id
     * @return returns readings with only temperatures and a datetime after begin and before end sorted chronologically
     */
    public static List<Reading> getTemperatures(LocalDateTime begin, LocalDateTime end, int sensorId) {
        var url = baseUrl + "readings?day_from=" +
                (dayDifference(begin) + 1) + "&day_to=" + dayDifference(end) + "&id=" + sensorId + "&req_type=2&" + key;
        return getClassObjects(url, Reading.class).parallelStream()
                .filter(temperature -> temperature.getDate().isAfter(begin) && temperature.getDate().isBefore(end))
                .sorted(Comparator.comparing(Reading::getDate))
                .collect(Collectors.toList());
    }

    /**
     * returns readings with only the pressures between begin and end
     *
     * @param begin readings with a datetime before this will not be returned
     * @param end readings with a datetime after this will not be returned
     * @return returns readings with only pressures and a datetime after begin and before end sorted chronologically
     */
    public static List<Reading> getPressures(LocalDateTime begin, LocalDateTime end) {
        var url = baseUrl + "readings?day_from=" +
                (dayDifference(begin) + 1) + "&day_to=" + dayDifference(end) + "&id=-1&req_type=3&" + key;
        return getClassObjects(url, Reading.class).parallelStream()
                .filter(pressure -> pressure.getDate().isAfter(begin) && pressure.getDate().isBefore(end))
                .sorted(Comparator.comparing(Reading::getDate))
                .collect(Collectors.toList());
    }

    /**
     * returns readings with only the pressures between begin and end from a specific sensor
     *
     * @param begin readings with a datetime before this will not be returned
     * @param end readings with a datetime after this will not be returned
     * @param sensorId a unique sensor id
     * @return returns readings with only pressures and a datetime after begin and before end sorted chronologically
     */
    public static List<Reading> getPressures(LocalDateTime begin, LocalDateTime end, int sensorId) {
        var url = baseUrl + "readings?day_from=" +
                (dayDifference(begin) + 1) + "&day_to=" + dayDifference(end) + "&id=" + sensorId + "&req_type=3&" + key;
        return getClassObjects(url, Reading.class).parallelStream()
                .filter(pressure -> pressure.getDate().isAfter(begin) && pressure.getDate().isBefore(end))
                .sorted(Comparator.comparing(Reading::getDate))
                .collect(Collectors.toList());
    }

    /**
     * returns readings with only the ambient light values between begin and end
     *
     * @param begin readings with a datetime before this will not be returned
     * @param end readings with a datetime after this will not be returned
     * @return returns readings with only ambient light values and a datetime after begin and before end sorted chronologically
     */
    public static List<Reading> getAmbientLights(LocalDateTime begin, LocalDateTime end) {
        var url = baseUrl + "readings?day_from=" +
                (dayDifference(begin) + 1) + "&day_to=" + dayDifference(end) + "&id=-1&req_type=1&" + key;
        return getClassObjects(url, Reading.class).parallelStream()
                .filter(ambientLight -> ambientLight.getDate().isAfter(begin) && ambientLight.getDate().isBefore(end))
                .sorted(Comparator.comparing(Reading::getDate))
                .collect(Collectors.toList());
    }

    /**
     * returns readings with only the ambient light values between begin and end from a specific sensor
     *
     * @param begin readings with a datetime before this will not be returned
     * @param end readings with a datetime after this will not be returned
     * @param sensorId a unique sensor id
     * @return returns readings with only ambient light values and a datetime after begin and before end sorted chronologically
     */
    public static List<Reading> getAmbientLights(LocalDateTime begin, LocalDateTime end, int sensorId) {
        var url = baseUrl + "readings?day_from=" +
                (dayDifference(begin) + 1) + "&day_to=" + dayDifference(end) + "&id=" + sensorId + "&req_type=1&" + key;
        return getClassObjects(url, Reading.class).parallelStream()
                .filter(ambientLight -> ambientLight.getDate().isAfter(begin) && ambientLight.getDate().isBefore(end))
                .sorted(Comparator.comparing(Reading::getDate))
                .collect(Collectors.toList());
    }

    /**
     * returns the average values between the two timepoints sorted on sensor id
     * due to a limitation in the design it's accuracy is in days
     *
     * @param begin readings after this timepoint will be used in the averages
     * @param end readings before this timepoint will be used in the averages
     * @return a list of averages sorted on sensor id
     */
    public static List<Average> getAverages(LocalDateTime begin, LocalDateTime end) {
        var url = baseUrl + "average?day_from=" +
                (dayDifference(begin) + 1) + "&day_to=" + dayDifference(end) + "&id=-1&" + key;
        return getClassObjects(url, Average.class).stream()
                .sorted(Comparator.comparing(Average::getSensor_id))
                .collect(Collectors.toList());
    }

    /**
     * returns the average values from the specified sensor
     *
     * @param begin readings after this timepoint will be used in the averages
     * @param end readings before this timepoint will be used in the averages
     * @param sensorId the unique id of the sensor
     * @return an average from the specific sensor
     */
    public static Average getAverages(LocalDateTime begin, LocalDateTime end, int sensorId) {
        var url = baseUrl + "average?day_from=" +
                (dayDifference(begin) + 1) + "&day_to=" + dayDifference(end) + "&id=" + sensorId + '&' + key;
        return getClassObjects(url, Average.class).stream()
                .findAny()
                .orElse(new Average(0, 0, 0, 0));
    }
}
