package Tests;

import FrameWork.BaseTest;
import FrameWork.Command;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by udi.valer on 8/29/2016.
 */
public class TenFreeApps extends BaseTest {

    public TenFreeApps(String deviceOS, String deviceSN, String testName) {

        super(deviceOS, deviceSN, testName);
    }


    @Override
    protected void androidRunTest() {

        client.launch("com.android.vending/.AssetBrowserActivity", false, true);

        if (client.waitForElement("NATIVE", "xpath=//*[@text='ACCEPT']", 0, 20000)) {
            client.click("NATIVE", "xpath=//*[@text='ACCEPT']", 0, 1);
        }

        if (client.waitForElement("NATIVE", "xpath=//*[@text='TOP CHARTS' or @text='Top Charts']", 0, 20000)) {
            client.click("NATIVE", "xpath=//*[@text='TOP CHARTS'or @text='Top Charts']", 0, 1);
            if (client.waitForElement("NATIVE", "xpath=//*[contains(@text,'TOP FREE')]", 0, 20000)) {
                client.click("NATIVE", "xpath=//*[contains(@text,'TOP FREE')]", 0, 1);
                int countOfOnScreenApps;
                ArrayList<String> freeApps = new ArrayList<>();
                long startTime = System.currentTimeMillis();
                while ((freeApps.size() < 10) || (System.currentTimeMillis() > startTime + 30000)) {
                    countOfOnScreenApps = client.getElementCount("NATIVE", "//*[@id='li_title' and @onScreen='true']");
                    for (int i = 0; i < countOfOnScreenApps; i++) {
                        String temp = client.elementGetText("NATIVE", "//*[@id='li_title' and @onScreen='true']", i);
                        freeApps.add(temp);
                    }
                    client.swipe("Down", 200, 500);
                }

                System.out.println("------------------- 10's free apps today ------------------");
                for (int i = 0; i < 10; i++) {
                    System.out.println(freeApps.get(i));
                }
            }


        } else

        {
            client.report("Can't Find xpath=//*[@text='TOP CHARTS']", false);
            throw new RuntimeException("Can't Find xpath=//*[@text='TOP CHARTS']");

        }

        System.out.println("-----------------------------------------------------------");
        String appNAme = client.getCurrentApplicationName();
        client.applicationClose(appNAme);
    }

    private void sync(int time, int sensitivity, int timeout) {
        boolean b = client.sync(time, sensitivity, timeout);
        if (!b) {
            client.report("first sync failed", false);
            client.sync(time, sensitivity, timeout);
        }
    }

    @Override
    protected void iOSRunTest() {
        client.launch("com.apple.AppStore", false, true);
        client.setProperty("ios.non-instrumented.dump.parameters", "20 , 500 , 25");
        sync(1000, 0, 10000);
        if(client.isElementFound("native","//*[contains@text='Don't Allow']"))
        client.click("NATIVE", "xpath=//*[@text='Top Charts']", 0, 1);
        if(client.isElementFound("native","xpath=//*[@text='All Categories']",0)){
            client.click("native","xpath=//*[@text='Cancel']",0,1);
        }
        if (client.isElementFound("NATIVE", "xpath=//*[@text='Free' and @knownSuperClass='UIButton']", 0))
            client.click("NATIVE", "xpath=//*[@text='Free' and @knownSuperClass='UIButton']", 0, 1);
        else if (!client.isElementFound("web", "xpath=//*[@text='Free' and @class='UIACollectionView']", 0)) {
            client.waitForElement("NATIVE", "xpath=//*[@text='Free' and (@knownSuperClass='UISegment' or @XCElementType='XCUIElementTypeButton')]", 0, 15000);
            client.click("NATIVE", "xpath=//*[@text='Free' and (@knownSuperClass='UISegment' or @XCElementType='XCUIElementTypeButton')]", 0, 1);
        }

        int countOfOnScreenApps;
        ArrayList<String> freeApps = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        String iPadCellString = "//XCUIElementTypeCollectionView[@text='Free']//XCUIElementTypeOther[@text]";
//        String iPhoneCellString = "//XCUIElementTypeCollectionView/XCUIElementTypeCell/XCUIElementTypeOther/XCUIElementTypeOther[@text]";
        String iPhoneCellString = "//XCUIElementTypeCollectionView/XCUIElementTypeCell/XCUIElementTypeOther[@x=0 and @y!=0]";
        String cellIdentifier = (client.getDeviceProperty("device.category").contains("TABLET")) ? iPadCellString : iPhoneCellString;
        while (freeApps.size() < 10 && (System.currentTimeMillis() < startTime + 30000)) {

            countOfOnScreenApps = client.getElementCount("NATIVE", cellIdentifier);
            for (int i = 0; i < countOfOnScreenApps; i++) {
                String temp = client.elementGetText("NATIVE", cellIdentifier, i);
                System.out.println("text - " + temp);
                if (!freeApps.contains(temp))
                    freeApps.add(temp);
            }
            if (client.isElementFound("NATIVE", "xpath=//*[@text='Free' and ./*[@class='UIAView']]", 0))
                client.elementSwipe("NATIVE", "xpath=//*[@text='Free' and ./*[@class='UIAView']]", 0, "Down", 200, 3500);
            else
                client.swipe("Down", 200, 3500);
        }

        ArrayList<String> toPrint = new ArrayList<>();
        for (String temp : freeApps) {
            try {
                String[] toCut = temp.split(",");
                String toAdd = toCut[1];
                toPrint.add(toAdd);
            } catch (Exception e) {
                // e.printStackTrace();
            }
        }

        System.out.println("------------------- " + freeApps.size() + "'s free apps today ------------------");
        for (int i = 0; i < toPrint.size(); i++) {
            System.out.println((i + 1) + ")" + toPrint.get(i));
        }
        System.out.println("-----------------------------------------------------------");

        String appNAme = client.getCurrentApplicationName();
        client.applicationClose(appNAme);

    }

}


