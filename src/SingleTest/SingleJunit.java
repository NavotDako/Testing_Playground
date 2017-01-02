package SingleTest;

import com.experitest.client.*;
import org.junit.*;

import java.io.File;

public class SingleJunit {
    private static final boolean GRID = false;
    private String host = "localhost";
    private int port = 8889;
    protected Client client = null;
    String projectBaseDirectory = "C:\\Users\\DELL\\workspace\\project18";

    String deviceQuery = "@os='ios' and not(contains(@version,'8'))";

    String iOSAppString = "cloud:com.experitest.ExperiBank";
    String androidAppString = "com.experitest.ExperiBank/.LoginActivity";
    String workingAppString = (deviceQuery.contains("ios")) ? iOSAppString : androidAppString;

    @Before
    public void setUp(){

      /*  if (GRID) {
            GridClient grid = new GridClient("admin", "Experitest2012", "", "192.168.2.13", 8090, false);
            client =  grid.lockDeviceForExecution("NavotTest", deviceQuery, 2,60000 );
        } else {
            client = new Client(host,port,true);
            client.waitForDevice(deviceQuery,30000);
        }*/
       //client.setProjectBaseDirectory(projectBaseDirectory);
      //  client.setReporter("xml", "Reports", "Untitled");
        //client.setShowPassImageInReport(false);
    }

    @Test
    public void TheTest(){

        client = new Client(host,port,true);
        String device = client.waitForDevice("@os='ios' and not(contains(@version,'8')) and (contains(@version,'10.2.9')) ",30000); /*String device = client.waitForDevice("@os='android'",30000);*/
        client.deviceAction("Unlock");


    }

    @After
    public void tearDown(){
       // System.out.println(client.generateReport(false));
       // client.releaseClient();
    }
}