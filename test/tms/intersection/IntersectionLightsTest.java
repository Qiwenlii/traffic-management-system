package tms.intersection;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tms.route.Route;
import tms.route.TrafficSignal;
import tms.util.InvalidOrderException;
import tms.util.RouteNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class IntersectionLightsTest {
    private List<Route> order = new ArrayList<>();
    private Route a, b, c, d;
    private Intersection e,f,g,h,i;

    @Before
    public void setUp() throws RouteNotFoundException {
        e = new Intersection("e");
        f = new Intersection("f");
        g = new Intersection("g");
        h = new Intersection("h");
        i = new Intersection("i");
        e.addConnection(f,30);
        e.addConnection(g,33);
        e.addConnection(h,43);
        e.addConnection(i,34);
        a = e.getConnection(f);
        b = e.getConnection(g);
        c = e.getConnection(h);
        d = e.getConnection(i);
    }

    @After
    public void tearDown() {
        a = null;
        b = null;
        c = null;
        d = null;
        e = null;
        f = null;
        h = null;
        i = null;
        order = null;
    }


    @Test
    public void IntersectionLightsTest() {
        order = e.getConnections();
        for (Route demo : order) {
            demo.addTrafficLight();
        }
        IntersectionLights light = new IntersectionLights(order,3,6);

        Assert.assertEquals(light.toString(),"6:f,g,h,i");
        Assert.assertTrue(order.get(0).getTrafficLight().getSignal().equals(TrafficSignal.GREEN));
    }

    @Test
    public void getYellowTimeTest() {
        order = e.getConnections();
        for (Route demo : order) {
            demo.addTrafficLight();
        }
        IntersectionLights light = new IntersectionLights(order,3,6);
        Assert.assertEquals(light.getYellowTime(), 3);
    }

    @Test
    public void setDurationTest() {
        order = e.getConnections();
        for (Route demo : order) {
            demo.addTrafficLight();
        }
        IntersectionLights light = new IntersectionLights(order, 1, 6);
        order.get(0).setSignal(TrafficSignal.YELLOW);
        light.setDuration(8);
        light.oneSecond();
        Assert.assertEquals(light.toString(), "8:f,g,h,i");
        Assert.assertEquals(order.get(0).getTrafficLight().getSignal(), TrafficSignal.GREEN);

    }

    @Test
    public void oneSecondTest() {
        order = e.getConnections();
        for (Route demo : order) {
            demo.addTrafficLight();
        }
        IntersectionLights light = new IntersectionLights(order,1,2);
        Assert.assertEquals(order.get(0).getTrafficLight().getSignal(),TrafficSignal.GREEN);
        Assert.assertEquals(order.get(1).getTrafficLight().getSignal(), TrafficSignal.RED);
        Assert.assertEquals(order.get(2).getTrafficLight().getSignal(), TrafficSignal.RED);
        Assert.assertEquals(order.get(3).getTrafficLight().getSignal(), TrafficSignal.RED);

        light.oneSecond();
        Assert.assertEquals(order.get(0).getTrafficLight().getSignal(),TrafficSignal.YELLOW);
        Assert.assertEquals(order.get(1).getTrafficLight().getSignal(), TrafficSignal.RED);
        Assert.assertEquals(order.get(2).getTrafficLight().getSignal(), TrafficSignal.RED);
        Assert.assertEquals(order.get(3).getTrafficLight().getSignal(), TrafficSignal.RED);

        light.oneSecond();
        Assert.assertEquals(order.get(0).getTrafficLight().getSignal(),TrafficSignal.RED);
        Assert.assertEquals(order.get(1).getTrafficLight().getSignal(), TrafficSignal.GREEN);
        Assert.assertEquals(order.get(2).getTrafficLight().getSignal(), TrafficSignal.RED);
        Assert.assertEquals(order.get(3).getTrafficLight().getSignal(), TrafficSignal.RED);
        light.oneSecond();
        Assert.assertEquals(order.get(0).getTrafficLight().getSignal(),TrafficSignal.RED);
        Assert.assertEquals(order.get(1).getTrafficLight().getSignal(),TrafficSignal.YELLOW);
        Assert.assertEquals(order.get(2).getTrafficLight().getSignal(), TrafficSignal.RED);
        Assert.assertEquals(order.get(3).getTrafficLight().getSignal(),TrafficSignal.RED);
        light.oneSecond();
        Assert.assertEquals(order.get(0).getTrafficLight().getSignal(),TrafficSignal.RED);
        Assert.assertEquals(order.get(1).getTrafficLight().getSignal(),TrafficSignal.RED);
        Assert.assertEquals(order.get(2).getTrafficLight().getSignal(), TrafficSignal.GREEN);
        Assert.assertEquals(order.get(3).getTrafficLight().getSignal(),TrafficSignal.RED);
        light.oneSecond();
        Assert.assertEquals(order.get(0).getTrafficLight().getSignal(),TrafficSignal.RED);
        Assert.assertEquals(order.get(1).getTrafficLight().getSignal(),TrafficSignal.RED);
        Assert.assertEquals(order.get(2).getTrafficLight().getSignal(), TrafficSignal.YELLOW);
        Assert.assertEquals(order.get(3).getTrafficLight().getSignal(),TrafficSignal.RED);
        light.oneSecond();
        Assert.assertEquals(order.get(0).getTrafficLight().getSignal(),TrafficSignal.RED);
        Assert.assertEquals(order.get(1).getTrafficLight().getSignal(),TrafficSignal.RED);
        Assert.assertEquals(order.get(2).getTrafficLight().getSignal(), TrafficSignal.RED);
        Assert.assertEquals(order.get(3).getTrafficLight().getSignal(),TrafficSignal.GREEN);
        light.oneSecond();
        Assert.assertEquals(order.get(0).getTrafficLight().getSignal(),TrafficSignal.RED);
        Assert.assertEquals(order.get(1).getTrafficLight().getSignal(),TrafficSignal.RED);
        Assert.assertEquals(order.get(2).getTrafficLight().getSignal(), TrafficSignal.RED);
        Assert.assertEquals(order.get(3).getTrafficLight().getSignal(),TrafficSignal.YELLOW);
        light.oneSecond();
        Assert.assertEquals(order.get(0).getTrafficLight().getSignal(),TrafficSignal.GREEN);
        Assert.assertEquals(order.get(1).getTrafficLight().getSignal(),TrafficSignal.RED);
        Assert.assertEquals(order.get(2).getTrafficLight().getSignal(), TrafficSignal.RED);
        Assert.assertEquals(order.get(3).getTrafficLight().getSignal(),TrafficSignal.RED);

    }

    @Test
    public void toStringTest() {
        order = e.getConnections();
        for (Route demo : order) {
            demo.addTrafficLight();
        }
        IntersectionLights light = new IntersectionLights(order,3,6);
        Assert.assertEquals(light.toString(),"6:f,g,h,i");
    }

}
