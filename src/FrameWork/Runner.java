package FrameWork;

import com.experitest.client.GridClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Runner {

    static int repNum = 1;
    static int retry = 3;
    static String method = "all"; //file/all
    static String cloud = "my"; //my/public/qa
    static boolean GRID = true;
    static boolean scanLogs = true;

    static String reportFolderString = "c:\\temp\\Reports";
    static String deviceQuery = "";
    static int commandIndex = 1;
    public static cloudPropReader cpr = null;
    public static serialPropReader spr = null;
    public static long buildNum;
    public static boolean reporter = false;

    public static void main(String[] args) throws Exception {
        buildNum = System.currentTimeMillis();
        cpr = new cloudPropReader(cloud);

        String resources = getResources();
        prepareReportsFolders();

        switch (method) {
            case "all":
                runOnAllAvailableDevices();
                break;
            case "file":
                useSerialListToRunThreads();
                break;
        }

        System.out.println("Start Resources:\n" + resources);
        System.out.println("End Resources:\n" + getResources());
    }

    private static void runOnAllAvailableDevices() throws IOException, SAXException, ParserConfigurationException, InterruptedException {
        List<Thread> threadList = new ArrayList<>();
        GridClient grid = new GridClient(cpr.getString("user"), cpr.getString("password"), cpr.getString("project"), cpr.getString("server_host"), cpr.getInt("server_port"), cpr.getBool("secured"));
        System.out.println(grid.getDevicesInformation());
        List<String> iosList = readXMLForAllAvailableDevices(grid.getDevicesInformation(), "ios");
        List<String> androidList = readXMLForAllAvailableDevices(grid.getDevicesInformation(), "android");
        String deviceOS = "";
        System.out.println("----------------------------");
        System.out.println("----------------------------");

        for (int i = 0; i < iosList.size(); i++) {
            deviceOS = "ios";
            String tempDeviceQuery = "@serialnumber='" + iosList.get(i) + "'";
            System.out.println(tempDeviceQuery);
            Thread thread = new Thread(new Suite(repNum, reportFolderString, deviceOS, tempDeviceQuery));
            threadList.add(thread);
            thread.start();
        }
        for (int i = 0; i < androidList.size(); i++) {
            deviceOS = "android";
            String tempDeviceQuery = "@serialnumber='" + androidList.get(i) + "'";
            System.out.println(tempDeviceQuery);

            Thread thread = new Thread(new Suite(repNum, reportFolderString, deviceOS, tempDeviceQuery));
            threadList.add(thread);
            thread.start();
        }

        System.out.println("----------------------------");
        System.out.println("----------------------------");

        waitForThreadsToFinish(threadList);

    }

    public static void useSerialListToRunThreads() throws Exception {
        spr = new serialPropReader();
        List<Thread> threadList = new ArrayList<>();
        GridClient grid = new GridClient(cpr.getString("user"), cpr.getString("password"), cpr.getString("project"), cpr.getString("server_host"), cpr.getInt("server_port"), cpr.getBool("secured"));
        String devicesInformation = grid.getDevicesInformation();
        System.out.println(devicesInformation);
        while (spr.hasNext()) {
            String deviceID = spr.getNext();
            deviceQuery = "@serialnumber='" + deviceID + "'";
            System.out.println(deviceQuery);
            String deviceOS = readXMLForOS(deviceID, devicesInformation);

            Thread thread = new Thread(new Suite(repNum, reportFolderString, deviceOS, deviceQuery));
            threadList.add(thread);
            thread.start();
        }
        waitForThreadsToFinish(threadList);
    }

    private static List<String> readXMLForAllAvailableDevices(String devicesInformation, String os) throws ParserConfigurationException, IOException, SAXException {
        List<String> tempList = new ArrayList<>();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();

        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(devicesInformation));
        Document doc = db.parse(is);

        System.out.println(os + " devices :");
        NodeList nList = doc.getElementsByTagName("device");

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                if (os.equals(eElement.getAttribute("os")) && eElement.getAttribute("status").equals("online") && eElement.getAttribute("currentuser").equals("")) {
                    System.out.println("id : "
                            + eElement.getAttribute("serialnumber"));
                    tempList.add(eElement.getAttribute("serialnumber"));
                }
            }
        }
        System.out.println("----------------------------");
        return tempList;

    }

    private static void waitForThreadsToFinish(List<Thread> threadList) throws InterruptedException {
        for (int i = 0; i < threadList.size(); i++) {
            while (threadList.get(i).isAlive()) {
                Thread.sleep(1000);
            }
        }
    }

    private static String readXMLForOS(String deviceID, String devicesInformation) throws ParserConfigurationException, IOException, SAXException {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();

        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(devicesInformation));
        Document doc = db.parse(is);

        NodeList nList = doc.getElementsByTagName("device");
        String tempOS = "";
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                if (deviceID.toLowerCase().equals(eElement.getAttribute("serialnumber"))) {
                    System.out.println("id : " + eElement.getAttribute("serialnumber"));
                    System.out.println("os : " + eElement.getAttribute("os"));
                    System.out.println("--------------------------------------------------------------");
                    tempOS = eElement.getAttribute("os");
                }
            }
        }

        return tempOS;
    }

    private static void prepareReportsFolders() {
        System.out.println("Preparing the reports folder");
        try {
            File Report = new File("Reports");
            for (File file : Report.listFiles()) file.delete();
            File ReportFolder = new File(reportFolderString);
            for (File file : ReportFolder.listFiles()) DeleteRecursive(file);
            System.out.println("Finished preparing the reports folder");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void DeleteRecursive(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                DeleteRecursive(child);

        fileOrDirectory.delete();

    }

    private static String getResources() throws IOException {
        System.out.println("Getting the studio.exe resources");
        String line;
        String savedLine = "";
        Process process = Runtime.getRuntime().exec("tasklist.exe");
        BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
        while ((line = input.readLine()) != null) {
            if (line.startsWith("studio.exe")) { // I only want the processes that have 'studio.exe' for a name.
                savedLine = line;
            }
        }
        return savedLine;
    }


}
