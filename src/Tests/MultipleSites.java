package Tests;

import FrameWork.AbsTest;
import FrameWork.Command;
import FrameWork.MyClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by richi.lebovich on 9/4/2016.
 */
public class MultipleSites extends AbsTest {

    public MultipleSites(String deviceToTest, String deviceQuery, String testName, Map<String, Command> commandMap) {
        super(deviceToTest, deviceQuery, testName, commandMap);
    }

    @Override
    protected void AndroidRunTest() {
        Map<String, String> sites = getSites();
        String prifix = "chrome:";
        String homeIdentifier = "//*[@class='android.widget.TextView']";
        client.setProperty("chrome.load.timeout", "1000");

        for (Map.Entry site : sites.entrySet()) {
            client.launch(prifix + site.getKey(), true, true);
            try {
                client.hybridWaitForPageLoad(20000);
            } catch (Exception e) {
                client.report("hybridWaitForPageLoad - Trying again",false);
                client.hybridWaitForPageLoad(20000);
            }
            client.sync(1000,1,30000);
            client.verifyElementFound("WEB", "" + site.getValue(), 0);
            int count = client.getElementCount("web", "" + site.getValue());
            if (count > 1) {
                client.report("MoreThenOne", false);
            }
            client.deviceAction("home");
            client.verifyElementFound("Native", homeIdentifier, 0);
        }
    }

    private Map<String, String> getSites() {
        Map<String, String> sitesMap = new HashMap<>();
        //sitesMap.put("www.cnn.com", "//*[@id='logo']");
        sitesMap.put("www.bbc.com", "//*[@alt='BBC']");
        sitesMap.put("www.google.com", "//*[@id='hplogo']");
        sitesMap.put("www.amazon.com", "//*[@class='nav-logo-base nav-sprite']");
        sitesMap.put("www.apple.com", "//*[@id='ac-gn-firstfocus-small' or @id='ac-gn-firstfocus']");
        //sitesMap.put("www.youtube.com", "xpath=//*[@class='_moec _mvgc']");
        sitesMap.put("www.facebook.com", "xpath=//*[@id='header' or @class='clearfix loggedout_menubar']");
        sitesMap.put("www.baidu.com", "xpath=//*[@id='logo' or @alt='logo'] ");
        sitesMap.put("www.wikipedia.org", "xpath=//*[@alt='WikipediA']");
        sitesMap.put("www.yahoo.com", "xpath=//*[@id='yucs-logo-img']");
        sitesMap.put("www.twitter.com", "xpath=//*[@class='AppBar-icon Icon Icon--twitter' or @text='Welcome to Twitter']");
        sitesMap.put("www.instagram.com", "xpath=//*[@class='_du7bh _soakw coreSpriteLoggedOutWordmark']");
        sitesMap.put("www.reddit.com", "xpath=//*[@class='TopNav-text-vcentering']");
        sitesMap.put("www.linkedin.com", "xpath=//*[@alt='LinkedIn' and @class='lazy-loaded']");
        sitesMap.put("www.aliexpress.com", "xpath=//*[@class='downloadbar-logo']");
        sitesMap.put("www.netflix.com", "xpath=//*[@nodeName='svg']");
        sitesMap.put("www.stackoverflow.com", "xpath=//*[@class='topbar-icon js-site-switcher-button icon-site-switcher-bubble']");
        sitesMap.put("www.imdb.com", "xpath=//*[@class='navbar-link']");
        sitesMap.put("www.paypal.com", "xpath=//*[@text='PayPal' and @class='paypal-img-logo']");
        sitesMap.put("www.dropbox.com", "xpath=//*[@class='dropbox-logo__type' or @alt='Dropbox']");
        sitesMap.put("www.ask.com", "xpath=//*[@class='sb-logo posA']");
        //sitesMap.put("www.espn.com", "xpath=//*[@class='container']");
        return sitesMap;
    }

    @Override
    protected void IOSRunTest() {
        Map<String, String> sites = getSites();
        String prifix = "safari:";
        String homeIdentifier = "//*[@accessibilityLabel='Settings']";

        for (Map.Entry site : sites.entrySet()) {
            client.launch(prifix + site.getKey(), true, false);
         /*   try {
                client.hybridWaitForPageLoad(20000);
            } catch (Exception e) {
                client.report("hybridWaitForPageLoad - Trying again",false);
                client.hybridWaitForPageLoad(20000);
            }*/
            client.sync(2000,1,30000);
            client.syncElements(2000,30000);
            client.verifyElementFound("WEB", "" + site.getValue(), 0);
            int count = client.getElementCount("web", "" + site.getValue());

            client.deviceAction("home");
            client.verifyElementFound("Native", homeIdentifier, 0);
        }

    }
}
