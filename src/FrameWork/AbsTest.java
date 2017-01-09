package FrameWork;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public abstract class AbsTest {
    protected int success = 0;

    protected String deviceName = null;
    protected String reportFolder;
    protected String deviceOS;
    protected String testName;
    protected String deviceQuery = "";
    protected MyClient client;
    protected Map<String, Command> commandMap = null;

    public AbsTest(String deviceOS, String deviceQuery, String testName, Map<String, Command> commandMap) {
        this.commandMap = commandMap;
        this.reportFolder = Runner.reportFolderString;
        this.deviceOS = deviceOS;
        this.deviceQuery = deviceQuery;
        this.testName = testName;

        StartTesting();

        double successRate = ((double) success / (double) (Runner.repNum));
        String finish = "FINISHED - " + deviceName + " - " + testName + " - " + Thread.currentThread().getName() + " - Success Rate: " + success + "/" + (Runner.repNum) + " = " + successRate;
        System.out.println(finish);
    }


    public boolean StartTesting() {
        long time = 0;

        for (int i = 0; i < Runner.repNum; i++) {
            try {
                SetUpTest(i);
            } catch (Exception e) {
                System.out.println("Failed on SetUp - " + Thread.currentThread().getName() + " - Device - " + deviceName + " - test - " + testName);
                Failure(i, e, time);
                continue;
            }
            try {
                time = ExecuteTest(i);
            } catch (Exception e1) {
                System.out.println("Failed On Execution - " + Thread.currentThread().getName() + " - Device - " + deviceName + " - test - " + testName);
                Failure(i, e1, time);
                continue;
            }
            try {
                Finish(i);
            } catch (Exception e2) {
                System.out.println("Failed On Finish - " + Thread.currentThread().getName() + " - Device - " + deviceName + " - test - " + testName);
                e2.printStackTrace();
                Failure(i, e2, time);
            }
        }
        //Write(finish);
        return true;
    }

    public void SetUpTest(int i) {
        client = ClientFactory.SetUp("EriBank", this.deviceOS, this.deviceQuery, commandMap, deviceName);
        if (deviceName == null) this.deviceName = client.getDeviceProperty("device.name");
        System.out.println("----------------@---------------- " + Thread.currentThread().getName() + "  STARTING - " + deviceName + " - " + testName + ": Iteration - " + (i + 1));
        client.sendText("{UNLOCK}");
        client.sendText("{HOME}");
        System.out.println("----------------@---------------- " + Thread.currentThread().getName() + "  Set Reporter - " + client.setReporter("xml", reportFolder, deviceName.substring(deviceName.indexOf(":") + 1) + " " + deviceOS + " - " + testName + " - " + (i + 1)));

    }

    private long ExecuteTest(int i) {

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

    public void Failure(int i, Exception e, long time) {
        String stringToWrite = WriteAndGetResults(i, time, false);
        StringWriter errors = GetErrors(e);
        String generatedReport = null;

        try {
            generatedReport = client.generateReport(false);
        } catch (Exception e1) {
            System.out.println("Failed to Generate Report- " + Thread.currentThread().getName() + " - Device - " + deviceName + " - test - " + testName);
            e1.printStackTrace();
        }

        WriteFailure(stringToWrite, errors, generatedReport);

        try {
            client.collectSupportData(generatedReport + "\\SupportData", "", deviceName, "", "", "", true, false);
        } catch (Exception e2) {
            System.out.println("Failed to Collect Support Data - " + Thread.currentThread().getName() + " - Device - " + deviceName + " - test - " + testName);
            e2.printStackTrace();
        }

        try {
            client.releaseDevice(deviceName, true, IsCloudDevice(), true);
            client.releaseClient();
        } catch (Exception e3) {
            System.out.println("Failed to Release Device - " + Thread.currentThread().getName() + " - Device - " + deviceName + " - test - " + testName);
            client = null;
        }

    }

    public void Finish(int i) {
        System.out.println("Finished Iteration - "+i+" - In - "+Thread.currentThread().getName() + "  " + deviceName + " - " + "REPORT - " + client.generateReport(false));
        client.releaseDevice(deviceName, true, IsCloudDevice(), true);
        client.releaseClient();
    }

    private boolean IsCloudDevice() {

        if (!Runner.GRID) {
            if (client.getDeviceProperty("device.remote").contains("true"))
                return true;
            else return false;
        } else {
            return true;
        }
    }

    private void WriteFailure(String stringToWrite, StringWriter errors, String generatedReport) {
        Write("*** " + stringToWrite + " ***");
        Write("  " + deviceName + " - " + errors.toString());
        Write(Thread.currentThread().getName() + "  " + deviceName + " - " + "REPORT - " + generatedReport + "\n");
    }

    private StringWriter GetErrors(Exception e) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        System.err.println(Thread.currentThread().getName() + " " + deviceName + " - StackTrace: " + errors.toString());
        return errors;
    }

    private String WriteAndGetResults(int i, long time, boolean succeeded) {
        double successRate = ((double) success / (double) (i + 1));
        String status = (succeeded) ? "SUCCESS" : "FAILURE";
        String stringToWrite = status + "- " + deviceName + " - " + testName + " - " + Thread.currentThread().getName() + ": Iteration - " + (i + 1) + " - Success Rate: " + success + "/" + (i + 1) + " = " + successRate + "    Time - " + time / 1000 + "s";
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
            if (stringToWrite.contains("Success Rate")) {
                writer.append(Runner.commandIndex + " \t" + sdf.format(new Date(System.currentTimeMillis())) + ": " + stringToWrite + "\n");
                Runner.commandIndex++;
            } else
                writer.append(" \t" + sdf.format(new Date(System.currentTimeMillis())) + ": " + stringToWrite + "\n");

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
