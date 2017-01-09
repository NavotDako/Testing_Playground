package FrameWork;

import com.experitest.client.GridClient;

import java.util.Map;

/**
 * Created by navot.dako on 1/8/2017.
 */
public class ClientFactory {
    static String serverHost = "192.168.2.13";
    static String projectBaseDirectory = "C:\\Users\\DELL\\workspace\\project11";
    static int serverPort = 8090;


    public static MyClient SetUp(String testName, String deviceOS, String deviceQuery, Map<String, Command> commandMap, String deviceName) {

        MyClient client = null;
        try {
            if (Runner.GRID) {
                client = getGridClient(testName, commandMap, deviceOS, deviceQuery, deviceName);
            } else {
                client = getClient(commandMap, deviceOS, deviceQuery, deviceName);
            }
            if (client != null) {
                deviceName = client.getDeviceProperty("device.name");
                System.out.println(Thread.currentThread().getName() + " - " + deviceName.substring(deviceName.indexOf(":")));
                FinishSetUP(client);
            } else {
                System.out.println("WAIT FOR DEVICE FAILED for - " + "@os = '" + deviceOS + "'" + deviceQuery);
            }
            return client;

        } catch (Exception e) {
            System.out.println("---------------" + deviceName + " - CAN NOT GET A DEVICE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            return client;
        }
    }

    public static void FinishSetUP(MyClient client) {
        client.setProjectBaseDirectory("C:\\Users\\DELL\\workspace\\project18");
        if (!Runner.GRID) client.openDevice();
        client.deviceAction("Unlock");
    }

    /*private void InstallChromeIfNeeded() {
        String apps = client.getInstalledApplications();
        if (!apps.contains("com.android.chrome")) {

        }
    }*/

    public static MyClient getGridClient(String testName, Map<String, Command> commandMap, String deviceOS, String deviceQuery, String deviceName) {

        MyClient myclient = null;
        GridClient grid = new GridClient("admin", "Experitest2012", "", serverHost, serverPort, false);
        if (deviceName == null) {
            System.out.println("@os='" + deviceOS + "'" + deviceQuery);
            myclient = new MyClient(commandMap, grid.lockDeviceForExecution(testName, "@os='" + deviceOS + "'" + deviceQuery, Runner.repNum * 5, 30000));

            //if (client.getDeviceProperty("deviceName.os").contains("android")) InstallChromeIfNeeded();
        } else {
            System.out.println("@name='" + deviceName.substring(deviceName.indexOf(":") + 1) + "'" + deviceQuery);
            myclient = new MyClient(commandMap, grid.lockDeviceForExecution(testName, "@name='" + deviceName.substring(deviceName.indexOf(":") + 1) + "'", Runner.repNum * 5, 30000));
        }
        return myclient;
    }

    public static MyClient getClient(Map<String, Command> commandMap, String deviceOS, String deviceQuery, String deviceName) {

        MyClient myclient = new MyClient(commandMap, null);
        if (deviceName == null) {
            deviceName = myclient.waitForDevice("@os = '" + deviceOS + "'" + deviceQuery, 30000);
        } else {
            myclient.waitForDevice("@name = '" + deviceName.substring(deviceName.indexOf(":") + 1) + "'", 30000);
        }
        return myclient;
    }

}
