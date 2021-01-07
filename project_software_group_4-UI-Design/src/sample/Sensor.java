package sample;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Sensor {
    private int id;
    private String location;
    private String date;

    /**
     *
     * @return a unique integer id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @return the textual representation of the sensor, usually the location is in here
     */
    public String getLocation() {
        return location;
    }

    /**
     *
     * @return a LocalDateTime object that describes when the sensor was added
     */
    public LocalDateTime getDate() {
        return LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME);
    }

    /** compares two sensors to look if they are identical
     *
     * @param o the second sensor
     * @return true if sensors are identical, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var sensor = (Sensor) o;
        return sensor.id == id &&
                sensor.location.equals(location) &&
                sensor.date.equals(date);
    }

    /**
     *
     * @return returns the sensor data as it was in the original JSON format
     */
    @Override
    public String toString() {
        return '{' +
                "\"id\":" + id +
                ",\"location\":\"" + location + '\"' +
                ",\"date\":" + date +
                '}';
    }
}
