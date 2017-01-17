package Tests;//package <set your Memory package>;

import FrameWork.AbsTest;
import FrameWork.Command;

import java.util.Map;

public class EriBank extends AbsTest {


    public EriBank(String deviceToTest,String deviceQuery, String testName, Map<String, Command> commandMap) {
        super(deviceToTest,deviceQuery, testName, commandMap);
    }

    @Override
    protected void AndroidRunTest() {
      /*  client.uninstall("com.experitest.ExperiBank/.LoginActivity");
        try {
            client.install("http://192.168.2.72:8181/AndroidApps/eribank.apk", true, false);
        } catch (Exception e) {
            e.printStackTrace();
            client.install("http://192.168.2.72:8181/AndroidApps/eribank.apk", true, false);
        }*/
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
        if (client.capture()==null) client.report("Can't Get Capture!!!",false);

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
    protected void IOSRunTest() {
       /* client.uninstall("com.experitest.ExperiBankO");
        client.deviceAction("home");
        client.sleep(500);
        client.install("C:\\Users\\DELL\\EclipseWorkspace\\Testing Playground STA\\lib\\EriBankO.ipa", true, false);*/
        client.launch("com.experitest.ExperiBankO", true, true);
        client.syncElements(3000, 15000);
        client.verifyElementFound("NATIVE", "xpath=//*[@accessibilityIdentifier='usernameTextField']", 0);
        client.elementSendText("NATIVE", "xpath=//*[@accessibilityIdentifier='usernameTextField']", 0, "company");
        client.closeKeyboard();
        client.verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Password']", 0);
        client.elementSendText("NATIVE", "xpath=//*[@accessibilityLabel='Password']", 0, "company");
        client.closeKeyboard();
        client.verifyElementFound("NATIVE", "xpath=//*[@text='Login']", 0);
        client.click("NATIVE", "xpath=//*[@text='Login']", 0, 1);
        client.sendText("{LANDSCAPE}");
        client.sleep(1000);
        client.verifyElementFound("NATIVE", "xpath=//*[@text='Make Payment']", 0);
        client.click("NATIVE", "xpath=//*[@text='Make Payment']", 0, 1);
        client.verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Phone']", 0);
        client.elementSendText("NATIVE", "xpath=//*[@accessibilityLabel='Phone']", 0, "050-7937021");
        client.closeKeyboard();
        client.verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Name']", 0);
        client.elementSendText("NATIVE", "xpath=//*[@accessibilityLabel='Name']", 0, "Long Run");
        client.closeKeyboard();
        client.verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Amount']", 0);
        client.elementSendText("NATIVE", "xpath=//*[@accessibilityLabel='Amount']", 0, "100");
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
        client.click("NATIVE", "xpath=//*[@text='Send Payment']", 0, 1);
        client.sleep(500);
        client.click("NATIVE", "xpath=//*[@text='Yes']", 0, 1);
        client.click("NATIVE", "xpath=//*[@text='Logout']", 0, 1);
        client.sendText("{HOME}");
    }

}
