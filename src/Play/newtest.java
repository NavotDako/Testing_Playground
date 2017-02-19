package Play;

//package <set your test package>;
import com.experitest.client.*;
import org.junit.*;
/**
 *
 */
public class newtest {
    private String host = "localhost";
    private int port = 8889;
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
        client.setDevice("adb:HTC HTC_M8x");
        client.launch("com.experitest.ExperiBank/.LoginActivity", false, true);
        if(client.waitForElement("NATIVE", "partial_text=Login", 0, 30000)){
            // If statement
        }
        client.click("NATIVE", "text=Login", 0, 1);
        client.verifyElementFound("NATIVE", "text=Error", 0);
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
