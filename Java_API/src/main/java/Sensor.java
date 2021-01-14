package main.java;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
/**
 * An entity class to set the springboot server communicating with database table "sensor" in correct way.
 *
 * @author Ziru Hang
 * @version 1.0
 * @since 02-12-2020
 */
@Entity
@Table(name = "sensor")
public class Sensor {
    @Id
    @Column(name = "sensor_id")
    private int id;

    @Column(name = "location")
    private String location;

    @Column(name = "date_added")
    private Date date;

    /**
     * Getter for the sensor_id column of the sensor table.
     *
     * @return int The number of sensor_id gotten from database.
     */
    public int getId() {
        return id;
    }


    /**
     * Setter for the sensor_id column of the sensor table.
     *
     * @param id The new id of sensor to be set into database.
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * Getter for the location column of the sensor table.
     *
     * @return String The detail of location gotten from database.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Setter for the location column of the sensor table.
     *
     * @param location The new location of sensor to be set into database.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Getter for the date_added column of the sensor table.
     *
     * @return Date The recorded value of date gotten from database.
     */
    public Date getDate() {
        return date;
    }

    /**
     * Setter for the date_added column of the sensor table.
     *
     * @param date The new value of date to be set into database.
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Converting the object in json style string.
     *
     * @return String Show information in json format.
     */
    @Override
    public String toString() {
        return "Sensor{" +
                "\"sensor_id\" = " + id +
                ", \"location\" = '" + location + '\'' +
                ", \"date_added\" = " + date +
                '}';
    }
}
