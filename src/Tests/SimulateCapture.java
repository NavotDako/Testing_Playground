package Tests;

import FrameWork.AbsTest;
import FrameWork.MyClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.net.URL;


/**
 * Created by udi.valer on 8/30/2016.
 */
public class SimulateCapture extends AbsTest {

    public SimulateCapture(MyClient client , String device , int repNum , String reportFolder , String deviceToTest , String testName) {
        super(client, device, repNum, reportFolder, deviceToTest, testName);
    }

    @Override
    protected void AndroidRunTest() {
        checkAppData();
        boolean found = false;
        client.install("http://192.168.2.72:8181/AndroidApps/cameraFlash-%20simulateCapture/com.CameraFlash-.MainActivity_ver_11.0.apk" , true , false);
        client.launch("com.CameraFlash/.MainActivity" , true , true);
        File file = new File("lib/SimulateCapture/slider_etstalk1.jpg");
        client.simulateCapture(file.getAbsolutePath());
        client.textFilter("0xA8C02E",80);
        client.getText("TEXT");
        client.verifyElementFound("Text","Let",0);
        client.uninstall("com.CameraFlash/.MainActivity");
    }

    @Override
    protected void IOSRunTest() {

        boolean found = false;
        client.install("http://192.168.2.72:8181/iOSApps/PhotoPicker.ipa" , true , false);
        client.launch("com.example.apple-samplecode.PhotoPicker" , true , true);
        client.sleep(2000);
        File file = new File("lib\\SimulateCapture\\slider_etstalk1.jpg");
        client.simulateCapture(file.getAbsolutePath());
        client.click("NATIVE" , "xpath=//*[@class='_UIToolbarNavigationButton' and ./*[@class='UIImageView']]" , 0 , 1);
        client.sync(1500 , 0 , 6000);
        client.getText("TEXT");
        client.verifyElementFound("Text","Let",0);
        client.uninstall("com.example.apple-samplecode.PhotoPicker");
    }


    private static void checkAppData(){
        String startPath = System.getenv("APPDATA");
        System.out.println("Path from the system is: " + startPath);
        String path = startPath + "\\seetest\\app.properties";
        System.out.println("path is: " + path);
        File file = new File(path);
        boolean found=false;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            found = false;
            String line;
            while ((line = br.readLine()) != null){
                if(line.replace(" ", "").equalsIgnoreCase("android.instrumentation.camera=true")) {
                    found = true;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        if(!found)
            throw  new RuntimeException("App properties doesn't contain the 'android.instrumentation.camera = true' sentence");

    }
}
