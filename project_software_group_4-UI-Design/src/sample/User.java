package sample;

import java.util.List;

public class User {
    private static List<SensorBool> sensorBools;

    public static void setSensorBools(List<SensorBool> sensorBools) {
        User.sensorBools = sensorBools;
    }

    public static List<SensorBool> getSensorBools() {
        return sensorBools;
    }
}
