package MavenThings;

import org.xml.sax.SAXException;

import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by mahmoud.mahagna on 22/05/2017.
 */
public class MavenOrganizer {

    private Path RepositoryPath;
    private String ProductVersion;
    private String jarName;
    private String jarFolderName;
    private String jarVersion;
    private String GroupIdPath; //@Todo : integrate
    Path newJarPath;

    public MavenOrganizer(Path JarsRepository, String ProductVersion, Path newJarPath, String GroupId) throws IOException {

        GroupIdPath = "";
        this.RepositoryPath = JarsRepository;
        String arr[] = GroupId.split("\\.");
        for (int i = 0; i < arr.length; i++) {
            this.GroupIdPath += arr[i];
            this.GroupIdPath += "\\";
        }

        this.newJarPath = newJarPath;
        this.ProductVersion = ProductVersion;
        jarName = newJarPath.getFileName().toString();
        //If it doesn't contain a number then it will be renamed - adding the version of the cloud to it
        if (!jarName.matches(".*\\d+.*")) {
            jarName += "-" + this.ProductVersion;
            jarVersion = this.ProductVersion;
            jarFolderName = jarName.split("-\\d.*")[0];
        } else {
            jarVersion = jarName.split(jarName.split("-\\d.*")[0])[1].substring(1);
            jarFolderName = jarName.split("-\\d.*")[0];
        }
    }

    public HashMap.Entry<String, Path> addJar() throws IOException, NoSuchAlgorithmException {
        // System.out.println("jarName: " + jarName + " jarVersion: " +jarVersion+ " jarFolderName: "+ jarFolderName);
        int check = MavenOrganizerUtil.CheckToReplace(RepositoryPath, GroupIdPath, jarFolderName, jarVersion);
        if (check == 0) {
            return new HashMap.SimpleEntry<String, Path>("Failed : Already using the latest version", null);
        } else {
            try {
                //@todo : Deal with other possible values of 'check' variable
                MavenOrganizerUtil.addVersion_toMetadata(RepositoryPath, GroupIdPath, jarFolderName, jarVersion);
                //Creating SH1 & MD5 for the maven-metadata.xml
                MavenOrganizerUtil.replace_MD5_File(RepositoryPath + "\\" + GroupIdPath + jarFolderName + "\\maven-metadata.xml");
                MavenOrganizerUtil.replace_SHA1_File((RepositoryPath + "\\" + GroupIdPath + jarFolderName + "\\maven-metadata.xml"));


                MavenOrganizerUtil.Create_Folder_And_CopyJar(RepositoryPath + "\\" + GroupIdPath + jarFolderName, jarVersion, jarFolderName, this.newJarPath);
                //Creating SH1 & MD5 for the jar
                MavenOrganizerUtil.CreateSH1(Paths.get(RepositoryPath + "\\" + GroupIdPath + jarFolderName + "\\" + jarVersion + "\\" + jarFolderName + "-" + jarVersion + ".jar"));
                MavenOrganizerUtil.CreateMD5(Paths.get(RepositoryPath + "\\" + GroupIdPath + jarFolderName + "\\" + jarVersion + "\\" + jarFolderName + "-" + jarVersion + ".jar"));

                MavenOrganizerUtil.CopyAndUpdatePOM(RepositoryPath + "\\" + GroupIdPath + jarFolderName, jarVersion, jarFolderName);
                //Creating SH1 & MD5 for the POM
                MavenOrganizerUtil.replace_MD5_File(RepositoryPath + "\\" + GroupIdPath + jarFolderName + "\\" + jarVersion + "\\" + jarFolderName + "-" + jarVersion + ".pom");
                MavenOrganizerUtil.replace_SHA1_File(RepositoryPath + "\\" + GroupIdPath + jarFolderName + "\\" + jarVersion + "\\" + jarFolderName + "-" + jarVersion + ".pom");
            } catch (Exception e) {
                return new HashMap.SimpleEntry<String, Path>("Failed : Exception thrown in the proccess of creati" +
                        "ng folders and adding MD5 " + "& SHA1 files", null);
            }
        }
        return new HashMap.SimpleEntry<String, Path>("Successful", Paths.get(RepositoryPath + "\\" + GroupIdPath + "\\" + jarFolderName + "\\" + jarVersion));
    }

    public static void removeJar(Path JarsRepository, String jarVersion, String GroupId,String JarFolderName) throws IOException, TransformerException, SAXException, NoSuchAlgorithmException, XPathExpressionException {

        String PathToDirectory=MavenOrganizerUtil.getPathToJarsDirectory(JarsRepository,jarVersion,GroupId,JarFolderName);

        if(MavenOrganizerUtil.CheckIfRemovable(PathToDirectory, jarVersion)>0){
            MavenOrganizerUtil.deleteJarFolder(PathToDirectory,jarVersion);
            String replacingVersion = MavenOrganizerUtil.findReplacingVersion(PathToDirectory,jarVersion).toString();

            MavenOrganizerUtil.addVersion_toMetadata_Core(Paths.get(PathToDirectory), replacingVersion);
            MavenOrganizerUtil.removeVersion_fromMetadata(Paths.get(PathToDirectory),jarVersion);
            MavenOrganizerUtil.replace_MD5_File(PathToDirectory + "\\maven-metadata.xml");
            MavenOrganizerUtil.replace_SHA1_File(PathToDirectory + "\\maven-metadata.xml");

        }
        else{
            System.out.println("Couldn't remove jar , either it is the only jar version there or it is not there at all");
        }

    }

    public static void removeAllJarsButOne(Path JarsRepository, String jarVersion, String GroupId,String JarFolderName) throws IOException, NoSuchAlgorithmException, TransformerException, SAXException, XPathExpressionException {
        String PathToDirectory=MavenOrganizerUtil.getPathToJarsDirectory(JarsRepository,jarVersion,GroupId,JarFolderName);
        LinkedList<JarVersion> allVersions=MavenOrganizerUtil.getAllJarVersions(PathToDirectory);
        int sizeBefore=allVersions.size();
        for(int i=0;i<sizeBefore;i++){
            if(!allVersions.getFirst().toString().equals(jarVersion)){
                removeJar(JarsRepository,allVersions.getFirst().toString(),GroupId,JarFolderName);
                allVersions.removeFirst();
            }
            else{
                allVersions.removeFirst();
            }
        }

    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, TransformerException, SAXException, XPathExpressionException {
//        MavenOrganizer org = new MavenOrganizer(Paths.get("C:\\Users\\DELL\\Desktop\\repo"),
//                "11.2", Paths.get("C:\\Users\\DELL\\Desktop\\seetest-client"), "com.experitest");
//        org.addJar();

        MavenOrganizerUtil.replace_MD5_File("C:\\Users\\DELL\\Desktop\\repo\\com\\experitest\\seetest-client\\11.2\\seetest-client-11.2.pom");
        MavenOrganizerUtil.replace_SHA1_File("C:\\Users\\DELL\\Desktop\\repo\\com\\experitest\\seetest-client\\11.2\\seetest-client-11.2.pom");
    }

}