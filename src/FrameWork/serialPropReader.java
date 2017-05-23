package FrameWork;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class serialPropReader {
    List<String> deviceList = new ArrayList<>();
    int index = 0;

    public serialPropReader() throws IOException {

        File file = new File("serials.properties");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                deviceList.add(line);
            }
        }

    }

    public String getNext() throws Exception {
        String temp = null;
        try {
            temp = deviceList.get(index);
            index++;

        } catch (Exception e) {
            throw new Exception("NoMoreDevices");
        }
        return temp;

    }


    public boolean hasNext() {
        return (index < deviceList.size());
    }
}
