package FrameWork;

import Tests.*;

import java.util.HashMap;
import java.util.Map;


public class Suite implements Runnable{
    int repNum= 0;
    String host = "localhost";
    String projectBaseDirectory = "C:\\Users\\DELL\\workspace\\project11";
    MyClient client = null;
    int port= 8889;
    String device;
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
        EriBank eriBank = new EriBank(SetUp(),device,repNum,reportFolder,deviceOS, "EriBank");
        eriBank.runTest();
        NonInstrumented clockTest = new NonInstrumented(SetUp(),device,repNum,reportFolder,deviceOS,"Non-Instrumented");
        clockTest.runTest();
        Web browserTest = new Web(SetUp(),device,repNum,reportFolder,deviceOS, "Web");
        browserTest.runTest();
     /*   LaunchBrowserLoop launchBrowser = new LaunchBrowserLoop(SetUp(),device,repNum,reportFolder,deviceOS, "LaunchBrowserLoop");
        launchBrowser.runTest();
        Rebooting rebootTest = new Rebooting(SetUp(),device,repNum,reportFolder,deviceOS,"Reboot");
        rebootTest.runTest();*/

        System.out.println("----------------------------------------- DONE WITH "+device+"-----------------------------------------");

        WriteTimesForCommands();
    }

    public void WriteTimesForCommands() {
        for (Map.Entry<String, Command> entry: commandMap.entrySet()){
            int sum;
            if(entry.getValue().timeList.size()>0) {
                sum=0;
                for (Long t : entry.getValue().timeList)
                    sum+=t;
                System.out.println("---------------" + device + " - "+entry.getValue().commandName+" AVG - "+sum/entry.getValue().timeList.size()+ " "+ entry.getValue().commandName +" count - "+entry.getValue().timeList.size()+"-----------------");
            }
        }
    }

    public MyClient SetUp(){
        try{
            client = new MyClient(host, port, true,commandMap);
            //client.setProjectBaseDirectory(projectBaseDirectory);
            //( contains(@version,'9.') or contains(@version,'4.') )and
            device = client.waitForDevice("@os = '"+deviceOS+"'"+ deviceQuery, 10000);
            System.out.println("ThreadID "+Thread.currentThread().getId()+ " - "+ device.substring(8));
            client.openDevice();
            client.sendText("{UNLOCK}");
            client.sendText("{HOME}");
            //client.setShowPassImageInReport(false);
           // client.setProperty("on.device.xpath", "true");

            return client;}
        catch(Exception e){
            System.out.println("---------------" + device + " - CAN NOT GET A DEVICE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("---------------" + device + " - CAN NOT GET A DEVICE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("---------------" + device + " - CAN NOT GET A DEVICE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("---------------" + device + " - CAN NOT GET A DEVICE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("---------------" + device + " - CAN NOT GET A DEVICE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            return client;
        }
    }
}
		