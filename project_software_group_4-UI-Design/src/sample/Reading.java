package sample;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Reading {
    private int id;
    private int sensor_id;
    private int ambient_light;
    private double temperature;
    private String date;
    private double a_pressure;

    /**
     *
     * @return a unique integer id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @return the unique integer id from the sensor the reading came from
     */
    public int getSensor_id() {
        return sensor_id;
    }

    /**
     *
     * @return ambient light value, values range from 0 to 255 including
     */
    public int getAmbient_light() {
        return ambient_light;
    }

    /**
     *
     * @return temperature in degrees Celsius, single decimal precision
     */
    public double getTemperature() {
        return temperature;
    }

    /**
     *
     * @return a LocalDateTime object used in other functions, formatted as ISO_DATE_TIME
     */
    public LocalDateTime getDate() {
        return LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME);
    }

    /**
     *
     * @return atmospheric pressure, single decimal precision
     */
    public double getA_pressure() {
        return a_pressure;
    }

    /** compares two readings to look if they are identical
     *
     * @param o the second reading
     * @return true if readings are identical, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        var reading = (Reading) o;
        return reading.id == id &&
                reading.sensor_id == sensor_id &&
                reading.ambient_light == ambient_light &&
                reading.temperature == temperature &&
                reading.date.equals(date) &&
                reading.a_pressure == a_pressure;
    }

    /**
     *
     * @return returns the reading as it was in the original JSON format
     */
    @Override
    public String toString() {
        return '{' +
                "\"id\":" + id +
                ",\"sensor_id\":" + sensor_id +
                ",\"ambient_light\":" + ambient_light +
                ",\"temperature\":" + temperature +
                ",\"date\":\"" + date + '\"' +
                ",\"a_pressure\":" + a_pressure +
                '}';
    }
}