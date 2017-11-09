package SingleTest;

import com.experitest.client.Client;
import com.experitest.client.GridClient;


public class RunGridTests extends Thread {


    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < 30; i++) {
            Thread t = new Thread(new MyRunnable("@serialnumber='e306fb3224fe2fbef2d1eb60118ee4ad7b7bf902'"));
            t.start();

        }
    }
}

class MyRunnable implements Runnable {
    private static final boolean GRID = true;
    Client client = null;
    String device = "";

    public MyRunnable(String device) {
        //System.out.println(device);
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
        } finally {
            finish();
        }
    }

    private Client getClient() {
        Client client = new Client("localhost", 8889, true);
        client.waitForDevice(device, 10000);
        return client;
    }

    private Client getGridClient() {
        GridClient grid = new GridClient("user", "Experitest2012", "Default", "192.168.2.13", 80, false);
        Client client = grid.lockDeviceForExecution("test", device, 300, 300000);
        return client;
    }

    public void loop() {
       // String appString = (client.getDeviceProperty("device.os").contains("IOS")) ? "http://192.168.2.72:8181/iOSApps/EriBankO.ipa" : "http://192.168.2.72:8181/AndroidApps/eribank.apk";
        for (int i = 0; i < 300; i++) {
            client.launch("https://www.google.co.il/search?q=" + i + "&oq=1&aqs=chrome..69i60l3j69i57j69i60j69i65.1808j0j4&sourceid=chrome&ie=UTF-8", true, true);
        }
    }

    private void finish() {
        client.generateReport(false);
        client.releaseClient();

    }


}
//GridClient grid = new GridClient("navot", "Experitest2012", "", "192.168.1.210", 80, false);
//GridClient grid = new GridClient("dror", "good1lucK", "", "cloud.experitest.com", 443, true);
