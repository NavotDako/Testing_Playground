package Tests;

import FrameWork.AbsTest;
import FrameWork.MyClient;

import java.util.ArrayList;

/**
 * Created by udi.valer on 8/29/2016.
 */
public class TenFreeApps extends AbsTest {

    public  TenFreeApps(MyClient client , String device , int repNum , String reportFolder , String deviceToTest , String testName){
        super(client , device , repNum , reportFolder , deviceToTest , testName);
    }

    @Override
    protected void AndroidRunTest() {

        client.launch("com.android.vending/.AssetBrowserActivity", false, true);
        client.sync(1500 , 0 , 10000);
        if(client.isElementFound("NATIVE" , "xpath=//*[@text=concat('Couldn', \"'\", 't sign in')]" , 0)) {
            client.click("NATIVE", "xpath=//*[@text='Try again']", 0, 1);
            client.elementSendText("NATIVE", "", 0, "Experitest2013");
            client.click("NATIVE", "xpath=//*[@text='Sign in']", 0, 1);
            client.sleep(2000);
            client.click("NATIVE", "xpath=//*[@text='OK']", 0 , 1);
        }
        if(client.isElementFound("NATIVE" , "xpath=//*[@text='ACCEPT']" , 0))
            client.click("NATIVE" , "xpath=//*[@text='ACCEPT']" , 0 , 1);
        client.sync(1500 , 0 , 10000);
        if(client.isElementFound("NATIVE" ,"xpath=//*[@text='TOP CHARTS']" , 0))
            client.click("NATIVE" ,"xpath=//*[@text='TOP CHARTS']" , 0 , 1);
        client.click("NATIVE" , "xpath=//*[contains(@text,'TOP FREE')]" , 0 , 1);

        int countOfOnScreenApps;
        int counterOfEnteredApp = 0;
        ArrayList<String> freeApps = new ArrayList<>();

        while(freeApps.size()<10) {
            countOfOnScreenApps = client.getElementCount("NATIVE", "xpath=//*[@id='play_card']/*[@id='li_title' and @onScreen='true']");
            for (int i = 0; i < countOfOnScreenApps; i++) {
                String temp = client.elementGetText("NATIVE", "xpath=//*[@id='play_card']/*[@id='li_title' and @onScreen='true']", i);
                if(Character.getNumericValue(temp.charAt(0) - 1) == counterOfEnteredApp || (Character.getNumericValue(temp.charAt(0) - 1) == 0 && counterOfEnteredApp == 9)){
                    freeApps.add(temp);
                    counterOfEnteredApp++;
                }
            }
            client.swipeWhileNotFound("Down" , 200 , 3500 , "NATIVE" , "xpath=//*[@id='play_card']/*[@id='li_title' and @onScreen='true' and contains(@text , '" + countOfOnScreenApps+ 1 + "')]" , 0 , 1000 , 1 , false);
        }

        System.out.println("------------------- 10's free apps today ------------------");
        for(int i=0 ; i<10 ; i++) {
            System.out.println(freeApps.get(i));
        }
        System.out.println("-----------------------------------------------------------");

        String appNAme = client.getCurrentApplicationName();
        client.applicationClose(appNAme);
    }

    @Override
    protected void IOSRunTest() {

        client.launch("com.apple.AppStore", false, true);
        client.setProperty("ios.non-instrumented.dump.parameters" , "20 , 500 , 25");
        client.sync(1000 , 0 , 10000);
        client.click("NATIVE" , "xpath=//*[@text='Top Charts']" , 0 , 2);
        if(client.isElementFound("NATIVE" , "xpath=//*[@text='Free' and @knownSuperClass='UIButton']" , 0))
            client.click("NATIVE" , "xpath=//*[@text='Free' and @knownSuperClass='UIButton']" , 0 , 1);
        else
            client.click("NATIVE" , "xpath=//*[@text='Free' and @knownSuperClass='UISegment']" , 0 , 1);

        int countOfOnScreenApps;
        ArrayList<String> freeApps = new ArrayList<>();

        while(freeApps.size()<10) {
            countOfOnScreenApps = client.getElementCount("NATIVE", "//*[@class='UIAView' and @knownSuperClass='UIView' and @enabled='true' and @y>0 and @width>0 and @height>0 and @hidden='false' and @onScreen='true']/../*/*/*[@text='GET' or @text='Download']/../../../*[contains(@text , ',')]");
            for (int i = 0; i < countOfOnScreenApps; i++) {
                    String temp = client.elementGetText("NATIVE", "//*[@class='UIAView' and @knownSuperClass='UIView' and @enabled='true' and @y>0 and @width>0 and @height>0 and @hidden='false' and @onScreen='true']/../*/*/*[@text='GET' or @text='Download']/../../../*[contains(@text , ',')]", i);
                    if(!freeApps.contains(temp))
                        freeApps.add(temp);
            }
            if(client.isElementFound("NATIVE" , "xpath=//*[@text='Free' and ./*[@class='UIAView']]" , 0))
                client.elementSwipe("NATIVE", "xpath=//*[@text='Free' and ./*[@class='UIAView']]", 0 , "Down", 200 , 3500);
            else
                client.swipeWhileNotFound("Down" , 200 , 3500 , "NATIVE" , "//*[@class='UIAView' and @knownSuperClass='UIView' and @enabled='true' and @y>0 and @width>0 and @height>0 and @hidden='false' and @onScreen='true']/../*/*/*[@text='GET' or @text='Download']/../../../*[contains(@text , '"+ (countOfOnScreenApps+2) + ",')]" , 0 , 1000 , 1 , false);
        }

        ArrayList<String> toPrint = new ArrayList<>();
        for(String temp:freeApps){
            String[] toCut = temp.split(",");
            String toAdd = toCut[1];
            toPrint.add(toAdd);
        }

        System.out.println("------------------- 10's free apps today ------------------");
        for(int i=0 ; i<10 ; i++) {
            System.out.println((i + 1) + ")" +toPrint.get(i));
        }
        System.out.println("-----------------------------------------------------------");

        String appNAme = client.getCurrentApplicationName();
        client.applicationClose(appNAme);

    }

}


