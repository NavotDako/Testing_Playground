package FrameWork;

import Tests.*;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


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
        AbsTest test = new LaunchBrowserLoop(deviceOS, deviceQuery, "LaunchBrowserLoop", commandMap);

        deviceName = test.deviceName;
        deviceQuery = " and @serialnumber='" + test.deviceSN + "'";

        new MultipleSites(deviceOS, deviceQuery, "MultipleSites", commandMap);

        new Web(deviceOS, deviceQuery, "Web", commandMap);

        new EriBank(deviceOS, deviceQuery, "EriBank", commandMap);

        new NonInstrumented(deviceOS, deviceQuery, "Non-Instrumented", commandMap);

        new TenFreeApps(deviceOS, deviceQuery, "TenFreeApps", commandMap);

        if (!deviceOS.contains("ios")) {
            new SimulateCapture(deviceOS, deviceQuery, "SimulateCapture", commandMap);
        }

        new WebTabs(deviceOS, deviceQuery, "WebTabs", commandMap);



        System.out.println("----------------------------------------- DONE WITH " + deviceName + "-----------------------------------------");
       /* setDeviceQuery();
        JUnitCore junit = new JUnitCore();
        Result result = junit.run(SingleTest.InstrumentationTest.class);*/
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
    public String setDeviceQuery(){
        return System.setProperty(Thread.currentThread().getName(),deviceQuery);
    }

}
