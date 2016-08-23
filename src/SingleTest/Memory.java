package SingleTest;

import FrameWork.AbsTest;
import FrameWork.MyClient;
import com.experitest.client.Client;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by user on 8/18/2016.
 */
public class Memory  {
    private String host = "localhost";
    private int port = 8889;
    protected Client client = null;
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    @Test
    public void AndroidRunTest() throws IOException {
        String savedLine = getLine();
        for (int i = 0; i < 25 ; i++) {
            client = new Client(host, port, true);
            client.setReporter("xml", "reports", "Memory");
            System.out.println(sdf.format(new Date(System.currentTimeMillis())) +" Iteration: "+i);
            client.waitForDevice("@os='android'",10000);
            client.openDevice();
            client.sleep(2000);
            client.closeDevice();
            client.releaseDevice("",false,true,true);
            client.generateReport(false);
            client.releaseClient();
        }
        EndLine(savedLine);


    }

    @Test
    public void IOSRunTest() throws IOException {
        String savedLine = getLine();
        for (int i = 0; i < 25 ; i++) {
            client = new Client(host, port, true);
            client.setReporter("xml", "reports", "Memory");
            System.out.println(sdf.format(new Date(System.currentTimeMillis())) +" Iteration: "+i);
            client.waitForDevice("@os='ios'",10000);
            client.openDevice();
            client.sleep(2000);
            client.closeDevice();
            client.releaseDevice("",false,true,true);
            client.generateReport(false);
            client.releaseClient();
            System.out.println("----------------------------------------------------------------");

        }
        EndLine(savedLine);
    }
    private void EndLine(String savedLine) throws IOException {
        Process process;
        BufferedReader input;
        String line;
        process = Runtime.getRuntime().exec("tasklist.exe");
        input = new BufferedReader(new InputStreamReader(process.getInputStream()));
        System.out.println("Old Memory - " + savedLine);
        while ((line = input.readLine()) != null) {
            if (line.startsWith("studio.exe")) // I only want the processes that have 'java.exe' for a name.
                System.out.println(line);
        }
    }

    private String getLine() throws IOException {
        String line;
        String savedLine = "";
        Process process = Runtime.getRuntime().exec("tasklist.exe");
        BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
        while ((line = input.readLine()) != null) {
            if (line.startsWith("studio.exe")){ // I only want the processes that have 'studio.exe' for a name.
                System.out.println(line);
                savedLine = line;}
        }
        return savedLine;
    }

}
