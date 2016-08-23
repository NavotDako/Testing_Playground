package Cloud;

import java.util.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import com.experitest.client.Client;

public class CloudTest {
    private String host = "localhost";
    protected Client client = null;

    List<String> failed = new LinkedList<String>();
    String searchParam = "name";

    @Before
    public void setUp(){
        try {
            client = new Client(host, 8889, true);
        }catch(Exception e){
            System.out.println("Can't Connect to SeeTest Client");
            System.out.println(e.getMessage());

        }
    }

    @Test
    public void testVisualDump() {
        List<String> devices = GetGoodDevices();

        System.out.println("Devices found for Memory:");
        for (int i =0;i<devices.size(); i++) {
            System.out.println(i+": "+devices.get(i));

        }

        for (String dev : devices) {
            try {
                System.out.println("----------------------------------------");
                System.out.println("Starting "+ dev);
                System.out.println("----------------------------------------");

                client.waitForDevice("@"+searchParam+"='"+ dev +"'", 5000);
            }
            catch (Exception e) {
                failed.add(dev +": " + e.getMessage());
                continue;
            }
            try {
                client.openDevice();
                client.sendText("{HOME}");
                if (client.getVisualDump("NATIVE").length() < 50){
                    failed.add(dev + ": Dump too small");
                }
            }
            catch (Exception e) {
                failed.add(dev +": " + e.getMessage());
            }
            client.releaseDevice("", true, true, true);

        }

        System.out.println("----------------------------------------");
        System.out.println("Tested Devices:");
        for (String device : devices)
            System.out.println(device);
        System.out.println("----------------------------------------");


        Assert.assertTrue(failed.isEmpty());
    }

    private List<String> GetGoodDevices() {
        Scanner allInfo = new Scanner(client.getDevicesInformation());
        List<String> devices = new LinkedList<String>();
        while (allInfo.hasNextLine()) {
            String device = allInfo.findInLine(searchParam + "=\"[^\"]*\"");

            if (allInfo.findInLine("remote=\"true\"") != null && allInfo.findInLine("status=\"unreserved online\"") != null)
                devices.add(device.substring(searchParam.length() + 2, device.length() - 1));
            allInfo.nextLine();
        }
        allInfo.close();
        return devices;
    }


    @After
    public void tearDown(){
        if (!failed.isEmpty()) {
            System.out.println("----------------------------------------");
            System.out.println("Errors found:");

            for (String s : failed)
                System.out.println(s);
            System.out.println("----------------------------------------");
        }else{
            System.out.println("----------------------------------------");
            System.out.println("No Errors Found");
            System.out.println("----------------------------------------");
        }
    }
}