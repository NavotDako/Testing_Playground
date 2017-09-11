package Tests;

import FrameWork.BaseTest;
import FrameWork.Command;

import java.util.HashMap;
import java.util.Map;

public class MultipleSites extends BaseTest {

    public MultipleSites(String deviceOS, String deviceSN, String testName) {

        super(deviceOS, deviceSN, testName);
    }


    @Override
    protected void androidRunTest() {
        Map<String, String> sites = getSites();
        String prefix = "chrome:";
        String homeIdentifier = "//*[@class='android.widget.TextView']";
        client.setProperty("chrome.load.timeout", "1000");

        for (Map.Entry site : sites.entrySet()) {
            client.launch(prefix + site.getKey(), true, true);
            try {
                client.hybridWaitForPageLoad(20000);
            } catch (Exception e) {
                client.report("hybridWaitForPageLoad - Trying again",false);
                client.launch(prefix + site.getKey(), true, true);
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
        sitesMap.put("www.bbc.com", "//*[@alt='BBC']");
        sitesMap.put("www.amazon.com", "//*[@class='nav-logo-base nav-sprite']");
        sitesMap.put("www.apple.com", "//*[@id='ac-gn-firstfocus-small' or @id='ac-gn-firstfocus']");
        sitesMap.put("www.facebook.com", "//*[@id='header' or @class='clearfix loggedout_menubar']");
        sitesMap.put("www.wikipedia.org", "//*[@alt='WikipediA']");
        sitesMap.put("www.reddit.com", "//*[@class='TopNav-text-vcentering']");
        sitesMap.put("www.linkedin.com", "//*[@alt='LinkedIn' and @class='lazy-loaded']");
        sitesMap.put("www.stackoverflow.com", "//*[@class='topbar-icon js-site-switcher-button icon-site-switcher-bubble' or @text='Stack Overflow']");
        sitesMap.put("www.imdb.com", "//*[@class='navbar-link' or @text='IMDb']");
        sitesMap.put("www.paypal.com", "//*[@text='PayPal' and @class='paypal-img-logo']");



//        sitesMap.put("www.instagram.com", "//*[@class='_du7bh _soakw coreSpriteLoggedOutWordmark']");
//        sitesMap.put("www.google.com", "//*[@id='hplogo']");
//        sitesMap.put("www.netflix.com", "//*[@nodeName='svg']");
//        sitesMap.put("www.dropbox.com", "//*[@class='dropbox-logo__type' or @alt='Dropbox']");
//        sitesMap.put("www.yahoo.com", "xpath=//*[@id='yucs-logo-img']");
//        sitesMap.put("www.cnn.com", "//*[@id='logo']");
//        sitesMap.put("www.youtube.com", "xpath=//*[@class='_moec _mvgc']");
//        sitesMap.put("www.baidu.com", "xpath=//*[@id='logo' or @alt='logo'] ");
//        sitesMap.put("www.twitter.com", "xpath=//*[@class='AppBar-icon Icon Icon--twitter' or @text='Welcome to Twitter']");
//        sitesMap.put("www.aliexpress.com", "xpath=//*[@class='downloadbar-logo'or @text='AliExpress']");
//        sitesMap.put("www.ask.com", "xpath=//*[@class='sb-logo posA' or @class='sb-logo']");
//        sitesMap.put("www.espn.com", "xpath=//*[@class='container']");
        return sitesMap;
    }

    @Override
    protected void iOSRunTest() {
        Map<String, String> sites = getSites();
        String prefix = "safari:";
        String homeIdentifier = "//*[@accessibilityLabel='Settings']";

        for (Map.Entry site : sites.entrySet()) {
            client.launch(prefix + site.getKey(), true, true);
            try {
                client.hybridWaitForPageLoad(20000);
            } catch (Exception e) {
                client.report("hybridWaitForPageLoad - Trying again",false);
                client.launch(prefix + site.getKey(), true, true);
                client.hybridWaitForPageLoad(20000);
            }
            client.verifyElementFound("WEB", "" + site.getValue(), 0);
            int count = client.getElementCount("web", "" + site.getValue());

            client.deviceAction("home");
            client.verifyElementFound("Native", homeIdentifier, 0);
        }

    }
}
