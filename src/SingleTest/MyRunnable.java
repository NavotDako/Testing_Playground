package SingleTest;

import com.experitest.client.Client;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyRunnable implements Runnable {
    Client client = null;
    String device= null;
    SimpleDateFormat sdf;

    public MyRunnable(String host, int port, String query) {
        sdf = new SimpleDateFormat("HH:mm:ss");
        client = new Client(host, port, true);
        client.setReporter("xml", "reports", "MyRunnable");
        String device = client.waitForDevice(query, 10000);
        System.out.println("ThreadID "+Thread.currentThread().getId()+ " - "+ device.substring(device.indexOf(":")+1));
        client.openDevice();
        client.sendText("{UNLOCK}");
        client.sendText("{HOME}");
        client.startLoggingDevice(client.setReporter("xml", "reports", "SetNetworkConditions"));
    }

    @Override
    public void run() {

        try {
           // Instrumented();

            Non_Instrumented();

        } finally {
            client.stopLoggingDevice();
            client.generateReport(false);
            client.releaseClient();
            System.out.println(sdf.format(new Date(System.currentTimeMillis())) +" - ****************** # Success # ******************");
        }

    }

    public void Non_Instrumented() {
        client.click("NATIVE", "xpath=//*[@accessibilityLabel='Clock']", 0, 1);
        client.waitForElement("NATIVE", "xpath=//*[@text='World Clock' and (@knownSuperClass='UITabBarButton' or @class='UIAButton')]", 0, 10000);
        client.click("NATIVE", "xpath=//*[@text='World Clock' and (@knownSuperClass='UITabBarButton' or @class='UIAButton')]", 0, 1);
        if(!client.isElementFound("NATIVE", "xpath=/*//*[@text='Add' and @x>0 and @onScreen='true']")){
            String firstCountry = client.getAllValues("NATIVE", "xpath=//*[@text='World clocks']*//*[1]", "text")[0];
            deleteCountry(firstCountry);
        }
        client.click("NATIVE", "xpath=//*[@text='Add' and @x>0 and @onScreen='true']", 0, 1);
        client.click("NATIVE", "xpath=//*[@knownSuperClass='UISearchBarTextField' or @class='UIASearchBar']", 0, 1);

        client.sendText("LONDON");
        client.waitForElement("NATIVE", "xpath=/*//*[@text='London, England']", 0, 10000);
        client.click("NATIVE", "xpath=//*[@text='London, England']", 0, 1);
        deleteCountry("London");
    }

    public void Instrumented() {
        //client.install("C:\\Users\\DELL\\EclipseWorkspace\\Testing Playground STA\\apps\\EriBankO.ipa",true,false);
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

    private void deleteCountry(String countryStr) {
        String deleteElement = "xpath=//*[contains(@text,'Delete " + countryStr + "')]  | //*[contains(@text,'London')]/*/*[@text='Remove clock']";

        client.waitForElement("NATIVE", "xpath=//*[@text='Edit']", 0, 10000);
        client.click("NATIVE", "xpath=//*[@text='Edit']", 0, 1);
        client.verifyElementFound("NATIVE", deleteElement, 0);
        client.click("NATIVE", deleteElement, 0, 1);
        try {
            client.waitForElement("NATIVE", "xpath=//*[@text='Delete']", 0, 10000);
            client.click("NATIVE", "xpath=//*[@text='Delete']", 0, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        client.click("NATIVE", "xpath=//*[@accessibilityLabel='World Clock']", 0, 1);
    }
}