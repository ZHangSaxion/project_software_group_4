package main.java;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * An entity class to set the springboot server communicating with database table "readings" in correct way.
 *
 * @author Ziru Hang
 * @version 1.0
 * @since 02-12-2020
 */
@Entity
@Table(name = "readings")
public class Readings {
    private final SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    @Id
    @Column(name = "reading_id")
    private int id;

    @Column(name = "sensor_id")
    private int sensor_id;

    @Column(name = "b_pressure")
    private double pressure;

    @Column(name = "ambient_light")
    private double ambient_light;

    @Column(name = "temperature")
    private double temperature;

    @Column(name = "date_read")
    private Date date;

    /**
     * Getter for the reading_id column of the readings table.
     *
     * @return int The number of reading_id gotten from database.
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for the reading_id column of the readings table.
     *
     * @param id The new id of sensor to be set into database.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter for the b_pressure column of the readings table.
     *
     * @return int The recorded value of pressure gotten from database.
     */
    public double getA_pressure() {
        return pressure;
    }

    /**
     * Setter for the b_pressure column of the readings table.
     *
     * @param a_pressure The new value of pressure to be set into database.
     */
    public void setA_pressure(double a_pressure) {
        this.pressure = a_pressure;
    }

    /**
     * Getter for the ambient_light column of the readings table.
     *
     * @return int The recorded value of ambient light gotten from database.
     */
    public double getAmbient_light() {
        return ambient_light;
    }

    /**
     * Setter for the ambient_light column of the readings table.
     *
     * @param ambient_light The new value of ambient light to be set into database.
     */
    public void setAmbient_light(double ambient_light) {
        this.ambient_light = ambient_light;
    }

    /**
     * Getter for the temperature column of the readings table.
     *
     * @return int The recorded value of temperature gotten from database.
     */
    public double getTemperature() {
        return temperature;
    }

    /**
     * Setter for the temperature column of the readings table.
     *
     * @param temperature The new value of temperature to be set into database.
     */
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    /**
     * Getter for the date_read column of the readings table.
     *
     * @return Date The recorded date gotten from database.
     */
    public Date getDate() {
        return date;
    }

    /**
     * Setter for the date_read column of the readings table.
     *
     * @param date The updated date to be set into database.
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Getter for the sensor_id column of the readings table.
     *
     * @return Date The id number of the sensor gotten from database.
     */
    public int getSensor_id() {
        return sensor_id;
    }
    /**
     * Setter for the sensor_id column of the readings table.
     *
     * @param sensor_id The number of sensor's id needs to be set into database.
     */
    public void setSensor_id(int sensor_id) {
        this.sensor_id = sensor_id;
    }

    /**
     * Converting the object in json style string.
     *
     * @return String Show information in json format.
     */
    @Override
    public String toString() {
        return "{\"sensor_id\" = " + sensor_id +
                ", \"b_pressure\" = " + pressure +
                ", \"ambient_light\" = " + ambient_light +
                ", \"temperature\" = " + temperature +
                ", \"date\" = " + date.getTime() +
                '}';
    }
}
