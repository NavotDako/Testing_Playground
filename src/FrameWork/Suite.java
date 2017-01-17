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

    public Suite(int repNum, String reportFolder, String deviceToTest, String deviceQuery) {
        this.repNum = repNum;
        this.reportFolder = reportFolder;
        this.deviceOS = deviceToTest;
        this.deviceQuery = deviceQuery;
    }

    @Override
    public void run() {

        EriBank test = new EriBank(deviceOS, deviceQuery, "EriBank", commandMap);

        deviceName = test.deviceName;
        deviceQuery = " and @serialnumber='" + test.deviceSN + "'";

        new LaunchBrowserLoop(deviceOS, deviceQuery, "LaunchBrowserLoop", commandMap);

        new NonInstrumented(deviceOS, deviceQuery, "Non-Instrumented", commandMap);

        new Web(deviceOS, deviceQuery, "Web", commandMap);

        new TenFreeApps(deviceOS, deviceQuery, "TenFreeApps", commandMap);

        if (!deviceOS.contains("ios")) {
            new SimulateCapture(deviceOS, deviceQuery, "SimulateCapture", commandMap);
        }

        new WebTabs(deviceOS, deviceQuery, "WebTabs", commandMap);

        // new Rebooting(deviceOS, deviceQuery, "Rebooting", commandMap);

        // if (deviceOS.contains("android")) (new OfficeDepot(SetUp(),deviceQuery,repNum,reportFolder,deviceOS, "OfficeDepot")).StartTesting();

        // if (deviceOS.contains("ios")) (new PhilipsWeb(SetUp(),deviceQuery,repNum,reportFolder,deviceOS, "PhilipsWeb")).StartTesting();

        // if (deviceOS.contains("ios")) (new Authentication(SetUp("Authentication"),deviceQuery,repNum,reportFolder,deviceOS, "Authentication")).StartTesting();

        System.out.println("----------------------------------------- DONE WITH " + deviceName + "-----------------------------------------");

        WriteTimesForCommands();
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

}
