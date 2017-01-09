package SingleTest;//package <set your test package>;
import com.experitest.client.*;
import org.junit.*;
/**
 *
 */
public class WebTest {
    private String host = "localhost";
    private int port = 8889;
    private String projectBaseDirectory = "C:\\Users\\DELL\\workspace\\project21";
    protected Client client = null;

    @Before
    public void setUp(){
        client = new Client(host, port, true);
        client.setProjectBaseDirectory(projectBaseDirectory);
        client.setReporter("xml", "reports", "Untitled");
    }

    @Test
    public void testUntitled(){
        client.setDevice("ios_app:B0096 iPhone");
        int int0 = client.getElementCount("WEB", "id=kljh");
        client.swipe("Down", 200, 500);
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
