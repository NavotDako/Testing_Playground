package FrameWork;

import com.experitest.client.GridClient;

import java.util.Map;


public class ClientFactory {

    public static MyClient SetUp(String testName, String deviceOS, String deviceQuery, Map<String, Command> commandMap, String serial) {

        MyClient client = null;
        try {
            if (Runner.GRID) {
                client = getGridClient(testName, commandMap, deviceOS, deviceQuery, serial);
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



    public static MyClient getGridClient(String testName, Map<String, Command> commandMap, String deviceOS, String deviceQuery, String serial) {

        MyClient myclient = null;
       /* System.out.println(Runner.pr.getString("user") + "__________________"+Runner.pr.getString("password"));*/
        GridClient grid = new GridClient(Runner.pr.getString("user"), Runner.pr.getString("password"), "", Runner.pr.getString("server_host"), Runner.pr.getPort("server_port"), false);
        if (serial == null) {
            System.out.println("@os='" + deviceOS + "'" + deviceQuery);
            myclient = new MyClient(commandMap, grid.lockDeviceForExecution(testName, "@os='" + deviceOS + "'" + deviceQuery, Runner.repNum * 5, 200000));

            //if (client.getDeviceProperty("deviceName.os").contains("android")) InstallChromeIfNeeded();
        } else {
            System.out.println("@serialnumber='" + serial+"'");
            myclient = new MyClient(commandMap, grid.lockDeviceForExecution(testName, "@serialnumber='" + serial + "'", Runner.repNum * 5, 200000));
        }
        return myclient;
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
