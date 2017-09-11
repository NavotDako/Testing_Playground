package FrameWork;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CloudServer {
    private static String webPage;
    private static String authStringEnc;
    private static String DEVICES_URL = "/devices";
    public String HOST;
    public int PORT;
    public String USER;
    public String PROJECT;
    public String PASS;
    public boolean SECURED;
    CloudServerName cloudName;
    private String authString;
    String result;

    public CloudServer(CloudServerName cloudName) {
        System.out.println("Initiating The Cloud Object");
        this.cloudName = cloudName;
        updateCloudDetails();
        String prefix = (!this.SECURED) ? "http://" : "https://";
        authString = this.USER + ":" + this.PASS;
        webPage = prefix + this.HOST + ":" + this.PORT + "/api/v1";
        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
        authStringEnc = new String(authEncBytes);
        System.out.println("Done Initiating The Cloud Object");
        System.out.println("Cloud Details:\n" + this.toString());
        try {
            result = doGet(DEVICES_URL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        return (String.format("%-20s\n%-10s\n%-20s\n", "HOST - " + HOST, "PORT - " + PORT, "USER - " + USER));
    }

    public enum CloudServerName {
        MY, QA, MIRRON, PUBLIC,QA_Not_Secured,YEHUDA,MY_N_S, ATT
    }

    public void updateCloudDetails() {
        switch (cloudName) {
            case MY:
                HOST = "192.168.2.13";
                PORT = 80;
                USER = "admin";
                PASS = "Experitest2012";
                PROJECT = "";
                SECURED = false;
                break;
            case MY_N_S:
                HOST = "192.168.2.13";
                PORT = 8090;
                USER = "admin";
                PASS = "Experitest2012";
                break;
            case QA:
                HOST = "qacloud.experitest.com";
                PORT = 443;
                USER = "zekra";
                PASS = "Zekra1234";
                SECURED = true;
                break;
            case YEHUDA:
                HOST = "192.168.2.31";
                PORT = 1111;
                USER = "yehuda";
                PASS = "Experitest2012";
                break;
            case QA_Not_Secured:
                HOST = "192.168.2.135";
                PORT = 80;
                USER = "zekra";
                PASS = "Zekra1234";
                break;
            case MIRRON:
                HOST = "192.168.2.71";
                PORT = 8080;
                USER = "user1";
                PASS = "Welc0me!";
                break;
            case PUBLIC:
                HOST = "https://cloud.experitest.com";
                PORT = 443;
                USER = "zekra";
                PASS = "Zekra1234";
                break;
            default:
                HOST = "192.168.2.13";
                PORT = 80;
                USER = "admin";
                PASS = "Experitest2012";
                break;
        }
    }

    public List<String> getAllAvailableDevices(String os) throws IOException {
     //   result = doGet(DEVICES_URL);
        List<String> devicesList = getAvailableDevicesList(result, os);
        return devicesList;
    }

    private List<String> getAvailableDevicesList(String result, String os) {
        List<String> tempDevicesList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(result);
        Map obj = jsonObject.toMap();
        List<Object> data = (List<Object>) obj.get("data");
        Object[] devicesArray = GetFilteredDevices(data, os);

        for (int i = 0; i < devicesArray.length; i++) {
            String[] devicePropertiesArray = devicesArray[i].toString().replace("{", "").replace("]", "").split(",");
            int j = 0;

            boolean udidFlag = false;
            String udid = null;

            while (j < devicePropertiesArray.length && !udidFlag) {
                if (devicePropertiesArray[j].contains("udid")) {
                    udid = devicePropertiesArray[j].replace("udid=", "").trim();
                    udidFlag = true;
                }
                j++;
            }
            tempDevicesList.add(udid);
        }

        System.out.println("DevicesList size - " + tempDevicesList.size() + " - " + tempDevicesList.toString());
        return tempDevicesList;
    }

    private Object[] GetFilteredDevices(List<Object> data, String os) {
        Object[] devicesArray = new Object[0];
        switch (os) {
            case "android": {
                devicesArray = data
                        .stream()
                        .filter(device -> ((Map) device).get("displayStatus").equals("Available") && ((Map) device).get("deviceOs").equals("Android"))
                        .toArray();
                break;
            }
            case "ios": {
                devicesArray = data
                        .stream()
                        .filter(device -> ((Map) device).get("displayStatus").equals("Available") && ((Map) device).get("deviceOs").equals("iOS"))
                        .toArray();
                break;
            }
            case "all": {
                devicesArray = data
                        .stream()
                        .filter(device -> ((Map) device).get("displayStatus").equals("Available"))
                        .toArray();
                break;
            }
        }
        return devicesArray;
    }

    private String doGet(String entity) throws IOException {
        URL url = new URL(webPage + entity);
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
        InputStream is = urlConnection.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        int numCharsRead;
        char[] charArray = new char[1024];
        StringBuffer sb = new StringBuffer();
        while ((numCharsRead = isr.read(charArray)) > 0) {
            sb.append(charArray, 0, numCharsRead);
        }
        String result = sb.toString();
        if (((HttpURLConnection) urlConnection).getResponseCode() < 300) {
            return result;
        } else {
            throw new RuntimeException(result);
        }
    }

    public String getDeviceOSByUDID(String UDID) throws IOException {
        String deviceOS = getDeviceOS(result, UDID);
        return deviceOS;
    }

    private String getDeviceOS(String result, String udid) {
        JSONObject jsonObject = new JSONObject(result);
        Map obj = jsonObject.toMap();
        List<Object> data = (List<Object>) obj.get("data");
        Object[] devicesArray = data
                .stream()
                .filter(student -> ((Map) student).get("udid").equals(udid))
                .toArray();

        String[] devicePropertiesArray = devicesArray[0].toString().replace("{", "").replace("]", "").split(",");
        int j = 0;

        boolean udidFlag = false;
        boolean osFlag = false;
        String deviceOs = null;
        while (j < devicePropertiesArray.length && (!udidFlag || !osFlag)) {

            if (devicePropertiesArray[j].contains("deviceOs")) {
                deviceOs = devicePropertiesArray[j].replace("deviceOs=", "").trim().toLowerCase();
                osFlag = true;
            }
            j++;
        }
        return deviceOs;
    }

    private String getDeviceName(String result, String deviceID) {
        JSONObject jsonObject = new JSONObject(result);
        Map obj = jsonObject.toMap();
        List<Object> data = (List<Object>) obj.get("data");
        Object[] devicesArray = data
                .stream()
                .filter(student -> ((Map) student).get("udid").equals(deviceID))
                .toArray();

        String[] devicePropertiesArray = devicesArray[0].toString().replace("{", "").replace("]", "").split(",");
        int j = 0;

        boolean udidFlag = false;
        boolean osFlag = false;
        String deviceOs = null;
        while (j < devicePropertiesArray.length && !osFlag) {

            if (devicePropertiesArray[j].contains("deviceName")) {
                deviceOs = devicePropertiesArray[j].substring(devicePropertiesArray[j].indexOf("=") + 1).trim().toLowerCase();
                osFlag = true;
            }
            j++;
        }
        return deviceOs;
    }

    public String getDeviceNameByUDID(String deviceID) throws IOException {
        String deviceOS = getDeviceName(result, deviceID);
        return deviceOS;
    }

}
