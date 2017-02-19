package MavenThings;

import FrameWork.AbsTest;
import FrameWork.MyClient;
import com.experitest.client.Client;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by user on 8/18/2016.
 */
public class MavenWorker {
    static String version = "10.4";

    public static void main(String[] args) {

        File dir = new File("C:\\Program Files (x86)\\Experitest\\AppiumStudio10.4\\clients\\appium\\java");

        File[] fileList = dir.listFiles((dir1, name) -> {
            return name.toLowerCase().endsWith(".jar");
        });
        uploadFilesFromFolder(fileList);
      //  buildPom(fileList);

    }

    private static void buildPom(File[] fileList) {
        System.out.println("<project>\n" +
                "    <modelVersion>4.0.0</modelVersion>\n" +
                "    <groupId>com.experitest</groupId>\n" +
                "    <artifactId>client</artifactId>\n" +
                "    <version>" + version + "</version>\n" +
                "    <repositories>\n" +
                "            <repository>\n" +
                "            <releases>\n" +
                "                <enabled>true</enabled>\n" +
                "                <updatePolicy>always</updatePolicy>\n" +
                "                <checksumPolicy>fail</checksumPolicy>\n" +
                "            </releases>\n" +
                "            <id>Experitest.repo1</id>\n" +
                "            <name>YourName</name>\n" +
                "            <url>http://repo.experitest.com:8010/Maven2/</url>\n" +
                "            <layout>default</layout>\n" +
                "        </repository>\n" +
                "    </repositories>\n" +
                "     \n" +
                "<dependencies>");

        for (File file : fileList) {

            String name = file.getName();
            String jarVersion = version;
            String artId = name.substring(0, name.lastIndexOf("."));
            int count = StringUtils.countMatches(name, "-");

            if (count > 0) {
                String tempVersion = name.substring(name.lastIndexOf("-") + 1, name.lastIndexOf("."));
                if (tempVersion.contains(".")) {
                    jarVersion = tempVersion;
                    artId = name.substring(0, name.lastIndexOf("-"));
                }

            }
            System.out.println(" <dependency>\n" +
                    "    <groupId>com.experitest</groupId>\n" +
                    "    <artifactId>" + artId + "</artifactId>\n" +
                    "    <version>" + jarVersion + "</version>\n" +
                    "    </dependency>");

        }
        System.out.println("</dependencies>\n" +
                "</project>");

    }


    private static void uploadFilesFromFolder(File[] fileList) {

        for (File file : fileList) {

            String name = file.getName();
            String jarVersion = "10.4";
            String artId = name.substring(0, name.lastIndexOf("."));
            int count = StringUtils.countMatches(name, "-");

            if (count > 0) {
                String tempVersion = name.substring(name.lastIndexOf("-") + 1, name.lastIndexOf("."));
                if (tempVersion.contains(".")) {
                    jarVersion = tempVersion;
                    artId = name.substring(0, name.lastIndexOf("-"));
                }

            }
            String command = "mvn deploy:deploy-file -Dfile=\"" + file.getAbsolutePath() + "\" -DpomFile=./POM.xml -Dpackaging=jar -DgroupId=com.experitest -DartifactId=" + artId + " -DrepositoryId=Experitest.repo  -Dversion=" + jarVersion + " -Durl=ftp://192.168.1.232/maven2";
            System.out.println("mvn deploy:deploy-file -Dfile=\"" + file.getAbsolutePath() + "\" -DpomFile=./POM.xml -Dpackaging=jar -DgroupId=com.experitest -DartifactId=" + artId + " -DrepositoryId=Experitest.repo  -Dversion=" + jarVersion + " -Durl=ftp://192.168.1.232/maven2");

            if (!artId.contains("appium-client")) {
                try {
                    cmd(command);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                System.out.println("SKIPPED");
            }
        }
    }

    public static void cmd(String line) throws Exception {
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", "cd \"C:\\Users\\DELL\\Desktop\\uploadJars\" && " + line); //
        builder.redirectErrorStream(true);
        Process p = builder.start();

        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        while (true) {
            line = r.readLine();
            if (line == null) { break; }
            System.out.println(line);
        }

    }
}
