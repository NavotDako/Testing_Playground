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
//    private String host = "cloud.experitest.com";private int port = 443;String user = "shelinonervous";String password = "Experi1989";boolean secured = true;

//    private String host = "192.168.2.13";private int port = 8090;String user = "admin";String password = "Experitest2012";boolean secured = false;

    private String host = "qacloud.experitest.com";private int port = 443;String user = "zekra";String password = "Zekra123";boolean secured = true;


    private String projectBaseDirectory = "C:\\Users\\DELL\\workspace\\project1";
    protected Client client = null;
    private String query = "@os";
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
        GridClient grid = new GridClient(user, password, "", host, port, secured);
        Client tempClient = grid.lockDeviceForExecution("newName", query, 5, 300000);
        tempClient.setReporter("xml", "c:\\temp\\reports", "Untitled");
        return tempClient;
    }

    @Test
    public void testUntitled() throws URISyntaxException, IOException {

            client.collectSupportData("c:\\temp", "", deviceName, "", "", "", true, true);


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
