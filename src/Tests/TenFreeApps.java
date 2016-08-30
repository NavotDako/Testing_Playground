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
        client.click("NATIVE" , "xpath=//*[@text='TOP FREE']" , 0 , 1);

        int countOfOnScreenApps;
        ArrayList<String> freeApps = new ArrayList<>();

        while(freeApps.size()<10) {
            countOfOnScreenApps = client.getElementCount("NATIVE", "xpath=//*[@id='play_card']/*[@id='li_title' and @onScreen='true']");
            for (int i = 0; i < countOfOnScreenApps; i++) {
                String temp = client.elementGetText("NATIVE", "xpath=//*[@id='play_card']/*[@id='li_title' and @onScreen='true']", i);
                freeApps.add(temp);
            }
            client.swipeWhileNotFound("Down" , 200 , 3500 , "NATIVE" , "xpath=//*[@id='play_card']/*[@id='li_title' and @onScreen='true' and contains(@text , '" + countOfOnScreenApps+ 1 + "')]" , 0 , 1000 , 1 , false);
        }

        ArrayList<String> freeAppsNoDuplicate = removeDuplicate(freeApps);

        System.out.println("------------------- 10's free apps today ------------------");
        for(int i=0 ; i<10 ; i++) {
            System.out.println(freeAppsNoDuplicate.get(i));
        }
        System.out.println("-----------------------------------------------------------");

        String appNAme = client.getCurrentApplicationName();
        client.applicationClearData(appNAme);
        client.applicationClose(appNAme);
    }

    @Override
    protected void IOSRunTest() {

        client.click("NATIVE" , "xpath=//*[@text='App Store']" , 0 , 1);
        client.click("NATIVE" , "xpath=//*[@text='Top Charts']" , 0 , 1);
        client.click("NATIVE" , "xpath=//*[@text='Free']" , 0 , 1);
        int countOfOnScreenApps;
        ArrayList<String> freeApps = new ArrayList<>();

        while(freeApps.size()<10) {
            countOfOnScreenApps = client.getElementCount("NATIVE", "//*[@text = 'GET' or @text= 'Download' and @onScreen='true']/../../../*[@x=0]");
            for (int i = 0; i < countOfOnScreenApps; i++) {
                    String temp = client.elementGetText("NATIVE", "//*[@text = 'GET' or @text= 'Download' and @onScreen='true']/../../../*[@x=0]", i);
                    freeApps.add(temp);
            }
            client.swipeWhileNotFound("Down" , 200 , 3500 , "NATIVE" , "//*[@text = 'GET' or @text= 'Download' and @onScreen='true']/../../../*[@x=0 and contains(@text , '"+ countOfOnScreenApps+ "']" , 0 , 1000 , 1 , false);
        }

        ArrayList<String> freeAppsNoDuplicate = removeDuplicate(freeApps);
        ArrayList<String> toPrint = new ArrayList<>();
        for(String temp:freeAppsNoDuplicate){
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
        client.applicationClearData(appNAme);
        client.applicationClose(appNAme);

    }

    private static ArrayList<String> removeDuplicate(ArrayList<String> arr){
        Object[] st = arr.toArray();
        for (Object s : st) {
            if (arr.indexOf(s) != arr.lastIndexOf(s)) {
                arr.remove(arr.lastIndexOf(s));
            }
        }
        return arr;
    }

}


