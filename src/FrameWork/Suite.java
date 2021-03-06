package FrameWork;

import Tests.*;

import java.util.HashMap;
import java.util.Map;


public class Suite implements Runnable {
    static int repNum = 0;

    String deviceName;
    String reportFolder;
    String deviceOS;
    public String deviceQuery = "";
    Map<String, Command> commandMap = new HashMap<>();
    private BaseTest test;

    public Suite(int repNum, String reportFolder, String deviceOS, String deviceQuery) {
        Suite.repNum = repNum;
        this.reportFolder = reportFolder;
        this.deviceOS = deviceOS;
        this.deviceQuery = deviceQuery;
    }

    @Override
    public void run() {

        test = new EriBank(deviceOS, deviceQuery, "EriBank", commandMap);
        updateDeviceProperties(test);

        test = new LaunchBrowserLoop(deviceOS, deviceQuery, "LaunchBrowserLoop", commandMap);
        updateDeviceProperties(test);


        if (!test.deviceOS.equals("ios") || test.deviceOSVersion.contains("10")) {
            test = new AmitApp(deviceOS, deviceQuery, "AmitApp", commandMap);
            updateDeviceProperties(test);

        }

        test = new TenFreeApps(deviceOS, deviceQuery, "TenFreeApps", commandMap);
        updateDeviceProperties(test);

        test = new NonInstrumented(deviceOS, deviceQuery, "Non-Instrumented", commandMap);
        updateDeviceProperties(test);

        test = new eBay(deviceOS, deviceQuery, "eBay", commandMap);
        updateDeviceProperties(test);

        //new WebTabs(deviceOS, deviceQuery, "WebTabs", commandMap);

        new MultipleSites(deviceOS, deviceQuery, "MultipleSites", commandMap);

        if (!deviceOS.contains("ios")) {
            test = new SimulateCapture(deviceOS, deviceQuery, "SimulateCapture", commandMap);
            updateDeviceProperties(test);
        }

        System.out.println("----------------------------------------- DONE WITH " + deviceName + "-----------------------------------------");

        WriteTimesForCommands();
    }

    private void updateDeviceProperties(BaseTest test) {
        if (test.deviceSN != null && !deviceQuery.contains("serialnumber")) {
            deviceName = test.deviceName;
            deviceQuery = "@serialnumber='" + test.deviceSN + "'";
        }
    }

    public void WriteTimesForCommands() {
        for (Map.Entry<String, Command> entry : commandMap.entrySet()) {
            int sum;
            if (entry.getValue().timeList.size() > 0) {
                sum = 0;
                for (Long t : entry.getValue().timeList)
                    sum += t;
                System.out.println("---------------" + deviceQuery.replace("'", "").replace("@serialnumber=", "") + " - " + entry.getValue().commandName + " AVG - " + sum / entry.getValue().timeList.size() + " " + entry.getValue().commandName + " count - " + entry.getValue().timeList.size() + "-----------------");
            }
        }
    }
}
