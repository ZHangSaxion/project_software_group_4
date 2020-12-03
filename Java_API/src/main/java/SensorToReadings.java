package main.java;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Table;
@Entity
@Table(name = "sensor_to_readings")
public class SensorToReadings {
    @Id
    @Column(name = "sensor_id")
    private int sensor_id;
    @Column(name = "reading_id")
    private int reading_id;

    public int getSensor_id() {
        return sensor_id;
    }

    public void setSensor_id(int sensor_id) {
        this.sensor_id = sensor_id;
    }

    public int getReading_id() {
        return reading_id;
    }

    public void setReading_id(int reading_id) {
        this.reading_id = reading_id;
    }
}
