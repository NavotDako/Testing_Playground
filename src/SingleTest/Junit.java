package SingleTest;//package <set your test package>;
import com.experitest.client.*;
import org.junit.*;
/**
 *
 */
public class Junit {
    private String host = "localhost";
    private int port = 8889;
    protected Client client = null;

    @Before
    public void setUp(){
        client = new Client(host, port, true);
        client.getDevicesInformation();
        client.setReporter("xml", "C:\\temp\\Reports", "junit");
        String device = client.waitForDevice("@os='android'", 10000);
        System.out.println("ThreadID "+Thread.currentThread().getId()+ " - "+ device.substring(device.indexOf(":")+1));
        client.openDevice();
    }

    @Test
    public void Instrumented(){
        client.install("http://192.168.2.72:8181/AndroidApps/com.experitest.ExperiBank.LoginActivity.2.apk",true,false);
        client.launch("com.experitest.ExperiBank/.LoginActivity", true, true);
        client.syncElements(3000,15000);
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
        client.elementListSelect("", "text=Argentina", 0, false);
        client.click("NATIVE", "text=Argentina", 0, 1);
        client.verifyElementFound("NATIVE", "text=Argentina", 0);
        client.verifyElementFound("NATIVE", "text=Send Payment", 0);
        client.click("NATIVE", "text=Send Payment", 0, 1);
        client.click("NATIVE", "text=Yes", 0, 1);
        client.click("NATIVE", "text=Logout", 0, 1);
        client.uninstall("com.experitest.ExperiBank/.LoginActivity");

    }


    @Test
    public void nonInstrumeted() {
        String settingsXpath = "xpath=//*[(contains(@contentDescription,'ettings') or @text='Settings') and not(contains(@text,'Edit')) and not (contains(@contentDescription,'Edit')) or contains(@id,'settings_button')]";
        client.swipe("Up", 0, 500);
        if(!client.isElementFound("NATIVE", settingsXpath, 0)){
            client.swipe("Up", 0, 500);
        }
        client.click("NATIVE", settingsXpath, 0, 1);
        client.syncElements(3000,10000);
        int i=0;

        if (client.isElementFound("NATIVE", "xpath=//*[@text='General' and @id='tab_custom_view_text']", 0))
            client.click("NATIVE", "xpath=//*[@text='General' and @id='tab_custom_view_text']", 0, 1);

        client.swipeWhileNotFound("Down", 350, 2000, "NATIVE", "xpath=//*[contains(@text,'About') and @onScreen='true']", 0, 1000, 5, true);

        if (!client.isElementFound("NATIVE", "xpath=//*[@text='Android version' and @id='title']", 0)) {
            client.swipeWhileNotFound("Down", 350, 2000, "NATIVE", "xpath=//*[(contains(@text,'oftware info'))]", 0, 1000, 5, true);
            client.verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Android version')]", 0);
            client.click("NATIVE", "xpath=//*[@contentDescription='Navigate up' or @contentDescription='Back']", 0, 1);
        }

        if (client.isElementFound("NATIVE", "xpath=//*[@contentDescription='Navigate up' or @contentDescription='Back' or @id='up']", 0))
            client.click("NATIVE", "xpath=//*[@contentDescription='Navigate up' or @contentDescription='Back' or @id='up']", 0, 1);

        client.verifyElementFound("NATIVE", "xpath=//*[contains(@contentDescription,'App') or contains(@contentDescription,'apps')  or @contentDescription='Xperia™ Home']", 0);

    }
    @After
    public void tearDown(){

        client.generateReport(false);

        client.releaseClient();
    }
}
