package main.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Controller
@RequestMapping(value = "/weather_station_java_api")
public class APIController {
    @Autowired
    private SensorsRepository sensorsRepository;
    @Autowired
    private ReadingsRepository readingsRepository;

    private SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @GetMapping(path = "/all_sensor")
    @ResponseBody
    public Iterable<Sensor> getAllSensors() {
        return sensorsRepository.findAll();
    }

    @GetMapping(path = "/all_sensor_location")
    @ResponseBody
    public String getAllLocation() {
        StringBuffer result = new StringBuffer();

        result.append("{\"list\":[\n");
        sensorsRepository.findAll().forEach(s -> {
            result.append("{\n\"sensor_id\": \"" + s.getId() + "\",\n");
            result.append("\n\"location\": \"" + s.getLocation() + "\"\n},\n");
        });
        result.deleteCharAt(result.length() - 2);
        result.append("]\n}");

        return result.toString();
    }

    @GetMapping(path = "/all_readings")
    @ResponseBody
    public Iterable<Readings> getAllReadings() {
        return readingsRepository.findAll();
    }


    @GetMapping(path = "/now_sensor") // request time + last readings for each sensor
    @ResponseBody
    public String getReadingsByTime() {
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
            result.append("\"time\": \"" + ft.format(newestReading.getDate()) + "\",\n");
            result.append("\"temperature\": " + newestReading.getTemperature() + ",\n");
            result.append("\"ambient_light\": " + newestReading.getAmbient_light() + ",\n");
            result.append("\"b_pressure\": " + newestReading.getA_pressure() + "\n");
            result.append("},");
        }
        result.deleteCharAt(result.length() - 1);
        result.append("\n]\n}");
        return result.toString();
    }

    @GetMapping(path = "/reading_from_id_full")
    @ResponseBody
    public String getReadingsBySensor(@RequestParam int id) {
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
    }

    @GetMapping(path = "/reading_from_id_ambient_light")
    @ResponseBody
    public String getReadingsBySensorForAmbient(@RequestParam int id) {
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
    }

    @GetMapping(path = "/reading_from_id_temperature")
    @ResponseBody
    public String getReadingsBySensorForTemperature(@RequestParam int id) {
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
    }

    @GetMapping(path = "/reading_from_id_pressure")
    @ResponseBody
    public String getReadingsBySensorForPressure(@RequestParam int id) {
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
    }


    @GetMapping(path = "/reading_from_place_full")
    @ResponseBody
    public String getReadingsBySensorByPlace(@RequestParam String place) {
        AtomicInteger id = new AtomicInteger(1);
        sensorsRepository.findAll().forEach(s -> {
            if (s.getLocation().contains(place))
                id.set(s.getId());
        });
        return getReadingsBySensor(id.get());
    }

    @GetMapping(path = "/reading_from_place_ambient_light")
    @ResponseBody
    public String getReadingsBySensorForAmbientByPlace(@RequestParam String place) {
        AtomicInteger id = new AtomicInteger(1);
        sensorsRepository.findAll().forEach(s -> {
            if (s.getLocation().contains(place))
                id.set(s.getId());
        });
        return getReadingsBySensorForAmbient(id.get());
    }

    @GetMapping(path = "/reading_from_place_temperature")
    @ResponseBody
    public String getReadingsBySensorForTemperatureByPlace(@RequestParam String place) {
        AtomicInteger id = new AtomicInteger(1);
        sensorsRepository.findAll().forEach(s -> {
            if (s.getLocation().contains(place))
                id.set(s.getId());
        });
        return getReadingsBySensorForTemperature(id.get());
    }

    @GetMapping(path = "/reading_from_place_pressure")
    @ResponseBody
    public String getReadingsBySensorForPressureByPlace(@RequestParam String place) {
        AtomicInteger id = new AtomicInteger(1);
        sensorsRepository.findAll().forEach(s -> {
            if (s.getLocation().contains(place))
                id.set(s.getId());
        });
        return getReadingsBySensorForPressure(id.get());
    }

    @GetMapping(path = "/recent_readings_1sensor_id")
    @ResponseBody
    public String getReadingsByDays(@RequestParam int day, int id){
        StringBuffer result = new StringBuffer();
        result.append(headerForGetReadingsById(id));

        var dateFrom = Calendar.getInstance();
        dateFrom.add(Calendar.DATE,-day);

        var readings = readingsRepository.findAll();
        for (Readings r : readings) {
            if (r.getSensor_id() == id && r.getDate().after(dateFrom.getTime())) {
                result.append(fullInfoOfAReading(r));
            }
        }
        result.deleteCharAt(result.length() - 1);
        result.append("\n]\n}");
        return result.toString();
    }
    @GetMapping(path = "/recent_readings_1sensor")
    @ResponseBody
    public String getReadingsByDaysPlace(@RequestParam int day, String place){
        StringBuffer result = new StringBuffer();

        AtomicInteger id = new AtomicInteger(0);
        sensorsRepository.findAll().forEach(s -> {
            if(s.getLocation().contains(place))
                id.set(s.getId());
        });
        result.append(headerForGetReadingsById(id.get()));

        var dateFrom = Calendar.getInstance();
        dateFrom.add(Calendar.DATE,-day);

        var readings = readingsRepository.findAll();
        for (Readings r : readings) {
            if (r.getSensor_id() == id.get() && r.getDate().after(dateFrom.getTime())) {
                result.append(fullInfoOfAReading(r));
            }
        }
        result.deleteCharAt(result.length() - 1);
        result.append("\n]\n}");
        return result.toString();
    }
    @GetMapping(path = "/recent_readings")
    @ResponseBody
    public String getReadingsByDayForAllSensors(@RequestParam int day){
        StringBuffer result = new StringBuffer();
        result.append("{\n");

        var allSensor = new HashMap<Integer, String>();
        sensorsRepository.findAll().forEach(s -> {
            allSensor.put(s.getId(),s.getLocation());
        });
        result.append("\"list\":[\n");

        for(int id : allSensor.keySet()) {
//            result.append("\"location\":" + allSensor.get(id) + ",\n");
            result.append(getReadingsByDays(day,id));
            result.append(",\n");
        }
        result.deleteCharAt(result.length() - 1);
        result.append("]\n}");
        return result.toString();
    }


    /**
     *
     * @param id
     * @return
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

    private String fullInfoOfAReading(Readings r){
        StringBuffer result = new StringBuffer();
        result.append("{\n");
        result.append("\"time\": \"" + ft.format(r.getDate()) + "\",\n");
        result.append("\"temperature\": " + r.getTemperature() + ",\n");
        result.append("\"ambient_light\": " + r.getAmbient_light() + ",\n");
        result.append("\"b_pressure\": " + r.getA_pressure() + "\n");
        result.append("},");
        return result.toString();
    }

    private String temperatureOfAReading(Readings r){
        StringBuffer result = new StringBuffer();
        result.append("{\n");
        result.append("\"time\": \"" + ft.format(r.getDate()) + "\",\n");
        result.append("\"temperature\": " + r.getTemperature() + "\n");
        result.append("},");
        return result.toString();
    }

    private String ambientLightoOfAReading(Readings r){
        StringBuffer result = new StringBuffer();
        result.append("{\n");
        result.append("\"time\": \"" + ft.format(r.getDate()) + "\",\n");
        result.append("\"ambient_light\": " + r.getAmbient_light() + "\n");
        result.append("},");
        return result.toString();
    }
    private String pressureOfAReading(Readings r){
        StringBuffer result = new StringBuffer();
        result.append("{\n");
        result.append("\"time\": \"" + ft.format(r.getDate()) + "\",\n");
        result.append("\"b_pressure\": " + r.getA_pressure() + "\n");
        result.append("},");
        return result.toString();
    }
}
