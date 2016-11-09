package FrameWork;

import com.experitest.client.Client;
import com.experitest.client.MobileListener;

import java.io.*;
import java.util.Map;


public class MyClient extends Client {

    Map<String, Command> commandMap = null;
    String deviceName="??";
    String deviceOS="??";

    public MyClient(String host, int port, boolean t,Map<String,Command> commandMap) {
        super(host, port, t);
        this.commandMap= commandMap;

    }

    public void Finish(String command , String detail, long before) {
        long time = System.currentTimeMillis() - before;
        String stringToWrite= "??";
        if (!commandMap.containsKey(command)){
            commandMap.put(command,new Command(command));
        }
        if (commandMap.containsKey(command)){
            commandMap.get(command).timeList.add(time);
            commandMap.get(command).totalTime +=time;
            commandMap.get(command).avgTime = commandMap.get(command).totalTime/ commandMap.get(command).timeList.size();
            stringToWrite = commandMap.get(command).timeList.size() + ": " + deviceName + " --- " + detail + " --- " + time + " --- AVG - " + commandMap.get(command).avgTime + "\n";
        }

        try {
            Write(stringToWrite);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Write(String stringToWrite) throws IOException {
        String reportName =deviceName.substring(deviceName.indexOf(":")+1);
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("Reports/"+reportName +".txt", true)));
        System.out.println(stringToWrite);
        writer.append(stringToWrite);
        writer.close();

    }

    @Override
    public void launch(String activityURL, boolean instrument, boolean stopIfRunning) {
        long before = System.currentTimeMillis();
        if (deviceOS.contains("android")) {
            super.launch(activityURL, false, stopIfRunning);
        } else {
            super.launch(activityURL, instrument, stopIfRunning);
        }
        Finish("Launch","Launch - "+activityURL, before);

    }

    @Override
    public void click(String zone, String element, int index, int count) {

        long before = System.currentTimeMillis();
        super.click(zone, element, index, count);
        Finish("Click","Click - "+zone + " - " + element, before);

    }

    @Override
    public String setReporter(String type, String reportFolder, String reportName) {
        String reportPath = super.setReporter(type,reportFolder,reportName);
        super.startLoggingDevice(reportPath);
        return reportFolder;
    }

    @Override
    public boolean install(String app, boolean instrument, boolean stopIfRunning) {
        long before = System.currentTimeMillis();
        boolean result = super.install(app, instrument, stopIfRunning);
        Finish("Install","Install - "+deviceOS+" - "+app, before);
        return result;
    }

    @Override
    public String generateReport(boolean b) {
        super.stopLoggingDevice();
        String result = super.generateReport(b);

        //reportTheCommands();
        return result;
    }

    public void reportTheCommands() {

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

    @Override
    public boolean reboot(int timeOut) {
        System.out.println(Thread.currentThread().getName() + " " +deviceName+" Rebooting...");
        long before = System.currentTimeMillis();
        boolean result = super.reboot(timeOut);
        Finish("Reboot","Reboot - "+deviceOS, before);
        return result;
    }

    @Override
    public void elementSendText(String zone, String element, int index, String text){
        long before = System.currentTimeMillis();
        super.elementSendText(zone, element, index, text);
        Finish("elementSendText","elementSendText - "+ deviceOS+" - "+zone, before);
    }

    @Override
    public void  verifyElementFound(String zone, String element, int index){
        long before = System.currentTimeMillis();
        super.verifyElementFound(zone, element, index);
        Finish("verifyElementFound","verifyElementFound - "+ deviceOS+" - "+zone, before);
    }

    @Override
    public String waitForDevice(String query,int timeOut){
        long before = System.currentTimeMillis();
        deviceName = super.waitForDevice(query,timeOut);
        deviceOS = super.getProperty("device.os");
        Finish("waitForDevice","waitForDevice - "+deviceOS, before);
        System.out.println("##### "+ Thread.currentThread().getId() +" - Found Device - "+deviceName);
        return deviceName;
    }
    @Override
    public void collectSupportData(String zipDestination, String applicationPath, String device, String scenario, String expectedResult, String actualResult) {
        long before = System.currentTimeMillis();
        System.out.println(Thread.currentThread().getName()+ " "+device+" - "+"SupportData - "+zipDestination);
        try{
            super.collectSupportData(zipDestination,"",device,"","","",true,true);

        }catch(Exception e1){
            System.err.println(device + " - Can't get SupportData");
            e1.printStackTrace();
        }
        Finish("collectSupportData","collectSupportData - "+deviceOS, before);

    }
}
