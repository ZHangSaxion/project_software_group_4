package main.java;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "readings")
public class Readings {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getA_pressure() {
        return pressure;
    }

    public void setA_pressure(double a_pressure) {
        this.pressure = a_pressure;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getSensor_id() {
        return sensor_id;
    }

    public void setSensor_id(int sensor_id) {
        this.sensor_id = sensor_id;
    }

    @Override
    public String toString() {
        return "Readings{" +
                ", \"sensor_id\" = " + sensor_id +
                ", \"b_pressure\" = " + pressure +
                ", \"ambient_light\" = " + ambient_light +
                ", \"temperature\" = " + temperature +
                ", \"date\" = " + date +
                '}';
    }
}
