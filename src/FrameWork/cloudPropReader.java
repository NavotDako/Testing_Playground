package FrameWork;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class cloudPropReader {

    private final String cloud;

    public Properties properties;

    public cloudPropReader(String cloud) {
        File file = new File("CloudCreds.properties");
        properties = new Properties();
        try {
            FileInputStream fileInput = new FileInputStream(file);
            properties.load(fileInput);
            fileInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.cloud = cloud;
    }

    public String getString(String key){
        return String.valueOf(properties.get(cloud+"_"+key));
    }

    public int getInt(String key){
        return Integer.parseInt((String) properties.get(cloud+"_"+key));
    }

    public boolean getBool(String key){
        Boolean secured = (((String)properties.get(cloud+"_"+key)).contains("true")) ? true : false ;
        return secured;
    }

}
