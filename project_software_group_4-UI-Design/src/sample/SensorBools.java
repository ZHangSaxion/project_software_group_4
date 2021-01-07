package sample;

public class SensorBools {
    private Sensor sensor;
    private boolean temperature, pressure, ambientLight;

    public SensorBools(Sensor sensor, boolean temperature, boolean pressure, boolean ambientLight) {
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
}
