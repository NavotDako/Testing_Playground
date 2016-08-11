package Tests;

import FrameWork.AbsTest;
import FrameWork.MyClient;

/**
 * Created by navot.dako on 6/19/2016.
 */
public class Rebooting extends AbsTest {


    public Rebooting (MyClient client, String device, int repNum, String reportFolder, String deviceToTest, String testName){
        super( client,  device,  repNum,  reportFolder,  deviceToTest, testName);
    }

    @Override
    protected void AndroidRunTest() {

        client.verifyElementFound("NATIVE", "xpath=//*[contains(@contentDescription,'App') or contains(@contentDescription,'apps')  or @contentDescription='Xperia™ Home']", 0);
        for (int i = 0; i < 3 ; i++) {
        try{
            client.reboot(200000);
        }catch(Exception e){
            e.printStackTrace();
        }
            try{
                client.sendText("{UNLOCK}");
                client.sendText("{HOME}");
                if (!client.isElementFound("NATIVE", "xpath=//*[contains(@contentDescription,'App') or contains(@contentDescription,'apps')  or @contentDescription='Xperia™ Home']", 0)) client.sendText("{UNLOCK}");
                client.verifyElementFound("NATIVE", "xpath=//*[contains(@contentDescription,'App') or contains(@contentDescription,'apps')  or @contentDescription='Xperia™ Home']", 0);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void IOSRunTest() {
        client.verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Settings']", 0);
        String device = client.getDeviceProperty("device.name");
        for (int i = 0; i < 3 ; i++) {

            try{
                client.reboot(200000);
            }catch(Exception e){
                e.printStackTrace();
                client.waitForDevice("@name='"+device+"'",30000);
            }
            try {
                client.sendText("{UNLOCK}");
                client.verifyElementFound("NATIVE", "xpath=//*[@accessibilityLabel='Settings']", 0);
            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }



}
