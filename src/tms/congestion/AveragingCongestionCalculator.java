package tms.congestion;

import tms.sensors.Sensor;

import java.util.ArrayList;
import java.util.List;

public class AveragingCongestionCalculator {

    private List<Sensor> sensors;

    /**
     * Creates a new averaging congestion calculator for a given list of sensors
     * on a route.
     *
     * @param sensors list of sensors to use in congestion calculation
     */
    public  AveragingCongestionCalculator(List<Sensor> sensors) {
        this.sensors = sensors;
    }

    /**
     * Calculates the average congestion level, as returned by
     * Sensor.getCongestion(), of all the sensors stored by this calculator.
     * If there are no sensors stored, return 0.
     *
     * If the computed average is not an integer, it should be rounded to the
     * nearest integer before being returned.
     *
     * @return the average congestion
     */
    public int calculateCongestion() {
        int totalCongestion = 0;
        if (sensors.size() == 0) {
            return 0;
        } else {
            for (Sensor demo : sensors) {
                totalCongestion += demo.getCongestion();
            }
            return Math.round((float) totalCongestion / sensors.size());
        }
    }
}
