package SingleTest;

import com.experitest.client.*;
import org.junit.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

public class SingleJunit {
    private static final boolean GRID = true;

    private String host = "localhost";
    private int port = 8889;

    protected String serverHost = "192.168.1.210";
    protected int serverPort = 80;
    protected String user = "navot";
    protected String password ="Experitest2012";
    protected Client client = null;
    protected GridClient grid = null;


    private String device;

    @Before
    public void setUp() {

        String deviceQuery = "@os='ios'";// and (contains(@version,'5.0'))";
        //deviceQuery = "";
        client = getClient(deviceQuery, GRID);
        client.setShowPassImageInReport(false);
        client.setReporter("xml", "Reports", "Single Test");
        client.startVideoRecord();
    }

    public Client getClient(String deviceQuery, boolean grid) {
        this.grid = new GridClient(user,password, "", serverHost, serverPort, false);
        Client client = null;
        if (grid) {
            client = this.grid.lockDeviceForExecution("NavotTest", deviceQuery, 10, 60000);
            device = client.getDeviceProperty("device.name");

        } else {
            client = new Client(host, port, true);
            client.getConnectedDevices();
           // device = client.waitForDevice(deviceQuery, 30000);
        }
        return client;
    }

    @Test
    public void TheTest() throws Exception {


        client.launch("safari:http://www.google.com", true, true);
        client.capture();
        client.sleep(50);
        client.applicationClose("");
        client.launch("safari:http://www.google.com", true, true);


    }


    @After
    public void tearDown() {
        String reportPath = client.generateReport(false);
        if (!GRID) client.releaseDevice(device, true, true, true);
        client.releaseClient();
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }
}