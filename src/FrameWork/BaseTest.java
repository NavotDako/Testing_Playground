package FrameWork;

import com.experitest.manager.client.PManager;
import com.experitest.manager.client.ResultPublisher;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public abstract class BaseTest {
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
    String generatedReport = null;

    public BaseTest(String deviceOS, String deviceQuery, String testName, Map<String, Command> commandMap) {
        this.commandMap = commandMap;
        this.reportFolder = Runner.reportFolderString;
        this.deviceOS = deviceOS;
        this.deviceQuery = deviceQuery;
        this.testName = testName;

        StartTesting();

        printSummary(testName);
    }

    public void printSummary(String testName) {
        double successRate = ((double) success / (double) (Runner.repNum));
        String finish = "\n\nFINISHED - " + deviceName + " - " + testName + " - " + Thread.currentThread().getName() + " - Success Rate: " + success + "/" + (Runner.repNum) + " = " + successRate + "\n\n";
        System.out.println(finish);
    }

    public void StartTesting() {

        int failedToGetDeviceCounter = 0;

        for (int i = 0; i < Runner.repNum; i++) {
            long time = System.currentTimeMillis();
            try {
                getDevice(i);
                if (client == null) {
                    failedToGetDeviceCounter++;
                    simpleGetDeviceFailure(i, null, time);
                    continue;
                }
            } catch (Exception e) {
                if (failedToGetDeviceCounter < 3) {
                    failedToGetDeviceCounter++;
                    simpleGetDeviceFailure(i, e, time);
                    continue;
                } else {
                    failedToGetDeviceCounter = 0;
                    resetQuery(i, time, e);
                    continue;
                }
            }
            try {
                failedToGetDeviceCounter = 0;
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
                finishedSuccessIteration(i, System.currentTimeMillis() - time);
                ResultPublisher.publishResult(null, testName, "", null, true, null, null, false, time);
            } catch (Exception e) {
                failure("finish", 3, i, e, System.currentTimeMillis() - time);
            }
        }

    }

    public void resetQuery(int i, long time, Exception e) {

        failure("getDevice - RESETTING THE QUERY!!", 1, i, e, System.currentTimeMillis() - time);
        deviceQuery = null;
        deviceName = null;
        deviceShortName = null;
        String devicesInfo = client.getDevicesInformation();

    }

    public void simpleGetDeviceFailure(int i, Exception e, long time) {
        failure("getDevice", 1, i, e, System.currentTimeMillis() - time);

    }

    protected void prepareReporter(int i) {
        deviceName = client.getDeviceProperty("device.name");
        deviceShortName = deviceName.substring(deviceName.indexOf(":") + 1);
        System.setProperty("manager.url", "192.168.2.72:8787");
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

        boolean collected = false;
        switch (stage) {
            case 1: //getDevice
                System.out.println("Failed on " + failedStage + " - " + Thread.currentThread().getName() + " - Device - " + deviceQuery + " - test - " + testName);
                stringToWrite = createStringToWrite(failedStage, i, System.currentTimeMillis() - time, false);
                errors = GetErrors(e);
                WriteErrorsToSummaryReport(stringToWrite, errors, "No Report Was Generated");
                if (failedStage.contains("RESETTING")) {
                    WriteToSummaryReport("RESETTING THE QUERY!! -> " + deviceName);
                }
                sleep(3000);
                break;
            case 2: // prepareReporter
                System.out.println("Failed on " + failedStage + " - " + Thread.currentThread().getName() + " - Device - " + deviceQuery + " - test - " + testName);
                stringToWrite = createStringToWrite(failedStage, i, System.currentTimeMillis() - time, false);
                errors = GetErrors(e);
                tryToReleaseClient();
                WriteErrorsToSummaryReport(stringToWrite, errors, "No Report Was Generated");
                sleep(3000);
                break;
            case 3: //prepareDevice & executeTest & finish
                System.out.println("Failed on " + failedStage + " - " + Thread.currentThread().getName() + " - Device - " + deviceQuery + " - test - " + testName);
                stringToWrite = createStringToWrite(failedStage, i, System.currentTimeMillis() - time, false);
                errors = GetErrors(e);
                tryToCapture();
                generatedReport = tryToGenerateReport();
                tryToCollectSupportData(errors, generatedReport);
                tryToReleaseClient();
                WriteErrorsToSummaryReport(stringToWrite, errors, generatedReport);
                break;
        }

        ResultPublisher.publishResult(null, testName, "", null, false, errors.toString(), null, false, time);
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
        String supportData = null;
        try {
            client.collectSupportData(generatedReport + "\\SupportDataFor_" + deviceShortName + "_test_" + testName + "_" + System.currentTimeMillis() + ".zip", "", deviceName, errors.toString(), "", "", true, false);
            supportData = generatedReport + "\\SupportDataFor_" + deviceShortName + "_test_" + testName + "_" + System.currentTimeMillis() + ".zip";
        } catch (Exception e) {
            System.out.println(Thread.currentThread().getName() + " - Failed to Collect Support Data - " + Thread.currentThread().getName() + " - Device - " + deviceShortName + " - test - " + testName);
            e.printStackTrace();
            WriteToSummaryReport(Thread.currentThread().getName() + "  " + deviceName + " - " + " Failed to Collect Support Data" + "\n\n");
            return false;
        }
        if (supportData != null) PManager.getInstance().addReportZipFile(supportData);
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
        client.sleep(1000);
        if (generatedReport != null) PManager.getInstance().addReportFolder(generatedReport);

        if (generatedReport == null) {
            generatedReport = "c:\\Temp\\Reports";
            System.out.println(Thread.currentThread().getName() + " - Failed to Generate Report - unknown reason - Will save the support data to 'Reports'");
        }

        return generatedReport;
    }

    public void finishedSuccessIteration(int i, long time) throws InterruptedException {

        String stringToWrite = createStringToWrite("finished", i, time, true);
        WriteToSummaryReport(stringToWrite);
        reportFolder = tryToGenerateReport();
        System.out.println(Thread.currentThread().getName() + " - Finished Iteration - " + i + " - In - " + Thread.currentThread().getName() + "  " + deviceName + " - " + "REPORT - " + reportFolder);
        client.releaseClient();

    }

    private void WriteErrorsToSummaryReport(String stringToWrite, StringWriter errors, String generatedReport) {
        WriteToSummaryReport(stringToWrite);
        String errorString = (errors.toString().contains("<!DOCTYPE html><html>")) ? errors.toString().substring(errors.toString().indexOf("at com.experitest.")) : errors.toString();
        WriteToSummaryReport("\t" + deviceName + " -\n" + "\t" + errorString);
        WriteToSummaryReport(Thread.currentThread().getName() + "  " + deviceName + " - " + "REPORT - " + generatedReport + " - file:///" + generatedReport.replace('\\', '/') + "/index.html\n");
    }

    private StringWriter GetErrors(Exception e) {
        StringWriter errors = null;
        try {
            errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            System.err.println(Thread.currentThread().getName() + " " + deviceName + " - StackTrace: " + errors.toString());
        } catch (Exception e1) {

        }
        return errors;
    }

    private String createStringToWrite(String Stage, int i, long time, boolean succeeded) {
        double successRate = ((double) success / (double) (i + 1));
        String status = (succeeded) ? "pass" : "fail";
        String stringToWrite = String.format("%-20s%-20s%-30s%-30s%-15s%-5s", status, Stage, deviceName, testName, success + "/" + (i + 1) + "=" + successRate, time / 1000 + "s");

        addPropertiesToReporter(Stage, status);

        return stringToWrite;
    }

    private void addPropertiesToReporter(String Stage, String status) {
        String isGrid = (Runner.GRID) ? "grid" : "local";
        PManager.getInstance().addProperty("grid", isGrid);
        PManager.getInstance().addProperty("status", status);
        PManager.getInstance().addProperty("device", deviceName);
        PManager.getInstance().addProperty("Stage", Stage);
        PManager.getInstance().addProperty("testName", testName);
        PManager.getInstance().addProperty("build", Runner.buildNum + "");
    }

    protected abstract void AndroidRunTest();

    protected abstract void IOSRunTest();

    public void WriteToSummaryReport(String stringToWrite) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter("Reports/report.txt", true)));
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            if (stringToWrite.contains("pass") || stringToWrite.contains("fail")) {
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
