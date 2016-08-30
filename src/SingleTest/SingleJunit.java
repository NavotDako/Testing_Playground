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
        testUntitled("adb:LGE LG-H815");
    }


    public void testUntitled(String device){
        client.waitForDevice("@name = 'User’s iPhone'", 10000);
        client.swipeWhileNotFound("DOWN", 200, 1000, "WEB","xpath=//*[@id='wpCreateaccount' and @onScreen='true']" ,0 , 0, 2, true);
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