package sample;

public class SensorBool {
    private Sensor sensor;
    private boolean temperature, pressure, ambientLight;

    public SensorBool(Sensor sensor) {
        this.sensor = sensor;
    }

    public SensorBool(Sensor sensor, boolean temperature, boolean pressure, boolean ambientLight) {
        this.sensor = sensor;
        this.temperature = temperature;
        this.pressure = pressure;
        this.ambientLight = ambientLight;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public boolean isTemperature() {
        return temperature;
    }

    public void setTemperature(boolean temperature) {
        this.temperature = temperature;
    }

    public boolean isPressure() {
        return pressure;
    }

    public void setPressure(boolean pressure) {
        this.pressure = pressure;
    }

    public boolean isAmbientLight() {
        return ambientLight;
    }

    public void setAmbientLight(boolean ambientLight) {
        this.ambientLight = ambientLight;
    }

    @Override
    public String toString() {
        return "SensorBool{" +
                "sensor=" + sensor +
                ", temperature=" + temperature +
                ", pressure=" + pressure +
                ", ambientLight=" + ambientLight +
                '}';
    }
}
