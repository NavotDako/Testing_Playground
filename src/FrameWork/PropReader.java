package FrameWork;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class PropReader {
    public Properties properties;

    public PropReader() {
        File file = new File("CloudCreds.properties");
        properties = new Properties();
        try {
            FileInputStream fileInput = new FileInputStream(file);
            properties.load(fileInput);
            fileInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getString(String key){
        return String.valueOf(properties.get(key));
    }

    public int getPort(String key){
        return Integer.parseInt((String) properties.get(key));
    }

    public boolean getBool(String key){
        Boolean secured = (((String)properties.get(key)).contains("true")) ? true : false ;
        return secured;
    }

}
