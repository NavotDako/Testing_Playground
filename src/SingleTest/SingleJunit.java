package SingleTest;//package <set your test package>;
import com.experitest.client.*;
import com.experitest.manager.client.PManager;
import com.experitest.manager.client.ResultPublisher;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.junit.*;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 *
 */
public class SingleJunit {
    private String host = "localhost";
    private int port = 8889;
    private String projectBaseDirectory = "C:\\Users\\DELL\\workspace\\project1";
    protected Client client = null;
    private String query = "@os='ios'";

    @Before
    public void setUp(){
        client = new Client(host, port, true);
        System.setProperty("manager.url","192.168.2.72:8787");
      // PManager.getInstance().addProperty("status", "fail");
        PManager.getInstance().addProperty("device", query);
        client.setProjectBaseDirectory(projectBaseDirectory);
        client.setReporter("xml", "reports", "Untitled");
    }

    @Test
    public void testUntitled() throws URISyntaxException, IOException {
        String stubsApiBaseUri = "http://192.168.2.72:8787/api/keys";
        String domain = "default";
        String environment = "addNumbers";
        String stubName = "1+1=2";
        HttpClient httpClient = HttpClients.createDefault();

        URIBuilder builder = new URIBuilder(stubsApiBaseUri);
       /* builder.addParameter("domain", domain);
        builder.addParameter("env", environment);
        builder.addParameter("stub", stubName);*/
        String listStubsUri = builder.build().toString();
        HttpGet getStubMethod = new HttpGet(listStubsUri);


        HttpResponse getStubResponse = httpClient.execute(getStubMethod);
        int getStubStatusCode = getStubResponse.getStatusLine()
                .getStatusCode();
        if (getStubStatusCode < 200 || getStubStatusCode >= 300) {
            // Handle non-2xx status code
            return;
        }

        client.waitForDevice(query,10000);
       /* for (int i=0;i<1;i++) {
            client.deviceAction("Home");
            client.syncElements(2000,10000);
            client.swipe("up",10,500);
        }*/
        PManager.getInstance().addProperty("app.name", "EriBankdasfadsf");
        //PManager.getInstance().addProperty("status", "pass");
    }

    @After
    public void tearDown(){

        PManager.getInstance().addReportFolder(client.generateReport(false));

        client.releaseDevice("",true,true,true);
        client.releaseClient();

        ResultPublisher.publishResult(null,"aarrrrrsdfgsrsdfggsdfga", "bbbb", null,true,null,null,false, 10000);
    }
}
