package tms.congestion;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tms.sensors.*;

import java.util.ArrayList;
import java.util.List;
import static junit.framework.TestCase.fail;

public class AveragingCongestionCalculatorTest {
    private AveragingCongestionCalculator a;
    private DemoPressurePad pp;
    private DemoSpeedCamera sc;
    private DemoVehicleCount vc;
    private List<Sensor> sensors = new ArrayList<>();
    @Before
    public void setUp(){
        pp = new DemoPressurePad(new int[] {53, 61, 32, 77}, 90);
        vc = new DemoVehicleCount(new int[] {32, 55, 45, 80}, 67);
        sc = new DemoSpeedCamera(new int[] {37, 35, 60, 59}, 55);
        sensors.add(pp);
        sensors.add(vc);
        sensors.add(sc);
        a = new AveragingCongestionCalculator(sensors);

    }
    @After
    public void teardown() {
        pp = null;
        sc = null;
        vc = null;
        sensors= null;
        a= null;
    }

    @Test
    public void AveragingCongestionCalculatorTest() {
        try{
            AveragingCongestionCalculator congestion = new AveragingCongestionCalculator(sensors);
        } catch (Exception e){
            fail();
        }
    }
    @Test
    public void calculateCongestionTest() {
        Assert.assertEquals(a.calculateCongestion(), 48);
        pp.oneSecond();
        sc.oneSecond();
        vc.oneSecond();
        Assert.assertEquals(a.calculateCongestion(), 41);
    }

    @Test
    public void sensorIsNullTest() {
        List<Sensor> c = new ArrayList<>();
        AveragingCongestionCalculator b = new AveragingCongestionCalculator(c);
        Assert.assertEquals(b.calculateCongestion(), 0);
    }
}
