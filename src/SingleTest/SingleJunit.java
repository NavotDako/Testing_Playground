package SingleTest;//package <set your test package>;

import FrameWork.CloudServer;
import com.experitest.client.*;
import org.junit.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URISyntaxException;
import java.util.Map;

/**
 *
 */
public class SingleJunit {

    CloudServer cloudServer = new CloudServer(CloudServer.CloudServerName.MY);
    protected Client client = null;
    private String deviceName;
    private String deviceSN = "LGD85589b241b0";
    private boolean GRID = true;

    @Before
    public void setUp() throws IOException, SAXException, ParserConfigurationException {
        if (GRID) {
            client = getGridClient();
            deviceName = cloudServer.getDeviceNameByUDID(deviceSN);
        } else {
            client = getClient();
            deviceName = client.getDeviceProperty("device.name");
        }
    }

    public Client getClient() {
        Client tempClient = new Client("localhost", 8889, true);
        tempClient.waitForDevice("@serialNumber='" + deviceSN + "'", 10000);
        tempClient.setReporter("xml", "c:\\temp\\reports", "Untitled");
        return tempClient;
    }

    public Client getGridClient() throws IOException, SAXException, ParserConfigurationException {
        GridClient grid = new GridClient(cloudServer.USER, cloudServer.PASS, cloudServer.PROJECT, cloudServer.HOST, cloudServer.PORT, cloudServer.SECURED);
        System.out.println(grid.getDevicesInformation());
        Client tempClient = grid.lockDeviceForExecution("SingleTest", "@serialNumber='" + deviceSN + "'", 5, 300000);
        tempClient.setReporter("xml", "c:\\temp\\reports", "Untitled");

        return tempClient;
    }
    @Test
    public void testUntitled() throws URISyntaxException, IOException {
        client.deviceAction("Unlock");
//        client.launch("com.experitest.ExperiBank/.LoginActivity", true, true);
//        client.install("http://192.168.2.72:8181/AndroidApps/eribank.apk", true, false);
//        client.install("http://192.168.2.72:8181/AndroidApps/eribank.apk", true, false);

    }

    @After
    public void tearDown() {
        try {
            System.out.println(client.generateReport(false));
            client.releaseClient();
        } catch (Exception e) {
            e.printStackTrace();
            client.collectSupportData(".", "", deviceName, "", "", "", true, true);
        }

    }


}
