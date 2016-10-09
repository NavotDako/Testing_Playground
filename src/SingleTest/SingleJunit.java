package SingleTest;

import com.experitest.client.*;
import org.junit.*;

import java.io.File;

/**
 *
 */
public class SingleJunit {
    private String host = "localhost";
    private int port = 8889;
    protected Client client = null;

    @Before
    public void setUp(){
        client = new Client(host, port, true);
        // client.setProjectBaseDirectory(projectBaseDirectory);
        client.setReporter("xml", "reports", "Untitled");
    }

    @Test
    public void TheTest(){
        String device = client.waitForDevice("@os", 10000);
        client.launch("com.experitest.ExperiBankO", true, true);
        client.syncElements(3000,15000);
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

    @After
    public void tearDown(){
      client.generateReport(false);
        client.releaseClient();
    }
}