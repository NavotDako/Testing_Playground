package Tests;

import FrameWork.AbsTest;
import FrameWork.MyClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 9/5/2016.
 */
public class PhilipsWeb extends AbsTest {


    public PhilipsWeb(MyClient client, String device, int repNum, String reportFolder, String deviceToTest, String testName){
        super( client,  device,  repNum,  reportFolder,  deviceToTest,testName );

    }

    @Override
    protected void AndroidRunTest() {
        Map<String,String> Philips = new HashMap<>();
        Philips.put("click","xpath=//*[@nodeName='I' and @class='p-square p-icon-close p-small']");
        Philips.put("clickSearch","xpath=//*[@class='p-icon-magnifier p-icons-items']");
        Philips.put("ZONE","web");
        Philips.put("APP","http://tst.usa.philips.com/c-m/consumer-products");
        Philips.put("NORMAL","xpath=//*[@name='q']");
        Philips.put("LONG","xpath=//*[@nodeName='INPUT' and @name='q' and @placeholder='What are you lookıng for?' and @type='text' and @class='p-search-box' and @hidden='false' and @onScreen='true' and @top='true']");
        Philips.put("NOT","name=q");

        ExecuteByXpath(Philips,"NORMAL");
        ExecuteByXpath(Philips,"LONG");
        ExecuteByXpath(Philips,"NOT");

        String appNAme = client.getCurrentApplicationName();
        client.applicationClose(appNAme);

    }

    private void ExecuteByXpath(Map<String, String> philips,String type) {
        client.launch(philips.get("APP"), true, true);
        client.hybridWaitForPageLoad(25000);
        if(client.isElementFound(philips.get("ZONE"), philips.get("click"),0))
            client.click(philips.get("ZONE"), philips.get("click"),0,1);

        client.click(philips.get("ZONE"), philips.get("clickSearch"),0,1);
        client.hybridWaitForPageLoad(10000);
        for (int i = 0; i < 3; i++) {
            client.click(philips.get("ZONE"), philips.get(type),0,1);
            client.hybridWaitForPageLoad(20000);
            client.elementSendText(philips.get("ZONE"), philips.get(type),0,"abc: "+i);
        }
    }

    @Override
    protected void IOSRunTest() {
        Map<String,String> Philips = new HashMap<>();
        Philips.put("click","xpath=//*[@nodeName='I' and @class='p-square p-icon-close p-small']");
        Philips.put("clickSearch","xpath=//*[@class='p-icon-magnifier p-icons-items']");
        Philips.put("ZONE","web");
        Philips.put("APP","http://tst.usa.philips.com/c-m/consumer-products");
        Philips.put("NORMAL","xpath=//*[@name='q']");
        Philips.put("LONG","xpath=//*[@nodeName='INPUT' and @name='q' and @placeholder='What are you lookıng for?' and @type='text' and @class='p-search-box' and @hidden='false' and @onScreen='true' and @top='true']");
        Philips.put("NOT","name=q");

        ExecuteByXpath(Philips,"NORMAL");
        ExecuteByXpath(Philips,"LONG");
        ExecuteByXpath(Philips,"NOT");
        String appNAme = client.getCurrentApplicationName();
        client.applicationClearData(appNAme);
        client.applicationClose(appNAme);
    }
}
