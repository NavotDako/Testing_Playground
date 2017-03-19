package Play;

//package <set your test package>;
import com.experitest.client.*;
import org.junit.*;
/**
 *
 */
public class newtest {
    private String host = "localhost";
    private int port = 8890;
    private String projectBaseDirectory = "C:\\Users\\DELL\\workspace\\project24";
    protected Client client = null;

    @Before
    public void setUp(){
        client = new Client(host, port, true);
        client.setProjectBaseDirectory(projectBaseDirectory);
        client.setReporter("xml", "reports", "erfw");
    }

    @Test
    public void testerfw(){
        String deviceName = client.waitForDevice("@serialnumber='FA69J0308869'",10000);

        client.releaseDevice(deviceName,true,true,true);

        deviceName = client.waitForDevice("@serialnumber='FA69J0308869'",10000);

        client.releaseDevice(deviceName,true,true,true);

        deviceName = client.waitForDevice("@serialnumber='FA69J0308869'",10000);

        client.releaseDevice(deviceName,true,true,true);

    }

    @After
    public void tearDown(){
        // Generates a report of the test case.
        // For more information - https://docs.experitest.com/display/public/SA/Report+Of+Executed+Test
        client.generateReport(false);
        // Releases the client so that other clients can approach the agent in the near future.
        client.releaseClient();
    }
}
