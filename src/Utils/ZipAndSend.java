package Utils;
import javax.mail.MessagingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipAndSend {

    private List <String> fileList;
    private static String subject = "f sd f".replace(" ","_");
    private static final String recipientName = "Navot";
    private static final String SOURCE_FOLDER = "c:\\temp\\Reports\\grid-test_2017-05-23 01-05-40";

    private static final String OUTPUT_ZIP_FILE = "\\\\LONG-RUN-PC\\Users\\user\\Desktop\\files\\logs\\"+subject+".zip";
    private static final String userName=  "navot@experitest.com";
    private static final String password = "Seetest2015";

    private static String recipientEmail = null;

    public ZipAndSend() {
        fileList = new ArrayList < String > ();
    }

    public static void main(String[] args) throws IOException, MessagingException {
        ZipAndSend appZip = new ZipAndSend();
        appZip.generateFileList(new File(SOURCE_FOLDER));
        appZip.zipIt(OUTPUT_ZIP_FILE);
        recipientEmail = getRecipient(recipientName);
        if(!recipientEmail.equals("null")) SendEmail.Send(userName,password ,recipientEmail, "", subject,"http://192.168.2.72:8181/logs/"+subject+".zip");

    }

    private static String getRecipient(String recipientName) {
        File file = new File("src\\Utils\\emails.properties");
        Properties properties = new Properties();
        try {
            FileInputStream fileInput = new FileInputStream(file);
            properties.load(fileInput);
            fileInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return String.valueOf(properties.get(recipientName.toLowerCase()));
    }

    public void zipIt(String zipFile) {
        byte[] buffer = new byte[1024];
        String source = new File(SOURCE_FOLDER).getName();
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            fos = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(fos);

            System.out.println("Output to Zip : " + zipFile);
            FileInputStream in = null;

            for (String file: this.fileList) {
                System.out.println("File Added : " + file);
                ZipEntry ze = new ZipEntry(source + File.separator + file);
                zos.putNextEntry(ze);
                try {
                    in = new FileInputStream(SOURCE_FOLDER + File.separator + file);
                    int len;
                    while ((len = in .read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                } finally {
                    in.close();
                }
            }

            zos.closeEntry();
            System.out.println("Folder successfully compressed");

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                zos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void generateFileList(File node) {
        if (node.isFile()) {
            fileList.add(generateZipEntry(node.toString()));
        }

        if (node.isDirectory()) {
            String[] subNote = node.list();
            for (String filename: subNote) {
                generateFileList(new File(node, filename));
            }
        }
    }

    private String generateZipEntry(String file) {
        return file.substring(SOURCE_FOLDER.length() + 1, file.length());
    }
}