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

    private static boolean GRID = false;

    String host = "localhost";
    int port = 8900;
    String serverHost = "192.168.2.13";
    int serverPort = 8090;
    String user = "admin";
    String password = "Experitest2012";
    Client client = null;
    GridClient grid = null;

    String reportFolder = "c:\\Temp\\SingleTest";

    String deviceName;

    @Before
    public void setUp() {
        String deviceQuery = "@name='samsung SM-N7505'";
        client = getClient(deviceQuery, GRID);

        if (!GRID) client.openDevice();
        client.setShowPassImageInReport(false);
        client.startVideoRecord();
    }

    public Client getClient(String deviceQuery, boolean grid) {
        this.grid = new GridClient(user, password, "", serverHost, serverPort, false);
        Client client = null;
        if (grid) {
            client = this.grid.lockDeviceForExecution("My_Test_" + System.currentTimeMillis(), deviceQuery, 10, 60000);


        } else // if not grid
        {
            client = new Client(host, port, true);
            client.setReporter("xml", reportFolder, "Single Test");
            deviceName = client.waitForDevice(deviceQuery, 30000);
            deviceName = (deviceName == null) ? "" : deviceName;

        }
        return client;
    }

    @Test
    public void TheTest() throws Exception {
        client.launch("com.android.settings/.Settings", false, true);
        client.deviceAction("home");
        client.capture();
        client.getVisualDump("native");

    }

    @After
    public void tearDown() {
        String reportPath = reportFolder;
        try {
            reportPath = client.generateReport(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            client.collectSupportData(reportPath, "", "", "", "", "", true, true);
            client.closeDevice();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // if (!GRID) client.releaseDevice(device, true, true, true);
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