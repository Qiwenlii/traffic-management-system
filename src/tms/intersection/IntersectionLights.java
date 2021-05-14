package tms.intersection;

import tms.route.Route;
import tms.route.TrafficSignal;
import tms.util.TimedItem;
import tms.util.TimedItemManager;

import java.util.List;

public class IntersectionLights extends Object implements TimedItem {
    /** a list of incoming routes, the list cannot be empty. */
    private List<Route> connections;
    /** time in seconds for which lights will appear yellow. */
    private int yellowTime;
    /** time in seconds for which lights will appear yellow and green. */
    private int duration;
    /** record the green light index. */
    private int lightIndex = 0;
    /** record the current green time. */
    private int currentGreenTime = 0;
    /** record the current yellow time. */
    private int currentYellowTime = 0;

    /**
     * Creates a new set of traffic lights at an intersection.
     * The first route in the given list of incoming routes should have its
     * TrafficLight signal set to TrafficSignal.GREEN.
     *
     * @param connections  a list of incoming routes, the list cannot be empty
     * @param yellowTime time in seconds for which lights will appear yellow
     * @param duration time in seconds for which lights will appear yellow
     *        and green.
     * Requires:
     * connections.size() > 0 && yellowTime ≥ 1 && duration > yellowTime.
     */
    public IntersectionLights(List<Route> connections, int yellowTime,
                              int duration) {
        this.connections = connections;
        connections.get(0).setSignal(TrafficSignal.GREEN);
        this.yellowTime = yellowTime;
        this.duration = duration;
        TimedItemManager.getTimedItemManager().registerTimedItem(this);
    }

    /**
     *Returns the time in seconds for which a traffic light will appear
     *yellow when transitioning from green to red.
     *
     * @return yellow time in seconds for this set of traffic lights
     */
    public int getYellowTime() {
        return yellowTime;
    }

    /**
     * Sets a new duration of each green-yellow cycle.
     * The current progress of the lights cycle should be reset, such that
     * on the next call to oneSecond(), only one second of the new duration
     * has been elapsed for the incoming route that currently has a green light.
     *
     * @param duration the new light signal duration
     *
     * Requires:
     * duration > getYellowTime()
     */
    public void setDuration(int duration) {
        this.duration = duration;
        if (connections.get(lightIndex).getTrafficLight().getSignal()
                == TrafficSignal.YELLOW) {
            connections.get(lightIndex).getTrafficLight().setSignal
                    (TrafficSignal.GREEN);
        }
        currentGreenTime = 0;
        currentYellowTime = 0;
    }

    /**
     * Simulates one second passing and updates the state of this set of traffic
     * lights. If enough time has passed such that a full green-yellow duration
     * has elapsed, or such that the current green light should now be yellow,
     * the appropriate light signals should be changed:
     *<p>
     * When a traffic light signal has been green for 'duration - yellowTime'
     * seconds, it should be changed from green to yellow.
     * When a traffic light signal has been yellow for 'yellowTime' seconds, it
     * should be changed from yellow to red, and the next incoming route in the
     * order passed to IntersectionLights(List, int, int) should be given a
     * green light. If the end of the list of routes has been reached, simply
     * wrap around to the start of the list and repeat.
     * <p>
     * If no routes are connected to the intersection, the duration shall not
     * elapse and the call should simply return without changing anything.
     *
     * Specified by:
     * oneSecond in interface TimedItem
     */
    public void oneSecond() {
        int routeSize = connections.size();
        if (routeSize != 0) {
            // if the signal is green.
            if (connections.get(lightIndex).getTrafficLight().getSignal()
                    == TrafficSignal.GREEN) {
                currentGreenTime ++;
                if (currentGreenTime + yellowTime == duration) {
                    connections.get(lightIndex).getTrafficLight().setSignal
                            (TrafficSignal.YELLOW);
                    currentGreenTime = 0;
                }
            }
            // if the signal is yellow.
            else if (connections.get(lightIndex).getTrafficLight().getSignal()
                    == TrafficSignal.YELLOW) {
                currentYellowTime ++;
                if (currentYellowTime == yellowTime) {
                    connections.get(lightIndex).getTrafficLight().setSignal
                            (TrafficSignal.RED);
                    currentYellowTime = 0;
                    if(lightIndex == connections.size() - 1){
                        lightIndex = 0;
                    } else {
                        lightIndex ++;
                    }
                    connections.get(lightIndex).getTrafficLight().setSignal
                            (TrafficSignal.GREEN);
                }
            }
        }
    }

    /**
     * Returns the string representation of this set of IntersectionLights.
     * The format to return is "duration:list,of,intersection,ids" where
     * 'duration' is our current duration and 'list,of,intersection,ids' is a
     * comma-separated list of the IDs of all intersections that have an
     * incoming route to this set of traffic lights, in order given
     * to IntersectionLights' constructor.
     *<p>
     * For example, for a set of traffic lights with inbound routes from three
     * intersections - A, C and B - in that order, and a duration of 8 seconds,
     * return the string "8:A,C,B".
     *
     * Overrides:
     * toString in class Object
     *
     * @return formatted string representation
     */
    @Override
    public String toString() {
        String intersectionName = "";
        for (Route demo : connections) {
            intersectionName += demo.getFrom() + ",";
        }
        return duration + ":" + intersectionName.substring(0,
                intersectionName.length() - 1);
    }
}





















