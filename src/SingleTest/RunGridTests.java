package SingleTest;

import com.experitest.client.Client;
import com.experitest.client.GridClient;


public class RunGridTests extends Thread {
    public static void main(String[] args) {
        for (int i = 0; i < 1; i++) {
            Thread t = new Thread(new MyRunnable("@os='android'"));
            t.start();
        }
    }
}

class MyRunnable implements Runnable {
    private static final boolean GRID = true;
    Client client = null;
    String device = "";

    public MyRunnable(String device) {
        System.out.println(device);
        this.device = device;
    }

    @Override
    public void run() {
        client = (GRID) ? getGridClient() : getClient();
        client.setReporter("xml", "c:\\temp", "test");

        try {
            loop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            finish();
        }

    }

    private Client getClient() {
       Client client = new Client("localhost",8889,true);
        client.waitForDevice(device,10000);
        return client;
    }

    private Client getGridClient() {
        GridClient grid = new GridClient("admin", "Experitest2012", "", "192.168.2.13", 8090, false);
        System.out.println(grid.getDevicesInformation());
        //GridClient grid = new GridClient("dror", "good1lucK", "", "cloud.experitest.com", 443, true);
        Client client = grid.lockDeviceForExecution("test", device, 5, 240000);
        return client;
    }

    public void loop() {
        for (int i = 0; i < 30; i++) {
            client.launch("https://www.google.co.il/search?q=" + i + "&oq=1&aqs=chrome..69i60l3j69i57j69i60j69i65.1808j0j4&sourceid=chrome&ie=UTF-8", true, true);
            //client.install("http://192.168.2.72:8181/iOSApps/EriBankO.ipa", true, false);
        }
    }

    private void finish() {
        client.generateReport(false);
        System.out.println("Support Data destination - "+client.collectSupportData("c:\\temp", "", client.getDeviceProperty("device.name"), "", "", "", true, true));
        client.releaseClient();

    }


}
    //GridClient grid = new GridClient("navot", "Experitest2012", "", "192.168.1.210", 80, false);
    //GridClient grid = new GridClient("dror", "good1lucK", "", "cloud.experitest.com", 443, true);
