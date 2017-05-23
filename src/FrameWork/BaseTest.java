package FrameWork;

/*import com.experitest.manager.client.PManager;
import com.experitest.manager.client.ResultPublisher;*/

import com.experitest.manager.client.PManager;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static Utils.ExceptionExtractor.ExtractExceptions;

public abstract class BaseTest {
    protected int success = 0;
    public String deviceOSVersion = null;
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
        int failedTests = 0;
        int failedToGetDeviceCounter = 0;

        for (int i = 0; (i < Runner.repNum && failedTests < Runner.retry); i++) {
            long time = System.currentTimeMillis();
            try {
                getDevice(i);
                if (client == null) {
                    failedTests++;
                    failedToGetDeviceCounter++;
                    simpleGetDeviceFailure(i, null, time);
                    continue;
                }
            } catch (Exception e) {
                failedTests++;
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
                failedTests++;
                failure("prepareReporter", i, e, System.currentTimeMillis() - time);
                continue;
            }
            try {
                prepareDevice(i);
            } catch (Exception e) {
                failedTests++;
                failure("prepareDevice", i, e, System.currentTimeMillis() - time);
                continue;
            }
            try {
                executeTest(i);
            } catch (Exception e) {
                failedTests++;
                failure("executeTest", i, e, System.currentTimeMillis() - time);
                continue;
            }
            try {
                finishedSuccessIteration(i, System.currentTimeMillis() - time);
            } catch (Exception e) {
                failure("finish", i, e, System.currentTimeMillis() - time);
            }

        }

    }

    public void resetQuery(int i, long time, Exception e) {

        failure("getDevice", i, e, System.currentTimeMillis() - time);
        deviceQuery = null;
        deviceName = null;
        deviceShortName = null;
        String devicesInfo = client.getDevicesInformation();

    }

    public void simpleGetDeviceFailure(int i, Exception e, long time) {
        failure("getDevice", i, e, System.currentTimeMillis() - time);

    }

    protected void prepareReporter(int i) {
        deviceName = client.getDeviceProperty("device.name");
        deviceShortName = deviceName.substring(deviceName.indexOf(":") + 1);
        if (Runner.reporter) System.setProperty("manager.url", "192.168.2.72:8787");
        System.out.println("----------------@---------------- " + Thread.currentThread().getName() + "  STARTING - " + deviceName + " - " + testName + ": Iteration - " + (i + 1));
        System.out.println("----------------@---------------- " + Thread.currentThread().getName() + "  Set Reporter - " + client.setReporter("xml", reportFolder, deviceShortName + " - " + deviceOS + " - " + testName + "_" + (i + 1)));

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
            deviceOSVersion = client.getDeviceProperty("device.version");
            client.capture();
            client.setShowPassImageInReport(false);
            client.setSpeed("FAST");
            client.deviceAction("unlock");
            client.deviceAction("portrait");
            client.deviceAction("home");
            client.capture();
            if (deviceOS.contains("ios")) client.setProperty("ios.non-instrumented.dump.parameters", "20,1000,50");

        } else {
            System.out.println("CLIENT IS NULL!!!");
            throw new Exception("CLIENT IS NULL!!! -> Moving to the next test");
        }
    }

    private void executeTest(int i) {

        if (deviceOS.equals("ios")) {
            iOSRunTest();
        } else {
            androidRunTest();
        }
        success++;
    }

    public void failure(String failedStage, int i, Exception e, long time) {
        String stringToWrite = null;
        StringWriter errors = null;

        boolean collected = false;
        switch (failedStage) {
            case "getDevice": //getDevice
                System.out.println("Failed on " + failedStage + " - " + Thread.currentThread().getName() + " - Device - " + deviceQuery + " - test - " + testName);
                stringToWrite = createStringToWrite(failedStage, i, time, false);
                errors = getErrors(e);
                //    if (Runner.reporter) PManager.getInstance().addProperty("Error", e.getMessage());
                writeErrorsToSummaryReport(stringToWrite, errors, "No Report Was Generated");
                if (failedStage.contains("RESETTING")) {
                    writeToSummaryReport("RESETTING THE QUERY!! -> " + deviceName);
                }
                sleep(3000);
                break;
            case "prepareReporter": // prepareReporter
                System.out.println("Failed on " + failedStage + " - " + Thread.currentThread().getName() + " - Device - " + deviceQuery + " - test - " + testName);
                stringToWrite = createStringToWrite(failedStage, i, time, false);
                //      if (Runner.reporter) PManager.getInstance().addProperty("Error", e.getMessage());
                errors = getErrors(e);
                tryToReleaseClient();
                writeErrorsToSummaryReport(stringToWrite, errors, "No Report Was Generated");
                sleep(3000);
                break;
            case "prepareDevice": //prepareDevice & executeTest & finish
                failedWithDevice(failedStage, i, e, time);
                break;
            case "executeTest": //prepareDevice & executeTest & finish
                failedWithDevice(failedStage, i, e, time);
                break;
            case "finish": //prepareDevice & executeTest & finish
                failedWithDevice(failedStage, i, e, time);
                break;
        }

        // if (Runner.reporter) ResultPublisher.publishResult(null, testName, "", null, false, errors.toString(), null, false, time);
    }

    public void failedWithDevice(String failedStage, int i, Exception e, long time) {
        String stringToWrite;
        StringWriter errors;
        System.out.println("Failed on " + failedStage + " - " + Thread.currentThread().getName() + " - Device - " + deviceQuery + " - test - " + testName);
        stringToWrite = createStringToWrite(failedStage, i, time, false);
        //         if (Runner.reporter) PManager.getInstance().addProperty("Error", e.getMessage());
        errors = getErrors(e);
        tryToCapture();
        String generatedReportPath = tryToGenerateReport();
        Boolean dataCollected = tryToCollectSupportData(errors, generatedReportPath);
        tryToReleaseClient();
        writeErrorsToSummaryReport(stringToWrite, errors, generatedReportPath);
        if (!dataCollected)
            writeToSummaryReport(Thread.currentThread().getName() + "  " + deviceName + " - " + " Failed to Collect Support Data" + "\n");
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
            client.collectSupportData(generatedReport + "\\SupportDataFor_" + deviceShortName + "_test_" + testName + "_" + System.currentTimeMillis() + ".zip", "", deviceName, errors.toString(), "", "", true, true);
            supportData = generatedReport + "\\SupportDataFor_" + deviceShortName + "_test_" + testName + "_" + System.currentTimeMillis() + ".zip";
        } catch (Exception e) {
            System.out.println(Thread.currentThread().getName() + " - Failed to Collect Support Data - " + Thread.currentThread().getName() + " - Device - " + deviceShortName + " - test - " + testName);
            e.printStackTrace();
            return false;
        }
        if (supportData != null && Runner.reporter) addSupportDataToReporter(supportData);

        return true;
    }

    public void addSupportDataToReporter(String supportData) {
        boolean collected = false;
        long startTime = System.currentTimeMillis();
        do {
            try {
                // if (Runner.reporter) PManager.getInstance().addReportZipFile(supportData);
                collected = true;
            } catch (Exception e) {
                System.out.println("Failed to COLLECT SUPPORT DATA");
                System.out.println("Going to sleep for 5 seconds");
                client.sleep(2000);
            }
        } while (!collected && (System.currentTimeMillis() - startTime) > 30000);
    }

    public String tryToGenerateReport() {
        String generatedReport = null;
        try {
            generatedReport = client.generateReport(false);
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + generatedReport + "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

        } catch (Exception e1) {
            System.out.println(Thread.currentThread().getName() + " - Failed to Generate Report- " + Thread.currentThread().getName() + " - Device - " + deviceName + " - test - " + testName);
            e1.printStackTrace();
        }
        client.sleep(1000);
        // if (generatedReport != null && Runner.reporter) if (Runner.reporter) PManager.getInstance().addReportFolder(generatedReport);

        if (generatedReport == null) {
            generatedReport = "c:\\Temp\\Reports";
            System.out.println(Thread.currentThread().getName() + " - Failed to Generate Report - unknown reason - Will save the support data to 'Reports'");
        }

        return generatedReport;
    }

    public void finishedSuccessIteration(int i, long time) throws InterruptedException {

        String stringToWrite = createStringToWrite("finished", i, time, true);

        String generatedReportFolder = tryToGenerateReport();

        writeToSummaryReport(stringToWrite);

        if (Runner.GRID && Runner.scanLogs) {
            getAndWriteExceptions(generatedReportFolder);
        }

        System.out.println(Thread.currentThread().getName() + " - Finished Iteration - " + i + " - In - " + Thread.currentThread().getName() + "  " + deviceName + " - " + "REPORT - " + generatedReportFolder);
        client.releaseClient();
        //   if (Runner.reporter) ResultPublisher.publishResult(null, testName, "", null, true, null, null, false, time);


    }

    public void getAndWriteExceptions(String generatedReportFolder) {
        ArrayList<String> exceptionArray = null;
        exceptionArray = tryToCheckTheLogForExceptions(generatedReportFolder);
        boolean flag = false;
        try {
            if (exceptionArray.size() > 0) {
                for (int j = 0; j < exceptionArray.size(); j++) {
                    if (j > 5) break;
                    if (!exceptionArray.get(j).contains("start ui automationCould")) {
                        if (!exceptionArray.get(j).contains("illegal node name")) {
                            if (!exceptionArray.get(j).contains("Failed to scroll the element into view")) {
                                String exceptionFinalString;
                                try {
                                    exceptionFinalString = exceptionArray.get(j).substring(0, exceptionArray.get(j).indexOf(" at ", 500));
                                } catch (Exception e) {
                                    exceptionFinalString = exceptionArray.get(j);
                                }
                                writeToSummaryReport("\t" + deviceName + " -\n" + "\t" + exceptionFinalString);
                                flag = true;
                            }
                        }
                    }
                }
                if (flag)
                    writeToSummaryReport(Thread.currentThread().getName() + "  " + deviceName + " - " + "REPORT - " + generatedReportFolder + " - file:///" + generatedReportFolder.replace('\\', '/') + "/index.html\n");

            }
        } catch (Exception e) {

        }
    }

    public ArrayList<String> tryToCheckTheLogForExceptions(String generatedReportFolder) {
        ArrayList<String> exceptionArray = null;

        try {
            exceptionArray = ExtractExceptions(generatedReportFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return exceptionArray;
    }

    private void writeErrorsToSummaryReport(String stringToWrite, StringWriter errors, String generatedReport) {
        writeToSummaryReport(stringToWrite);
        String errorString = (errors.toString().contains("<!DOCTYPE html><html>")) ? errors.toString().substring(errors.toString().indexOf("at com.experitest.")) : errors.toString();
        writeToSummaryReport("\t" + deviceName + " -\n" + "\t" + errorString);
        writeToSummaryReport(Thread.currentThread().getName() + "  " + deviceName + " - " + "REPORT - " + generatedReport + " - file:///" + generatedReport.replace('\\', '/') + "/index.html\n");
    }

    private StringWriter getErrors(Exception e) {
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

        //if (Runner.reporter) addPropertiesToReporter(Stage, status);

        return stringToWrite;
    }

    private void addPropertiesToReporter(String Stage, String status) {
        String isGrid = (Runner.GRID) ? "grid" : "local";
        if (Runner.reporter) PManager.getInstance().addProperty("grid", isGrid);
        if (Runner.reporter) PManager.getInstance().addProperty("status", status);
        if (Runner.reporter) PManager.getInstance().addProperty("device", deviceName);
        if (Runner.reporter) PManager.getInstance().addProperty("Stage", Stage);
        if (Runner.reporter) PManager.getInstance().addProperty("testName", testName);
        if (Runner.reporter) PManager.getInstance().addProperty("build", Runner.buildNum + "");
    }

    protected abstract void androidRunTest();

    protected abstract void iOSRunTest();

    public void writeToSummaryReport(String stringToWrite) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter("Reports/report.txt", true)));
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            if (stringToWrite.startsWith("pass") || stringToWrite.startsWith("fail")) {
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
