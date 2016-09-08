package SingleTest;

import com.experitest.client.*;
import org.junit.*;

import java.io.File;

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
        System.out.println(client.waitForDevice("@os='ios'", 10000));/*
        client.install("http://192.168.2.72:8181/AndroidApps/cameraFlash-%20simulateCapture/com.CameraFlash-.MainActivity_ver_11.0.apk" , true , false);
        client.launch("com.CameraFlash/.MainActivity" , true , true);
        File file = new File("lib/SimulateCapture/hello-android.png");
        client.simulateCapture(file.getAbsolutePath());
        client.textFilter("0xA8C02E",80);
        client.getText("TEXT");
        client.verifyElementFound("Text","Hello",0);
        client.uninstall("com.CameraFlash/.MainActivity");
*/
       /* for (int i = 0; i < 10; i++) {
            client.install("http://192.168.2.72:8181/iOSApps/UICatalog.ipa",true,false);
            client.sleep(500);
            client.launch("com.experitest.UICatalog", true, true);
            client.getVisualDump("native");
            client.sleep(500);
            client.applicationClose("com.experitest.UICatalog");
            client.sleep(500);
            client.uninstall("com.experitest.UICatalog");
            System.out.println("=============================================================================");
        }*/
    }

    @After
    public void tearDown(){
      client.generateReport(false);
        client.releaseClient();
    }
}