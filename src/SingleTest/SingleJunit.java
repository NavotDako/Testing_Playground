package SingleTest;//package <set your test package>;

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

/**
 *
 */
public class SingleJunit {
    private String host = "192.168.2.13";
    private int port = 8090;
    private String projectBaseDirectory = "C:\\Users\\DELL\\workspace\\project1";
    protected Client client = null;
    private String query = "@serialnumber='f759ec5d8343175b2c68f856c9c47559aa1fc0fc'";
    private boolean GRID = true;
    private String deviceName;

    @Before
    public void setUp() throws IOException, SAXException, ParserConfigurationException {
       // query = "@os='android'";
        if (GRID) {
            client = getGridClient();
        } else {
            client = getClient();
        }
        deviceName = client.getDeviceProperty("device.name");
    }

    public Client getClient() {
        Client tempClient = new Client("localhost", 8889, true);
        tempClient.waitForDevice(query, 10000);
        tempClient.setReporter("xml", "c:\\temp\\reports", "Untitled");
        return tempClient;
    }

    public Client getGridClient() throws IOException, SAXException, ParserConfigurationException {
        GridClient grid = new GridClient("admin", "Experitest2012", "", host, port, false);
        System.out.println(grid.getDevicesInformation());
        // utils.readXML(grid.getDevicesInformation(), "android");
        Client tempClient = grid.lockDeviceForExecution("newName", query, 5, 300000);
        tempClient.setReporter("xml", "c:\\temp\\reports", "Untitled");
        return tempClient;
    }

    @Test
    public void testUntitled() throws URISyntaxException, IOException {
        try {
            client.sleep(2000);
            client.sleep(2000);
            client.sleep(2000);
            client.sleep(2000);
            client.sleep(2000);
            client.sleep(2000);
            client.sleep(2000);
            client.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
            client.collectSupportData(".", "", deviceName, "", "", "", true, true);
        }

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
