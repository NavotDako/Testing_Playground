package Tests;

import static org.junit.Assert.fail;

import FrameWork.AbsTest;
import FrameWork.MyClient;


public class LaunchBrowserLoop extends AbsTest {
    String google = "www.google.com";
    String ebay = "m.ebay.com";
    String springboardIdentifier = "xpath=//*[contains(@contentDescription,'App') or contains(@contentDescription,'apps')  or @contentDescription='Xperia™ Home' or @id='workspace']";

    public LaunchBrowserLoop(MyClient client, String deviceQuery, int repNum, String reportFolder, String deviceToTest, String testName) {
        super(client, deviceQuery, repNum, reportFolder, deviceToTest, testName);
    }

    @Override
    protected void AndroidRunTest() {
        String xpathToVerify="";
        for (int i = 0; i < 10; i++) {
            String site  = (i%2==0) ? ebay : google;
            client.launch("chrome:"+site, true, true);
            client.hybridWaitForPageLoad(30000);
            xpathToVerify  = (i%2==0) ? "//*[@alt='eBay Home page']" : "//*[@id='hplogo']";
            client.verifyElementFound("WEB", "xpath="+xpathToVerify, 0);
            client.sendText("{HOME}");
            //HUAWEI - @id='dock_divider'
            client.verifyElementFound("NATIVE",springboardIdentifier , 0);
        }

    }

    @Override
    protected void IOSRunTest() {
        String xpathToVerify="";
        for (int i = 0; i < 10; i++) {
            String site  = (i%2==0) ? ebay : google;
            client.launch("safari:"+site, true, true);
            client.hybridWaitForPageLoad(30000);
            xpathToVerify  = (i%2==0) ? "//*[@alt='eBay Home page']" : "//*[@id='hplogo']";
            client.verifyElementFound("WEB", "xpath="+xpathToVerify, 0);
            client.sendText("{HOME}");
            client.verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Settings']", 0);
        }

    }
}


