package FrameWork;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class AbsTest {
    protected int success =0;
    protected int repNum = 0;
    protected MyClient client = null;
    protected String device;
    protected String reportFolder;
    protected String deviceOS;
    protected String testName;
    String deviceQuery = "";


    public AbsTest(MyClient client, String deviceQuery, int repNum, String reportFolder, String deviceOS, String testName){

        this.client = client;
        this.repNum  = repNum;
        this.reportFolder = reportFolder;
        this.deviceOS = deviceOS;
        this.testName = testName;
        this.deviceQuery = deviceQuery;
        getDevice();

    }

    private void getDevice() {
        try {
            device = client.waitForDevice("@os = '" + deviceOS + "'" + deviceQuery, 30000);
            System.out.println(Thread.currentThread().getName() + " - " + device.substring(device.indexOf(":")));
            client.openDevice();
            client.sendText("{UNLOCK}");
            client.sendText("{HOME}");

        }catch(Exception e){
            e.printStackTrace();
            try {
                System.out.println(Thread.currentThread().getName() + " - " + device.substring(device.indexOf(":")) + " getDevicesInformation - \n" + client.getDevicesInformation());
            }catch (Exception e1){
                e1.printStackTrace();
            }
            String dataPath = reportFolder+"\\SupportData_"+device+"_"+System.currentTimeMillis();
            client.collectSupportData(dataPath,"",device,"","","",true,true);
        }

    }

    public boolean StartTesting(){
        long time =0;
        if (device==null){
            getDevice();
            if (device==null) return false;
        }

        for (int i = 0; i < repNum; i++) {
            try{
                time = ExecuteTest(i);
                System.out.println(Thread.currentThread().getName() + "  " + device + " - " + "REPORT - " + client.generateReport(false));
            }catch(Exception e ){
                Failure(i, e,time);
            }
        }

        double successRate = ((double)success/(double)(repNum));
        String finish = "FINISHED- " + device+" - "+testName+ " - " +Thread.currentThread().getName() +" - Success Rate: "+success+"/"+(repNum)+" = "+  successRate;
        System.out.println(finish);
        Write(finish);

        client.releaseClient();
        return true;
    }

    private long ExecuteTest(int i) {
        System.out.println(Thread.currentThread().getName() +"  STARTING - " +device+ " - " +Thread.currentThread().getName()+": Iteration - " + (i+1));
        System.out.println(Thread.currentThread().getName() +"  Set Reporter - " + client.setReporter("xml", reportFolder, device.substring(8) + " "+ deviceOS +" - "+ testName+ " - "+ (i+1) ));
        long before =   System.currentTimeMillis();
        if (deviceOS.equals("ios")){
            IOSRunTest();
        }
        else{
            AndroidRunTest();
        }
        long time = System.currentTimeMillis() - before;
        success++;

        String stringToWrite = WriteAndGetResults(i, time,true);
        Write(stringToWrite);
        return time;
    }

    public void Failure(int i, Exception e, long time) {
        String stringToWrite = WriteAndGetResults(i, time,false);
        StringWriter errors = GetErrors(e);
        String generatedReport = TryGenerateReport();
        WriteFailure(stringToWrite, errors, generatedReport);
        TryCollectSupportData(generatedReport);
    }

    private void TryCollectSupportData(String generatedReport) {
        if (device!=null){
            String dataPath =generatedReport;
            System.out.println(Thread.currentThread().getName()+ " "+ device+" - "+"SupportData - "+dataPath);
            try{
                client.collectSupportData(dataPath+"\\SupportData","",device,"","","",true,true);
            }catch(Exception e1){
                System.err.println(device + " - Can't get SupportData");
                e1.printStackTrace();
            }
        }
    }

    private void WriteFailure(String stringToWrite, StringWriter errors, String generatedReport) {
        Write("*** " + stringToWrite + " ***");
        Write("  " + device + " - " + errors.toString());
        Write(Thread.currentThread().getName() + "  " + device + " - " + "REPORT - " + generatedReport + "\n");
    }

    private String TryGenerateReport() {
        String generatedReport = null;
        try {
            generatedReport = client.generateReport(false);
        } catch (Exception e1) {
            System.out.println(Thread.currentThread().getName()+ " " +device+" Unable to generate report");
            e1.printStackTrace();
        }
        return generatedReport;
    }

    private StringWriter GetErrors(Exception e) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        System.err.println(Thread.currentThread().getName()+ " "+device+ " - StackTrace: " + errors.toString());
        return errors;
    }

    private String WriteAndGetResults(int i, long time, boolean succeeded) {
        double successRate = ((double)success/(double)(i+1));
        String status = (succeeded)?"SUCCESS":"FAILURE";
        String stringToWrite = status +"- " +device+" - "+testName+ " - " +Thread.currentThread().getName()+": Iteration - " + (i+1) + " - Success Rate: "+success+"/"+(i+1)+" = "+  successRate + "    Time - "+time/1000 +"s";
        System.out.println("****************** ############################ " + stringToWrite + " ############################# ******************");
        return stringToWrite;
    }

    protected abstract void AndroidRunTest();

    protected abstract void IOSRunTest();

    public void Write(String stringToWrite)  {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter("Reports/report.txt", true)));
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            writer.append(sdf.format(new Date(System.currentTimeMillis())) +": " + stringToWrite+"\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
