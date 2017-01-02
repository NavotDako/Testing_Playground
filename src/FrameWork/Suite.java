package FrameWork;

import Tests.*;
import com.experitest.client.GridClient;

import java.util.HashMap;
import java.util.Map;


public class Suite implements Runnable{
    int repNum= 0;
    String serverHost = "192.168.2.13";
    String projectBaseDirectory = "C:\\Users\\DELL\\workspace\\project11";
    MyClient client = null;
    int serverPort = 8090;
    String deviceName;
    String reportFolder;
    String deviceOS;
    String deviceQuery = "";
    Map<String, Command> commandMap = new HashMap<>();

    public Suite(int repNum, String reportFolder, String deviceToTest, String deviceQuery){
        this.repNum = repNum;
        this.reportFolder = reportFolder;
        this.deviceOS = deviceToTest;
        this.deviceQuery = deviceQuery;
    }

    @Override
    public void run() {

        //if (deviceOS.contains("ios")) (new Authentication(SetUp("Authentication"),deviceQuery,repNum,reportFolder,deviceOS, "Authentication")).StartTesting();

        new EriBank(SetUp("EriBank"),repNum,reportFolder,deviceOS, "EriBank");

        new NonInstrumented(SetUp("Non-Instrumented"),repNum,reportFolder,deviceOS,"Non-Instrumented");

        new Web(SetUp("Web"),repNum,reportFolder,deviceOS, "Web");

        new LaunchBrowserLoop(SetUp("LaunchBrowserLoop"),repNum,reportFolder,deviceOS, "LaunchBrowserLoop");

        new TenFreeApps(SetUp("TenFreeApps"),repNum,reportFolder,deviceOS, "TenFreeApps");

        if (!deviceOS.contains("ios")) {
            new SimulateCapture(SetUp("SimulateCapture"), repNum, reportFolder, deviceOS, "SimulateCapture");
        }

        //if (deviceOS.contains("android")) (new OfficeDepot(SetUp(),deviceQuery,repNum,reportFolder,deviceOS, "OfficeDepot")).StartTesting();

        // if (deviceOS.contains("ios")) (new PhilipsWeb(SetUp(),deviceQuery,repNum,reportFolder,deviceOS, "PhilipsWeb")).StartTesting();

        // (new Rebooting(SetUp(),deviceQuery,repNum,reportFolder,deviceOS,"Reboot")).StartTesting();

        System.out.println("----------------------------------------- DONE WITH "+ deviceName +"-----------------------------------------");

        WriteTimesForCommands();
    }

    public void WriteTimesForCommands() {
        for (Map.Entry<String, Command> entry: commandMap.entrySet()){
            int sum;
            if(entry.getValue().timeList.size()>0) {
                sum=0;
                for (Long t : entry.getValue().timeList)
                    sum+=t;
                System.out.println("---------------" + deviceName + " - "+entry.getValue().commandName+" AVG - "+sum/entry.getValue().timeList.size()+ " "+ entry.getValue().commandName +" count - "+entry.getValue().timeList.size()+"-----------------");
            }
        }
    }

    public MyClient SetUp(String testName){

        try{
            if (Runner.GRID) {
                GridClient grid = new GridClient("admin", "Experitest2012", "", serverHost, serverPort, false);
                if (deviceName ==null) {
                    System.out.println("@os='"+deviceOS+"'"+deviceQuery);
                    client = new MyClient(commandMap, grid.lockDeviceForExecution(testName, "@os='"+deviceOS+"'"+deviceQuery, repNum * 5, 30000));
                    deviceName = client.getDeviceProperty("device.name");
                } else {
                    System.out.println("@name='"+ deviceName.substring(deviceName.indexOf(":")+1)+"'"+deviceQuery);
                    client = new MyClient(commandMap, grid.lockDeviceForExecution(testName, "@name='"+ deviceName +"'"+deviceQuery, repNum * 5, 30000));
                }
            }else{
                client = new MyClient(commandMap,null);
                if (deviceName ==null) {
                    deviceName = client.waitForDevice("@os = '" + deviceOS + "'" + deviceQuery, 30000);
                } else {
                    client.waitForDevice("@name = '" + deviceName.substring(deviceName.indexOf(":")+1) + "'" + deviceQuery, 30000);
                }


            }

            System.out.println(Thread.currentThread().getName() + " - " + deviceName.substring(deviceName.indexOf(":")));


            if (client!=null) {
                client.setProjectBaseDirectory("C:\\Users\\DELL\\workspace\\project18");
                if(!Runner.GRID) client.openDevice();
                client.deviceAction("Unlock");
            } else {
                System.out.println("WAIT FOR DEVICE FAILED for - "+"@os = '" + deviceOS + "'" + deviceQuery);
            }
            return client;

        }catch(Exception e){
            System.out.println("---------------" + deviceName + " - CAN NOT GET A DEVICE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            return client;

        }



    }
}
		