package FrameWork;

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
import java.util.Map;


public class ClientFactory {


    public static MyClient SetUp(String testName, String deviceSN) {

        MyClient client = null;
        try {
            if (Runner.GRID) {
                client = getGridClient(testName, deviceSN);
            } else {
                client = getClient(deviceSN);
            }
            if (client == null) {
                System.out.println("Client SetUp Failed for - " + deviceSN);
            } else {
                System.out.println("Client Was SetUp For - " + deviceSN);
            }
            return client;

        } catch (Exception e) {
            System.out.println("---------------" + deviceSN + " - CAN NOT GET THE DEVICE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            return client;
        }

    }

    public static MyClient getGridClient(String testName, String deviceSN) {

        GridClient grid = new GridClient(Runner.cloudServer.USER, Runner.cloudServer.PASS, Runner.cloudServer.PROJECT, Runner.cloudServer.HOST, Runner.cloudServer.PORT, Runner.cloudServer.SECURED);
        Client client = null;
        //  synchronized (Suite.class) {
        client = grid.lockDeviceForExecution(testName, "@serialnumber='" + deviceSN + "'", 5, 30000);
        //    }
        MyClient myclient = null;
        myclient = new MyClient(client);

        return myclient;
    }

    public static MyClient getClient(String deviceSN) {

        Client client = new Client("localhost", 8889, true);
        MyClient myClient = new MyClient(client);
        System.out.println("Boaz Hadad Is The King Of Client - Not Gridy");
        myClient.waitForDevice("@serialnumber='" + deviceSN + "'", 300000);
        return myClient;
    }

}
