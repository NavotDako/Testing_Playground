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
    String deviceQurey = "";


    public AbsTest(MyClient client, String deviceQurey, int repNum, String reportFolder, String deviceOS, String testName){

        this.client = client;
        this.repNum  = repNum;
        this.reportFolder = reportFolder;
        this.deviceOS = deviceOS;
        this.testName = testName;
        this.deviceQurey = deviceQurey;
        getDevice();

    }

    private void getDevice() {
        try {
            device = client.waitForDevice("@os = '" + deviceOS + "'" + deviceQurey, 10000);
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

    public boolean runTest(){
        long time =0;
        if (device==null){
            getDevice();
            if (device==null) return false;
        }

        for (int i = 0; i < repNum; i++) {
            try{
                time = Execute(i);
                System.out.println(Thread.currentThread().getName() + "  " + device + " - " + "REPORT - " + client.generateReport(false));
            }catch(Exception e ){
                Failure(i, e,time);
            }

        }
        System.out.println("############################ Device - "+ device +" - "+testName +" - "+Thread.currentThread().getName() +" - Finished #############################" );

        client.releaseClient();
        return true;
    }

    private long Execute(int i) {
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

        String stringToWrite = UpdateResults(i, time,true);
        try {
            Write(stringToWrite);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return time;
    }

    public void Failure(int i, Exception e, long time) {
        String stringToWrite = UpdateResults(i, time,false);
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
        try {
            Write("\n*** "+stringToWrite+" ***");
            Write("  "+device + " - "+errors.toString());
            Write(Thread.currentThread().getName() + "  " + device + " - " + "REPORT - " + generatedReport+"\n" );
        } catch (IOException e1) {
            e1.printStackTrace();
        }
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

    private String UpdateResults(int i, long time, boolean succeeded) {
        double successRate = ((double)success/(double)(i+1));
        String status = (succeeded)?"SUCCESS":"FAILURE";
        String stringToWrite = status +"- " +device+" - "+testName+ " - " +Thread.currentThread().getName()+": Iteration - " + (i+1) + " - Success Rate: "+success+"/"+(i+1)+" = "+  successRate + "    Time - "+time/1000 +"s";
        System.err.println("****************** ############################ " + stringToWrite + " ############################# ******************");
        return stringToWrite;
    }

    protected abstract void AndroidRunTest();

    protected abstract void IOSRunTest();

    public void Write(String stringToWrite) throws IOException {
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("Reports/report.txt", true)));
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        writer.append(sdf.format(new Date(System.currentTimeMillis())) +": " + stringToWrite+"\n");
        writer.close();

    }
}
