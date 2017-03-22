package SingleTest;//package <set your test package>;
import com.experitest.client.*;
import org.junit.*;
/**
 *
 */
public class SingleJunit {
    private String host = "localhost";
    private int port = 8890;
    private String projectBaseDirectory = "C:\\Users\\DELL\\workspace\\project1";
    protected Client client = null;

    @Before
    public void setUp(){
        client = new Client(host, port, true);
        client.setProjectBaseDirectory(projectBaseDirectory);
        client.setReporter("xml", "reports", "Untitled");
    }

    @Test
    public void testUntitled(){
        client.setDevice("adb:Google Pixel");
        for (int i=0;i<100;i++) {
            client.deviceAction("Home");
            String str0 = client.getVisualDump("Native");
            client.swipe("up",10,500);
        }
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
