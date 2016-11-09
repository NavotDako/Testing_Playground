package Tests;

import FrameWork.AbsTest;
import FrameWork.MyClient;

/**
 * Created by richi.lebovich on 8/31/2016.
 */
public class UiCatalog extends AbsTest {

    public UiCatalog(MyClient client, String deviceQurey, int repNum, String reportFolder, String deviceOS, String testName) {
        super(client, deviceQurey, repNum, reportFolder, deviceOS, testName);
    }

    @Override
    protected void AndroidRunTest() {
        client.uninstall("com.experitest.uicatalog");
        client.sleep(500);
        client.install("com.experitest.uicatalog/.MainActivity", true, false);
        client.launch("com.experitest.uicatalog/.MainActivity", true, true);
        client.swipeWhileNotFound("Up", 0, 2000, "NATIVE", "text=Lists", 0, 1000, 5, true);
        client.swipeWhileNotFound("Down", 0, 2000, "NATIVE", "text=Dangolan", 0, 1000, 5, true);
        client.swipeWhileNotFound("Up", 400, 2000, "NATIVE", "text=Omer", 0, 1000, 5, true);
        client.applicationClose("com.experitest.uicatalog");
        }

    @Override
    protected void IOSRunTest() {
        client.uninstall("com.experitest.UICatalog");
        client.sleep(500);
        client.install("http://192.168.2.72:8181/iOSApps/UICatalog.ipa",true,false);
        client.launch("com.experitest.UICatalog", true, true);
        client.swipeWhileNotFound("Down", 150, 2000, "NATIVE", "xpath=//*[@accessibilityLabel='Web']", 0, 1000, 5, true);
        client.verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='http://www.apple.com']", 0);
        client.swipeWhileNotFound("Down", 150, 2000, "WEB", "xpath=//*[@text='Apple Store' and @nodeName='H3']", 0, 1000, 2, true);
        client.click("WEB", "xpath=//*[@text='Apple Store App']", 0, 1);
        client.click("NATIVE", "xpath=//*[@text='Back to UICatalog']", 0, 1);
        client.click("WEB", "xpath=//*[@text='Apple Store' and @nodeName='H3']", 0, 1);
        client.swipeWhileNotFound("Down", 100, 2000, "WEB", "xpath=//*[@text='About Apple']", 0, 1000, 5, true);
        client.swipeWhileNotFound("Down", 100, 2000, "WEB", "xpath=//*[@text='Contact Apple']", 0, 1000, 5, true);
        client.swipeWhileNotFound("Down", 100, 2000, "WEB", "xpath=//*[@text='Mexico']", 0, 1000, 5, true);
        client.click("NATIVE", "xpath=//*[@text='Back']", 0, 1);
        client.applicationClose("com.experitest.UICatalog");
    }


}
