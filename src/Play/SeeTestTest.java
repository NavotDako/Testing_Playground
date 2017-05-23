package Play;// Insert your package here

import com.experitest.client.*;
import com.experitest.manager.client.PManager;
import com.experitest.manager.client.ResultPublisher;
import org.junit.*;

import java.util.concurrent.TimeUnit;

public class SeeTestTest {

    protected Client client = null;
    protected GridClient gridClient = null;

    @Before
    public void setUp() {
        // gridClient = new GridClient("zekra", "Experitest2012", "", "https://192.168.2.135:443");
        gridClient = new GridClient("admin", "Experitest2012", "", "http://192.168.2.13:8090");
        client = gridClient.lockDeviceForExecution("Untitled", "@serialnumber='P6Q7N15725000283'", 120, TimeUnit.MINUTES.toMillis(2));
        client.setReporter("xml", "reports", "Untitled");
      //  System.setProperty("manager.url", "192.168.2.72:8787");
    }

    @Test
    public void testUntitled() {

        client.launch("http://www.netflix.com",true,true );
        client.verifyElementFound("web","xpath=//*[@nodeName='svg']",1);

//        try {
//            client.getDeviceLog();
//        } catch (Exception e) {
//            PManager.getInstance().addProperty("Error", e.getMessage());
//            String s = client.collectSupportData("c:\\temp\\1.zip", "", "", "", "", "", true, true);
//            PManager.getInstance().addReportZipFile(s);
//        }
//
//        PManager.getInstance().addReportFolder();
//        ResultPublisher.publishResult(null, System.currentTimeMillis() + "_new", "", null, true, null, null, false, 1);

    }

    @After
    public void tearDown() {

        client.generateReport(false);
        client.releaseClient();
    }
}