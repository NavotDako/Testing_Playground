package SingleTest;

import FrameWork.MyClient;
import FrameWork.cloudPropReader;
import com.experitest.client.Client;
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
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by navot.dako on 4/23/2017.
 */
public class utils {
    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        //System.out.println("Using query for - "+deviceQuery);
        cloudPropReader pr = new cloudPropReader("qa");
        MyClient myclient = null;
        System.out.println(pr.getString("user") + " " + pr.getString("password") + " " + pr.getString("project") + " " + pr.getString("server_host") + " " + pr.getInt("server_port") + " " + pr.getBool("secured"));
        GridClient grid = new GridClient(pr.getString("user"), pr.getString("password"), pr.getString("project"), pr.getString("server_host"), pr.getInt("server_port"), pr.getBool("secured"));
        System.out.println(grid.getDevicesInformation());
        List<String> iosList = readXML(grid.getDevicesInformation(), "ios");
        System.out.println(iosList);
        List<String> androidList = readXML(grid.getDevicesInformation(), "android");
        System.out.println(androidList);
        Client client = grid.lockDeviceForExecution("testName", "@os='ios'", 10, 10000);
        client.collectSupportData("C:\\Temp\\New folder", "", client.getDeviceProperty("device.name"), "", "", "", true, true);



//        GridClient grid = new GridClient("user","password","project","server_host","server_port","secured");
//        Client client = grid.lockDeviceForExecution("testName", "@os='ios'", 10, 10000);
//        client.collectSupportData("C:\\Temp\\New folder", "", client.getDeviceProperty("device.name"), "", "", "", true, true);
    }

    public static List<String> readXML(String devicesInformation, String os) throws ParserConfigurationException, IOException, SAXException {
        List<String> tempList = new ArrayList<>();

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
                if (os.equals(eElement.getAttribute("os")) && eElement.getAttribute("status").equals("online") && eElement.getAttribute("currentuser").equals("")) {
                    System.out.println("id : "
                            + eElement.getAttribute("serialnumber"));
                    tempList.add(eElement.getAttribute("serialnumber"));
                }
            }
        }
        return tempList;

    }
}
