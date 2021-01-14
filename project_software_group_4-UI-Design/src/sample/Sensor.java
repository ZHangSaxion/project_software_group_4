package sample;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Sensor {
    /** a unique sensor id */
    private int sensor_id;

    /** the name/location of the sensor */
    private String location;

    /** the date the sensor was added to the database */
    private String date;

    /**
     * @return a unique integer id
     */
    public int getId() {
        return sensor_id;
    }

    /**
     * @return the textual representation of the sensor, usually the location is in here
     */
    public String getLocation() {
        return location;
    }

    /**
     * @return a LocalDateTime object that describes when the sensor was added
     */
    public LocalDateTime getDate() {
        return LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME);
    }

    /**
     * compares two sensors to look if they are identical
     *
     * @param o the second sensor
     * @return true if sensors are identical, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var sensor = (Sensor) o;
        return sensor.sensor_id == sensor_id &&
                sensor.location.equals(location) &&
                sensor.date.equals(date);
    }

    /**
     * @return returns the sensor data as it was in the original JSON format
     */
    @Override
    public String toString() {
        return '{' +
                "\"id\":" + sensor_id +
                ",\"location\":\"" + location + '\"' +
                ",\"date\":" + date +
                '}';
    }
}
