package sample;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class User {
    private static List<SensorBool> sensorBools = new ArrayList<>();
    private static LocalDateTime begin;
    private static LocalDateTime end;

    public static void setSensorBools(List<SensorBool> sensorBools) {
        User.sensorBools = sensorBools;
    }

    public static List<SensorBool> getSensorBools() {
        return sensorBools;
    }

    public static void setTimeFrame(LocalDateTime begin, LocalDateTime end) {
        User.begin = begin;
        User.end = end;
    }

    public static LocalDateTime getBegin() {
        return begin;
    }

    public static LocalDateTime getEnd() {
        return end;
    }
}
