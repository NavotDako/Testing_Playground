package FrameWork;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public abstract class AbsTest {
    protected int success = 0;
    public String deviceSN = null;
    public String deviceName = null;
    protected String reportFolder;
    protected String deviceOS;
    protected String testName;
    protected String deviceQuery = "";
    protected MyClient client;
    protected Map<String, Command> commandMap = null;
    public String deviceShortName = null;
    String serial = null;

    public AbsTest(String deviceOS, String deviceQuery, String testName, Map<String, Command> commandMap) {
        this.commandMap = commandMap;
        this.reportFolder = Runner.reportFolderString;
        this.deviceOS = deviceOS;
        this.deviceQuery = deviceQuery;
        this.testName = testName;

        try {
            StartTesting();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        double successRate = ((double) success / (double) (Runner.repNum));
        String finish = "\n\nFINISHED - " + deviceName + " - " + testName + " - " + Thread.currentThread().getName() + " - Success Rate: " + success + "/" + (Runner.repNum) + " = " + successRate + "\n\n";
        System.out.println(finish);
    }

    public void StartTesting() throws InterruptedException {


        for (int i = 0; i < Runner.repNum; i++) {
            long time = System.currentTimeMillis();
            try {
                GetDevice(i);
            } catch (Exception e) {
                System.out.println("Failed on SetUp - " + Thread.currentThread().getName() + " - Device - " + deviceQuery + " - test - " + testName);
                String stringToWrite = createStringToWrite(i, System.currentTimeMillis() - time, false);
                StringWriter errors = GetErrors(e);
                WriteFailure("\n\n" + stringToWrite, errors, "No Report Was Generated");
                System.out.println(Thread.currentThread().getName() + " Sleeping for 30 sec");
                Thread.sleep(30000);
                System.out.println(Thread.currentThread().getName() + " Coming Back From Sleeping - > Continuing...");
                //Failure(i, e, time);
                continue;
            }
            try {
                ExecuteTest(i);
            } catch (Exception e1) {
                System.out.println("Failed On Execution - " + Thread.currentThread().getName() + " - Device - " + deviceName + " - test - " + testName);
                Failure(i, e1, System.currentTimeMillis() - time);
                continue;
            }
            try {
                Finish(i, System.currentTimeMillis() - time);
            } catch (Exception e2) {
                System.out.println("Failed On Finish - " + Thread.currentThread().getName() + " - Device - " + deviceName + " - test - " + testName);
                e2.printStackTrace();
                // Failure(i, e2, time);
            }
        }

    }

    public void GetDevice(int i) throws Exception {
        client = ClientFactory.SetUp(testName, this.deviceOS, this.deviceQuery, commandMap, serial);
        if (client != null) {
            if (serial == null) {
                this.serial = client.getDeviceProperty("device.sn");
                this.deviceName = client.getDeviceProperty("device.name");
                deviceShortName = deviceName.substring(deviceName.indexOf(":") + 1);
            }
            FinishSetUp(i);

        } else {
            System.out.println("CLIENT FROM GRID IS NULL!!! -> Going to sleep for one minute");
            throw new Exception("CLIENT FROM GRID IS NULL!!! -> Moving to the next test");
        }

    }

    public void FinishSetUp(int i) {

        System.out.println("----------------@---------------- " + Thread.currentThread().getName() + "  STARTING - " + deviceName + " - " + testName + ": Iteration - " + (i + 1));
        System.out.println("----------------@---------------- " + Thread.currentThread().getName() + "  Set Reporter - " + client.setReporter("xml", reportFolder, deviceShortName + " " + deviceOS + " - " + testName + " - " + (i + 1)));
        client.setShowPassImageInReport(false);
        client.setSpeed("FAST");
        client.deviceAction("portrait");
        client.deviceAction("home");
    }

    private void ExecuteTest(int i) {

        if (deviceOS.equals("ios")) {
            IOSRunTest();
        } else {
            AndroidRunTest();
        }
        success++;
    }

    public void Failure(int i, Exception e, long time) {
        String stringToWrite = createStringToWrite(i, time, false);
        StringWriter errors = GetErrors(e);
        String generatedReport = null;

        try {
            client.capture();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        try {
            generatedReport = client.generateReport(false);
            if (generatedReport == null) {
                generatedReport = "c:\\Temp\\Reports";
                System.out.println(Thread.currentThread().getName() + " - Failed to Generate Report - unknown reason - Will save the support data to 'Reports'");
            }
        } catch (Exception e1) {
            System.out.println(Thread.currentThread().getName() + " - Failed to Generate Report- " + Thread.currentThread().getName() + " - Device - " + deviceName + " - test - " + testName);
            e1.printStackTrace();
        }

        WriteFailure(stringToWrite, errors, generatedReport);

        try {
            client.collectSupportData(generatedReport + "\\SupportDataFor_" + deviceShortName + "_test_" + testName + "_" + System.currentTimeMillis() + ".zip", "", client.getDeviceProperty("device.name"), errors.toString(), "", "", true, false);
        } catch (Exception e2) {
            System.out.println(Thread.currentThread().getName() + " - Failed to Collect Support Data - " + Thread.currentThread().getName() + " - Device - " + deviceShortName + " - test - " + testName);
            e2.printStackTrace();
        }

        try {
            client.releaseClient();
        } catch (Exception e3) {
            System.out.println(Thread.currentThread().getName() + " - Failed to Release Device - " + Thread.currentThread().getName() + " - Device - " + deviceName + " - test - " + testName);
            client = null;
            try {
                System.out.println(Thread.currentThread().getName() + " going to Sleep");
                Thread.sleep(3);
                System.out.println(Thread.currentThread().getName() + "Coming back from Sleep");
            } catch (InterruptedException e4) {
                e4.printStackTrace();
            }
        }
    }

    public void Finish(int i, long time) throws InterruptedException {

        System.out.println(Thread.currentThread().getName() + " - Finished Iteration - " + i + " - In - " + Thread.currentThread().getName() + "  " + deviceName + " - " + "REPORT - " + client.generateReport(false));
        //if (!Runner.GRID) client.releaseDevice(deviceName, true, true, true);

        client.releaseClient();
        String stringToWrite = createStringToWrite(i, time, true);
        Write(stringToWrite);
        System.out.println(Thread.currentThread().getName() + " going to Sleep for 3 sec");
        Thread.sleep(3000);
        System.out.println(Thread.currentThread().getName() + "Coming back from 3 sec Sleep");

    }

    private boolean IsCloudDevice() {

        if (!Runner.GRID) {
            return client.getDeviceProperty("device.remote").contains("true");
        } else {
            return true;
        }
    }

    private void WriteFailure(String stringToWrite, StringWriter errors, String generatedReport) {
        Write(stringToWrite);
        String errorString = (errors.toString().contains("<!DOCTYPE html><html>")) ? errors.toString().substring(errors.toString().indexOf("at com.experitest.")) : errors.toString();
        Write("\t" + deviceName + " -\n" + "\t" + errorString);
        Write(Thread.currentThread().getName() + "  " + deviceName + " - " + "REPORT - " + generatedReport + " - file:///" + generatedReport.replace('\\', '/') + "/index.html\n");
    }

    private StringWriter GetErrors(Exception e) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        System.err.println(Thread.currentThread().getName() + " " + deviceName + " - StackTrace: " + errors.toString());
        return errors;
    }

    private String createStringToWrite(int i, long time, boolean succeeded) {
        double successRate = ((double) success / (double) (i + 1));
        String status = (succeeded) ? "SUCSUC" : "XXX FAILURE XXX";
        String stringToWrite = String.format("%-15s%-30s%-30s%-15s%-5s", status, deviceName, testName, success + "/" + (i + 1) + "=" + successRate, time / 1000 + "s");
        return stringToWrite;
    }

    protected abstract void AndroidRunTest();

    protected abstract void IOSRunTest();

    public void Write(String stringToWrite) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter("Reports/report.txt", true)));
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            if (stringToWrite.contains("SUCSUC") || stringToWrite.contains("FAILURE")) {
                //writer.append(Runner.commandIndex + " \t" + sdf.format(new Date(System.currentTimeMillis())) + ": " + stringToWrite + "\n");
                writer.append(String.format("%-5s %-10s %-60s\n", Runner.commandIndex + ".", sdf.format(new Date(System.currentTimeMillis())), stringToWrite));
                Runner.commandIndex++;
            } else
                writer.append(stringToWrite + "\n");

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
