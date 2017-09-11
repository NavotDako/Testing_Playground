package FrameWork;

import Tests.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Suite implements Runnable {

    String deviceOS;
    public String deviceSN = "";
    Map<String, Command> commandMap = new HashMap<>();

    public Suite(String deviceSN) {

        this.deviceSN = deviceSN;
        try {
            if (Runner.GRID) this.deviceOS = Runner.cloudServer.getDeviceOSByUDID(deviceSN);
            else this.deviceOS = Runner.localDeviceManager.getDeviceOSByUDID(deviceSN);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (deviceOS==null){
            System.out.println("Couldn't get the os of - "+deviceSN);
            return;
        }
    }

    @Override
    public void run() {

        new EriBank(deviceOS, deviceSN, "EriBank");
//
//        new LaunchBrowserLoop(deviceOS, deviceSN, "LaunchBrowserLoop");
//
//        new TenFreeApps(deviceOS, deviceSN, "TenFreeApps");
//
//        new NonInstrumented(deviceOS, deviceSN, "Non-Instrumented");
//
//        new eBay(deviceOS, deviceSN, "eBay");
//
//        new MultipleSites(deviceOS, deviceSN, "MultipleSites");


//        if (!test.deviceOS.equals("ios") || test.deviceOSVersion.contains("10")) {
//            test = new AmitApp(deviceOS, deviceSN, "AmitApp");
//        }


        //new WebTabs(deviceOS, deviceQuery, "WebTabs", commandMap);


//        if (!deviceOS.contains("ios")) {
//            test = new SimulateCapture(deviceOS, deviceSN, "SimulateCapture");
//        }

        System.out.println("----------------------------------------- DONE WITH " + deviceSN + "-----------------------------------------");

        //   WriteTimesForCommands();
    }


    public void WriteTimesForCommands() {
        for (Map.Entry<String, Command> entry : commandMap.entrySet()) {
            int sum;
            if (entry.getValue().timeList.size() > 0) {
                sum = 0;
                for (Long t : entry.getValue().timeList)
                    sum += t;
                System.out.println("---------------" + deviceSN + " - " + entry.getValue().commandName + " AVG - " + sum / entry.getValue().timeList.size() + " " + entry.getValue().commandName + " count - " + entry.getValue().timeList.size() + "-----------------");
            }
        }
    }
}
