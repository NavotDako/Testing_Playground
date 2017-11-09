package MavenThings;

import org.apache.commons.io.FileUtils;
import org.bouncycastle.util.encoders.Hex;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;

public class MavenOrganizerUtil {

    public static void addVersion_toMetadata(Path repositoryPath, String groupIdPath, String jarFolderName, String jarVersion) throws TransformerException, SAXException, XPathExpressionException {
        addVersion_toMetadata_Core(Paths.get(repositoryPath+"\\"+groupIdPath+jarFolderName),jarVersion);
    }


    public static void addVersion_toMetadata_Core(Path Path, String jarVersion) throws SAXException, TransformerException, XPathExpressionException {
        Boolean flag = true;

        try {
            String filepath = Path.toString()  + "\\maven-metadata.xml";
            DocumentBuilderFactory docFactory = DocumentBuilderFactory
                    .newInstance();

            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(filepath);

            // Get the root element
            Node data = doc.getFirstChild();
            NodeList nodeList = data.getChildNodes();

            for (int i = 0; i < nodeList.getLength(); i++) {
                if (nodeList.item(i).getNodeName().compareTo("versioning") == 0) {
                    nodeList.item(i).getChildNodes().item(1).setTextContent(jarVersion);
                    Node n1 = nodeList.item(i).getChildNodes().item(3);
                    for (int j = 0; j < n1.getChildNodes().getLength(); j++) {
                        if (n1.getChildNodes().item(j).getTextContent().compareTo(jarVersion) == 0) {
                            flag = false;
                        }
                    }
                    if (flag == true) {
                        Element name = doc.createElement("version");
                        name.appendChild(doc.createTextNode(jarVersion));
                        n1.appendChild(name);
                    }
                    DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                    Date date = new Date();
                    nodeList.item(i).getChildNodes().item(5).setTextContent((dateFormat.format(date)));
                    break;
                }
            }
            // write the content into xml file

            XPathFactory xpathFactory = XPathFactory.newInstance();
            // XPath to find empty text nodes.
            XPathExpression xpathExp = xpathFactory.newXPath().compile(
                    "//text()[normalize-space(.) = '']");
            NodeList emptyTextNodes = (NodeList)
                    xpathExp.evaluate(doc, XPathConstants.NODESET);

            // Remove each empty text node from document.
            for (int i = 0; i < emptyTextNodes.getLength(); i++) {
                Node emptyTextNode = emptyTextNodes.item(i);
                emptyTextNode.getParentNode().removeChild(emptyTextNode);
            }

            TransformerFactory transformerFactory = TransformerFactory
                    .newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filepath));
            transformer.transform(source, result);


        } catch (ParserConfigurationException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void UpdatePomXML(String path, String jarVersion) throws XPathExpressionException {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory
                    .newInstance();

            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(path);

            // Get the root element
            Node data = doc.getFirstChild();
            NodeList nodeList = data.getChildNodes();
            nodeList.item(7).setTextContent(jarVersion);
            // write the content into xml file

            XPathFactory xpathFactory = XPathFactory.newInstance();
            // XPath to find empty text nodes.
            XPathExpression xpathExp = xpathFactory.newXPath().compile(
                    "//text()[normalize-space(.) = '']");
            NodeList emptyTextNodes = (NodeList)
                    xpathExp.evaluate(doc, XPathConstants.NODESET);

            // Remove each empty text node from document.
            for (int i = 0; i < emptyTextNodes.getLength(); i++) {
                Node emptyTextNode = emptyTextNodes.item(i);
                emptyTextNode.getParentNode().removeChild(emptyTextNode);
            }
            TransformerFactory transformerFactory = TransformerFactory
                    .newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(path));
            transformer.transform(source, result);


        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void CopyAndUpdatePOM(String JarDestPath, String jarVersion, String jarFolderName) throws IOException, XPathExpressionException {
        String differentVersion = "";
        Double max =0.0;
        File[] directories = new File(JarDestPath).listFiles(File::isDirectory);
        for (File f : directories) {
            if(f.getName().length()>=6)
                continue;
            Double versionInDouble=Double.parseDouble(f.getName());
            if (!f.getName().equals(jarVersion) && versionInDouble >max) {
                differentVersion = f.getName();
                max =versionInDouble;
            }
        }
        try {
            Files.copy(Paths.get(JarDestPath.toString() + "\\" + differentVersion + "\\" + jarFolderName + "-"
                    + differentVersion + ".pom"), Paths.get(JarDestPath + "\\" + jarVersion + "\\" + jarFolderName + "-" + jarVersion + ".pom"));

        } catch (java.nio.file.FileAlreadyExistsException e) {

        }
        UpdatePomXML(JarDestPath + "\\" + jarVersion + "\\" + jarFolderName + "-" + jarVersion + ".pom", jarVersion);
    }


    /*
    Results :
    0 if jar exists.
    1 if jar is not added and group ID folders already exist and JAR folder is  created
    2 if jar is not added and group ID folders already exist but JAR folder is not created
    3  if jar is not added and group ID folders doesn't exist
    * */
    public static int CheckToReplace(Path repositoryPath,
                                     String groupIdPath, String jarFolderName, String jarVersion) {
        File file = new File(repositoryPath.toString() + "\\" + groupIdPath + jarFolderName + "\\" + jarVersion + "\\" + jarFolderName + "-" + jarVersion + ".jar");
        if (file.exists()) {
            return 0;
        } else {
            File file2 = new File(repositoryPath.toString() + "\\" + groupIdPath.substring(0, groupIdPath.length() - 1));
            if (file2.exists()) {
                File file3 = new File(repositoryPath.toString() + "\\" + groupIdPath + jarFolderName);
                if (file3.exists()) {
                    return 1;
                }
                return 2;
            } else {
                return 3;
            }
        }

    }


    public static String readFile(String pathToFile) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(pathToFile));
        String everything;
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            everything = sb.toString();
        } finally {
            br.close();
        }

        return everything;
    }

    public static void replace_MD5_File(String path) throws IOException, NoSuchAlgorithmException {
        try {
            DeleteFile(path + ".md5");
        }
        catch(Exception e){
        }

        CreateMD5(Paths.get(path));
    }

    public static void replace_SHA1_File(String path) throws IOException, NoSuchAlgorithmException {
        try{
            DeleteFile(path + ".sha1");
    }
    catch(Exception e){
    }
        CreateSH1(Paths.get(path));
    }

    private static void DeleteFile(String path) throws IOException {
        Files.delete(Paths.get(path));
    }


    public static void CreateSH1(final Path baseFile) throws NoSuchAlgorithmException, IOException {
        final FileInputStream fis = new FileInputStream(baseFile.toFile());
        final MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");//.digest(Files.readAllBytes(baseFile));
        messageDigest.reset();
        final byte[] buffer = new byte[1024];
        int size = fis.read(buffer, 0, 1024);
        while (size >= 0) {
            messageDigest.update(buffer, 0, size);
            size = fis.read(buffer, 0, 1024);
        }
        final String result = new String(Hex.encode(messageDigest.digest()));
        final Path sha1File = baseFile.getFileSystem().getPath(baseFile.toString() + ".sha1");
        FileUtils.writeStringToFile(new File(sha1File.toString()), result);

    }

    public static void CreateMD5(final Path baseFile) throws NoSuchAlgorithmException, IOException {
        final FileInputStream fis = new FileInputStream(baseFile.toFile());
        final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.reset();
        final byte[] buffer = new byte[1024];
        int size = fis.read(buffer, 0, 1024);
        while (size >= 0) {
            messageDigest.update(buffer, 0, size);
            size = fis.read(buffer, 0, 1024);
        }

        final String result = new String(Hex.encode(messageDigest.digest()));
        fis.close();

        final Path md5File = baseFile.getFileSystem().getPath(baseFile.toString() + ".md5");
        FileUtils.writeStringToFile(new File(md5File.toString()), result);
    }


    public static void Create_Folder_And_CopyJar(String JarDestPath, String jarVersion, String jarFolderName, Path newJarPath) throws IOException {
        File file = new File(JarDestPath + "\\" + jarVersion);
        if (!file.exists()) {
            if (file.mkdir()) {
                System.out.println("Jar Directory is created!");
            } else {
                System.out.println("Failed to create Jar directory!");
            }
        }
        Files.copy(Paths.get(newJarPath.toString() + ".jar"), Paths.get(file.getPath() + "\\" + jarFolderName + "-" + jarVersion + ".jar"));
    }

    //@Todo: with more difficult scenarios when there's already existing jar directory or groupID directory
    public static void CreateGroupID_And_Metadata(Path repositoryPath, String groupIdPath, String jarFolderName, String jarVersion) throws IOException {
        File files = new File(repositoryPath.toString() + "\\" + groupIdPath);
        if (!files.exists()) {
            if (files.mkdirs()) {
                System.out.println("Multiple directories are created!");
            } else {
                System.out.println("Failed to create multiple directories!");
            }
        }

        String metadataXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<metadata>\n" +
                "  <groupId>GROUPXXID</groupId>\n" +
                "  <artifactId>jarFolderName</artifactId>\n" +
                "  <versioning>\n" +
                "    <release>jarVersion</release>\n" +
                "    <versions>\n" +
                "      <version>jarVersion2</version>\n" +
                "    </versions>\n" +
                "    <lastUpdated>DATE</lastUpdated>\n" +
                "  </versioning>\n" +
                "</metadata>\n";

        metadataXML.replace("GROUPXXID", groupIdPath.split("\\\"")[0] + groupIdPath.split("\\\"")[1]);
        metadataXML.replace("jarFolderName", jarFolderName);
        metadataXML.replace("jarVersion", jarVersion);
        metadataXML.replace("jarVersion2", jarVersion);
        Calendar calendar = Calendar.getInstance();
        metadataXML.replace("DATE", Long.toString(calendar.getTimeInMillis()));
        FileUtils.writeStringToFile(new File(repositoryPath.toString() + "\\" + groupIdPath + jarFolderName + ".xml"), metadataXML);

    }

    //0 can't remove
    //1 can remove
    public static int CheckIfRemovable(String pathToDirectory, String jarVersion) {
        int flag1 = 0,flag2=0;
        File file = new File(pathToDirectory);
        String[] names = file.list();

        for (String name : names) {
            if (new File(pathToDirectory + name).isDirectory() && !name.equals(jarVersion)) {
                flag1 = 1;
            }
        }
        for (String name : names) {
            if (name.equals(jarVersion)) {
                flag2 = 1;
            }
        }

        return flag1*flag2;
    }


    public static void deleteJarFolder(String pathToDirectory, String jarVersion) throws IOException {
        FileUtils.deleteDirectory(new File(pathToDirectory+jarVersion));

    }


    public static void removeVersion_fromMetadata(Path path, String jarVersion) throws XPathExpressionException {
        try {
            String filepath = path.toString()  + "\\maven-metadata.xml";
            DocumentBuilderFactory docFactory = DocumentBuilderFactory
                    .newInstance();

            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(filepath);

            // Get the root element
            Node data = doc.getFirstChild();
            NodeList nodeList = data.getChildNodes();

            for (int i = 0; i < nodeList.getLength(); i++) {
                if (nodeList.item(i).getNodeName().compareTo("versioning") == 0) {
                    Node n1 = nodeList.item(i).getChildNodes().item(3);
                    for (int j = 0; j < n1.getChildNodes().getLength(); j++) {
                        if (n1.getChildNodes().item(j).getTextContent().compareTo(jarVersion) == 0) {
                            n1.removeChild(n1.getChildNodes().item(j));
                        }
                    }

                }
            }
            // write the content into xml file

            XPathFactory xpathFactory = XPathFactory.newInstance();
            // XPath to find empty text nodes.
            XPathExpression xpathExp = xpathFactory.newXPath().compile(
                    "//text()[normalize-space(.) = '']");
            NodeList emptyTextNodes = (NodeList)
                    xpathExp.evaluate(doc, XPathConstants.NODESET);

            // Remove each empty text node from document.
            for (int i = 0; i < emptyTextNodes.getLength(); i++) {
                Node emptyTextNode = emptyTextNodes.item(i);
                emptyTextNode.getParentNode().removeChild(emptyTextNode);
            }
            TransformerFactory transformerFactory = TransformerFactory
                    .newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filepath));
            transformer.transform(source, result);


        } catch (ParserConfigurationException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String getPathToJarsDirectory(Path jarsRepository, String jarVersion, String groupId, String jarFolderName) {
        String GroupIdPath="";
        String arr[] = groupId.split("\\.");
        for (int i = 0; i < arr.length; i++) {
            GroupIdPath += arr[i];
            GroupIdPath += "\\";
        }
        String PathToJar=jarsRepository+"\\"+GroupIdPath+"\\"+jarFolderName+"\\"+jarVersion+"\\"+jarFolderName+"-"+jarVersion+".jar";
        String PathToDirectory = "";


        arr = PathToJar.split("\\\\");
        for (int i = 0; i < arr.length - 2; i++) {
            PathToDirectory += arr[i];
            PathToDirectory += "\\";
        }

        return PathToDirectory;
    }

    public static LinkedList<JarVersion> getAllJarVersions(String pathToDirectory) throws IOException {

        File file = new File(pathToDirectory);
        String[] names = file.list();
        LinkedList<JarVersion> jarVersions=new LinkedList<>();

        for (String name : names) {
            if (new File(pathToDirectory + name).isDirectory()) {
                jarVersions.addLast(new JarVersion(name));
            }
        }
        return jarVersions;
    }
    public static JarVersion findReplacingVersion(String pathToDirectory, String jarVersion) throws IOException {

        File file = new File(pathToDirectory);
        String[] names = file.list();
        LinkedList<JarVersion> jarVersions=new LinkedList<>();

        for (String name : names) {
            if (new File(pathToDirectory + name).isDirectory() && !name.equals(jarVersion)) {
                jarVersions.addLast(new JarVersion(name));
            }
        }
        Collections.sort(jarVersions);
        return jarVersions.get(jarVersions.size()-1);
    }

}
