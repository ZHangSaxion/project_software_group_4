package main.java;

public class OneSensor {
    private String location;
    private String time;
    private double temperature;
    private double ambient_light;
    private double b_pressure;

    public OneSensor(String location, String time, double temperature, double ambient_light, double b_pressure) {
        this.location = location;
        this.time = time;
        this.temperature = temperature;
        this.ambient_light = ambient_light;
        this.b_pressure = b_pressure;
    }

    public double getB_pressure() {
        return b_pressure;
    }

    public void setB_pressure(double b_pressure) {
        this.b_pressure = b_pressure;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
