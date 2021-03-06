package tms.network;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tms.util.InvalidNetworkException;

import javax.imageio.IIOException;
import java.io.IOException;

import static junit.framework.TestCase.fail;

public class NetworkInitialiserTest {
    private Network network;

    @After
    public void tearDownTest() {
        network = null;
    }

    @Test
    public void loadNetworkTest() {
        try {
            network = NetworkInitialiser.loadNetwork("networks/demo.txt");
            String[] toString = network.toString().split(System.lineSeparator(),-1);
            Assert.assertEquals(toString[0],"4");
            Assert.assertEquals(toString[1],"5");
            Assert.assertEquals(toString[2],"1");
            Assert.assertEquals(toString[3],"W");
            Assert.assertEquals(toString[4],"X");
            Assert.assertEquals(toString[5],"Y:3:Z,X");
            Assert.assertEquals(toString[6],"Z");
            Assert.assertEquals(toString[7],"X:Y:60:0");
            Assert.assertEquals(toString[8],"Y:X:60:1");
            Assert.assertEquals(toString[9],"PP:5:5,2,4,4,1,5,2,7,3,5,6,5,8,5,4,2,3,3,2,5");
            Assert.assertEquals(toString[10],"Y:Z:100:2");
            Assert.assertEquals(toString[11],"PP:8:1,3,2,1,1,3,4,7,4,7,9,7,8,4,8,8,5,3,2,2");
            Assert.assertEquals(toString[12],"VC:50:42,40,37,34,35,31,36,41,41,47,48,50,53,48,54,58,52,52,61,55");
            Assert.assertEquals(toString[13],"Z:X:40:1");
            Assert.assertEquals(toString[14],"SC:40:39,40,40,40,36,32,25,28,31,39,40,40,40,40,40,40,36,35,39,40");
            Assert.assertEquals(toString[15],"Z:Y:100:0:80");
        } catch (InvalidNetworkException e) {
            System.out.println(e);
            fail();
        } catch (IOException e) {
            fail();
        }
    }


    @Test
    public void intersectionsNum1Test() {
        try {
            NetworkInitialiser.loadNetwork("networks/intersectionnum1.txt");
            fail();
        }
        catch (InvalidNetworkException e){}
        catch (IOException  e) {
            System.out.println("file index wrong");
            fail();
        }
    }

    @Test
    public void routesNum1Test() {
        try {
            NetworkInitialiser.loadNetwork("networks/routesnum1.txt");
            fail();
        }
        catch (InvalidNetworkException e){
            System.out.println(e.getMessage());
        }
        catch (IOException  e) {
            System.out.println("file index wrong");
            fail();
        }
    }
    @Test
    public void routesNum2Test() {
        try {
            NetworkInitialiser.loadNetwork("networks/routesnum2.txt");
            fail();
        } catch (InvalidNetworkException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("file index wrong");
            fail();
        }
    }
    @Test
    public void sensorNum1Test() {
        try {
            NetworkInitialiser.loadNetwork("networks/sensornum1.txt");
            fail();
        } catch (InvalidNetworkException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("file index wrong");
            fail();
        }
    }

    @Test
    public void sensorNum2Test() {
        try {
            NetworkInitialiser.loadNetwork("networks/sensornum2.txt");
            fail();
        } catch (InvalidNetworkException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("file index wrong");
            fail();
        }
    }
    @Test
    public void routenotExist1Test() {
        try {
            NetworkInitialiser.loadNetwork("networks/routenotexist1.txt");
            fail();
        } catch (InvalidNetworkException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("file index wrong");
            fail();
        }
    }
    @Test
    public void routenotExist2Test() {
        try {
            NetworkInitialiser.loadNetwork("networks/routenotexist2.txt");
            fail();
        } catch (InvalidNetworkException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("file index wrong");
            fail();
        }
    }

    @Test
    public void wrongID1Test() {
        try {
            NetworkInitialiser.loadNetwork("networks/wrongid1.txt");
            fail();
        } catch (InvalidNetworkException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("file index wrong");
            fail();
        }
    }
    @Test
    public void wrongID2Test() {
        try {
            NetworkInitialiser.loadNetwork("networks/wrongid2.txt");
            fail();
        } catch (InvalidNetworkException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("file index wrong");
            fail();
        }
    }
    @Test
    public void sameIntersectionIDTest() {
        try {
            NetworkInitialiser.loadNetwork("networks/sameintersectionid.txt");
            fail();
        } catch (InvalidNetworkException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("file index wrong");
            fail();
        }
    }

    @Test
    public void routeSameStartEndTest() {
        try {
            NetworkInitialiser.loadNetwork("networks/samestartend.txt");
            fail();
        } catch (InvalidNetworkException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("file index wrong");
            fail();
        }
    }

    @Test
    public void wrongSensorTypeTest() {
        try {
            NetworkInitialiser.loadNetwork("networks/wrongsensortype.txt");
            fail();
        } catch (InvalidNetworkException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("file index wrong");
            fail();
        }
    }

    //
    @Test
    public void sameSensorTypeTest() {
        try {
            NetworkInitialiser.loadNetwork("networks/samesensortype.txt");
            fail();
        } catch (InvalidNetworkException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("file index wrong");
            fail();
        }
    }

    @Test
    public void yellowTimeWrongTest() {
        try {
            NetworkInitialiser.loadNetwork("networks/yellowtimewrong.txt");
            fail();
        } catch (InvalidNetworkException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("file index wrong");
            fail();
        }
    }
    @Test
    public void durationLessThanYellowTimePlus1Test() {
        try {
            NetworkInitialiser.loadNetwork("networks/durationlesswrong.txt");
            fail();
        } catch (InvalidNetworkException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("file index wrong");
            fail();
        }
    }

    @Test
    public void lightOrderPermutationWrong1Test() {
        try {
            NetworkInitialiser.loadNetwork("networks/lightorderwrong1.txt");
            fail();
        } catch (InvalidNetworkException e) {
            System.out.println(e.toString());
        } catch (IOException e) {
            System.out.println("file index wrong");
            fail();
        }
    }

    @Test
    public void lightOrderEmptyTest() {
        try {
            NetworkInitialiser.loadNetwork("networks/lightorderwrong2.txt");
            fail();
        } catch (InvalidNetworkException e) {
            System.out.println(e.toString());
        } catch (IOException e) {
            System.out.println("file index wrong");
            fail();
        }
    }
    @Test
    public void routeSpeedNegativeTest() {
        try {
            NetworkInitialiser.loadNetwork("networks/routespeednegative.txt");
            fail();
        } catch (InvalidNetworkException e) {
            System.out.println(e.toString());
        } catch (IOException e) {
            System.out.println("file index wrong");
            fail();
        }
    }
    @Test
    public void speedSignNegativeTest() {
        try {
            NetworkInitialiser.loadNetwork("networks/speedsignnegative.txt");
            fail();
        } catch (InvalidNetworkException e) {
            System.out.println(e.toString());
        } catch (IOException e) {
            System.out.println("file index wrong");
            fail();
        }
    }

    @Test
    public void sensorThresholdzeroTest() {
        try {
            NetworkInitialiser.loadNetwork("networks/thresholdzero.txt");
            fail();
        } catch (InvalidNetworkException e) {
            System.out.println(e.toString());
        } catch (IOException e) {
            System.out.println("file index wrong");
            fail();
        }
    }

    @Test
    public void sensorThresholdnegativeTest() {
        try {
            NetworkInitialiser.loadNetwork("networks/thresholdnegative.txt");
            fail();
        } catch (InvalidNetworkException e) {
            System.out.println(e.toString());
        } catch (IOException e) {
            System.out.println("file index wrong");
            fail();
        }
    }

    @Test
    public void sensorDataNegativeTest() {
        try {
            NetworkInitialiser.loadNetwork("networks/sensordatanegative.txt");
            fail();
        } catch (InvalidNetworkException e) {
            System.out.println(e.toString());
        } catch (IOException e) {
            System.out.println("file index wrong");
            fail();
        }
    }

    @Test
    public void intersectionColon1Test() {
        try {
            NetworkInitialiser.loadNetwork("networks/intersectioncolon1.txt");
            fail();
        } catch (InvalidNetworkException e) {
            System.out.println(e.toString());
        } catch (IOException e) {
            System.out.println("file index wrong");
            fail();
        }
    }

    @Test
    public void intersectionColon2Test() {
        try {
            NetworkInitialiser.loadNetwork("networks/intersectioncolon2.txt");
            fail();
        } catch (InvalidNetworkException e) {
            System.out.println(e.toString());
        } catch (IOException e) {
            System.out.println("file index wrong");
            fail();
        }
    }

    @Test
    public void routeColon1Test() {
        try {
            NetworkInitialiser.loadNetwork("networks/routecolon1.txt");
            fail();
        } catch (InvalidNetworkException e) {
            System.out.println(e.toString());
        } catch (IOException e) {
            System.out.println("file index wrong");
            fail();
        }
    }
    @Test
    public void routeColon2Test() {
        try {
            NetworkInitialiser.loadNetwork("networks/routecolon2.txt");
            fail();
        } catch (InvalidNetworkException e) {
            System.out.println(e.toString());
        } catch (IOException e) {
            System.out.println("file index wrong");
            fail();
        }
    }

    @Test
    public void sensorColon1Test() {
        try {
            NetworkInitialiser.loadNetwork("networks/sensorcolon1.txt");
            fail();
        } catch (InvalidNetworkException e) {
            System.out.println(e.toString());
        } catch (IOException e) {
            System.out.println("file index wrong");
            fail();
        }
    }
    @Test
    public void numericValueFailTest() {
        try {
            NetworkInitialiser.loadNetwork("networks/numericvaluefails.txt");
            fail();
        } catch (InvalidNetworkException e) {
            System.out.println(e.toString());
        } catch (IOException e) {
            System.out.println("file index wrong");
            fail();
        }
    }
    @Test
    public void emptyLine1Test() {
        try {
            NetworkInitialiser.loadNetwork("networks/emptyline1.txt");
            fail();
        } catch (InvalidNetworkException e) {
            System.out.println(e.toString());
        } catch (IOException e) {
            System.out.println("file index wrong");
            fail();
        }
    }
    @Test
    public void emptyLine2Test() {
        try {
            NetworkInitialiser.loadNetwork("networks/emptyline2.txt");
            fail();
        } catch (InvalidNetworkException e) {
            System.out.println(e.toString());
        } catch (IOException e) {
            System.out.println("file index wrong");
            fail();
        }
    }
    @Test
    public void twoNewLineTest() {
        try {
            NetworkInitialiser.loadNetwork("networks/twonewline.txt");
            fail();
        } catch (InvalidNetworkException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("file index wrong");
            fail();
        }
    }
}

