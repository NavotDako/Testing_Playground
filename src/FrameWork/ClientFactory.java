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
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;


public class ClientFactory {

    public static MyClient SetUp(String testName, String deviceOS, String deviceQuery, Map<String, Command> commandMap, String serial) {

        MyClient client = null;
        try {
            if (Runner.GRID) {
                if (deviceQuery.equals("")) client = getGridClient(testName, commandMap, deviceOS, deviceQuery, serial);
                else {
                    client = getGridClientFromQuery(testName, commandMap, deviceQuery);
                }
            } else {
                client = getClient(commandMap, deviceOS, deviceQuery, serial);
            }
            if (client != null) {
                serial = client.getDeviceProperty("device.sn");
            } else {
                System.out.println("WAIT FOR DEVICE FAILED for - " + "@os = '" + deviceOS + "'" + deviceQuery);
            }
            return client;

        } catch (Exception e) {
            System.out.println("---------------" + serial + " - CAN NOT GET A DEVICE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            return client;
        }
    }

    private static MyClient getGridClientFromQuery(String testName, Map<String, Command> commandMap, String deviceQuery) {
        System.out.println("Using query for - "+deviceQuery);
        MyClient myclient = null;
        GridClient grid = new GridClient(Runner.pr.getString("user"), Runner.pr.getString("password"), Runner.pr.getString("project"), Runner.pr.getString("server_host"), Runner.pr.getPort("server_port"), Runner.pr.getBool("secured"));

        myclient = new MyClient(commandMap, grid.lockDeviceForExecution(testName, deviceQuery, Runner.repNum * 5, 300000));
        return myclient;
    }

    public static MyClient getGridClient(String testName, Map<String, Command> commandMap, String deviceOS, String deviceQuery, String serial) {

        MyClient myclient = null;
        GridClient grid = new GridClient(Runner.pr.getString("user"), Runner.pr.getString("password"), Runner.pr.getString("project"), Runner.pr.getString("server_host"), Runner.pr.getPort("server_port"), Runner.pr.getBool("secured"));

        String serialnumber = null;
        if (serial == null) {

            try {
                serialnumber = readXML(grid.getDevicesInformation(), deviceOS);
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
        } else {
            serialnumber = serial;
        }
        //  System.out.println(grid.getDevicesInformation());
        if (serialnumber != null) {
            System.out.println("@serialnumber='" + serialnumber + "'");
            myclient = new MyClient(commandMap, grid.lockDeviceForExecution(testName, "@serialnumber='" + serialnumber + "'", Runner.repNum * 5, 300000));

            //if (client.getDeviceProperty("deviceName.os").contains("android")) InstallChromeIfNeeded();
        } else {
            System.out.println(Thread.currentThread().getName() + " - NO DEVICE WAS FOUND!");
        }
        return myclient;
    }

    private static String readXML(String devicesInformation, String os) throws ParserConfigurationException, IOException, SAXException {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();

        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(devicesInformation));
        Document doc = db.parse(is);

        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        NodeList nList = doc.getElementsByTagName("device");
        System.out.println("----------------------------");

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                //System.out.println("\nCurrent device :" + eElement.getAttribute("os") + " - " + eElement.getAttribute("name"));
                if (os.equals(eElement.getAttribute("os")) && eElement.getAttribute("currentuser").equals("") && eElement.getAttribute("status").equals("online")) {
                    System.out.println("id : "
                            + eElement.getAttribute("serialnumber"));
                    return eElement.getAttribute("serialnumber");
                }
            }
        }
        return null;

    }

    public static MyClient getClient(Map<String, Command> commandMap, String deviceOS, String deviceQuery, String serial) {

        MyClient myclient = new MyClient(commandMap, null);
        if (serial == null) {
            myclient.waitForDevice("@os = '" + deviceOS + "'" + deviceQuery, 30000);
        } else {
            myclient.waitForDevice("@serialnumber='" + serial + "'", 30000);
        }
        return myclient;
    }

}
