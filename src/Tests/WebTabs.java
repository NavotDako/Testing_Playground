package Tests;

import static org.junit.Assert.fail;

import FrameWork.BaseTest;
import FrameWork.Command;

import java.util.Map;


public class WebTabs extends BaseTest {

    public WebTabs(String deviceToTest, String deviceQuery, String testName, Map<String, Command> commandMap) {
        super(deviceToTest, deviceQuery, testName, commandMap);
    }

    @Override
    protected void androidRunTest() {
        // launch ebay
        client.launch("chrome:http://www.ebay.com", true, false);

        // ebay test
        ebayTest("back bag");

        // add new tab and open espn
        client.click("NATIVE", "xpath=//*[@id='menu_button']", 0, 1);
        client.sleep(1000);
        client.click("NATIVE", "xpath=//*[@text='New tab']", 0, 1);
        client.click("NATIVE", "xpath=//*[@id='search_box_text']", 0, 1);
        client.elementSendText("NATIVE", "xpath=//*[@id='url_bar']", 0, "http://www.google.co.il");
        client.sendText("{Enter}");

        // google site test
        GoogleTest("Manchester United");

        // switch tab
        switchTabAnd(1);
        ebayTest("manchester united");
        switchTabAnd(2);
        GoogleTest("Chelsea");

    }


    @Override
    protected void iOSRunTest() {
        client.hybridClearCache(true, true);
        client.launch("safari:http://www.ebay.com", true, false);
        // ebay test
        ebayTest("back bag");

        // add new tab and open espn
        client.click("NATIVE", "xpath=//*[@text='Pages']", 0, 1);
        if (client.getProperty("device.model").contains("Pad"))
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
        String capture = client.capture();
        if (client.capture() == null) client.report("Can't Get Capture!!!", false);
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
        client.hybridWaitForPageLoad(10000);
    }


    void ebayTest(String stuffToSearch) {
        if (client.isElementFound("WEB", "xpath=//*[@id='kw']", 0))
            client.elementSendText("WEB", "xpath=//*[@id='kw']", 0, stuffToSearch);
        else {
            client.waitForElement("WEB", "xpath=//*[@id='gh-ac']", 0, 20000);
            client.elementSendText("WEB", "xpath=//*[@id='gh-ac']", 0, stuffToSearch);
        }
        if (client.isElementFound("WEB", "xpath=//*[@id='searchTxtBtn']", 0))
            client.click("WEB", "xpath=//*[@id='searchTxtBtn']", 0, 1);
        else
            client.click("WEB", "xpath=//*[@id='gh-btn']", 0, 1);
        if (client.isElementFound("WEB", "xpath=//*[@class='cl lgoCl']", 0))
            client.click("WEB", "xpath=//*[@class='cl lgoCl']", 0, 1);
        else
            client.click("WEB", "xpath=//*[@id='gh-la']", 0, 1);

        client.sleep(2000);
    }

    void GoogleTest(String searchTeam) {
        client.elementSendText("WEB", "xpath=//*[@id='lst-ib']", 0, searchTeam);
        client.sendText("{Enter}");
        if (client.isElementFound("WEB", "xpath=//*[@class='_Ejh']"))
            client.click("WEB", "xpath=//*[@class='_Ejh']", 0, 1);
        client.sleep(3000);
    }

    private void switchTabAnd(int i) {
        //click switch tab button
        if (client.isElementFound("NATIVE", "xpath=//*[@id='tab_switcher_button']"))
            client.click("NATIVE", "xpath=//*[@id='tab_switcher_button']", 0, 1);
        // switch to tab 1
        client.textFilter("0x5A5A5A", 15);
        if (i == 1) {

            if (client.isElementFound("TEXT", "Electronics")) client.click("TEXT", "Electronics", 0, 1);
            else client.click("TEXT", "ebay", 0, 1);
        }
        // switch to tab 2
        else {

            client.click("TEXT", "Google", 0, 1);
        }
    }


}



