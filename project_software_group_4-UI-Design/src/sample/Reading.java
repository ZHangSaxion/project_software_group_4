package sample;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Reading {
    /** the format used to convert LocalDateTime to a String and vice versa */
    private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /** a unique reading id */
    private int id;

    /** the id of the sensor the reading came from */
    private int sensor_id;

    /** the datetime formatted as a string due to the nature of the json parser */
    private String date;

    /** the ambient light value in percent */
    private double ambient_light;

    /** the temperature in degrees Celsius */
    private double temperature;

    /** the barometric pressure in hPa */
    private double b_pressure;

    /**
     * @return a unique integer id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the unique integer id from the sensor the reading came from
     */
    public int getSensor_id() {
        return sensor_id;
    }

    /**
     * @return ambient light value, values range from 0.0 to 100.0 including
     */
    public double getAmbient_light() {
        return (double) Math.round(ambient_light / 2.55 * 10) / 10;
    }

    /**
     * @return temperature in degrees Celsius, single decimal precision
     */
    public double getTemperature() {
        return temperature;
    }

    /**
     * @return a LocalDateTime object used in other functions, formatted as ISO_DATE_TIME
     */
    public LocalDateTime getDate() {
        return LocalDateTime.parse(date, format);
    }

    /**
     * function used to alter the datetime for rounding timestamps in graphs
     *
     * @param date the new datetime
     */
    public void setDate(LocalDateTime date) {
        this.date = date.format(format);
    }

    /**
     * @return atmospheric pressure, single decimal precision
     */
    public double getB_pressure() {
        return b_pressure;
    }

    /**
     * compares two readings to look if they are identical
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
                reading.b_pressure == b_pressure;
    }

    /**
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
                ",\"a_pressure\":" + b_pressure +
                '}';
    }
}
