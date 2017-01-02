package SingleTest;

import com.experitest.client.Client;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class runJunit {
    static String reportFolderString = "c:\\temp\\Reports\\SingleTestReports";

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, InterruptedException {

        File ReportFolder = new File(reportFolderString);
        for (File file : ReportFolder.listFiles()) DeleteRecursive(file);

        int counter = 0;
        while (true){
            System.out.println("-------------------------------------------------------------------------");
            String[] devicesArray = getDevicesStrings();
            System.out.println("-------------------------------------------------------------------------");
            Thread t =null;
            Thread t2=null;

            int randomDeviceIndex=0;
            int randomDeviceIndex2=0;
            while (randomDeviceIndex==randomDeviceIndex2) {
                randomDeviceIndex = (int) (Math.random() * devicesArray.length);
                randomDeviceIndex2 = (int) (Math.random() * devicesArray.length);
            }
            counter++;
            t = new Thread(new RunTest(devicesArray[randomDeviceIndex],counter));
            t.start();
            counter++;
            t2 = new Thread(new RunTest(devicesArray[randomDeviceIndex2],counter));
            t2.start();

            while (t.isAlive() || t2.isAlive()){
                System.out.println("Threads are alive");
                Thread.sleep(1000);
            }
            System.out.println("GOING TO SLEEP");
            Thread.sleep(1000*15);
        }

    }

    public static String[] getDevicesStrings() {
        String host = "localhost";
        int port = 8889;
        Client client;
        client = new Client(host, port, true);
        String devices = client.getConnectedDevices();
        client.releaseClient();
        String[] devicesArray = devices.split("\n");
        System.out.println();
        for (int i = 0; i < devicesArray.length; i++) {
            System.out.println("device - "+i+" = "+devicesArray[i]);
        }
        return devicesArray;
    }

    public static void DeleteRecursive(File fileOrDirectory) {
        System.out.println("Deleting Report Folder - "+fileOrDirectory.getAbsolutePath());
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                DeleteRecursive(child);

        fileOrDirectory.delete();

    }
}

