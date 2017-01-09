package Tests;

import static org.junit.Assert.fail;

import FrameWork.AbsTest;
import FrameWork.Command;

import java.util.Map;


public class WebTabs extends AbsTest {

    public WebTabs(String deviceToTest, String deviceQuery, String testName, Map<String, Command> commandMap) {
        super(deviceToTest, deviceQuery, testName, commandMap);
    }

    @Override
    protected void AndroidRunTest() {

    }

    @Override
    protected void IOSRunTest() {
        client.launch("safari:http://www.ebay.com", true, false);
        // ebay test
        ebayTest("back bag");

        // add new tab and open espn
        client.click("NATIVE", "xpath=//*[@text='Pages']", 0, 1);
        if (client.getProperty("deviceName.model").contains("Pad"))
            client.click("NATIVE", "xpath=//*[@text='New tab']", 0, 1);
        else
            client.click("NATIVE", "xpath=//*[@text='New page']", 0, 1);
        client.elementSendText("NATIVE", "xpath=//*[@text='Address']", 0, "http://www.google.co.il");

        client.sendText("{Enter}");
        // espn site test
        GoogleTest("Manchester United");
        // switch tab
        switchTabIos(1);
        ebayTest("manchester united");
        switchTabIos(2);
        GoogleTest("Chelsea");
    }

    private void switchTabIos(int i) {
        client.click("NATIVE", "xpath=//*[@text='Pages']", 0, 1);
        // switch to tab 1
        if (i == 1) {
            client.click("NATIVE", "xpath=//*[@text='Electronics, Cars, Fashion, Collectibles, Coupons and More | eBay']", 0, 1);
        }
        // switch to tab 2
        else
            client.click("NATIVE", "xpath=//*[@text='Google']", 0, 1);
    }


    void ebayTest(String stuffTosearch) {
        if (client.getProperty("deviceName.model").contains("Pad")) {
            client.elementSendText("WEB", "xpath=//*[@id='gh-ac']", 0, stuffTosearch);
            client.click("WEB", "xpath=//*[@id='gh-btn']", 0, 1);
            client.waitForElement("WEB", "xpath=//*[@text='Auction']", 0, 30000);
            client.click("WEB", "xpath=//*[@id='gh-la']", 0, 1);

        } else {
            client.elementSendText("WEB", "xpath=//*[@id='kw']", 0, stuffTosearch);
            client.click("WEB", "xpath=//*[@id='searchTxtBtn']", 0, 1);
            client.waitForElement("WEB", "xpath=//*[@text='Auction']", 0, 30000);
            client.click("WEB", "xpath=//*[@id='gh-mlogo']", 0, 1);
        }
    }

    void GoogleTest(String searchTeam) {
        client.elementSendText("WEB", "xpath=//*[@id='lst-ib']", 0, searchTeam);
        client.click("WEB", "xpath=//*[@name='btnG']", 0, 1);
        client.click("WEB", "xpath=//*[@class='_Ejh']", 0, 1);
    }


}



