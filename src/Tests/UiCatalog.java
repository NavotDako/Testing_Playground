package Tests;

import FrameWork.BaseTest;
import FrameWork.Command;

import java.util.Map;

/**
 * Created by richi.lebovich on 8/31/2016.
 */
public class UiCatalog extends BaseTest {

    public UiCatalog(String deviceToTest,String deviceQuery, String testName, Map<String, Command> commandMap) {
        super(deviceToTest, deviceQuery ,testName);
    }

    @Override
    protected void androidRunTest() {

    }

    @Override
    protected void iOSRunTest() {
        client.uninstall("com.experitest.UICatalog");
        client.sleep(500);
        client.install("http://192.168.2.72:8181/iOSApps/UICatalog.ipa",true,false);
        client.launch("com.experitest.UICatalog", true, true);
        client.swipeWhileNotFound("Down", 150, 2000, "NATIVE", "xpath=//*[@accessibilityLabel='eBay']", 0, 1000, 5, true);
        client.verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='http://www.apple.com']", 0);
        client.swipeWhileNotFound("Down", 150, 2000, "WEB", "xpath=//*[@text='Apple Store' and @nodeName='H3']", 0, 1000, 2, true);
        client.click("WEB", "xpath=//*[@text='Apple Store App']", 0, 1);
        client.click("NATIVE", "xpath=//*[@text='Back to UICatalog']", 0, 1);
        client.click("WEB", "xpath=//*[@text='Apple Store' and @nodeName='H3']", 0, 1);
        client.swipeWhileNotFound("Down", 0, 2000, "WEB", "xpath=//*[@text='About Apple']", 0, 1000, 5, true);
        client.swipeWhileNotFound("Down", 0, 2000, "WEB", "xpath=//*[@text='Contact Apple']", 0, 1000, 5, true);
        client.swipeWhileNotFound("Down", 0, 2000, "WEB", "xpath=//*[@text='Mexico']", 0, 1000, 5, true);
        client.click("NATIVE", "xpath=//*[@text='Back']", 0, 1);
        client.applicationClose("com.experitest.UICatalog");
    }


}
