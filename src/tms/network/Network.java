package tms.network;

import tms.congestion.AveragingCongestionCalculator;
import tms.intersection.Intersection;
import tms.route.Route;
import tms.sensors.Sensor;
import tms.util.DuplicateSensorException;
import tms.util.IntersectionNotFoundException;
import tms.util.InvalidOrderException;
import tms.util.RouteNotFoundException;

import java.util.*;

public class Network {
    /**Tree map of Intersections and ID. */
    private TreeMap<String, Intersection> intersections;
    /**Tree map of routes and ID. */
    private TreeMap<String, Route> routes;
    /** time in seconds for which lights will appear yellow. */
    private int yellowTime;

    /**
     * Creates a new empty network with no intersections.
     */
    public Network() {
        this.intersections = new TreeMap<>();
        this.routes = new TreeMap<>();
    }

    /**
     * Returns the yellow time for all traffic lights in this network.
     *
     * @return traffic light yellow time (in seconds).
     */
    public int getYellowTime() {
        return yellowTime;
    }

    /**
     * Sets the time that lights appear yellow between turning from green to red
     * (in seconds) for all new traffic lights added to this network.
     *
     * Existing traffic lights should not have their yellow time changed after
     * this method is called.
     *
     * The yellow time must be at least one (1) second. If the argument provided
     * is below 1, throw an exception and do not set the yellow time.
     *
     * @param yellowTime new yellow time for all new traffic lights in network.
     * @throws IllegalArgumentException if yellowTime < 1
     */
    public void setYellowTime(int yellowTime) {
        this.yellowTime = yellowTime;
    }

    /**
     * Creates a new intersection with the given ID and adds it to this network.
     *
     * @param id identifier of the intersection to be created
     *
     * @throws IllegalArgumentException if an intersection already exists with
     *         the given ID, or if the given ID contains the colon character (:)
     *         or if the id contains only whitespace (space, newline, tab, etc.)
     *          characters
     *
     * Requires:
     * id != null
     */
    public void createIntersection(String id)
            throws IllegalArgumentException {
        if (id.trim().isEmpty() || id.contains(":") ||
                intersections.containsKey(id)) {
            throw new IllegalArgumentException("id already exit or contain" +
                    " : or whitespace");
        }
        Intersection newIntersection = new Intersection(id);
        intersections.put(id, newIntersection);
    }

    /**
     * Creates a connecting route between the two intersections with the given
     * IDs.
     * The new route should start at 'from' and end at 'to', and have a default
     * speed of 'defaultSpeed'.
     *
     * @param from         ID of origin intersection
     * @param to           ID of destination intersection
     * @param defaultSpeed speed limit of the route to create
     * @throws IntersectionNotFoundException if no intersection exists with
     *          an ID of 'from' or 'to'.
     * @throws IllegalStateException if a route already exists between the
     *         given two intersections.
     * @throws IllegalArgumentException if defaultSpeed is negative.
     */
    public void connectIntersections(String from, String to, int defaultSpeed)
            throws IntersectionNotFoundException, IllegalStateException,
            IllegalArgumentException {
        findIntersection(to).addConnection(findIntersection(from),
                defaultSpeed);
        try {
            Route newRoute = findIntersection(to).getConnection(
                    findIntersection(from));
            routes.put(from + ":" + to, newRoute);
        }
        catch (RouteNotFoundException ignore) {
        }
    }

    /**
     * Adds traffic lights to the intersection with the given ID.
     * The traffic lights will change every duration seconds and will cycle
     * in the order given by intersectionOrder, whereby each element in the
     * list represents the intersection from which each incoming route
     * originates. The yellow time will be the network's yellow time value.
     * <p>
     * If the intersection already has traffic lights, the existing lights
     * should be completely overwritten and reset, and the new duration
     * and order should be set.
     *
     * @param intersectionId    ID of intersection to add traffic lights to
     * @param duration          number of seconds between traffic light cycles
     * @param intersectionOrder list of origin intersection IDs, traffic
     *                          lights will go green in this order
     * @throws IntersectionNotFoundException if no intersection with the given
     *          ID exists.
     * @throws InvalidOrderException if the order specified is not a permutation
     *          of the intersection's incoming routes; or if order is empty.
     * @throws IllegalArgumentException      if the given duration is less
     *          than the network's yellow time plus one
     *
     * See Also:
     * Intersection.addTrafficLights(List, int, int)
     */
    public void addLights(String intersectionId, int duration,
                          List<String> intersectionOrder)
            throws IntersectionNotFoundException, InvalidOrderException,
            IllegalArgumentException {
        List<Route> routeOrder = new ArrayList<>();
        for (String demo : intersectionOrder) {
            try {
                routeOrder.add(getConnection(demo, intersectionId));
            } catch (RouteNotFoundException r) {
                throw new InvalidOrderException("route not find");
            }
        }
        findIntersection(intersectionId).addTrafficLights(routeOrder,
                yellowTime, duration);
    }

    /**
     * Adds an electronic speed sign on the route between the two given
     * intersections.
     * <p>
     * The new speed sign should have an initial displayed speed of
     * 'initialSpeed'.
     *
     * @param from         ID of origin intersection
     * @param to           ID of destination intersection
     * @param initialSpeed initial speed to be displayed on speed sign
     * @throws IntersectionNotFoundException if no intersection exists with an
     *          ID given by 'from' or 'to'.
     * @throws RouteNotFoundException if no route exists between the two given
     *         intersections.
     * @throws IllegalArgumentException if the given speed is negative.
     */
    public void addSpeedSign(String from, String to, int initialSpeed)
            throws IntersectionNotFoundException, RouteNotFoundException {
        this.getConnection(from, to).addSpeedSign(initialSpeed);
    }

    /**
     * Sets the speed limit on the route between the two given intersections.
     * Speed limits can only be changed on routes with an electronic speed sign.
     * Calling this method on a route without an electronic speed sign should
     * result in an exception.
     *
     * @param from     ID of origin intersection
     * @param to       ID of destination intersection
     * @param newLimit new speed limit
     * @throws IntersectionNotFoundException - if no intersection exists with an
     *          ID given by 'from' or 'to'
     * @throws RouteNotFoundException - if no route exists between the two given
     *          intersections
     * @throws IllegalStateException - if the specified route does not have an
     *          electronic speed sign
     */
    public void setSpeedLimit(String from, String to, int newLimit)
            throws IntersectionNotFoundException, RouteNotFoundException {
        this.getConnection(from, to).setSpeedLimit(newLimit);
    }

    /**
     * Sets the duration of each green-yellow cycle for the given intersection's
     * traffic lights.
     *
     * @param intersectionId ID of target intersection
     * @param duration       new duration of traffic lights
     * @throws IntersectionNotFoundException if no intersection exists with an
     *          ID given by 'intersectionId'
     * @throws IllegalStateException if the given intersection has no traffic
     *          lights
     * @throws IllegalArgumentException if the given duration is less than the
     *          network's yellow time plus one.
     * See Also:
     * Intersection.setLightDuration(int)
     */
    public void changeLightDuration(String intersectionId, int duration)
            throws IntersectionNotFoundException {
        findIntersection(intersectionId).setLightDuration(duration);
    }

    /**
     * Returns the route that connects the two given intersections.
     *
     * @param from origin intersection
     * @param to   ID of destination intersection
     * @return Route that connects these intersections
     * @throws IntersectionNotFoundException - if no intersection exists with an
     *          ID given by 'to'
     * @throws RouteNotFoundException - if no route exists between the two given
     *          intersections
     */
    public Route getConnection(String from, String to)
            throws IntersectionNotFoundException, RouteNotFoundException {
        Intersection intersectionFrom = findIntersection(from);
        Intersection intersectionTo = findIntersection(to);
        return intersectionTo.getConnection(intersectionFrom);
    }

    /**
     * Adds a sensor to the route between the two intersections with the given
     * IDs.
     *
     * @param from   ID of intersection at which the route originates
     * @param to     ID of intersection at which the route ends
     * @param sensor sensor instance to add to the route
     * @throws DuplicateSensorException - if a sensor already exists on the
     *          route with the same type
     * @throws IntersectionNotFoundException - if no intersection exists with an
     *          ID given by 'from' or 'to'
     * @throws RouteNotFoundException - if no route exists between the given
     *          to/from intersections
     */
    public void addSensor(String from, String to, Sensor sensor)
            throws DuplicateSensorException, IntersectionNotFoundException,
            RouteNotFoundException {
        this.getConnection(from, to).addSensor(sensor);
    }

    /**
     * Returns the congestion level on the route between the two given
     * intersections.
     *
     * @param from ID of origin intersection
     * @param to   ID of destination intersection
     * @throws IntersectionNotFoundException if no intersection exists with an
     *          ID given by 'from' or 'to'
     * @throws RouteNotFoundException if no connecting route exists between the
     *          given two intersections
     * @return congestion level (integer between 0 and 100) of connecting route.
     *
     * See Also:
     * Route.getCongestion()
     */
    public int getCongestion(String from, String to)
            throws IntersectionNotFoundException, RouteNotFoundException {
        return getConnection(from, to).getCongestion();
    }

    /**
     * Attempts to find an Intersection instance in this network with the same
     * identifier as the given 'id' string.
     *
     * @param id intersection identifier to search for
     * @return the intersection that was found (if one was found)
     * @throws IntersectionNotFoundException if no intersection could be found
     *          with the given identifier
     */
    public Intersection findIntersection(String id)
            throws IntersectionNotFoundException {
        if (! intersections.containsKey(id)) {
            throw new IntersectionNotFoundException("no intersection could be" +
                    " found with the given identifier");
        }
        return intersections.get(id);
    }

    /**
     * Creates a new connecting route in the opposite direction to an existing
     * route.
     * The newly created route should start at the intersection with the ID
     * given by 'to' and end at the intersection with the ID given by 'from'. It
     * should have the same default speed limit as the current speed limit of
     * the existing route, as returned by Route.getSpeed().
     * <p>
     * If the existing route has an electronic speed sign, then a new electronic
     * speed sign should be added to the new route with the same displayed speed
     * as the existing speed sign.
     *
     * @param from ID of intersection that the existing route starts at
     * @param to   ID of intersection that the existing route ends at
     * @throws IntersectionNotFoundException if no intersection exists with the
     *                                       ID given by 'from' or 'to'
     * @throws RouteNotFoundException        if no route currently exists between given
     *                                       two intersections
     * @throws IllegalStateException         if a route already exists in the opposite
     *                                       direction to the existing route
     */
    public void makeTwoWay(String from, String to)
            throws IntersectionNotFoundException, RouteNotFoundException {
        Intersection intersectionFrom = findIntersection(from);
        Intersection intersectionTo = findIntersection(to);
        if (routes.containsKey(to + ":" + from)) {
            throw new IllegalStateException("a route already exists in the " +
                    "opposite direction to the existing route");
        }
        Route newRouteTo = intersectionTo.getConnection(intersectionFrom);
        intersectionFrom.addConnection(intersectionTo, newRouteTo.getSpeed());
        Route newRouteFrom = intersectionFrom.getConnection(intersectionTo);
        routes.put(intersectionTo + ":" + intersectionFrom, newRouteFrom);
        if (newRouteTo.hasSpeedSign()) {
            newRouteFrom.addSpeedSign(newRouteTo.getSpeed());
        }
    }

    /**
     * Returns a new list containing all the intersections in this network.
     * Adding/removing intersections from this list should not affect the
     * network's internal list of intersections.
     *
     * @return list of all intersections in this network
     */
    public List<Intersection> getIntersections() {
        List<Intersection> copyIntersection = new ArrayList<>();
        for (Intersection demo : intersections.values()) {
            copyIntersection.add(demo);
        }
        return copyIntersection;
    }


    /**
     * Returns true if and only if this network is equal to the other given
     * network.
     * For two networks to be equal, they must have the same number of
     * intersections, and all intersections in the first network must be
     * contained in the second network, and vice versa.
     *
     * Overrides:
     * equals in class Object.
     *
     * @param obj other object to compare equality.
     * @return true if equal, false otherwise.
     */
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if(obj == null){
            return false;
        }
        if (obj instanceof Network) {
            List<String> thisIntersection = new ArrayList<>();
            List<String> otherIntersetion = new ArrayList<>();
            for (Intersection demo : this.getIntersections()) {
                thisIntersection.add(demo.toString());
            }
            for (Intersection demo : ((Network) obj).getIntersections()) {
                otherIntersetion.add(demo.toString());
            }
            Collections.sort(thisIntersection);
            Collections.sort(otherIntersetion);
            if ( ! thisIntersection.equals(otherIntersetion)) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * Returns the hash code of this network.
     * Two networks that are equal must have the same hash code.
     *
     * Overrides:
     * hashCode in class Object
     *
     * @return hash code of the network.
     */
    public int hashCode() {
        int hashCodeSum = 0;
        for (Intersection demo : this.getIntersections()) {
            hashCodeSum += demo.hashCode();
        }
        return hashCodeSum;
    }

    /**
     * Returns the string representation of this network.
     * The format of the string to return is identical to that described in
     * NetworkInitialiser.loadNetwork(String). All intersections in the network,
     * including all connecting routes with their respective sensors, should be
     * included in the returned string.
     *
     * Intersections and routes should be listed in alphabetical order, similar
     * to the way in which sensors are sorted alphabetically in Route.toString().
     *
     * Comments (lines beginning with a semicolon character ";") are not added
     * to the string representation of a network.
     *
     * See the example network save file (demo.txt) for an example of the string
     * representation of a network.
     *
     * Overrides:
     * toString in class Object
     * @return string representation of this network
     *
     * See Also:
     * NetworkInitialiser.loadNetwork(String)
     */
    @Override
    public String toString() {
        String result = "";
        result += intersections.size() + System.lineSeparator() + routes.size()
                + System.lineSeparator() + this.getYellowTime()
                + System.lineSeparator();
        for (Intersection demo : intersections.values()) {
            result += demo.toString() + System.lineSeparator();
        }
        for (Route demo : routes.values()) {
            result += demo.toString() + System.lineSeparator();
        }
        return result;
    }
}

