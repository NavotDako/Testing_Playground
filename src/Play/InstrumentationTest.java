package Play;

import FrameWork.Command;
import FrameWork.MyClient;
import FrameWork.Runner;
import com.experitest.client.*;
import org.junit.*;
import org.junit.rules.TestName;
import org.junit.runners.Suite;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

/**
 *
 */
public class InstrumentationTest {
    static String serverHost = "192.168.2.13";
    static int serverPort = 8090;
    private String host = "localhost";
    private int port = 8889;
    protected Client client = null;
    public String bundleName;
    public static String deviceName = null;
    public static String deviceQuery = "@os='android'";
    public String appsDirectory = "C:\\Users\\DELL\\EclipseWorkspace\\Testing Playground STA\\lib\\AndroidApps\\";

    @Rule
    public TestName testName = new TestName();
    Boolean GRID = true;

    @Before
    public void setUp() {

        client = getClient(deviceQuery, GRID);
        deviceName = client.getDeviceProperty("device.name");
        deviceQuery = "@serialnumber='" + client.getDeviceProperty("device.sn") + "'";
        client.setReporter("xml", "reports", testName.getMethodName());

    }

    private Client getClient(String deviceQuery, Boolean grid) {
        Client temp_client = null;
        if (GRID) {
            temp_client = getGridClient(deviceQuery);
        } else {
            temp_client = getLocalClient(deviceQuery);
        }
        return temp_client;
    }

    public Client getGridClient(String deviceQuery) {
        Client myclient = null;
        GridClient grid = new GridClient("admin", "Experitest2012", "", serverHost, serverPort, false);
        myclient = grid.lockDeviceForExecution(testName.getMethodName(), deviceQuery, 5, 120000);
        return myclient;
    }

    public Client getLocalClient(String deviceQuery) {
        Client myclient = new Client(host, port, true);
        deviceName = myclient.waitForDevice(deviceQuery, 30000);
        return myclient;
    }

    @Test
    public void beatTheTraffic() {
        // Installation

        bundleName = "triangleSoftware.traffic.android/com.travelersnetwork.lib.ui.SplashScreenActivity";

        client.sendText("{PORTRAIT}");
        client.install(appsDirectory + "BeatTheTraffic.apk", true, false);
        client.launch(bundleName, true, false);
        client.waitForElement("NATIVE", "xpath=//*[@text='Safety Warning']", 0, 50000);
        client.isElementFound("NATIVE", "xpath=//*[@text='Safety Warning']");
        client.applicationClose(bundleName);
        // Test - Start
        for (int i = 0; i < 6; i++) {

            client.launch(bundleName, true, false);

            System.out.println(client.getVisualDump("NATIVE"));
            client.applicationClose(bundleName);
        }
        // Test - End
    }

    @Test
    public void bankBazaar() {
        // Installation
        bundleName = "com.bankbazaar.app/.MainActivity";
        client.sendText("{PORTRAIT}");
        client.install(appsDirectory + "BankBazaar_1.3.1release_17.apk", true, false);
        for (int i = 0; i < 6; i++) {
            client.launch(bundleName, true, false);
            // Test - Start
            client.waitForElement("NATIVE", "xpath=//*[@id='imgInfo' and ./following-sibling::*[@text='Browse & Apply']]", 0, 10000);
            client.swipe("Right", 100, 500);
            client.isElementFound("NATIVE", "xpath=//*[@id='imgInfo' and ./following-sibling::*[@text='Search & Compare']]");
            client.swipe("Right", 100, 500);
            client.isElementFound("NATIVE", "xpath=//*[@id='imgInfo' and ./following-sibling::*[@text='Customer Support']]");
            client.applicationClose(bundleName);
        }
        // Test - End
    }

    @Test
    public void cellTrust() {
        // Installation
        bundleName = "com.celltrust.securesms/com.celltrust.secureline.securesms.gui.MainScreen";
        client.sendText("{PORTRAIT}");
        client.install(appsDirectory + "com.celltrust.securesms.apk", true, false);
        for (int i = 0; i < 6; i++) {
            client.launch(bundleName, true, false);
            // Test - Start
            client.waitForElement("NATIVE", "xpath=//*[@id='titleLogoLtrs']", 0, 10000);
            client.waitForElement("NATIVE", "xpath=//*[@text='   Save   ']", 0, 60000);
            client.click("NATIVE", "xpath=//*[@text='   Save   ']", 0, 1);
            client.waitForElement("NATIVE", "xpath=//*[@text='Accept']", 0, 60000);
            client.click("NATIVE", "xpath=//*[@text='Accept']", 0, 1);

            client.applicationClose(bundleName);
        }
        // Test - End
    }

    @Test
    public void good() {
        // Installation
        bundleName = "com.good.gdgma/com.good.chrome.gdgma.LaunchActivity";
        client.sendText("{PORTRAIT}");
        client.install(appsDirectory + "com.good.gdgma.apk", true, false);
        for (int i = 0; i < 6; i++) {
            client.launch(bundleName, true, false);
            // Test - Start
            client.sleep(15000);
            client.waitForElement("NATIVE", "xpath=//*[@text='OK']", 0, 10000);
            client.closeKeyboard();
            client.click("NATIVE", "xpath=//*[@text='Learn More']", 0, 1);
            client.closeKeyboard();

            client.isElementFound("NATIVE", "xpath=//*[@text='Setup Application']");
            client.closeKeyboard();
            client.applicationClose(bundleName);
        }
        // Test - End
    }

    @Test
    public void globalLogic() {
        // Installation
        bundleName = "org.afree/.demo.activity.ChartHierarchyActivity";
        client.sendText("{PORTRAIT}");
        client.install(appsDirectory + "GlobalLogic.apk", true, false);
        for (int i = 0; i < 6; i++) {
            client.launch(bundleName, true, false);
            // Test - Start
            client.waitForElement("NATIVE", "xpath=//*[@text='Demo Slideshow']", 0, 10000);
            client.click("NATIVE", "xpath=//*[@text='Demo Slideshow']", 0, 1);
            client.isElementFound("NATIVE", "xpath=//*[@text='Start']");
            client.applicationClose(bundleName);
        }
        // Test - End
    }

    @Test
    public void vodafoneItaly() {
        // Installation
        bundleName = "it.vodafone.my190/.ui.activity.MainActivity";
        client.sendText("{PORTRAIT}");
        client.install(appsDirectory + "it_vodafone_my190-620.apk", true, false);
        client.launch(bundleName, true, false);
        client.waitForElement("NATIVE", "xpath=//*[@text='Accetta']", 0, 10000);
        client.click("NATIVE", "xpath=//*[@text='Accetta']", 0, 1);
        client.applicationClose(bundleName);
        for (int i = 0; i < 6; i++) {
            client.launch(bundleName, true, false);
            // Test - Start
            client.isElementFound("NATIVE", "xpath=//*[@text='Consensi e Preferenze']");
            client.applicationClose(bundleName);
        }
        // Test - End
    }

    @Test
    public void sfrTv() {
        // Installation
        bundleName = "com.sfr.android.mobiletv/.MobileTV";
        client.sendText("{PORTRAIT}");
        client.install(appsDirectory + "3049_SFRTV-6.1.0-prodRelease_2015-05-26_12-12-58.apk", true, false);
        client.launch(bundleName, true, false);
        // Test - Start
        client.waitForElement("NATIVE", "xpath=//*[@text='OK']", 0, 10000);
        client.click("NATIVE", "xpath=//*[@text='OK']", 0, 1);
        client.waitForElement("NATIVE", "xpath=//*[@id='close_button']", 0, 10000);
        client.sleep(10000);
        client.click("NATIVE", "xpath=//*[@id='close_button']", 0, 1);
        client.sleep(10000);
        client.isElementFound("NATIVE", "xpath=//*[@text='SFR TV']");
        client.applicationClose(bundleName);
        for (int i = 0; i < 6; i++) {
            client.launch(bundleName, true, false);
            // Test - Start
            client.isElementFound("NATIVE", "xpath=//*[@text='SFR TV']");
            client.applicationClose(bundleName);
        }
        // Test - End
    }

    @Test
    public void wemo() {
        // Installation
        bundleName = "com.belkin.wemoandroid/com.belkin.activity.MainActivity";
        client.sendText("{PORTRAIT}");
        client.install(appsDirectory + "com.belkin.wemoandroid.apk", true, false);
        for (int i = 0; i < 6; i++) {
            client.launch(bundleName, true, false);
            // Test - Start
            client.waitForElement("WEB", "xpath=//*[@text='Get Started']", 0, 10000);
            client.click("WEB", "xpath=//*[@text='Get Started']", 0, 1);
            client.verifyElementFound("WEB", "xpath=//*[@id='wemo_sensorSwitch']", 0);

            client.applicationClose(bundleName);
        }
        // Test - End
    }

    @Test
    public void ebusiness() {
        // Installation
        bundleName = "com.eBusiness/.eBusiness";
        client.sendText("{PORTRAIT}");
        client.install(appsDirectory + "ebusiness-mobileEBusinessAndroid08242015.apk", true, false);
        for (int i = 0; i < 6; i++) {
            client.launch(bundleName, true, false);
            // Test - Start
            client.waitForElement("WEB", "xpath=//*[@nodeName='SPAN' and @width>0 and ./*[@text='Login']]", 0, 10000);
            client.swipe("Down", 300, 500);
            client.verifyElementFound("WEB", "xpath=//*[@id='main-menu-jumbotron']", 0);

            client.applicationClose(bundleName);
        }
        // Test - End
    }

    @Test
    public void svb() {
        // Installation
        bundleName = "com.svb.mobilebanking/.MainActivity";
        client.sendText("{PORTRAIT}");
        client.install(appsDirectory + "com.svb.mobilebanking.apk", true, false);
        for (int i = 0; i < 6; i++) {
            client.launch(bundleName, true, false);
            // Test - Start
            client.waitForElement("WEB", "xpath=//*[@class='menu-home-down-arrow']", 0, 10000);
            client.click("WEB", "xpath=//*[@class='menu-home-down-arrow']", 0, 1);
            client.click("WEB", "xpath=//*[@text='About']", 0, 1);
            client.verifyElementFound("WEB", "xpath=//*[@text='SVB.com']", 0);
            client.applicationClose(bundleName);
        }
        // Test - End
    }

    @After
    public void tearDown() {
        client.uninstall(bundleName);
        client.generateReport(false);
        client.releaseClient();
    }
}
