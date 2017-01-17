package SingleTest;

import com.experitest.client.*;
import org.junit.*;

public class SingleJunit {
    private static final boolean GRID = false;

    private String host = "localhost";
    private int port = 8889;

    protected String serverHost = "192.168.2.13";
    protected int serverPort = 8090;

    protected Client client = null;
    protected GridClient grid = null;


    private String device;

    @Before
    public void setUp() {


        String deviceQuery = "@os='android' and not(contains(@version,'10'))";

        client = getClient(deviceQuery, GRID);

        client.setReporter("xml", "Reports", "Single Test");

    }

    public Client getClient(String deviceQuery, boolean grid) {
        this.grid = new GridClient("admin", "Experitest2012", "", serverHost, serverPort, false);
        Client client = null;
        if (grid) {
            client = this.grid.lockDeviceForExecution("NavotTest", deviceQuery, 10, 60000);
            device = client.getDeviceProperty("device.name");
        } else {
            client = new Client(host, port, true);
            device = client.waitForDevice(deviceQuery, 30000);
        }
        return client;
    }

    @Test
    public void TheTest() {
        if(client.install("cloud:com.experitest.ExperiBank/.LoginActivity", true, false)){

            client.launch("com.experitest.ExperiBank/.LoginActivity ", true, true);}
    }

    @After
    public void tearDown() {
        String reportPath = client.generateReport(false);
        if (!GRID) client.releaseDevice(device, true, true, true);
        client.releaseClient();
    }
}