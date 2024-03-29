package main.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
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
    private final SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

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
     * @param key This is get from the path value to verify whether the user has permission to require information.
     * @return String This returns a string listing of sensors' locations and its name in json style when the requirement is valid.
     */
    @GetMapping(path = "/all_location")
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
     * List the all the information of the newest records saved in the table "sensor" in the database.
     *
     * @param key This is get from the path value to verify whether the user has permission to require information.
     * @return String This returns a string listing of sensors' locations and its name in json style when the requirement is valid.
     */
    @GetMapping(path = "/newest") // request time + last readings for each sensor
    @ResponseBody
    public String getReadingsByTime(@RequestParam String key) {
        if (key.equals(auth_code)) {
            StringBuffer result = new StringBuffer();
            result.append("{\n\t\"time_requested\":\"");
            result.append(new Date() + "\",\n");
            result.append("\"list\": [\n");
            var allReadings = readingsRepository.findAll();
            var allSensor = sensorsRepository.findAll();
            Set<Integer> ids = new HashSet<>();
            for(Sensor s: allSensor){
                ids.add(s.getId());
            }
            for (var id : ids) {
                var readingList = new ArrayList<Readings>();
                for (Readings r : allReadings) {
                    if (r.getSensor_id() == id) {
                        readingList.add(r);
                    }
                }
                readingList.sort(new NewestReadingComparator().reversed());
                var newestReading = readingList.get(0);

                String location = "unknown place";
                for (Sensor s : allSensor) {
                    if (s.getId() == newestReading.getSensor_id())
                        location = s.getLocation();
                }
                result.append("{\n");
                result.append("\"location\": \"" + location + "\",\n");
                result.append("\"date\": \"" + ft.format(newestReading.getDate()) + "\",\n");
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
     * @param req_type The type of requirement, 0 for full detail, 1 for ambient light, 2 for temperature, and 3 for pressure
     * @param day_from The number of days between the begging date till today.
     * @param day_to   The number of days between the ending date till today.
     * @param id       The id number of the sensor which is selected by user.
     * @param key      This is get from the path value to verify whether the user has permission to require information.
     * @return String This returns a string listing required information in json style when the requirement is valid.
     */
    @GetMapping(path = "/readings")
    @ResponseBody
    public String getReadings(@RequestParam int req_type, int day_from, int day_to, int id, String key) {
        if (key.equals(auth_code)) {
            var dateFrom = Calendar.getInstance();
            dateFrom.add(Calendar.DATE, -day_from);
            var dateTo = Calendar.getInstance();
            dateFrom.add(Calendar.DATE, -day_to);

            switch (req_type) {
                case 0:
                    return getReadingsBySensor(dateFrom, dateTo, id, key);
                case 1:
                    return getReadingsBySensorForAmbient(dateFrom, dateTo, id, key);
                case 2:
                    return getReadingsBySensorForTemperature(dateFrom, dateTo, id, key);
                case 3:
                    return getReadingsBySensorForPressure(dateFrom, dateTo, id, key);
                default:
                    return "Wrong requirement type id. Please use an integer from 0 to 3.";
            }
        }
        return "invalid key";
    }

    /**
     * List the all the information of each record saved in the table "reading" in the database for a specific sensor.
     *
     * @param dateFrom A calendar of the begging date.
     * @param dateTo   A calendar of the ending date.
     * @param id  The id number of the sensor which is selected by user.
     * @param key This is get from the path value to verify whether the user has permission to require information.
     * @return String This returns a string listing required information in json style when the requirement is valid.
     */
    public String getReadingsBySensor(Calendar dateFrom, Calendar dateTo, int id, String key) {
        if (key.equals(auth_code)) {
            if (id == -1) {
                return getAllReadings(dateFrom,dateTo);
            } else {
                StringBuffer result = new StringBuffer();
                result.append("{\n");
                result.append("\"list\":[\n");

                var readings = readingsRepository.findAll();
                for (Readings r : readings) {
                    if (r.getSensor_id() == id && r.getDate().after(dateFrom.getTime()) && r.getDate().before(dateTo.getTime())) {
                        result.append(fullInfoOfAReading(r));
                    }
                }
                result.deleteCharAt(result.length() - 1);
                result.append("\n]\n}");
                return result.toString();
            }
        } else
            return "invalid key";
    }

    /**
     * List the values of ambient light with date of each record saved in the table "reading" in the database for a specific sensor.
     *
     * @param dateFrom A calendar of the begging date.
     * @param dateTo   A calendar of the ending date.
     * @param id  The id number of the sensor which is selected by user.
     * @param key This is get from the path value to verify whether the user has permission to require information.
     * @return String This returns a string listing required information in json style when the requirement is valid.
     */
    public String getReadingsBySensorForAmbient(Calendar dateFrom, Calendar dateTo, int id, String key) {
        if (key.equals(auth_code)) {
            if (id == -1) {
                return getAllReadingsAmbient(dateFrom,dateTo);
            } else {
                StringBuffer result = new StringBuffer();
                result.append(headerForGetReadingsById(id));

                var readings = readingsRepository.findAll();
                for (Readings r : readings) {
                    if (r.getSensor_id() == id && r.getDate().after(dateFrom.getTime()) && r.getDate().before(dateTo.getTime())) {
                        result.append(ambientLightoOfAReading(r));
                    }
                }
                result.deleteCharAt(result.length() - 1);
                result.append("\n]\n}");
                return result.toString();
            }
        } else
            return "invalid key";
    }

    /**
     * List the values of temperature with date of each record saved in the table "reading" in the database for a specific sensor.
     *
     * @param dateFrom A calendar of the begging date.
     * @param dateTo   A calendar of the ending date.
     * @param id  The id number of the sensor which is selected by user.
     * @param key This is get from the path value to verify whether the user has permission to require information.
     * @return String This returns a string listing required information in json style when the requirement is valid.
     */
    public String getReadingsBySensorForTemperature(Calendar dateFrom, Calendar dateTo, int id, String key) {
        if (key.equals(auth_code)) {
            if (id == -1) {
                return getAllReadingsTemperature(dateFrom,dateTo);
            } else {
                StringBuffer result = new StringBuffer();
                result.append(headerForGetReadingsById(id));

                var readings = readingsRepository.findAll();
                for (Readings r : readings) {
                    if (r.getSensor_id() == id && r.getDate().after(dateFrom.getTime()) && r.getDate().before(dateTo.getTime())) {
                        result.append(temperatureOfAReading(r));
                    }
                }
                result.deleteCharAt(result.length() - 1);
                result.append("\n]\n}");
                return result.toString();
            }
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
    public String getReadingsBySensorForPressure(Calendar dateFrom, Calendar dateTo, int id, String key) {
        if (key.equals(auth_code)) {
            if (id == -1) {
                return getAllReadingsPressure(dateFrom,dateTo);
            } else {
                StringBuffer result = new StringBuffer();
                result.append(headerForGetReadingsById(id));

                var readings = readingsRepository.findAll();
                for (Readings r : readings) {
                    if (r.getSensor_id() == id && r.getDate().after(dateFrom.getTime()) && r.getDate().before(dateTo.getTime())) {
                        result.append(pressureOfAReading(r));
                    }
                }
                result.deleteCharAt(result.length() - 1);
                result.append("\n]\n}");
                return result.toString();
            }
        } else
            return "invalid key";
    }

    /**
     * List all kinds of the averages for each column saved in the table "reading" in the database
     * limited by date.
     *
     * @param dateFrom A calendar of the begging date.
     * @param dateTo   A calendar of the ending date.
     * @param id       The id number of the sensor which is selected by user, -1 means all sensors.
     * @param key      This is get from the path value to verify whether the user has permission to require information.
     * @return String This returns a string listing required information in json style when the requirement is valid.
     */
    @GetMapping(path = "/average")
    @ResponseBody
    public String getAverage(@RequestParam Calendar dateFrom, Calendar dateTo, int id, String key) {
        if (key.equals(auth_code)) {
            if (id == -1) {
                return getAllAverage(dateFrom, dateTo);
            } else {
                StringBuffer result = new StringBuffer();
                result.append("[");

                var readings = readingsRepository.findAll();
                double sum_temperature = 0;
                double sum_ambient = 0;
                double sum_pressure = 0;
                int count = 0;
                for (Readings r : readings) {
                    if (r.getSensor_id() == id && r.getDate().after(dateFrom.getTime()) && r.getDate().before(dateTo.getTime())) {
                        sum_ambient += r.getAmbient_light();
                        sum_temperature += r.getTemperature();
                        sum_pressure += r.getA_pressure();
                        count++;
                    }
                }
                result.append("{\"sensor_id\": " + id + ", \"ambient_light\": " + sum_ambient / count + ", "
                        + "\"temperature\": " + sum_temperature / count + ", "
                        + "\"b_pressure\": " + sum_pressure / count
                        + "}");
                result.append("\n]");
                return result.toString();
            }
        }
        return "invalid key";
    }

    /**
     * List all kinds of the averages for each column saved in the table "reading" in the database
     * limited by date.
     *
     * @param dateFrom A calendar of the begging date.
     * @param dateTo   A calendar of the ending date.
     * @return String This returns a string listing required information in json style when the requirement is valid.
     */
    public String getAllAverage(@RequestParam Calendar dateFrom, Calendar dateTo) {

        StringBuffer result = new StringBuffer();
        result.append("[");

        var readings = readingsRepository.findAll();

        Set<Integer> ids = new HashSet<Integer>();
        for (Readings r : readings) {
            ids.add(r.getSensor_id());
        }

        for (int id : ids) {
            double sum_temperature = 0;
            double sum_ambient = 0;
            double sum_pressure = 0;
            int count = 0;
            for (Readings r : readings) {
                if (r.getSensor_id() == id && r.getDate().after(dateFrom.getTime()) && r.getDate().before(dateTo.getTime())) {
                    sum_ambient += r.getAmbient_light();
                    sum_temperature += r.getTemperature();
                    sum_pressure += r.getA_pressure();
                    count++;
                }
            }
            result.append("{\"sensor_id\": " + id + ", \"ambient_light\": " + sum_ambient / count + ", "
                    + "\"temperature\": " + sum_temperature / count + ", "
                    + "\"b_pressure\": " + sum_pressure / count
                    + "},");
        }
        result.deleteCharAt(result.length() - 1);
        result.append("\n]");
        return result.toString();

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
        result.append("\"sensor_id\": \"" + r.getSensor_id() + "\",\n");
        result.append("\"date\": \"" + ft.format(r.getDate()) + "\",\n");
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
        result.append("\"sensor_id\": \"" + r.getSensor_id() + "\",\n");
        result.append("\"date\": \"" + ft.format(r.getDate()) + "\",\n");
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
        result.append("\"sensor_id\": \"" + r.getSensor_id() + "\",\n");
        result.append("\"date\": \"" + ft.format(r.getDate()) + "\",\n");
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
        result.append("\"sensor_id\": \"" + r.getSensor_id() + "\",\n");
        result.append("\"date\": \"" + ft.format(r.getDate()) + "\",\n");
        result.append("\"b_pressure\": " + r.getA_pressure() + "\n");
        result.append("},");
        return result.toString();
    }


    /**
     * List all the information saved in the table "readings" in the database.
     *
     * @param dateFrom A calendar of the begging date.
     * @param dateTo   A calendar of the ending date.
     * @return String The list of readings' information. The output will automatically in json style.
     */
    public String getAllReadings(Calendar dateFrom, Calendar dateTo) {
        StringBuffer result = new StringBuffer();
        result.append("[\n");

        var allR = readingsRepository.findAll();

        boolean hasFound = false;
        for(Readings r: allR){
            if(r.getDate().after(dateFrom.getTime()) && r.getDate().before(dateTo.getTime())){
                hasFound = true;
                result.append("{\n");
                result.append("\"sensor_id\": \"" + r.getSensor_id() + "\",\n");
                result.append("\"date\": \"" + ft.format(r.getDate()) + "\",\n");
                result.append("\"temperature\": " + r.getTemperature() + ",\n");
                result.append("\"ambient_light\": " + r.getAmbient_light() + ",\n");
                result.append("\"b_pressure\": " + r.getA_pressure() + "\n");
                result.append("},");
            }
        }
        if (!hasFound){
            result.append("No record is found!");
        }
        result.deleteCharAt(result.length() - 1);
        result.append("]\n");
        return result.toString();
    }


    /**
     * List ambient light values saved in the table "readings" in the database for all the readings.
     *
     * @param dateFrom A calendar of the begging date.
     * @param dateTo   A calendar of the ending date.
     * @return String The result of all the ambient light records.
     */
    public String getAllReadingsAmbient(Calendar dateFrom, Calendar dateTo) {
        var allR = readingsRepository.findAll();
        StringBuffer result = new StringBuffer();
        result.append("[");

        boolean hasFound = false;
        for(Readings r: allR){
            if(r.getDate().after(dateFrom.getTime()) && r.getDate().before(dateTo.getTime())){
                hasFound = true;
                result.append(ambientLightoOfAReading(r));
            }
        }
        if (!hasFound){
            result.append("No record is found!");
        }
        result.deleteCharAt(result.length() - 1);
        result.append("]");
        return result.toString();
    }

    /**
     * List temperature values saved in the table "readings" in the database for all the readings.
     *
     * @param dateFrom A calendar of the begging date.
     * @param dateTo   A calendar of the ending date.
     * @return String The result of all the temperature records.
     */
    public String getAllReadingsTemperature(Calendar dateFrom, Calendar dateTo) {
        var allR = readingsRepository.findAll();
        StringBuffer result = new StringBuffer();
        result.append("[");

        boolean hasFound = false;
        for(Readings r: allR){
            if(r.getDate().after(dateFrom.getTime()) && r.getDate().before(dateTo.getTime())){
                hasFound = true;
                result.append(temperatureOfAReading(r));
            }
        }
        if (!hasFound){
            result.append("No record is found!");
        }
        result.deleteCharAt(result.length() - 1);
        result.append("]");
        return result.toString();
    }

    /**
     * List pressure values saved in the table "readings" in the database for all the readings.
     *
     * @param dateFrom A calendar of the begging date.
     * @param dateTo   A calendar of the ending date.
     * @return String The result of all the pressure records.
     */
    public String getAllReadingsPressure(Calendar dateFrom, Calendar dateTo) {
        var allR = readingsRepository.findAll();
        StringBuffer result = new StringBuffer();
        result.append("[");

        boolean hasFound = false;
        for(Readings r: allR){
            if(r.getDate().after(dateFrom.getTime()) && r.getDate().before(dateTo.getTime())){
                hasFound = true;
                result.append(pressureOfAReading(r));
            }
        }
        if (!hasFound){
            result.append("No record is found!");
        }
        result.deleteCharAt(result.length() - 1);
        result.append("]");
        return result.toString();
    }
}
