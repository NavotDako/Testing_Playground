package FrameWork;

import org.junit.runners.Suite;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

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

    public AbsTest(MyClient client, String device, int repNum, String reportFolder, String deviceOS, String testName){
        this.device=device;
        this.client = client;
        this.repNum  = repNum;
        this.reportFolder = reportFolder;
        this.deviceOS = deviceOS;
        this.testName = testName;
    }

    public void runTest(){
        for (int i = 0; i < repNum; i++) {
            try{
                System.out.println("STARTING - " +device+ " - " +Thread.currentThread().getName()+": Iteration - " + (i+1));
                System.out.println("Set Reporter - " + client.setReporter("xml", reportFolder, device.substring(8) + " "+ deviceOS +" - "+ testName+ " - "+ (i+1) ));
                if (deviceOS.equals("ios")){
                    IOSRunTest();
                }
                else{
                    AndroidRunTest();
                }
                success ++;
                String stringToWrite = "SUCCESS - " +device+" - "+testName +" - " +Thread.currentThread().getName()+": Iteration - " + (i+1) + " - Success Rate: "+success+"/"+(i+1)+" = "+(success/(i+1));
                System.out.println("############################ "+stringToWrite+" ##############################");
                try {
                    Write(stringToWrite);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }catch(Exception e ){
                Failure(i, e);
            }
            System.out.println(device+" - "+"REPORT - "+ client.generateReport(false));

        }
        System.out.println("############################ Device - "+ device +" - "+testName +" - "+Thread.currentThread().getName() +" - Finished #############################" );
        //client.closeDevice();
        client.releaseClient();

    }

    public void Failure(int i, Exception e) {

        String stringToWrite = "FAILURE - " +device+" - "+testName+ " - " +Thread.currentThread().getName()+": Iteration - " + (i+1) + " - Success Rate: "+success+"/"+(i+1)+" = "+  (success/(i+1));

        System.err.println("****************** ############################ " + stringToWrite + " ############################# ******************");
        System.err.println(device + " - StackTrace: "); System.err.println(device + " - "+e.getMessage()); e.printStackTrace();

        try {
            Write("*** "+stringToWrite+" ***");
            Write("         "+device + " - "+e.getMessage());
        } catch (IOException e1) {
            System.err.println(device + " - can't write stacktrace to report.txt :");
            e1.printStackTrace();
        }
        try{
            System.err.println(device+" - "+"Log - "+client.getDeviceLog());

        }catch(Exception e1){
            System.err.println(device + " - Can't get Device Log");
        }
        try{
            System.err.println(device+" - "+"SupportData - ");
            String dataPath =reportFolder+"\\SupportData_"+device+"_"+System.currentTimeMillis();
            client.collectSupportData(dataPath,null,null,"","","",true,true);
            client.report(dataPath,true);
        }catch(Exception e1){
            System.err.println(device + " - Can't get SupportData");
            e1.printStackTrace();
        }

    }

    protected abstract void AndroidRunTest();

    protected abstract void IOSRunTest();

    public void Write(String stringToWrite) throws IOException {
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("Reports/report.txt", true)));
        writer.append(stringToWrite+"\n");
        writer.close();

    }
}
