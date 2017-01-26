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

        StartTesting();

        double successRate = ((double) success / (double) (Runner.repNum));
        String finish = "\n\nFINISHED - " + deviceName + " - " + testName + " - " + Thread.currentThread().getName() + " - Success Rate: " + success + "/" + (Runner.repNum) + " = " + successRate + "\n\n";
        System.out.println(finish);
    }

    public void StartTesting() {
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
                Finish(i, time);
            } catch (Exception e2) {
                System.out.println("Failed On Finish - " + Thread.currentThread().getName() + " - Device - " + deviceName + " - test - " + testName);
                e2.printStackTrace();
                Failure(i, e2, time);
            }
        }

    }

    public void SetUpTest(int i) throws Exception {
        client = ClientFactory.SetUp(testName, this.deviceOS, this.deviceQuery, commandMap, deviceName);
        if (client != null) {
            if (deviceName == null) {
                this.deviceName = client.getDeviceProperty("device.name");
                deviceShortName = deviceName.substring(deviceName.indexOf(":") + 1);
            }
            if (deviceSN==null) this.deviceSN = client.getDeviceProperty("device.sn");

            System.out.println("----------------@---------------- " + Thread.currentThread().getName() + "  STARTING - " + deviceName + " - " + testName + ": Iteration - " + (i + 1));
            System.out.println("----------------@---------------- " + Thread.currentThread().getName() + "  Set Reporter - " + client.setReporter("xml", reportFolder, deviceShortName + " " + deviceOS + " - " + testName + " - " + (i + 1)));
            client.setShowPassImageInReport(false);
//            client.deviceAction("unlock");
            client.sendText("{HOME}");
        } else {
            throw new Exception("CLIENT FROM GRID IS NULL!!!");
        }

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
        return time;
    }

    public void Failure(int i, Exception e, long time) {
        String stringToWrite = WriteAndGetResults(i, time, false);
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
                System.out.println(Thread.currentThread().getName()+" - Failed to Generate Report - unknown reason - Will save the support data to 'Reports'");
            }
        } catch (Exception e1) {
            System.out.println(Thread.currentThread().getName()+" - Failed to Generate Report- " + Thread.currentThread().getName() + " - Device - " + deviceName + " - test - " + testName);
            e1.printStackTrace();
        }

        WriteFailure(stringToWrite, errors, generatedReport);

        try {
            client.collectSupportData(generatedReport + "\\SupportDataFor_" + deviceShortName + "_test_" + testName + "_" + System.currentTimeMillis() + ".zip", "", deviceShortName, errors.toString(), "", "", true, false);
        } catch (Exception e2) {
            System.out.println(Thread.currentThread().getName()+" - Failed to Collect Support Data - " + Thread.currentThread().getName() + " - Device - " + deviceShortName + " - test - " + testName);
            e2.printStackTrace();
        }

        try {
            client.releaseClient();
        } catch (Exception e3) {
            System.out.println(Thread.currentThread().getName()+" - Failed to Release Device - " + Thread.currentThread().getName() + " - Device - " + deviceName + " - test - " + testName);
            client = null;
        }

    }

    public void Finish(int i, long time) {

        System.out.println(Thread.currentThread().getName()+" - Finished Iteration - " + i + " - In - " + Thread.currentThread().getName() + "  " + deviceName + " - " + "REPORT - " + client.generateReport(false));
       // if(!Runner.GRID) client.releaseDevice(deviceName, true, true, true);
        client.releaseClient();

        String stringToWrite = WriteAndGetResults(i, time, true);
        Write(stringToWrite);
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
        Write("\t" + deviceName + " -\n" + "\t"+errors.toString());
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
