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
    private AbsTest test;

    public Suite(int repNum, String reportFolder, String deviceToTest, String deviceQuery) {
        Suite.repNum = repNum;
        this.reportFolder = reportFolder;
        this.deviceOS = deviceToTest;
        this.deviceQuery = deviceQuery;
    }

    @Override
    public void run() {

       test = new EriBank(deviceOS, deviceQuery, "EriBank", commandMap);
        updateDeviceProperties(test);

        test = new AmitApp(deviceOS, deviceQuery, "AmitApp", commandMap);
        updateDeviceProperties(test);

        test = new TenFreeApps(deviceOS, deviceQuery, "TenFreeApps", commandMap);
        updateDeviceProperties(test);

        test = new NonInstrumented(deviceOS, deviceQuery, "Non-Instrumented", commandMap);
        updateDeviceProperties(test);


        test = new LaunchBrowserLoop(deviceOS, deviceQuery, "LaunchBrowserLoop", commandMap);
        updateDeviceProperties(test);

        new eBay(deviceOS, deviceQuery, "eBay", commandMap);


        /* new WebTabs(deviceOS, deviceQuery, "WebTabs", commandMap);*/

       /* new MultipleSites(deviceOS, deviceQuery, "MultipleSites", commandMap);

        if (!deviceOS.contains("ios")) {
            test = new SimulateCapture(deviceOS, deviceQuery, "SimulateCapture", commandMap);
            updateDeviceProperties(test);
        }
*/
        System.out.println("----------------------------------------- DONE WITH " + deviceName + "-----------------------------------------");

        WriteTimesForCommands();
    }

    private void updateDeviceProperties(AbsTest test) {
        if (test.deviceSN != null && !deviceQuery.contains("serialnumber")) {
            deviceName = test.deviceName;
            deviceQuery = "and @serialnumber='" + test.deviceSN + "'";
        }
    }

    public void WriteTimesForCommands() {
        for (Map.Entry<String, Command> entry : commandMap.entrySet()) {
            int sum;
            if (entry.getValue().timeList.size() > 0) {
                sum = 0;
                for (Long t : entry.getValue().timeList)
                    sum += t;
                System.out.println("---------------" + deviceName + " - " + entry.getValue().commandName + " AVG - " + sum / entry.getValue().timeList.size() + " " + entry.getValue().commandName + " count - " + entry.getValue().timeList.size() + "-----------------");
            }
        }
    }

    public String setDeviceQuery() {
        return System.setProperty(Thread.currentThread().getName(), deviceQuery);
    }

}
