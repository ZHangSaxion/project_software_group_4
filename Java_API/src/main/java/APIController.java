package main.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
            result.append("\"b_pressure\": " + newestReading.getA_pressure() + ",\n");
            result.append("},\n");
        }
        result.append("\n]\n}");
        return result.toString();
    }

    @GetMapping(path = "/reading_from_id_full/{id}")
    public @ResponseBody
    String getReadingsBySensor(@PathVariable(name = "id") long id) {
        StringBuffer result = new StringBuffer();
        result.append(headerForGetReadingsById(id));

        var readings = readingsRepository.findAll();
        for (Readings r : readings) {
            if (r.getSensor_id() == id) {
                result.append("{\n");
                result.append("\"time\": \"" + ft.format(r.getDate()) + "\",\n");
                result.append("\"temperature\": " + r.getTemperature() + ",\n");
                result.append("\"ambient_light\": " + r.getAmbient_light() + ",\n");
                result.append("\"b_pressure\": " + r.getA_pressure() + ",\n");
                result.append("}\n");
            }
        }

        result.append("\n]\n}");
        return result.toString();
    }

    @GetMapping(path = "/reading_from_id_ambient_light/{id}")
    public @ResponseBody
    String getReadingsBySensorForAmbient(@PathVariable(name = "id") long id) {
        StringBuffer result = new StringBuffer();
        result.append(headerForGetReadingsById(id));

        var readings = readingsRepository.findAll();
        for (Readings r : readings) {
            if (r.getSensor_id() == id) {
                result.append("{\n");
                result.append("\"time\": \"" + ft.format(r.getDate()) + "\",\n");
                result.append("\"ambient_light\": " + r.getAmbient_light() + ",\n");
                result.append("}\n");
            }
        }

        result.append("\n]\n}");
        return result.toString();
    }

    @GetMapping(path = "/reading_from_id_temperature/{id}")
    public @ResponseBody
    String getReadingsBySensorForTemperature(@PathVariable(name = "id") long id) {
        StringBuffer result = new StringBuffer();
        result.append(headerForGetReadingsById(id));

        var readings = readingsRepository.findAll();
        for (Readings r : readings) {
            if (r.getSensor_id() == id) {
                result.append("{\n");
                result.append("\"time\": \"" + ft.format(r.getDate()) + "\",\n");
                result.append("\"temperature\": " + r.getTemperature() + ",\n");
                result.append("}\n");
            }
        }

        result.append("\n]\n}");
        return result.toString();
    }

    @GetMapping(path = "/reading_from_id_pressure/{id}")
    public @ResponseBody
    String getReadingsBySensorForPressure(@PathVariable(name = "id") long id) {
        StringBuffer result = new StringBuffer();
        result.append(headerForGetReadingsById(id));

        var readings = readingsRepository.findAll();
        for (Readings r : readings) {
            if (r.getSensor_id() == id) {
                result.append("{\n");
                result.append("\"time\": \"" + ft.format(r.getDate()) + "\",\n");
                result.append("\"b_pressure\": " + r.getA_pressure() + ",\n");
                result.append("}\n");
            }
        }

        result.append("\n]\n}");
        return result.toString();
    }


    @GetMapping(path = "/reading_from_place_full/{place}")
    public @ResponseBody
    String getReadingsBySensorByPlace(@PathVariable(name = "place") String place) {
        AtomicInteger id = new AtomicInteger(1);
        sensorsRepository.findAll().forEach(s -> {
            if (s.getLocation().equals(place))
                id.set(s.getId());
        });
        return getReadingsBySensor(id.get());
    }

    @GetMapping(path = "/reading_from_place_ambient_light/{place}")
    public @ResponseBody
    String getReadingsBySensorForAmbientByPlace(@PathVariable(name = "place") String place) {
        AtomicInteger id = new AtomicInteger(1);
        sensorsRepository.findAll().forEach(s -> {
            if (s.getLocation().equals(place))
                id.set(s.getId());
        });
        return getReadingsBySensorForAmbient(id.get());
    }

    @GetMapping(path = "/reading_from_place_temperature/{place}")
    public @ResponseBody
    String getReadingsBySensorForTemperatureByPlace(@PathVariable(name = "place") String place) {
        AtomicInteger id = new AtomicInteger(1);
        sensorsRepository.findAll().forEach(s -> {
            if (s.getLocation().equals(place))
                id.set(s.getId());
        });
        return getReadingsBySensorForTemperature(id.get());
    }

    @GetMapping(path = "/reading_from_place_pressure/{place}")
    public @ResponseBody
    String getReadingsBySensorForPressureByPlace(@PathVariable(name = "place") String place) {
        AtomicInteger id = new AtomicInteger(1);
        sensorsRepository.findAll().forEach(s -> {
            if (s.getLocation().equals(place))
                id.set(s.getId());
        });
        return getReadingsBySensorForPressure(id.get());
    }

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
}
