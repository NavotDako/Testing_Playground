package FrameWork;

import com.experitest.client.Client;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Runner {
    static int repNum = 10;
    static int retry = 4;
    static String method = "all"; //file/all
    static boolean GRID = true;
    static boolean scanLogs = false;

    static String reportFolderString = "c:\\temp\\Reports";
    static int commandIndex = 1;
    public static serialPropReader spr = null;
    public static long buildNum;
    public static boolean reporter = false;
    static CloudServer cloudServer;
    static DeviceManager localDeviceManager;

    public static void main(String[] args) throws Exception {
        if (GRID) cloudServer = new CloudServer(CloudServer.CloudServerName.MY);
        else localDeviceManager = new DeviceManager("localhost", 8889);

        buildNum = System.currentTimeMillis();

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

        List<String> iosList = (GRID) ? cloudServer.getAllAvailableDevices("ios") : localDeviceManager.getDeviceList("ios");
        List<String> androidList = (GRID) ? cloudServer.getAllAvailableDevices("android") : localDeviceManager.getDeviceList("android");
        System.out.println("------------------------------------------------------------------------------------");
        ExecutorService executorService = Executors.newFixedThreadPool(iosList.size() + androidList.size());

        List<Future> res = new LinkedList<>();

        for (int i = 0; i < iosList.size(); i++) {
            Suite suite = new Suite(iosList.get(i));
            res.add(executorService.submit(suite));
            System.out.println(String.format("%-50s%-15s%-3s", iosList.get(i), "- submitted -", (i + 1)));
        }
        System.out.println("------------------------------------------------------------------------------------");

        for (int i = 0; i < androidList.size(); i++) {
            Suite suite = new Suite(androidList.get(i));
            res.add(executorService.submit(suite));
            System.out.println(String.format("%-50s%-15s%-3s", androidList.get(i), "- submitted -", (i + 1)));
        }
        System.out.println("------------------------------------------------------------------------------------");

        for (Future re : res) {
            try {
                re.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        executorService.shutdown();
    }

    private static List<String> getLocalDevices(String os) {

        return null;
    }

    public static void useSerialListToRunThreads() throws Exception {
        spr = new serialPropReader();
        List<String> devicesList = new ArrayList<>();

        while (spr.hasNext()) {
            String deviceID = spr.getNext();
            devicesList.add(deviceID);

        }

        ExecutorService executorService = Executors.newFixedThreadPool(devicesList.size() + devicesList.size());

        List<Future> res = new LinkedList<>();
        for (int i = 0; i < devicesList.size(); i++) {
            Suite suite = new Suite(devicesList.get(i));
            res.add(executorService.submit(suite));
            System.out.println(String.format("%-50s%-15s%-3s", devicesList.get(i), "- submitted -", (i + 1)));
        }

        for (Future re : res) {
            try {
                re.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        executorService.shutdown();
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

    static class DeviceManager {
        NodeList deviceNodeList;

        public DeviceManager(String host, int port) {
            Client client = new Client(host, port, true);
            String devicesXml = client.getDevicesInformation();
            client.releaseClient();
            try {
                deviceNodeList = parseDocument(devicesXml);
            } catch (Exception e) {
                System.out.println("Couldn't parse connected devices list");
                System.exit(0);
            }
        }

        private NodeList parseDocument(String devicesXml) throws ParserConfigurationException, IOException, SAXException {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            StringBuilder xmlStringBuilder = new StringBuilder();
            xmlStringBuilder.append(devicesXml);
            ByteArrayInputStream input = new ByteArrayInputStream(xmlStringBuilder.toString().getBytes("UTF-8"));
            Document doc = builder.parse(input);
            doc.getDocumentElement().normalize();
            System.out.println("Root element :"
                    + doc.getDocumentElement().getNodeName());
            return doc.getElementsByTagName("device");
        }

        private List<String> getDeviceList(String requestedOS) throws ParserConfigurationException, IOException, SAXException {
            List<String> devicesList = new ArrayList<>();
            for (int temp = 0; temp < deviceNodeList.getLength(); temp++) {
                Node nNode = deviceNodeList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    if (eElement.getAttribute("remote").contains("false") || eElement.getAttribute("status").contains("unreserved online")) {
                        if (eElement.getAttribute("os").contains(requestedOS)) {
                            String serialnumber = eElement.getAttribute("serialnumber");
                            devicesList.add(eElement.getAttribute("serialnumber"));
                        }
                    }

                }
            }
            return devicesList;
        }

        public String getDeviceOSByUDID(String deviceSN) {
            for (int temp = 0; temp < deviceNodeList.getLength(); temp++) {
                Node nNode = deviceNodeList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    if (eElement.getAttribute("serialnumber").contains(deviceSN)) {
                        return eElement.getAttribute("os");
                    }
                }
            }
            return null;
        }
    }
}


