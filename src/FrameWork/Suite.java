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
       /* (new EriBank(SetUp(),deviceQuery,repNum,reportFolder,deviceOS, "EriBank")).StartTesting();

        (new NonInstrumented(SetUp(),deviceQuery,repNum,reportFolder,deviceOS,"Non-Instrumented")).StartTesting();
*/
        (new Web(SetUp(),deviceQuery,repNum,reportFolder,deviceOS, "Web")).StartTesting();

        (new LaunchBrowserLoop(SetUp(),deviceQuery,repNum,reportFolder,deviceOS, "LaunchBrowserLoop")).StartTesting();

        (new TenFreeApps(SetUp(),deviceQuery,repNum,reportFolder,deviceOS, "TenFreeApps")).StartTesting();

        (new SimulateCapture(SetUp(),deviceQuery,repNum,reportFolder,deviceOS, "SimulateCapture")).StartTesting();

       /* (new Rebooting(SetUp(),deviceQuery,repNum,reportFolder,deviceOS,"Reboot")).StartTesting();*/

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
            return client;
        }catch(Exception e){
            System.out.println("---------------" + device + " - CAN NOT GET A DEVICE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            return client;
        }
    }
}
		