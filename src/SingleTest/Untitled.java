package SingleTest;

import com.experitest.client.*;
import org.junit.*;

/**
 *
 */
public class Untitled {
    private String host = "localhost";
    private int port = 8889;
    protected Client client = null;

    @Before
    public void setUp() {
        client = new Client(host, port, true);
        System.out.println("first - " + client.getConnectedDevices().toString());

        client.setReporter("xml", "reports", "Untitled");

        System.out.println("sec - " + client.getConnectedDevices().toString());
    }

    @Test
    public void testUntitled() {

    }

    @After
    public void tearDown() {
        client.generateReport(false);
        client.releaseClient();
    }
}