package Utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.zip.*;

public class ExceptionExtractor {
	
	private static final String EXTRACT_TO = "\\log";
	private static final int BUFFER_SIZE = 4096;

	public static void main(String[] args) throws IOException {
		System.out.println(ExtractExceptions("c:\\temp\\Reports\\grid-test_2017-05-14 06-21-35"));
	}

	private static void unzip(String zipPath, String dest) throws IOException{
		File destDir = new File(dest);
		if (!destDir.exists()) {
            destDir.mkdir();
        }
		ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipPath));
		ZipEntry entry = zipIn.getNextEntry();
		while(entry != null){
			String entryName = entry.getName();
			if(entryName.startsWith("SeeTest-")){
				extractFile(zipIn, dest + File.separator + entryName);
			}
			zipIn.closeEntry();
			entry = zipIn.getNextEntry();
		}
	}

    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

    public static ArrayList<String> ExtractExceptions(String path) throws IOException{
		path += "\\headless.logs.zip";
		String logpath = (System.getProperty("user.dir") + EXTRACT_TO + "-" + new Date().getTime());
		unzip(path, logpath);
		final File LogDir = new File(logpath);
		BufferedReader br = new BufferedReader(new FileReader(LogDir.listFiles()[0].getPath()));
		ArrayList<String> resoult = new ArrayList<String>();
		try{
			StringBuilder sb = new StringBuilder();
		    String line = br.readLine();
		    String prevLine ="";
		    
		    boolean readingExeption = false;
		    
		    while (line != null){
		    	if (!readingExeption){
		    		if(line.matches(".*Exception.*")){
		    			readingExeption = true;
		    			if(!line.matches("^\\d\\d\\d\\d-\\d\\d-\\d\\d .*")){
		    				resoult.add(prevLine + "\n");
		    			} else{
		    				resoult.add(line + "\n");
		    			}
		    		}
		    	}
		    	else if(readingExeption){
		    		if(line.matches("^\\d\\d\\d\\d-\\d\\d-\\d\\d .*") || line.matches("")){
		    			readingExeption = false;
		    		} else{
		    			resoult.set((resoult.size() - 1), resoult.get(resoult.size() - 1) + line + "\n");
		    		}
		    	}
		    	prevLine = line;
		    	line = br.readLine();
		    }
		    
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			br.close();
			File logdir = new File(logpath);
			String[]entries = logdir.list();
			for(String entry: entries){
				File currentFile = new File(logdir.getPath(),entry);
			    currentFile.delete();
			}
			logdir.delete();
		} 
		return resoult;
	}
}
