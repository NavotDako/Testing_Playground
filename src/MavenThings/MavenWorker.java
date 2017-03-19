package MavenThings;

import org.apache.commons.lang3.StringUtils;

import java.io.*;

/**
 * Created by user on 8/18/2016.
 */
public class MavenWorker {
    static String version = "10.4";

    public static void main(String[] args) {

        File dir = new File("C:\\Program Files (x86)\\Experitest\\AppiumStudio10.5\\clients\\appium\\java");
        //File dir = new File("C:\\Program Files (x86)\\Experitest\\SeeTest 10.4\\clients\\java");
        File[] fileList = dir.listFiles((dir1, name) -> {
            return name.toLowerCase().endsWith(".jar");
        });
        createPoms(fileList);
        //uploadFilesFromFolder(fileList);
        // buildPom(fileList);

    }

    private static void createPoms(File[] fileList) {
        for (int i = 0; i < fileList.length; i++) {

            String name = fileList[i].getName();
            String artId;
            int count = StringUtils.countMatches(name, "-");

            if (count > 0) artId = name.substring(0, name.lastIndexOf("-"));
            else artId = name.substring(0, name.lastIndexOf("."));


            if (count > 0) {
                version = name.substring(name.lastIndexOf("-") + 1, name.lastIndexOf("."));
            }

            String fileString =artId;
            File tempFolder = new File("C:\\Users\\DELL\\Desktop\\SeeTestPoms\\" + fileString);
            tempFolder.mkdir();
            File tempPomFile = new File(tempFolder.getAbsolutePath()+"\\POM.xml");
            writeToFile(tempPomFile, artId, version);
            String command = "mvn deploy:deploy-file -Dfile=\"" + fileList[i].getAbsolutePath() + "\" -DpomFile=\""+tempPomFile.getAbsolutePath()+"\" -Dpackaging=jar -DgroupId=com.experitest -DartifactId=" + artId + " -DrepositoryId=Experitest.repo  -Dversion=" + version + " -Durl=ftp://192.168.1.232/maven2";
            System.out.println(command);

          /*  if (!artId.contains("appium")) {
                try {
                    cmd(tempFolder,command);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("SKIPPED");
            }
*/
        }
    }

    private static void writeToFile(File tempPomFile, String artId, String version) {
        BufferedWriter writer = null;
        try {
            //create a temporary file

            writer = new BufferedWriter(new FileWriter(tempPomFile));
            writer.write("<project>\n" +
                    "\t<modelVersion>4.0.0</modelVersion>\n" +
                    "\t<groupId>com.experitest</groupId>\n" +
                    "\t<artifactId>" + artId + "</artifactId>\n" +
                    "\t<version>" + version + "</version>\n" +
                    "\t<build>\n" +
                    "\t\t<extensions>\n" +
                    "\t\t\t<extension>\n" +
                    "\t\t\t\t<groupId>org.apache.maven.wagon</groupId>\n" +
                    "\t\t\t\t<artifactId>wagon-webdav</artifactId>\n" +
                    "\t\t\t\t<version>1.0-beta-2</version>\n" +
                    "\t\t\t</extension>\n" +
                    "\n" +
                    "\t\t\t<extension>\n" +
                    "\t\t\t\t<groupId>org.apache.maven.wagon</groupId>\n" +
                    "\t\t\t\t<artifactId>wagon-ftp</artifactId>\n" +
                    "\t\t\t\t<version>1.0-alpha-6</version>\n" +
                    "\t\t\t</extension>\n" +
                    "\t\t</extensions>\n" +
                    "\n" +
                    "\n" +
                    "\t</build>\n" +
                    "\t<distributionManagement>\n" +
                    "\t\t<repository>\n" +
                    "\t\t\t<id>Experitest.repo</id>\n" +
                    "\t\t\t<name>Experitest Repository</name>\n" +
                    "\t\t\t<url>ftp://192.168.1.232/maven2</url>\n" +
                    "\t\t</repository>\n" +
                    "\n" +
                    "\n" +
                    "\t</distributionManagement>\n" +
                    " \n" +
                    "</project>");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (Exception e) {
            }
        }
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
            String jarVersion = "10.5";
            String artId = name.substring(0, name.lastIndexOf("."));
            int count = StringUtils.countMatches(name, "-");

            if (count > 0) {
                String tempVersion = name.substring(name.lastIndexOf("-") + 1, name.lastIndexOf("."));
                if (tempVersion.contains(".")) {
                    jarVersion = tempVersion;
                    artId = name.substring(0, name.lastIndexOf("-"));
                }
            }
            String command = "mvn deploy:deploy-file -Dfile=\"" + file.getAbsolutePath() + "\" -DpomFile=\"C:\\Users\\DELL\\Desktop\\uploadJars\\POM.xml\" -Dpackaging=jar -DgroupId=com.experitest -DartifactId=" + artId + " -DrepositoryId=Experitest.repo  -Dversion=" + jarVersion + " -Durl=ftp://192.168.1.232/maven2";
            System.out.println(command);

           /* if (!artId.contains("appium-client")) {
                try {
                    //cmd("", command);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("SKIPPED");
            }*/
        }
    }

    public static void cmd(File folder, String line) throws Exception {
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", "cd \""+folder.getAbsolutePath()+"\"" +" && "  + line); //
        builder.redirectErrorStream(true);
        Process p = builder.start();

        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        while (true) {
            line = r.readLine();
            if (line == null) {
                break;
            }
            System.out.println(line);
        }

    }
}
