package tms.network;

import tms.intersection.Intersection;
import tms.sensors.DemoPressurePad;
import tms.sensors.DemoSpeedCamera;
import tms.sensors.DemoVehicleCount;
import tms.sensors.Sensor;
import tms.util.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NetworkInitialiser {
    /** Delimiter used to separate individual pieces of data on a single line.*/
    public static final String LINE_INFO_SEPARATOR = ":";
    /**
     * Delimiter used to separate individual elements in a variable-length list
     * on a single line
     */
    public static final String LINE_LIST_SEPARATOR = ",";

    public NetworkInitialiser() { }


    /**
     * Loads a saved Network from the file with the given filename.
     * Network files have the following structure. Square brackets indicate that
     * the data inside them is optional. For example, a route does not
     * necessarily need a speed sign (speedSignSpeed).
     * <p>
     * See the demo network for an example (demo.txt).
     * <p>
     * ; This is a comment. It should be ignored.
     * numIntersections
     * numRoutes
     * yellowTime
     * intersectionId[:duration:sequence,of,intersection,ids]
     * ... (more intersections)
     * intersectionFromId:intersectionToId:defaultSpeed:numSensors
     * [:speedSignSpeed]
     * SENSORTYPE:threshold:list,of,data,values
     * ... (more routes and sensors)
     * <p>
     * A network file is invalid if any of the following conditions are true:
     * <p>
     * The number of intersections specified is not equal to the number of
     * intersections read from the file.
     * The number of routes specified does not match the number read from the
     * file.
     * The number of sensors specified for a route does not match the number
     * read from the line below.
     * An intersection referenced by a route does not exist.
     * An intersection has an invalid ID according to
     * Network.createIntersection(String).
     * Two or more intersections have the same identifier string.
     * Two or more routes have the same starting and ending intersections,
     * e.g. a route X???Y and another route X???Y. A route is allowed to end at its
     * starting intersection, i.e. X???X is allowed.
     * A sensor type that is not one of the three provided demo sensors.
     * A route contains sensors of the same type.
     * The traffic light yellow time is less than one (1).
     * A traffic light duration is less than the traffic light yellow time plus
     * one (1).
     * For intersections with traffic lights:
     * The traffic light order for an intersection is not a permutation of that
     * intersection's incoming routes.
     * The traffic light order for an intersection is empty.
     * Any numeric value that should be non-negative is less than zero. This
     * includes:
     * route speeds
     * speed sign speeds
     * sensor thresholds (also, cannot be zero)
     * sensor data values
     * The colon-delimited format is violated, i.e. there are more/fewer colons
     * than expected.
     * Any numeric value fails to be parsed.
     * An empty line occurs where a non-empty line is expected.
     * The file contains any more than two (2) newline characters at the end of
     * the file.
     *
     * @param filename  name of the file from which to load a network.
     * @return the Network loaded from file.
     * @throws IOException any IOExceptions encountered when reading the file
     *          are bubbled up.
     * @throws InvalidNetworkException if the file format of the given file is
     *          invalid.
     */
    public static Network loadNetwork(String filename)
            throws IOException, InvalidNetworkException {
        int totalIntersection = 0, totalRoute = 0, nowLine = 1;
        List<String[]> allLights = new ArrayList<>();
        Network network = new Network();
        try {
            BufferedReader file = new BufferedReader(new FileReader(filename));
            String line = file.readLine();
            while (line != null) {
                if (line.isEmpty()) {
                    throw new InvalidNetworkException("empty line");
                }
                if (line.charAt(0) == ';') {
                    line = file.readLine();
                    continue;
                }
                //record the intersections amount.
                else if (nowLine == 1) {
                    totalIntersection = Integer.parseInt(line);
                }
                //record the routes amount.
                else if (nowLine == 2) {
                    totalRoute = Integer.parseInt(line);
                }
                //record the yellow time.
                else if (nowLine == 3) {
                    network.setYellowTime(Integer.parseInt(line));
                    if (Integer.parseInt(line) < 1) {
                        throw new InvalidNetworkException("yellow time < 1");
                    }
                }
                // add all intersections.
                else if (nowLine - 3 <= totalIntersection) {
                    if (line.split(LINE_INFO_SEPARATOR,-1).length == 3) {
                        String[] intersectionContent = line.split(":|,", -1);
                        allLights.add(intersectionContent);
                    }
                    createIntersection(line, network);
                }
                //add all routes and sensors.
                else {
                    String[] routeContent = line.split(LINE_INFO_SEPARATOR,-1);
                    //after split, route line length is 4 or 5.
                    if (routeContent.length == 5 || routeContent.length == 4) {
                        network.connectIntersections(routeContent[0], routeContent[1],
                                Integer.parseInt(routeContent[2]));
                    }
                    // add speed sign (if the route has speed sign)
                    if (routeContent.length == 5) {
                        network.getConnection(routeContent[0],
                                routeContent[1]).addSpeedSign
                                (Integer.parseInt(routeContent[4]));
                    }
                    else if (routeContent.length != 4) {
                        throw new InvalidNetworkException("wrong Rounte" +
                                " content");
                    }
                    // if the route has sensors, add sensors.
                    if (Integer.parseInt(routeContent[3]) != 0) {
                        if (Integer.parseInt(routeContent[3]) < 0) {
                            throw new InvalidNetworkException("sensor num < 0");
                        }
                        for (int i = 0; i < Integer.parseInt(routeContent[3]);
                             i++) {
                            line = file.readLine();
                            if (line == null) {
                                throw new InvalidNetworkException("sensor is " +
                                        "empty");
                            }
                            addSensor (line, network, routeContent[0],
                                    routeContent[1]);
                        }
                    }
                }
                line = file.readLine();
                nowLine++;
            }
            // add traffic Light
            addLight(allLights, network);
            if (totalIntersection != network.getIntersections().size()) {
                throw new InvalidNetworkException("intersection num wrong");
            }
            int actualRouteNum = 0;
            for (Intersection demo : network.getIntersections()) {
                actualRouteNum += demo.getConnections().size();
            }
            if (totalRoute != actualRouteNum) {
                throw new InvalidNetworkException("file route num wrong");
            }
            if (nowLine < 4) {
                throw new InvalidNetworkException("empty file");
            }
            file.close();
        }
        catch (RouteNotFoundException | DuplicateSensorException |
                IntersectionNotFoundException | InvalidOrderException |
                IllegalStateException | IllegalArgumentException  e) {
            throw new InvalidNetworkException(e.toString());
        }
        return network;
    }

    /*
     * A method to create intersections.
     *
     * @param line String of the intersection line.
     * @param network a network of intersections connected by routes.
     * @throws InvalidNetworkException if the file format of the given file is
     *          invalid.
     */
    private static void createIntersection(String line, Network network)
            throws InvalidNetworkException {
        // this intersection has light.
        if (line.split(LINE_INFO_SEPARATOR,-1).length == 3) {
            String[] intersectionContent = line.split(":|,",-1);
            network.createIntersection(intersectionContent[0]);
        }
        else if (line.split(LINE_INFO_SEPARATOR,-1).length == 1) {
            network.createIntersection(line);
        } else {
            throw new InvalidNetworkException("wrong intersection format");
        }
    }

    /*
     * A method about add light to intersection.
     *
     * @param allLignts record all intersection light information.
     * @param network a network of intersections connected by routes.
     *
     * @throws IntersectionNotFoundException if no intersection with the given
     *           ID exists.
     * @throws InvalidOrderException if the order specified is not a permutation
     *           of the intersection's incoming routes; or if order is empty.
     */
    private static void addLight(List<String[]> allLights, Network network)
            throws IntersectionNotFoundException, InvalidOrderException,
            InvalidNetworkException {
        for (int i = 0; i < allLights.size(); i++) {
            //record each intersection light information.
            List<String> order = new ArrayList<>();
            for (int k = 2; k < allLights.get(i).length; k++) {
                order.add(allLights.get(i)[k]);
            }
            if (Integer.parseInt(allLights.get(i)[1]) < 1) {
                throw new InvalidNetworkException("duration < 1");
            }
            network.addLights(allLights.get(i)[0],
                    (Integer.parseInt(allLights.get(i)[1])), order);
        }
    }

    /*
     * A method about add sensors to route.
     *
     * @param line String of sensor information.
     * @param network a network of intersections connected by routes.
     * @param from  ID of origin intersection.
     * @param to  ID of destination intersection.
     *
     * @throws IntersectionNotFoundException if no intersection with the given
     *           ID exists.
     * @throws DuplicateSensorException if a sensor already exists on the route
     *          with the same type.
     * @throws RouteNotFoundException if no route exists between the given
     *          to/from intersections.
     * @throws InvalidNetworkException if the file format of the given file is
     *          invalid.
     */
    private static void addSensor(String line, Network network, String from,
                                  String to)
            throws IntersectionNotFoundException, DuplicateSensorException,
            RouteNotFoundException, InvalidNetworkException {
        String[] sensorContent = line.split(LINE_INFO_SEPARATOR,-1);
        if (sensorContent.length != 3) {
            throw new InvalidNetworkException("sensor format wrong");
        }
        Sensor newSensor;
        int threshold = Integer.parseInt(sensorContent[1]);
        if (threshold <= 0) {
            throw new InvalidNetworkException("threshold<=0");
        }
        String[] sensorValue = sensorContent[2].split(LINE_LIST_SEPARATOR,
                -1);
        int[] sensorData = new int[sensorValue.length];
        for (int k = 0; k < sensorValue.length; k++) {
            sensorData[k] = Integer.parseInt(sensorValue[k]);
            if(sensorData[k] < 0) {
                throw new InvalidNetworkException("sensor data < 0");
            }
        }
        if (sensorContent[0].equals("PP")) {
            newSensor = new DemoPressurePad
                    (sensorData, threshold);
        }
        else if (sensorContent[0].equals("SC")) {
            newSensor = new DemoSpeedCamera
                    (sensorData, threshold);
        }
        else if (sensorContent[0].equals("VC")) {
            newSensor = new DemoVehicleCount
                    (sensorData, threshold);
        } else {
            throw new IllegalArgumentException
                    ("invalid sensor");
        }
        network.addSensor(from, to, newSensor);
    }
}

