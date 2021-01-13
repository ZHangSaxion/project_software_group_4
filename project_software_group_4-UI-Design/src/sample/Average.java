package sample;

public class Average {
    /** a unique sensor id */
    private final int sensor_id;

    /** the temperature in degrees Celsius */
    private final double temperature;

    /** the barometric pressure in hPa */
    private final double b_pressure;

    /** the ambient light in percent */
    private final double ambient_light;

    /**
     * constructor used in AverageTable because of limitations in API
     *
     * @param sensor_id sensor_id
     * @param temperature temperature
     * @param b_pressure b_pressure
     * @param ambient_light ambient_light
     */
    public Average(int sensor_id, double temperature, double b_pressure, double ambient_light) {
        this.sensor_id = sensor_id;
        this.temperature = temperature;
        this.b_pressure = b_pressure;
        this.ambient_light = ambient_light;
    }

    /**
     * @return a unique sensor id
     */
    public int getSensor_id() {
        return sensor_id;
    }

    /**
     * @return the average temperature rounded to a single decimal
     */
    public double getTemperature() {
        return (double) Math.round(temperature * 10) / 10;
    }

    /**
     * @return the average pressure rounded to a single decimal
     */
    public double getB_pressure() {
        return (double) Math.round(b_pressure * 10) / 10;
    }

    /**
     * @return the average ambient light rounded to a single decimal
     */
    public double getAmbient_light() {
        return (double) Math.round(ambient_light * 10) / 10;
    }

    /**
     * compares two averages to look if they are identical
     *
     * @param o the second average
     * @return true if averages are identical, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var average = (Average) o;
        return average.sensor_id == sensor_id &&
                average.temperature == temperature &&
                average.b_pressure == b_pressure &&
                average.ambient_light == ambient_light;
    }

    /**
     * @return returns the average as it was in the original JSON format
     */
    @Override
    public String toString() {
        return '{' +
                "\"sensor_id\"=" + sensor_id +
                ",\"temperature\"=" + temperature +
                ",\"b_pressure\"=" + b_pressure +
                ",\"ambient_light\"=" + ambient_light +
                '}';
    }
}
