package SingleTest;

import com.experitest.client.*;
import org.junit.*;

import java.io.File;

public class SingleJunit {
    private static final boolean GRID = false;
    private String host = "localhost";
    private int port = 8889;
    protected Client client = null;
    GridClient grid;
    String deviceQuery = "@os='android' and not(contains(@version,'8'))";

    String projectBaseDirectory = "C:\\Users\\DELL\\workspace\\project18";

    String iOSAppString = "cloud:com.experitest.ExperiBank";
    String androidAppString = "com.experitest.ExperiBank/.LoginActivity";
    String workingAppString = (deviceQuery.contains("ios")) ? iOSAppString : androidAppString;
    private String device;

    @Before
    public void setUp() {
        if (GRID) {
           // grid = new GridClient("navot", "Experitest2012", "", "192.168.2.5", 8080, false);
            grid = new GridClient("admin", "Experitest2012", "", "192.168.2.13", 8090, false);
            client = grid.lockDeviceForExecution("NavotTest", deviceQuery, 4, 60000);
            device = client.getDeviceProperty("deviceName.name");
        } else {
            client = new Client(host, port, true);
            device = client.waitForDevice(deviceQuery, 30000);
        }

        client.setProjectBaseDirectory(projectBaseDirectory);
        client.setReporter("xml", "Reports", "Single Test");

    }

    @Test
    public void TheTest() throws InterruptedException {

        client.install("http://192.168.2.72:8181/AndroidApps/eribank.apk", true, false);
    }

    @After
    public void tearDown() {
//        client.collectSupportData("C:\\Temp\\Reports\\data","","","","","",true,true);
        System.out.println(client.generateReport(false));
//        client.releaseDevice(deviceName, true, true, true);
        client.releaseClient();
       // grid.releaseDeviceFromExecution(client);
    }
}