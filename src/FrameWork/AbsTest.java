package FrameWork;

import org.junit.runners.Suite;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by navot.dako on 2/24/2016.
 */
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
            }catch(Exception e ){
                Failure(i, e,time);
            }

            try {
                System.out.println(Thread.currentThread().getName() + "  " + device + " - " + "REPORT - " + client.generateReport(false));
            }catch(Exception e){
                e.printStackTrace();
                return false;
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
        double successRate = success/(i+1);
        String stringToWrite = "SUCCESS - " +device+" - "+testName +" - " +Thread.currentThread().getName()+": Iteration - " + (i+1) + " - Success Rate: "+success+"/"+(i+1)+" = "+successRate + "    Time - "+time/1000 +"s";
        System.out.println(Thread.currentThread().getName() +"  ############################ "+stringToWrite+" ##############################");
        try {
            Write(stringToWrite);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return time;
    }

    public void Failure(int i, Exception e, long time) {
        double successRaate = (double)(success/(i+1));

        String stringToWrite = "FAILURE - " +device+" - "+testName+ " - " +Thread.currentThread().getName()+": Iteration - " + (i+1) + " - Success Rate: "+success+"/"+(i+1)+" = "+  successRaate + "    Time - "+time/1000 +"s";
        System.err.println("****************** ############################ " + stringToWrite + " ############################# ******************");
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        System.err.println(Thread.currentThread().getName()+ " "+device+ " - StackTrace: " + errors.toString());

        try {
            Write("\n*** "+stringToWrite+" ***");
            Write("  "+device + " - "+errors.toString());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        if (device!=null){
            String dataPath =reportFolder+"\\SupportData_"+device+"_"+System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName()+ " "+device+" - "+"SupportData - "+dataPath);
            try{
                client.collectSupportData(dataPath,"",device,"","","",true,true);
                client.report(dataPath,true);
            }catch(Exception e1){
                System.err.println(device + " - Can't get SupportData");
                e1.printStackTrace();
            }
        }
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
