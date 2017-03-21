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
                getDevice(i);
            } catch (Exception e) {
                failure("getDevice", 1, i, e, System.currentTimeMillis() - time);
                continue;
            }
            try {
                prepareReporter(i);
            } catch (Exception e) {
                failure("prepareReporter", 2, i, e, System.currentTimeMillis() - time);
                continue;
            }
            try {
                prepareDevice(i);
            } catch (Exception e) {
                failure("prepareDevice", 3, i, e, System.currentTimeMillis() - time);
                continue;
            }
            try {
                executeTest(i);
            } catch (Exception e) {
                failure("executeTest", 3, i, e, System.currentTimeMillis() - time);
                continue;
            }
            try {
                finish(i, System.currentTimeMillis() - time);
            } catch (Exception e) {
                failure("finish", 3, i, e, time);
            }
        }

    }

    protected void prepareReporter(int i) {
        deviceName = client.getDeviceProperty("device.name");
        deviceShortName = deviceName.substring(deviceName.indexOf(":") + 1);
        System.out.println("----------------@---------------- " + Thread.currentThread().getName() + "  STARTING - " + deviceName + " - " + testName + ": Iteration - " + (i + 1));
        System.out.println("----------------@---------------- " + Thread.currentThread().getName() + "  Set Reporter - " + client.setReporter("xml", reportFolder, deviceShortName + "_" + deviceOS + " - " + testName + " - " + (i + 1)));

    }

    public void sleep(int millisec) {
        System.out.println(Thread.currentThread().getName() + " Sleeping for 3 sec");
        try {
            Thread.sleep(millisec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " Coming Back From Sleeping - > Continuing...");
    }

    public void getDevice(int i) throws Exception {
        client = ClientFactory.SetUp(testName, this.deviceOS, this.deviceQuery, commandMap, deviceSN);
    }

    public void prepareDevice(int i) throws Exception {

        if (client != null) {
            if (deviceSN == null) {
                deviceSN = client.getDeviceProperty("device.sn");
            }
            client.setShowPassImageInReport(false);
            client.setSpeed("FAST");
            client.deviceAction("portrait");
            client.deviceAction("home");

        } else {
            System.out.println("CLIENT IS NULL!!!");
            throw new Exception("CLIENT IS NULL!!! -> Moving to the next test");
        }
    }

    private void executeTest(int i) {

        if (deviceOS.equals("ios")) {
            IOSRunTest();
        } else {
            AndroidRunTest();
        }
        success++;
    }

    public void failure(String failedStage, int stage, int i, Exception e, long time) {
        String stringToWrite = null;
        StringWriter errors = null;
        String generatedReport = null;
        boolean collected = false;
        switch (stage) {
            case 1:
                System.out.println("Failed on " + failedStage + " - " + Thread.currentThread().getName() + " - Device - " + deviceQuery + " - test - " + testName);
                stringToWrite = createStringToWrite(i, System.currentTimeMillis() - time, false);
                errors = GetErrors(e);
                WriteFailure(stringToWrite, errors, "No Report Was Generated");
                sleep(3000);
                break;
            case 2:
                System.out.println("Failed on " + failedStage + " - " + Thread.currentThread().getName() + " - Device - " + deviceQuery + " - test - " + testName);
                stringToWrite = createStringToWrite(i, System.currentTimeMillis() - time, false);
                errors = GetErrors(e);
                tryToReleaseClient();
                WriteFailure(stringToWrite, errors, "No Report Was Generated");
                sleep(3000);
                break;
            case 3:
                System.out.println("Failed on " + failedStage + " - " + Thread.currentThread().getName() + " - Device - " + deviceQuery + " - test - " + testName);
                stringToWrite = createStringToWrite(i, System.currentTimeMillis() - time, false);
                errors = GetErrors(e);
                tryToCapture();
                generatedReport = tryToGenerateReport();
                collected = tryToCollectSupportData(errors, generatedReport);
                tryToReleaseClient();
                WriteFailure(stringToWrite, errors, generatedReport);
                if (!collected) Write(Thread.currentThread().getName() + "  " + deviceName + " - " + " Failed to Collect Support Data - " + e.getMessage());
                sleep(3000);
                break;
        }
    }

    public void tryToReleaseClient() {
        try {
            client.releaseClient();
        } catch (Exception e3) {
            System.out.println(Thread.currentThread().getName() + " - Failed to Release Device - " + Thread.currentThread().getName() + " - Device - " + deviceName + " - test - " + testName);
            System.out.println("Assigning 'null' to the client");
            client = null;
        }
    }

    public void tryToCapture() {
        try {
            client.capture();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public boolean tryToCollectSupportData(StringWriter errors, String generatedReport) {

        try {
            client.collectSupportData(generatedReport + "\\SupportDataFor_" + deviceShortName + "_test_" + testName + "_" + System.currentTimeMillis() + ".zip", "", deviceName, errors.toString(), "", "", true, false);
        } catch (Exception e) {
            System.out.println(Thread.currentThread().getName() + " - Failed to Collect Support Data - " + Thread.currentThread().getName() + " - Device - " + deviceShortName + " - test - " + testName);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public String tryToGenerateReport() {
        String generatedReport = null;
        try {
            generatedReport = client.generateReport(false);

        } catch (Exception e1) {
            System.out.println(Thread.currentThread().getName() + " - Failed to Generate Report- " + Thread.currentThread().getName() + " - Device - " + deviceName + " - test - " + testName);
            e1.printStackTrace();
        }
        if (generatedReport == null) {
            generatedReport = "c:\\Temp\\Reports";
            System.out.println(Thread.currentThread().getName() + " - Failed to Generate Report - unknown reason - Will save the support data to 'Reports'");
        }

        return generatedReport;
    }

    public void finish(int i, long time) throws InterruptedException {

        System.out.println(Thread.currentThread().getName() + " - Finished Iteration - " + i + " - In - " + Thread.currentThread().getName() + "  " + deviceName + " - " + "REPORT - " + client.generateReport(false));
        //if (!Runner.GRID) client.releaseDevice(deviceName, true, true, true);

        client.releaseClient();
        String stringToWrite = createStringToWrite(i, time, true);
        Write(stringToWrite);
        System.out.println(Thread.currentThread().getName() + " going to Sleep for 3 sec");
        Thread.sleep(3000);
        System.out.println(Thread.currentThread().getName() + "Coming back from 3 sec Sleep");

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
        String stringToWrite = String.format("%-20s%-30s%-30s%-15s%-5s", status, deviceName, testName, success + "/" + (i + 1) + "=" + successRate, time / 1000 + "s");
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
