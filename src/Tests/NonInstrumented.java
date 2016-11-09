package Tests;//package <set your Memory package>;
import static org.junit.Assert.fail;

import FrameWork.AbsTest;
import FrameWork.MyClient;

public class NonInstrumented extends AbsTest {


    public NonInstrumented(MyClient client, String device, int repNum, String reportFolder, String deviceToTest, String testName){
        super( client,  device,  repNum,  reportFolder,  deviceToTest, testName);
    }

    @Override
    protected void AndroidRunTest() {
        String settingsXpath = "xpath=//*[((contains(@contentDescription,'ettings') or @text='Settings') and not(contains(@text,'quick')) and not(contains(@text,'Edit')) and not (contains(@contentDescription,'Edit')) or contains(@id,'settings_button') or @id='settings_button' or @id='setting_button' ) and not(contains(@contentDescription,'settings.'))]";
        String springboardIdentifier = "xpath=//*[contains(@contentDescription,'App') or contains(@contentDescription,'apps')  or @contentDescription='Xperia™ Home' or @id='workspace']";
        client.swipe("Up", 0, 500);


        if(!client.isElementFound("NATIVE", settingsXpath, 0)){
            if (client.isElementFound("NATIVE","xpath=//*[@text='Shortcuts' and @id='toolbox_bt']",0))
                client.click("NATIVE","xpath=//*[@text='Shortcuts' and @id='toolbox_bt']",0,1);
            else client.swipe("Up", 0, 500);
        }
        client.click("NATIVE", settingsXpath, 0, 1);
        client.syncElements(3000,10000);
        int i=0;
        //HUAWEI - @text='More' and not(@class='android.widget.TextView'
        if (client.isElementFound("NATIVE","xpath=//*[@text='More' and not(@class='android.widget.TextView')]",0))
            client.click("NATIVE","xpath=//*[@text='More' and not(@class='android.widget.TextView')]",0,1);


        if (client.isElementFound("NATIVE", "xpath=//*[@text='General' and @id='tab_custom_view_text']", 0))
            client.click("NATIVE", "xpath=//*[@text='General' and @id='tab_custom_view_text']", 0, 1);

        if (client.isElementFound("NATIVE","xpath=//*[@id='headers']",0))
            client.elementSwipeWhileNotFound("NATIVE", "xpath=//*[@id='headers']","Down", 0, 1000, "Native",  "xpath=//*[contains(@text,'About') and @onScreen='true']", 0, 1000, 10, false);

        client.swipeWhileNotFound("Down", 350, 2000, "NATIVE", "xpath=//*[contains(@text,'About') and @onScreen='true']", 0, 1000, 10, true);

        if (!client.isElementFound("NATIVE", "xpath=//*[@text='Android version' and @id='title']", 0)) {
            client.swipeWhileNotFound("Down", 350, 2000, "NATIVE", "xpath=//*[(contains(@text,'oftware info'))]", 0, 1000, 5, true);
            client.verifyElementFound("NATIVE", "xpath=//*[contains(@text,'Android version')]", 0);
            client.click("NATIVE", "xpath=//*[@contentDescription='Navigate up' or @contentDescription='Back']", 0, 1);
        }

        if (client.isElementFound("NATIVE", "xpath=//*[@contentDescription='Navigate up' or @contentDescription='Back' or @id='up']", 0))
            client.click("NATIVE", "xpath=//*[@contentDescription='Navigate up' or @contentDescription='Back' or @id='up']", 0, 1);

        client.sendText("{HOME}");
        client.verifyElementFound("NATIVE",springboardIdentifier, 0);

    }

    @Override
    protected void IOSRunTest() {
        String countriesString = "xpath=//*[(@knownSuperClass='UILabel' or @knownSuperClass='UICollectionViewCell' or @knownSuperClass='UIAccessibilityElement') and (not(contains(@text,'Today')) and not(contains(@text,':')) and not(contains(@text,'Add')))][1]";
        client.sendText("{HOME}");
        client.click("NATIVE", "xpath=//*[@accessibilityLabel='Clock']", 0, 1);
        client.waitForElement("NATIVE", "xpath=//*[@text='World Clock' and (@knownSuperClass='UITabBarButton' or @class='UIAButton')]", 0, 10000);
        client.click("NATIVE", "xpath=//*[@text='World Clock' and (@knownSuperClass='UITabBarButton' or @class='UIAButton')]", 0, 1);

        if(!client.isElementFound("NATIVE", "xpath=/*//*[@text='Add' and @x>0 and @onScreen='true']")){
            String firstCountry = client.getAllValues("NATIVE", countriesString, "text")[0];
            deleteCountry(firstCountry);
        }
        client.verifyElementFound("NATIVE", "xpath=/*//*[@text='Add' and @x>0 and @onScreen='true']", 0);
        client.click("NATIVE", "xpath=//*[@text='Add' and @x>0 and @onScreen='true']", 0, 1);
        client.click("NATIVE", "xpath=//*[@knownSuperClass='UISearchBarTextField' or @class='UIASearchBar']", 0, 1);

        client.sendText("LONDON");
        client.waitForElement("NATIVE", "xpath=/*//*[@text='London, England']", 0, 10000);
        client.click("NATIVE", "xpath=//*[@text='London, England']", 0, 1);
        deleteCountry("London");
        client.capture(); //TEST
        client.sendText("{HOME}");
        client.verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Settings']", 0);
    }

    private void deleteCountry(String countryStr) {
        String deleteElement = "xpath=//*[contains(@text,'Delete " + countryStr + "')]  | //*[contains(@text,'London')]/*/*[@text='Remove clock']";

        client.waitForElement("NATIVE", "xpath=//*[@text='Edit']", 0, 10000);
        client.click("NATIVE", "xpath=//*[@text='Edit']", 0, 1);
        client.verifyElementFound("NATIVE", deleteElement, 0);
        client.click("NATIVE", deleteElement, 0, 1);
        if (client.waitForElement("NATIVE", "xpath=//*[@text='Delete']", 0, 10000))
            client.click("NATIVE", "xpath=//*[@text='Delete']", 0, 1);
        client.click("NATIVE", "xpath=//*[@accessibilityLabel='World Clock']", 0, 1);
    }
}
