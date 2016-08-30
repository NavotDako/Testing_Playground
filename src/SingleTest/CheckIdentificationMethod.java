package SingleTest;

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
    String deviceName = "adb:samsung GT-I9301I";

    @Before
    public void setUp(){
        client = new Client(host, port, true);
        client.setReporter("xml", "reports", "Untitled");
        client.setDevice(deviceName);
    }

    @Test
    public void IdentificationTest(){
        Map<String,String> WEB;
        Map<String,String> iOS_NATIVE;
        Map<String,String> ANDROID_NATIVE;

        iOS_NATIVE = BuildIOSNative();
        WEB = BuildWeb();
        ANDROID_NATIVE = BuildAndroidNative();

        if (deviceName.contains("ios")){
            StartExecuteZone(iOS_NATIVE);
        } else StartExecuteZone(ANDROID_NATIVE);

        System.out.println("===================================================================");
        System.out.println("===================================================================");
        StartExecuteZone(WEB);

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
    public  Map<String,String> BuildAndroidNative() {
        Map<String,String> NATIVE = new HashMap<>();
        NATIVE.put("ZONE","native");
        NATIVE.put("APP","com.experitest.ExperiBank/.LoginActivity");
        NATIVE.put("NORMAL","xpath=//*[@id='usernameTextField']");
        NATIVE.put("LONG","xpath=//*[@id='usernameTextField' and @hint='Username' and @hidden='false' and @onScreen='true' and @top='true']");
        NATIVE.put("NOT","id=usernameTextField");
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
        System.out.println("Starting "+type+" Xpath");
        long before = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
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