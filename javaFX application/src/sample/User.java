package sample;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class User {
    /** a list of SensorBools containing the configurations for plotting */
    private static List<SensorBool> sensorBools = new ArrayList<>();

    /** begin time used for obtaining averages */
    private static LocalDateTime begin;

    /** end time used for obtaining averages */
    private static LocalDateTime end;

    /**
     * update sensor configurations
     *
     * @param sensorBools new configurations
     */
    public static void setSensorBools(List<SensorBool> sensorBools) {
        User.sensorBools = sensorBools;
    }

    /**
     * @return retrieve sensor configurations
     */
    public static List<SensorBool> getSensorBools() {
        return sensorBools;
    }

    /**
     * configure time window for use during average calculations
     *
     * @param begin begin time
     * @param end end time
     */
    public static void setTimeFrame(LocalDateTime begin, LocalDateTime end) {
        User.begin = begin;
        User.end = end;
    }

    /**
     * @return retrieve begin time
     */
    public static LocalDateTime getBegin() {
        return begin;
    }

    /**
     * @return retrieve end time
     */
    public static LocalDateTime getEnd() {
        return end;
    }
}
