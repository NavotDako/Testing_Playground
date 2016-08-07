package SingleTest;//package <set your test package>;
import com.experitest.client.*;
import org.junit.*;
/**
 *
 */
public class Junit {
    private String host = "localhost";
    private int port = 8889;
    private String projectBaseDirectory = "C:\\Users\\DELL\\workspace\\project18";
    protected Client client = null;

    @Before
    public void setUp(){
        client = new Client(host, port, true);
        client.setProjectBaseDirectory(projectBaseDirectory);
        client.setReporter("xml", "C:\\temp\\reports", "junit");
        String device = client.waitForDevice("@os = 'ios'", 10000);
        System.out.println("ThreadID "+Thread.currentThread().getId()+ " - "+ device.substring(device.indexOf(":")+1));
        //client.openDevice();
    }

    @Test
    public void testSetNetworkConditions(){

        client.install("http://192.168.2.72:8181/iOSApps/EriBankO.ipa",true,false);
        String firstCountry = client.getAllValues("NATIVE", "xpath=//*[@knownSuperClass='UILabel' and not(contains(@text,'Today')) and not(contains(@text,':'))]", "text")[0];
        client.reboot(120000);

    }


  /*  @Test
    public void nonInstrumeted() {

        String settingsXpath = "xpath=/*//*[(contains(@contentDescription,'ettings') or @text='Settings') and not(contains(@text,'Edit')) and not (contains(@contentDescription,'Edit')) or contains(@id,'settings_button')]";
        client.swipe("Up", 0, 500);
        if (!client.isElementFound("NATIVE", settingsXpath, 0)) {
            client.swipe("Up", 0, 500);
        }
        client.click("NATIVE", settingsXpath, 0, 1);
        client.syncElements(3000, 10000);
        int i = 0;

        if (client.isElementFound("NATIVE", "xpath=/*//*[@text='General' and @id='tab_custom_view_text']", 0))
            client.click("NATIVE", "xpath=/*//*[@text='General' and @id='tab_custom_view_text']", 0, 1);

        client.swipeWhileNotFound("Down", 350, 2000, "NATIVE", "xpath=/*//*[contains(@text,'About') and @onScreen='true']", 0, 1000, 5, true);

        if (!client.isElementFound("NATIVE", "xpath=/*//*[@text='Android version' and @id='title']", 0)) {
            client.swipeWhileNotFound("Down", 350, 2000, "NATIVE", "xpath=/*//*[(contains(@text,'oftware info'))]", 0, 1000, 5, true);
            client.verifyElementFound("NATIVE", "xpath=/*//*[contains(@text,'Android version')]", 0);
            client.click("NATIVE", "xpath=/*//*[@contentDescription='Navigate up' or @contentDescription='Back']", 0, 1);
        }

        if (client.isElementFound("NATIVE", "xpath=/*//*[@contentDescription='Navigate up' or @contentDescription='Back' or @id='up']", 0))
            client.click("NATIVE", "xpath=/*//*[@contentDescription='Navigate up' or @contentDescription='Back' or @id='up']", 0, 1);

    }*/
    @After
    public void tearDown(){
        // Generates a report of the test case.
        // For more information - https://docs.experitest.com/display/public/SA/Report+Of+Executed+Test
        client.generateReport(false);
        // Releases the client so that other clients can approach the agent in the near future.
        client.releaseClient();
    }
}
