package Tests;

import FrameWork.AbsTest;
import FrameWork.MyClient;

/**
 * Created by navot.dako on 6/19/2016.
 */
public class Rebooting extends AbsTest {


    public Rebooting (MyClient client, String deviceQuery, int repNum, String reportFolder, String deviceToTest, String testName){
        super( client,  deviceQuery,  repNum,  reportFolder,  deviceToTest, testName);
    }

    @Override
    protected void AndroidRunTest() {
        //HUAWEI - @id='dock_divider'
        String HOME_SCREEN = "xpath=//*[contains(@contentDescription,'App') or contains(@contentDescription,'apps')  or @contentDescription='Xperia™ Home' or @id='dock_divider']";
        client.verifyElementFound("NATIVE", HOME_SCREEN, 0);
        for (int i = 0; i < 2 ; i++) {
            client.reboot(150000);
            client.sendText("{HOME}");
            client.sendText("{UNLOCK}");
            client.verifyElementFound("NATIVE", HOME_SCREEN, 0);
        }
    }

    @Override
    protected void IOSRunTest() {
        client.verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Settings']", 0);
        for (int i = 0; i < 2 ; i++) {
            client.reboot(150000);
            if (client.isElementFound("NATIVE", "xpath=//*[contains(@text,'OK') or contains(@contentDescription,'OK')]", 0))
                client.click("NATIVE", "xpath=//*[contains(@text,'OK') or contains(@contentDescription,'OK')]", 0,1);
            client.sendText("{HOME}");
            client.sendText("{UNLOCK}");
            client.verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Settings']", 0);
        }
    }



}
