package Play;

import com.experitest.client.Client;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;


public class CheckIdentificationMethod {
    private String host = "localhost";
    private int port = 8889;
    protected Client client = null;

    @Before
    public void setUp(){
        client = new Client(host, port, true);
        client.setReporter("xml", "reports", "Untitled");
        client.setDevice("adb:htc Nexus 9");
    }

    @Test
    public void AndroidTest(){
        Map<String,String> WEB;
        Map<String,String> NATIVE;
        Map<String,String> PHILIPS;

        NATIVE = BuildIOSNative();
        WEB = BuildWeb();
        PHILIPS = BuildPhilips();

        StartExecuteZone(PHILIPS);
        System.out.println("===================================================================");
        System.out.println("===================================================================");
        //StartExecuteZone(WEB);

    }

    public  Map<String,String> BuildIOSNative() {
        Map<String,String> NATIVE = new HashMap<>();
        NATIVE.put("ZONE","native");
        NATIVE.put("APP","com.experitest.ExperiBankO");
        NATIVE.put("NORMAL","xpath=//*[@accessibilityLabel='usernameTextField']");
        NATIVE.put("LONG","xpath=//*[@accessibilityLabel='usernameTextField' and @hidden='false' and @onScreen='true' and @top='false']");
        NATIVE.put("NOT","accessibilityLabel=usernameTextField");
        return NATIVE;
    }

    public  Map<String,String> BuildWeb() {
        Map<String,String> WEB = new HashMap<>();
        WEB.put("ZONE","web");
        WEB.put("APP","http://www.google.com");
        WEB.put("NORMAL","xpath=//*[@id='lst-ib']");
        WEB.put("LONG","xpath=//*[@id='lst-ib' and @name='q' and @hidden='false' and @onScreen='true' and @top='true']");
        WEB.put("NOT","id=lst-ib");
        return WEB;
    }
    public  Map<String,String> BuildPhilips() {
        Map<String,String> Philips = new HashMap<>();
        Philips.put("click","xpath=//*[@nodeName='I' and @class='p-square p-icon-close p-small']");
        Philips.put("clickSearch","xpath=//*[@class='p-icon-magnifier p-icons-items']");
        Philips.put("ZONE","web");
        Philips.put("APP","http://tst.usa.philips.com/c-m/consumer-products");
        Philips.put("NORMAL","xpath=//*[@name='q']");
        Philips.put("LONG","xpath=//*[@nodeName='INPUT' and @name='q' and @placeholder='What are you lookıng for?' and @type='text' and @class='p-search-box' and @hidden='false' and @onScreen='true' and @top='true']");
        Philips.put("NOT","name=q");
        return Philips;
    }

    public void StartExecuteZone(Map<String, String> MAP) {

        long NORMAL = StartExecuteXpath(MAP,"NORMAL");
        long LONG = StartExecuteXpath(MAP,"LONG");
        long NOT = StartExecuteXpath(MAP,"NOT");
        System.out.println("===================================================================");
        System.out.println("===================================================================");
        System.out.println("NORMAL: "+NORMAL +System.lineSeparator()+"LONG: "+LONG + System.lineSeparator()+"NOT: "+ NOT);
    }

    public long StartExecuteXpath(Map<String, String> MAP, String type) {
        client.launch(MAP.get("APP"), true, true);
        if(MAP.get("click")!=null && client.isElementFound(MAP.get("ZONE"),MAP.get("click"),0)){
            client.click(MAP.get("ZONE"),MAP.get("click"),0,1);
        }
        if(MAP.get("clickSearch")!=null){
            client.click(MAP.get("ZONE"),MAP.get("clickSearch"),0,1);
        }

        System.out.println("Starting "+type+" Xpath");

        long before = System.currentTimeMillis();
        for (int i = 0; i < 20; i++) {
            client.click(MAP.get("ZONE"),MAP.get(type),0,1);
            client.elementSendText(MAP.get("ZONE"),MAP.get(type),0,"abc: "+i);
        }
        long time = System.currentTimeMillis() - before;
        System.out.println("Time for "+type +" Xpath - " +time);
        return time;
    }

    @After
    public void tearDown(){
        client.generateReport(false);
        client.releaseClient();
    }
}