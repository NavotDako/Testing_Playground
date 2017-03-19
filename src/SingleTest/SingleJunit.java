package SingleTest;

import com.experitest.client.*;
import org.junit.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class SingleJunit {

    private static boolean GRID = true;

    String host = "localhost";
    int port = 8900;
    String serverHost = "192.168.2.13";
    int serverPort = 8090;

    String cloudAdminName = "admin";
    String projectAdminName = "projectAdmin";
    String userName = "user";

    String password = "Experitest2012";
    String project = "Default";

    Client client = null;
    GridClient grid = null;

    String reportFolder = "c:\\Temp\\SingleTest";

    String deviceName;

    @Before
    public void setUp() {
       throw new RuntimeException("fuck");
    }

    public Client getClient(String deviceQuery, boolean grid) {
        this.grid = new GridClient(userName, password, project, serverHost, serverPort, false);
        Client client = null;
        if (grid) {
            client = this.grid.lockDeviceForExecution("My_Test_" + System.currentTimeMillis(), deviceQuery, 10, 60000);
            client.setReporter("xml", reportFolder, "Single Test");

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
        client.launch("com.experitest.ExperiBankO", true, true);
        client.syncElements(3000, 15000);
        client.verifyElementFound("NATIVE", "xpath=//*[@placeholder='Username']", 0);
        client.elementSendText("NATIVE", "xpath=//*[@placeholder='Username']", 0, "company");

        client.verifyElementFound("NATIVE", "xpath=//*[@placeholder='Password']", 0);
        client.elementSendText("NATIVE", "xpath=//*[@placeholder='Password']", 0, "company");
        client.closeKeyboard();
        client.verifyElementFound("NATIVE", "xpath=//*[@text='Login']", 0);
        client.click("NATIVE", "xpath=//*[@text='Login']", 0, 1);
        client.sendText("{LANDSCAPE}");
        client.sleep(1000);
        client.verifyElementFound("NATIVE", "xpath=//*[@text='Make Payment']", 0);
        client.syncElements(1000, 5000);
        client.click("NATIVE", "xpath=//*[@text='Make Payment']", 0, 1);

        client.verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='phoneTextField']", 0);
        client.syncElements(1000, 5000);
        client.elementSendText("NATIVE", "xpath=//*[@accessibilityLabel='phoneTextField']", 0, "050-7937021");
        client.closeKeyboard();
        client.verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='nameTextField']", 0);
        client.elementSendText("NATIVE", "xpath=//*[@accessibilityLabel='nameTextField']", 0, "Long Run");
        client.closeKeyboard();
        client.verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='amountTextField']", 0);
        client.elementSendText("NATIVE", "xpath=//*[@accessibilityLabel='amountTextField']", 0, "100");
        client.closeKeyboard();
        client.verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Country']", 0);
        client.verifyElementFound("NATIVE", "xpath=//*[@text='Select']", 0);
        client.sleep(1000);
    }

    @After
    public void tearDown() {
       /* String reportPath = reportFolder;
        try {
            reportPath = client.generateReport(false);
            System.out.println("reportPath - " +reportPath);
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
        client.releaseClient();*/
        System.out.println("444444444444444444444444444444444444444444444444444444444444444");
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