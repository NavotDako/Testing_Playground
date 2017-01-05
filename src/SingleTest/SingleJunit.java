package SingleTest;

import com.experitest.client.*;
import org.junit.*;

import java.io.File;

public class SingleJunit {
    private static final boolean GRID = false;
    private String host = "localhost";
    private int port = 8889;
    protected Client client = null;

    String deviceQuery = "@os='android' and not(contains(@version,'8'))";

    String projectBaseDirectory = "C:\\Users\\DELL\\workspace\\project18";

    String iOSAppString = "cloud:com.experitest.ExperiBank";
    String androidAppString = "com.experitest.ExperiBank/.LoginActivity";
    String workingAppString = (deviceQuery.contains("ios")) ? iOSAppString : androidAppString;
    private String device;

    @Before
    public void setUp() {
      /*  if (GRID) {
            GridClient grid = new GridClient("admin", "Experitest2012", "", "192.168.2.13", 8090, false);
            client = grid.lockDeviceForExecution("NavotTest", deviceQuery, 2, 60000);
        } else {
            client = new Client(host, port, true);
            device = client.waitForDevice(deviceQuery, 30000);
        }

        client.setProjectBaseDirectory(projectBaseDirectory);
        client.setReporter("xml", "Reports", "Single Test");*/
        //client.setShowPassImageInReport(false);
    }

    @Test
    public void TheTest() throws InterruptedException {
        String host = "localhost";
        int port = 8889;
        Client client = null;

        String deviceQuery = "@os='android' and not(contains(@version,'8'))";
        client = new Client(host, port, true);
        device = client.waitForDevice(deviceQuery, 30000);
        client.getDeviceProperty("device.os");
        client.releaseDevice(device, false, false, false);
        client.releaseClient();
    }

    @After
    public void tearDown() {
       /* System.out.println(client.generateReport(false));
        client.releaseDevice(device, true, true, true);
        client.releaseClient();*/
    }
}