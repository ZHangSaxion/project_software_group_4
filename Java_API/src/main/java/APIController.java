package main.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/weather_station_java_api")
public class APIController {
    @Autowired
    private SensorsRepository sensorsRepository;

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<SensorData> getAllUsers() {
        return sensorsRepository.findAll();
    }
}
