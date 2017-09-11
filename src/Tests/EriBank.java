package Tests;

import FrameWork.BaseTest;
import FrameWork.Command;

import java.util.Map;

public class EriBank extends BaseTest {


    public EriBank(String deviceOS, String deviceSN, String testName) {

        super(deviceOS, deviceSN, testName);
    }

    @Override
    protected void androidRunTest() {
        client.install("http://192.168.2.72:8181/AndroidApps/eribank.apk", true, false);
        client.launch("com.experitest.ExperiBank/.LoginActivity", true, true);
        client.syncElements(3000, 15000);
        client.verifyElementFound("NATIVE", "hint=Username", 0);
        client.click("NATIVE", "hint=Username", 0, 1);
        client.elementSendText("NATIVE", "hint=Username", 0, "company");
        client.verifyElementFound("NATIVE", "hint=Password", 0);
        client.elementSendText("NATIVE", "hint=Password", 0, "company");
        client.closeKeyboard();
        client.verifyElementFound("NATIVE", "text=Login", 0);
        client.click("NATIVE", "text=Login", 0, 1);
        client.verifyElementFound("NATIVE", "text=Make Payment", 0);
        client.click("NATIVE", "text=Make Payment", 0, 1);
        client.verifyElementFound("NATIVE", "hint=Phone", 0);
        client.elementSendText("NATIVE", "hint=Phone", 0, "050-7937021");
        client.verifyElementFound("NATIVE", "hint=Name", 0);
        client.elementSendText("NATIVE", "hint=Name", 0, "Long Run");
        client.verifyElementFound("NATIVE", "hint=Amount", 0);
        client.elementSendText("NATIVE", "hint=Amount", 0, "100");
        client.verifyElementFound("NATIVE", "hint=Country", 0);
        client.verifyElementFound("NATIVE", "text=Select", 0);
        client.click("NATIVE", "text=Select", 0, 1);
        client.sleep(1500);
        if (client.capture() == null) client.report("Can't Get Capture!!!", false);

        client.elementListSelect("", "text=Argentina", 0, false);
        client.click("NATIVE", "text=Argentina", 0, 1);
        client.verifyElementFound("NATIVE", "text=Argentina", 0);
        client.verifyElementFound("NATIVE", "text=Send Payment", 0);
        client.click("NATIVE", "text=Send Payment", 0, 1);
        client.click("NATIVE", "text=Yes", 0, 1);
        client.click("NATIVE", "text=Logout", 0, 1);
        client.uninstall("com.experitest.ExperiBank/.LoginActivity");
    }

    @Override
    protected void iOSRunTest() {
        client.install("http://192.168.2.72:8181/iOSApps/EriBank.ipa", true, false);
        client.launch("com.experitest.ExperiBank", true, true);
        client.syncElements(3000, 15000);
        if(client.isElementFound("native","xpath=//*[@text='“EriBankO” May Slow Down Your iPad']",0)){
            client.click("navite","xpath=//*[@text='OK']",0,1);
        }
        client.verifyElementFound("NATIVE", "xpath=//*[@placeholder='Username']", 0);
        client.elementSendText("NATIVE", "xpath=//*[@placeholder='Username']", 0, "company");

        client.verifyElementFound("NATIVE", "xpath=//*[@placeholder='Password']", 0);
        client.elementSendText("NATIVE", "xpath=//*[@placeholder='Password']", 0, "company");
        client.closeKeyboard();

        client.verifyElementFound("NATIVE", "xpath=//*[@text='Login' or @text='loginButton']", 0);
        client.click("NATIVE", "xpath=//*[@text='Login' or @text='loginButton']", 0, 1);
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
        client.click("NATIVE", "xpath=//*[@text='Select']", 0, 1);
        client.elementListSelect("", "text=Argentina", 0, false);
        client.click("NATIVE", "xpath=//*[@accessibilityLabel='Argentina']", 0, 1);
        client.sendText("{PORTRAIT}");
        client.sleep(1000);
        client.verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Argentina']", 0);
        client.verifyElementFound("NATIVE", "xpath=//*[@text='Send Payment']", 0);
        client.syncElements(1000, 5000);
        client.click("NATIVE", "xpath=//*[@text='Send Payment']", 0, 1);
        client.sleep(500);
        client.click("NATIVE", "xpath=//*[@text='Yes']", 0, 1);
        client.click("NATIVE", "xpath=//*[@text='Logout']", 0, 1);
        client.sendText("{HOME}");
        client.uninstall("com.experitest.ExperiBankO");

    }

}
