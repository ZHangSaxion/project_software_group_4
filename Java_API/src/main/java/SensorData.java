package main.java;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
//@Table(name = "sensor")
public class SensorData {
    @Id
//    @Column(name = "sensor_id")
    private int id;

//    @Column(name = "a_b_pressure")
    private double a_pressure;

//    @Column(name = "a_ambient_light")
    private double ambient_light;

//    @Column(name = "a_temperature")
    private double temperature;

//    @Column(name = "location")
    private String location;

//    @Column(name = "date_added")
    private Date date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getA_pressure() {
        return a_pressure;
    }

    public void setA_pressure(double a_pressure) {
        this.a_pressure = a_pressure;
    }

    public double getAmbient_light() {
        return ambient_light;
    }

    public void setAmbient_light(double ambient_light) {
        this.ambient_light = ambient_light;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
