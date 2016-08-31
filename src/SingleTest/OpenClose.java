package SingleTest;

import com.experitest.client.*;

import org.junit.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class OpenClose {

    private String host = "localhost";
    private int port = 8889;
    private String projectBaseDirectory = "C:\\Users\\DELL\\workspace\\project18";
    protected Client client = null;

    @Before
    public void setUp() throws IOException {

    }

    @Test
    public void testSetNetworkConditions() throws IOException {
        List<String> strings = new ArrayList<String>();

        for (int i = 0; i < 50 ; i++) {

            strings.add(GetMemory());
            System.out.println("Iter - "+i);
            client = new Client(host, port, true);
            client.setProjectBaseDirectory(projectBaseDirectory);
            client.setReporter("xml", "reports", "SingleTest.OpenClose");
            client.waitForDevice("@os='ios'",10000);
            client.openDevice();
            client.sleep(2000);
            client.closeDevice();
            client.releaseDevice("",false,true,true);
            client.generateReport(false);
            client.releaseClient();
            client = null;
        }
        for (int i = 0; i < strings.size(); i++) {
            System.out.println(strings.get(i));
        }

    }

    private String GetMemory() {
        String line;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("tasklist.exe");
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
        try {
            while ((line = input.readLine()) != null) {
                if (line.startsWith("studio.exe")){ // I only want the processes that have 'studio.exe' for a name.
                    System.out.println(line);
                    return line;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @After
    public void tearDown(){
        // Generates a report of the Memory case.
        // For more information - https://docs.experitest.com/display/public/SA/Report+Of+Executed+Test

        // Releases the client so that other clients can approach the agent in the near future.

    }
}
