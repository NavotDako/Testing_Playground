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
        String device = client.waitForDevice("@os='ios'", 10000);
        String path = client.generateReport(false);
        client.collectSupportData(path+"\\SupportData","",device,"","","",true,true);    }

    @After
    public void tearDown(){
        // Generates a report of the test case.
        // For more information - https://docs.experitest.com/display/public/SA/Report+Of+Executed+Test

        // Releases the client so that other clients can approach the agent in the near future.
        client.releaseClient();
    }
}