package main.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The controller class is using libraries from Spring
 * to response information requirements from user interfaces.
 *
 * @author Ziru Hang
 * @version 1.0
 * @since 02-12-2020
 */
@Controller
@RequestMapping(value = "/weather_station_java_api")
public class APIController {
    @Autowired
    private SensorsRepository sensorsRepository;

    @Autowired
    private ReadingsRepository readingsRepository;

    private final String auth_code = "saxion_group_4";

    /**
     * List all the information saved in the table "sensor" in the database.
     *
     * @return Iterable<Sensor> This returns an iterable list of sensors' information. The output will automatically in json style.
     */
    @GetMapping(path = "/all_sensor")
    @ResponseBody
    public Iterable<Sensor> getAllSensors() {
        return sensorsRepository.findAll();
    }

    /**
     * List the location column saved in the table "sensor" in the database.
     *
     * @return String This returns a string listing of sensors' locations and its name in json style when the requirement is valid.
     */
    @GetMapping(path = "/all_sensor_location")
    @ResponseBody
    public String getAllLocation(@RequestParam String key) {
        if (key.equals(auth_code)) {
            StringBuffer result = new StringBuffer();

            result.append("{\"list\":[\n");
            sensorsRepository.findAll().forEach(s -> {
                result.append("{\n\"sensor_id\": \"" + s.getId() + "\",\n");
                result.append("\n\"location\": \"" + s.getLocation() + "\"\n},\n");
            });
            result.deleteCharAt(result.length() - 2);
            result.append("]\n}");

            return result.toString();
        } else
            return "invalid key";
    }

    /**
     * List all the information saved in the table "readings" in the database.
     *
     * @param key This is get from the path value to verify whether the user has permission to require information.
     * @return Iterable<Readings> This returns an iterable list of readings' information. The output will automatically in json style.
     */
    @GetMapping(path = "/all_readings")
    @ResponseBody
    public Iterable<Readings> getAllReadings(@RequestParam String key) {
        if (key.equals(auth_code))
            return readingsRepository.findAll();
        return null;
    }

    /**
     * List the all the information of the newest records saved in the table "sensor" in the database.
     *
     * @param key This is get from the path value to verify whether the user has permission to require information.
     * @return String This returns a string listing of sensors' locations and its name in json style when the requirement is valid.
     */
    @GetMapping(path = "/now_sensor") // request time + last readings for each sensor
    @ResponseBody
    public String getReadingsByTime(@RequestParam String key) {
        if (key.equals(auth_code)) {
            StringBuffer result = new StringBuffer();
            result.append("{\n\t\"time_requested\":\"");
            result.append(new Date() + "\",\n");
            result.append("\"list\": [\n");
            var allReadings = readingsRepository.findAll();
            for (int numOfSensors = 1; numOfSensors <= 4; numOfSensors++) {
                var readingList = new ArrayList<Readings>();
                for (Readings r : allReadings) {
                    if (r.getSensor_id() == numOfSensors) {
                        readingList.add(r);
                    }
                }
                readingList.sort(new NewestReadingComparator().reversed());
                var newestReading = readingList.get(0);
                var allSensor = sensorsRepository.findAll();
                String location = "unknown place";
                for (Sensor s : allSensor) {
                    if (s.getId() == newestReading.getSensor_id())
                        location = s.getLocation();
                }
                result.append("{\n");
                result.append("\"location\": \"" + location + "\",\n");
                result.append("\"time\": \"" + newestReading.getDate().getTime()+ "\",\n");
                result.append("\"temperature\": " + newestReading.getTemperature() + ",\n");
                result.append("\"ambient_light\": " + newestReading.getAmbient_light() + ",\n");
                result.append("\"b_pressure\": " + newestReading.getA_pressure() + "\n");
                result.append("},");
            }
            result.deleteCharAt(result.length() - 1);
            result.append("\n]\n}");
            return result.toString();
        } else
            return "invalid key";
    }

    /**
     * List the all the information of each record saved in the table "reading" in the database for a specific sensor.
     *
     * @param id  The id number of the sensor which is selected by user.
     * @param key This is get from the path value to verify whether the user has permission to require information.
     * @return String This returns a string listing required information in json style when the requirement is valid.
     */
    @GetMapping(path = "/reading_from_id_full")
    @ResponseBody
    public String getReadingsBySensor(@RequestParam int id, String key) {
        if (key.equals(auth_code)) {
            StringBuffer result = new StringBuffer();
            result.append(headerForGetReadingsById(id));

            var readings = readingsRepository.findAll();
            for (Readings r : readings) {
                if (r.getSensor_id() == id) {
                    result.append(fullInfoOfAReading(r));
                }
            }
            result.deleteCharAt(result.length() - 1);
            result.append("\n]\n}");
            return result.toString();
        } else
            return "invalid key";
    }

    /**
     * List the values of ambient light with date of each record saved in the table "reading" in the database for a specific sensor.
     *
     * @param id  The id number of the sensor which is selected by user.
     * @param key This is get from the path value to verify whether the user has permission to require information.
     * @return String This returns a string listing required information in json style when the requirement is valid.
     */
    @GetMapping(path = "/reading_from_id_ambient_light")
    @ResponseBody
    public String getReadingsBySensorForAmbient(@RequestParam int id, String key) {
        if (key.equals(auth_code)) {
            StringBuffer result = new StringBuffer();
            result.append(headerForGetReadingsById(id));

            var readings = readingsRepository.findAll();
            for (Readings r : readings) {
                if (r.getSensor_id() == id) {
                    result.append(ambientLightoOfAReading(r));
                }
            }
            result.deleteCharAt(result.length() - 1);
            result.append("\n]\n}");
            return result.toString();
        } else
            return "invalid key";
    }

    /**
     * List the values of temperature with date of each record saved in the table "reading" in the database for a specific sensor.
     *
     * @param id  The id number of the sensor which is selected by user.
     * @param key This is get from the path value to verify whether the user has permission to require information.
     * @return String This returns a string listing required information in json style when the requirement is valid.
     */
    @GetMapping(path = "/reading_from_id_temperature")
    @ResponseBody
    public String getReadingsBySensorForTemperature(@RequestParam int id, String key) {
        if (key.equals(auth_code)) {
            StringBuffer result = new StringBuffer();
            result.append(headerForGetReadingsById(id));

            var readings = readingsRepository.findAll();
            for (Readings r : readings) {
                if (r.getSensor_id() == id) {
                    result.append(temperatureOfAReading(r));
                }
            }
            result.deleteCharAt(result.length() - 1);
            result.append("\n]\n}");
            return result.toString();
        } else
            return "invalid key";
    }

    /**
     * List the values of pressure with date of each record saved in the table "reading" in the database for a specific sensor.
     *
     * @param id  The id number of the sensor which is selected by user.
     * @param key This is get from the path value to verify whether the user has permission to require information.
     * @return String This returns a string listing required information in json style when the requirement is valid.
     */
    @GetMapping(path = "/reading_from_id_pressure")
    @ResponseBody
    public String getReadingsBySensorForPressure(@RequestParam int id, String key) {
        if (key.equals(auth_code)) {
            StringBuffer result = new StringBuffer();
            result.append(headerForGetReadingsById(id));

            var readings = readingsRepository.findAll();
            for (Readings r : readings) {
                if (r.getSensor_id() == id) {
                    result.append(pressureOfAReading(r));
                }
            }
            result.deleteCharAt(result.length() - 1);
            result.append("\n]\n}");
            return result.toString();
        } else
            return "invalid key";
    }

    /**
     * List the all of information of each record saved in the table "reading" in the database for a specific sensor.
     *
     * @param place The name of the sensor locating which is selected by user.
     * @param key   This is get from the path value to verify whether the user has permission to require information.
     * @return String This returns a string listing required information in json style when the requirement is valid.
     */
    @GetMapping(path = "/reading_from_place_full")
    @ResponseBody
    public String getReadingsBySensorByPlace(@RequestParam String place, String key) {
        if (key.equals(auth_code)) {
            AtomicInteger id = new AtomicInteger(1);
            sensorsRepository.findAll().forEach(s -> {
                if (s.getLocation().contains(place))
                    id.set(s.getId());
            });
            return getReadingsBySensor(id.get(), key);
        } else
            return "invalid key";
    }

    /**
     * List the values of ambient light with date of each record saved in the table "reading" in the database for a specific sensor.
     *
     * @param place The name of the sensor locating which is selected by user.
     * @param key   This is get from the path value to verify whether the user has permission to require information.
     * @return String This returns a string listing required information in json style when the requirement is valid.
     */
    @GetMapping(path = "/reading_from_place_ambient_light")
    @ResponseBody
    public String getReadingsBySensorForAmbientByPlace(@RequestParam String place, String key) {
        if (key.equals(auth_code)) {
            AtomicInteger id = new AtomicInteger(1);
            sensorsRepository.findAll().forEach(s -> {
                if (s.getLocation().contains(place))
                    id.set(s.getId());
            });
            return getReadingsBySensorForAmbient(id.get(), key);
        } else
            return "invalid key";
    }

    /**
     * List the values of temperature with date of each record saved in the table "reading" in the database for a specific sensor.
     *
     * @param place The name of the sensor locating which is selected by user.
     * @param key   This is get from the path value to verify whether the user has permission to require information.
     * @return String This returns a string listing required information in json style when the requirement is valid.
     */
    @GetMapping(path = "/reading_from_place_temperature")
    @ResponseBody
    public String getReadingsBySensorForTemperatureByPlace(@RequestParam String place, String key) {
        if (key.equals(auth_code)) {
            AtomicInteger id = new AtomicInteger(1);
            sensorsRepository.findAll().forEach(s -> {
                if (s.getLocation().contains(place))
                    id.set(s.getId());
            });
            return getReadingsBySensorForTemperature(id.get(), key);
        } else
            return "invalid key";
    }

    /**
     * List the values of pressure with date of each record saved in the table "reading" in the database for a specific sensor.
     *
     * @param place The name of the sensor locating which is selected by user.
     * @param key   This is get from the path value to verify whether the user has permission to require information.
     * @return String This returns a string listing required information in json style when the requirement is valid.
     */
    @GetMapping(path = "/reading_from_place_pressure")
    @ResponseBody
    public String getReadingsBySensorForPressureByPlace(@RequestParam String place, String key) {
        if (key.equals(auth_code)) {
            AtomicInteger id = new AtomicInteger(1);
            sensorsRepository.findAll().forEach(s -> {
                if (s.getLocation().contains(place))
                    id.set(s.getId());
            });
            return getReadingsBySensorForPressure(id.get(), key);
        } else
            return "invalid key";
    }

    /**
     * List all of the information of the newest records
     * saved in the table "reading" in the database for a specific sensor
     * limited by date.
     *
     * @param day number of days which is selected by user.
     * @param id  The id number of the sensor which is selected by user.
     * @param key This is get from the path value to verify whether the user has permission to require information.
     * @return String This returns a string listing required information in json style when the requirement is valid.
     */
    @GetMapping(path = "/recent_readings_1sensor_id")
    @ResponseBody
    public String getReadingsByDays(@RequestParam int day, int id, String key) {
        if (key.equals(auth_code)) {
            StringBuffer result = new StringBuffer();
            result.append(headerForGetReadingsById(id));

            var dateFrom = Calendar.getInstance();
            dateFrom.add(Calendar.DATE, -day);

            var readings = readingsRepository.findAll();
            for (Readings r : readings) {
                if (r.getSensor_id() == id && r.getDate().after(dateFrom.getTime())) {
                    result.append(fullInfoOfAReading(r));
                }
            }
            result.deleteCharAt(result.length() - 1);
            result.append("\n]\n}");
            return result.toString();
        } else
            return "invalid key";
    }

    /**
     * List all of the information of the newest records
     * saved in the table "reading" in the database for a specific sensor
     * limited by date.
     *
     * @param day   number of days which is selected by user.
     * @param place The name of the sensor locating which is selected by user.
     * @param key   This is get from the path value to verify whether the user has permission to require information.
     * @return String This returns a string listing required information in json style when the requirement is valid.
     */
    @GetMapping(path = "/recent_readings_1sensor")
    @ResponseBody
    public String getReadingsByDaysPlace(@RequestParam int day, String place, String key) {
        if (key.equals(auth_code)) {
            StringBuffer result = new StringBuffer();

            AtomicInteger id = new AtomicInteger(0);
            sensorsRepository.findAll().forEach(s -> {
                if (s.getLocation().contains(place))
                    id.set(s.getId());
            });
            result.append(headerForGetReadingsById(id.get()));

            var dateFrom = Calendar.getInstance();
            dateFrom.add(Calendar.DATE, -day);

            var readings = readingsRepository.findAll();
            for (Readings r : readings) {
                if (r.getSensor_id() == id.get() && r.getDate().after(dateFrom.getTime())) {
                    result.append(fullInfoOfAReading(r));
                }
            }
            result.deleteCharAt(result.length() - 1);
            result.append("\n]\n}");
            return result.toString();
        } else
            return "invalid key";
    }

    /**
     * List all of the information of the newest records
     * saved in the table "reading" in the database for all sensor
     * limited by date.
     *
     * @param day number of days which is selected by user.
     * @param key This is get from the path value to verify whether the user has permission to require information.
     * @return String This returns a string listing required information in json style when the requirement is valid.
     */
    @GetMapping(path = "/recent_readings")
    @ResponseBody
    public String getReadingsByDayForAllSensors(@RequestParam int day, String key) {
        if (key.equals(auth_code)) {
            StringBuffer result = new StringBuffer();
            result.append("{\n");

            var allSensor = new HashMap<Integer, String>();
            sensorsRepository.findAll().forEach(s -> {
                allSensor.put(s.getId(), s.getLocation());
            });
            result.append("\"list\":[\n");

            for (int id : allSensor.keySet()) {
//            result.append("\"location\":" + allSensor.get(id) + ",\n");
                result.append(getReadingsByDays(day, id, key));
                result.append(",\n");
            }
            result.deleteCharAt(result.length() - 1);
            result.append("]\n}");
            return result.toString();
        } else
            return "invalid key";
    }


    /**
     * Create a head in json style for required information of one specific sensor.
     *
     * @param id The id number of the sensor which is selected by user.
     * @return String This returns a string listing required information in json style when the requirement is valid.
     */
    private String headerForGetReadingsById(long id) {
        StringBuffer result = new StringBuffer();
        result.append("{\n\t\"sensor\":");

        AtomicReference<String> location = new AtomicReference<>("unknown place");
        sensorsRepository.findAll().forEach(s -> {
            if (s.getId() == id)
                location.set(s.getLocation());
        });

        result.append("\"" + location.get() + "\",\n");
        result.append("\"list\": [\n");
        return result.toString();
    }

    /**
     * Convert a reading into String on json format with its all information.
     *
     * @param r A reading record needs to be print in json style string.
     * @return String This returns a string listing required information in json style when the requirement is valid.
     */
    private String fullInfoOfAReading(Readings r) {
        StringBuffer result = new StringBuffer();
        result.append("{\n");
        result.append("\"time\": \"" + r.getDate().getTime() + "\",\n");
        result.append("\"temperature\": " + r.getTemperature() + ",\n");
        result.append("\"ambient_light\": " + r.getAmbient_light() + ",\n");
        result.append("\"b_pressure\": " + r.getA_pressure() + "\n");
        result.append("},");
        return result.toString();
    }

    /**
     * Convert a reading into String on json format including its temperature value only.
     *
     * @param r A reading record needs to be print in json style string.
     * @return String This returns a string listing required information in json style when the requirement is valid.
     */
    private String temperatureOfAReading(Readings r) {
        StringBuffer result = new StringBuffer();
        result.append("{\n");
        result.append("\"time\": \"" + r.getDate().getTime() + "\",\n");
        result.append("\"temperature\": " + r.getTemperature() + "\n");
        result.append("},");
        return result.toString();
    }

    /**
     * Convert a reading into String on json format including its ambient light value only.
     *
     * @param r A reading record needs to be print in json style string.
     * @return String This returns a string listing required information in json style when the requirement is valid.
     */
    private String ambientLightoOfAReading(Readings r) {
        StringBuffer result = new StringBuffer();
        result.append("{\n");
        result.append("\"time\": \"" + r.getDate().getTime() + "\",\n");
        result.append("\"ambient_light\": " + r.getAmbient_light() + "\n");
        result.append("},");
        return result.toString();
    }

    /**
     * Convert a reading into String on json format including its pressure value only.
     *
     * @param r A reading record needs to be print in json style string.
     * @return String This returns a string listing required information in json style when the requirement is valid.
     */
    private String pressureOfAReading(Readings r) {
        StringBuffer result = new StringBuffer();
        result.append("{\n");
        result.append("\"time\": \"" + r.getDate().getTime() + "\",\n");
        result.append("\"b_pressure\": " + r.getA_pressure() + "\n");
        result.append("},");
        return result.toString();
    }
}
