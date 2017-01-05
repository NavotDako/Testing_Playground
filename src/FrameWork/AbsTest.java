package FrameWork;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class AbsTest {
    protected int success = 0;
    protected int repNum = 0;
    protected MyClient client = null;
    protected String device;
    protected String reportFolder;
    protected String deviceOS;
    protected String testName;
    String deviceQuery = "";


    public AbsTest(MyClient client, int repNum, String reportFolder, String deviceOS, String testName) {

        this.client = client;
        this.repNum = repNum;
        this.reportFolder = reportFolder;
        this.deviceOS = deviceOS;
        this.testName = testName;

        if (client != null) {
            try {
                this.device = client.getDeviceProperty("device.name");
                // client.sendText("{UNLOCK}");
                client.sendText("{HOME}");
                boolean result = StartTesting();
                if (result) {
                    double successRate = ((double) success / (double) (repNum));
                    String finish = "FINISHED - " + device + " - " + testName + " - " + Thread.currentThread().getName() + " - Success Rate: " + success + "/" + (repNum) + " = " + successRate;
                    System.out.println(finish);
                    Finish("FINISHED TEST - " + testName);
                } else {
                    String finish = "STOPPED - " + device + " - " + testName + " - " + Thread.currentThread().getName() + " - ";
                    System.out.println(finish);
                    Finish("FINISHED TEST - " + testName);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Finish("CAN NOT START THE TEST - " + testName);
            }
        } else {
            Finish("CAN NOT GET A DEVICE - " + testName);
        }

    }

    public void Finish(String message) {
        System.out.println("---------------" + Thread.currentThread().getName() + device + " - " + message + " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        client.releaseDevice(device, true, IsCloudDevice(), true);
        client.releaseClient();
    }

    private boolean IsCloudDevice() {
        if (client.getDeviceProperty("device.remote").contains("true"))
            return true;
        else return false;
    }

    public boolean StartTesting() {
        long time = 0;

        for (int i = 0; i < repNum; i++) {
            try {
                time = ExecuteTest(i);
                System.out.println(Thread.currentThread().getName() + "  " + device + " - " + "REPORT - " + client.generateReport(false));
            } catch (Exception e) {
                try {
                    Failure(i, e, time);
                } catch (Exception e2) {
                    System.out.println("Failure Mechanism Did Not Worked Properly - " + Thread.currentThread().getName() + " - Device - " + device);
                    e2.printStackTrace();
                }
            }
        }
        //Write(finish);
        return true;
    }

    private long ExecuteTest(int i) {
        System.out.println("################ " + Thread.currentThread().getName() + "  STARTING - " + device + " - " + testName + ": Iteration - " + (i + 1));
        System.out.println("################ " + Thread.currentThread().getName() + "  Set Reporter - " + client.setReporter("xml", reportFolder, device.substring(device.indexOf(":") + 1) + " " + deviceOS + " - " + testName + " - " + (i + 1)));
        long before = System.currentTimeMillis();
        if (deviceOS.equals("ios")) {
            IOSRunTest();
        } else {
            AndroidRunTest();
        }
        long time = System.currentTimeMillis() - before;
        success++;

        String stringToWrite = WriteAndGetResults(i, time, true);
        Write(stringToWrite);
        return time;
    }

    public void Failure(int i, Exception e, long time) throws Exception {
        String stringToWrite = WriteAndGetResults(i, time, false);
        StringWriter errors = GetErrors(e);
        String generatedReport = TryGenerateReport();
        WriteFailure(stringToWrite, errors, generatedReport);

        TryCollectSupportData(generatedReport);

    }

    private void TryCollectSupportData(String generatedReport) throws Exception {
        if (device != null) {
            String dataPath = generatedReport;
            System.out.println(Thread.currentThread().getName() + " " + device + " - " + "SupportData - " + dataPath);
            try {
                client.collectSupportData(dataPath + "\\SupportData", "", device, "", "", "", true, true);
            } catch (Exception e1) {
                System.err.println(device + " - Can't get SupportData");
                throw (e1);
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
            System.out.println(Thread.currentThread().getName() + " " + device + " Unable to generate report");
            e1.printStackTrace();
        }
        return generatedReport;
    }

    private StringWriter GetErrors(Exception e) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        System.err.println(Thread.currentThread().getName() + " " + device + " - StackTrace: " + errors.toString());
        return errors;
    }

    private String WriteAndGetResults(int i, long time, boolean succeeded) {
        double successRate = ((double) success / (double) (i + 1));
        String status = (succeeded) ? "SUCCESS" : "FAILURE";
        String stringToWrite = status + "- " + device + " - " + testName + " - " + Thread.currentThread().getName() + ": Iteration - " + (i + 1) + " - Success Rate: " + success + "/" + (i + 1) + " = " + successRate + "    Time - " + time / 1000 + "s";
        System.out.println("****************** ############################ " + stringToWrite + " ############################# ******************");
        return stringToWrite;
    }

    protected abstract void AndroidRunTest();

    protected abstract void IOSRunTest();

    public void Write(String stringToWrite) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter("Reports/report.txt", true)));
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            writer.append(Runner.commandIndex + " \t" + sdf.format(new Date(System.currentTimeMillis())) + ": " + stringToWrite + "\n");
            Runner.commandIndex++;
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
