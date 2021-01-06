package sample;

public class Readings {

    private final int id;
    private final double temp;
    private final double pressure;
    private final double light;

    public Readings (int id, double temp, double pressure, double light) {
        this.id = id;
        this.temp = temp;
        this.pressure = pressure;
        this.light = light;
    }

}
