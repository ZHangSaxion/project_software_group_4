package sample;

public class SensorBool {
    /** the sensor the configuration is stored of */
    private Sensor sensor;

    /**
     * the boolean variables that specify what information has to be shown
     * of the readings that came from this sensor
     */
    private boolean temperature, pressure, ambientLight;

    /**
     * initializes a SensorBool object with a specific sensor,
     * this can possibly be useful since booleans in java default to false
     *
     * @param sensor the sensor object to initialize with
     */
    public SensorBool(Sensor sensor) {
        this.sensor = sensor;
    }

    /**
     * initializes a SensorBool object with a specific sensor alongside it's configuration
     *
     * @param sensor the sensor object to initialize with
     * @param temperature depending on the value temperatures of this sensor will be shown
     * @param pressure depending on the value pressures of this sensor will be shown
     * @param ambientLight depending on the value ambient light values of this sensor will be shown
     */
    public SensorBool(Sensor sensor, boolean temperature, boolean pressure, boolean ambientLight) {
        this.sensor = sensor;
        this.temperature = temperature;
        this.pressure = pressure;
        this.ambientLight = ambientLight;
    }

    /**
     * @return the sensor of the configuration
     */
    public Sensor getSensor() {
        return sensor;
    }

    /**
     * @param sensor the new sensor of the configuration
     */
    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    /**
     * @return whether the temperatures of this sensor have to be shown
     */
    public boolean isTemperature() {
        return temperature;
    }

    /**
     * @param temperature set whether the temperatures of this sensor have to be shown
     */
    public void setTemperature(boolean temperature) {
        this.temperature = temperature;
    }

    /**
     * @return whether the pressures of this sensor have to be shown
     */
    public boolean isPressure() {
        return pressure;
    }

    /**
     * @param pressure set whether the pressures of this sensor have to be shown
     */
    public void setPressure(boolean pressure) {
        this.pressure = pressure;
    }

    /**
     * @return whether the ambient light values of this sensor have to be shown
     */
    public boolean isAmbientLight() {
        return ambientLight;
    }

    /**
     * @param ambientLight set whether the ambient light values of this sensor have to be shown
     */
    public void setAmbientLight(boolean ambientLight) {
        this.ambientLight = ambientLight;
    }

    /**
     * @return a textual representation of a SensorBool object
     */
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
