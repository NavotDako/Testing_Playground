package SingleTest;

import com.experitest.client.Client;

/**
 * Created by navot.dako on 9/15/2016.
 */
public class CloudClient extends Client {
    public CloudClient(String host, int port, Creds p2, boolean b) {

    }
    @Override
    public String waitForDevice(String query,int timeout)
    {

        return query;
    }
}
