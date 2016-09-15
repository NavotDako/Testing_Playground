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
    protected Client client = null;

    @Before
    public void setUp(){
        client = new Client(host, port, true);
        // client.setProjectBaseDirectory(projectBaseDirectory);
        client.setReporter("xml", "reports", "Untitled");
    }

    @Test
    public void TheTest(){
        String device = client.waitForDevice("@os", 10000);
        client.openDevice();
        System.out.println(device);
        String link = (device.contains("ios")) ? "http://192.168.2.72:8181/iOSApps/fmr_ios.ipa" : "http://192.168.2.72:8181/AndroidApps/fmr_quotesIssue.apk";
        String appName = (device.contains("ios")) ? "com.fidelity.watchlist-AdHoc" : "com.fidelity.android/.activity.AppConfigLoaderActivity";
        for (int i = 0; i < 3; i++) {
            System.out.println("========================== Iteration: "+i+" ================================");
            client.install(link,true,false);
            client.sleep(1000);
            client.launch(appName,true,true);
            client.sleep(1000);
            System.out.println(client.getVisualDump("native"));
            client.uninstall(appName);
            client.sleep(1000);

        }
    }

    @After
    public void tearDown(){
      client.generateReport(false);
        client.releaseClient();
    }
}