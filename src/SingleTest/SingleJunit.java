package SingleTest;

import com.experitest.client.*;
import org.junit.*;
/**
 *
 */
public class SingleJunit {
    private String host = "localhost";
    private int port = 8889;
    // private String projectBaseDirectory = "C:\\Users\\Admin.user13\\workspace\\project7";
    protected Client client = null;

    @Before
    public void setUp(){
        client = new Client(host, port, true);
        // client.setProjectBaseDirectory(projectBaseDirectory);
        client.setReporter("xml", "reports", "Untitled");
    }

    @Test
    public void AndroidTest(){
        testUntitled();
    }


    public void testUntitled(){
        String device = client.waitForDevice("@os='ios' and contains(@version,'8')", 10000);
        for (int i = 0; i < 10; i++) {
            client.launch("http://tst.usa.philips.com/c-m/consumer-products", true, true);
            client.getVisualDump("web");
        }
    }

    @After
    public void tearDown(){
        // Generates a report of the test case.
        // For more information - https://docs.experitest.com/display/public/SA/Report+Of+Executed+Test

        // Releases the client so that other clients can approach the agent in the near future.
        client.releaseClient();
    }
}