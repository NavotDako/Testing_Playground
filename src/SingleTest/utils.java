package SingleTest;

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
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

/**
 * Created by navot.dako on 4/23/2017.
 */
public class utils {

    public static String readXML(String devicesInformation, String os) throws ParserConfigurationException, IOException, SAXException {

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
                if (os.equals(eElement.getAttribute("os"))) {
                    System.out.println("id : "
                            + eElement.getAttribute("serialnumber"));
                    return eElement.getAttribute("serialnumber");
                }
            }
        }
        return null;

    }

    public static String writeStringToXML(String fileName, String devicesInformation) throws IOException {
        File f = new File("devices" + System.currentTimeMillis() + ".xml");
        FileWriter fw = new FileWriter(f);
        fw.append(devicesInformation);
        fw.close();
        return f.getAbsolutePath();
    }
}
