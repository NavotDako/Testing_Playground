package Cloud;

import com.experitest.client.Client;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import java.io.File;
import java.io.IOException;

/**
 * Created by navot.dako on 2/18/2016.
 */
public class CloudTestRunner {
    static String[] CloudArray = {"210","463","4134"};

    public static void main(String[] args) throws IOException {
        JUnitCore junit = new JUnitCore();

        for (int i = 0; i < CloudArray.length; i++) {
            ChangeConnection(CloudArray[i]);
            RestartSeeTest();
            Result result = junit.run(CloudTest.class);
        }
    }

    private static void RestartSeeTest() {
        String host = "localhost";
        int port = 8889;
        String projectBaseDirectory = "project15";
        Client client = null;
        client = new Client(host, port, true);
        client.exit();




    }

    private static void ChangeConnection(String s) throws IOException {
        File file = new File("C:\\Users\\DELL\\AppData\\Roaming\\seetest\\"+s);
        File file2 = new File("C:\\Users\\DELL\\AppData\\Roaming\\seetest\\app.properties");

        if (file2.exists())
            throw new java.io.IOException("file exists");
        boolean success = file.renameTo(file2);
        if (!success) {
            // File was not successfully renamed
        }
    }


}
