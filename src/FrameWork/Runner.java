package FrameWork;

import java.io.*;

public class Runner {

	static int iOSDevicesNum = 1;
	static int androidDevicesNum = 1;
	static int repNum = 1;

	static String reportFolderString = "c:\\temp\\Reports";
	static String deviceQuery= "";

	public static void main(String[] args) throws InterruptedException, IOException {
		//deviceQuery= " and contains(@version,'9.')";
		System.out.println("Getting the studio.exe resources");
		String resources = getResources();

        System.out.println("Preparing the reports folder");
        try {
			File Report = new File("Reports");
			for (File file : Report.listFiles()) file.delete();
			File ReportFolder = new File(reportFolderString);
			for (File file : ReportFolder.listFiles()) DeleteRecursive(file);
            System.out.println("Finished preparing the reports folder");
        }catch(Exception e){
			e.printStackTrace();
            throw e;
		}

		Thread[] iOSTheadPool = new Thread[iOSDevicesNum];
		pool(iOSTheadPool,"ios");
		Thread[] androidTheadPool = new Thread[androidDevicesNum];
		pool(androidTheadPool,"android");

		for (int i = 0; i < iOSTheadPool.length; i++) {
			while (iOSTheadPool[i].isAlive()){
				Thread.sleep(1000);
			}
		}
		for (int i = 0; i < androidTheadPool.length; i++) {
			while (androidTheadPool[i].isAlive()){
				Thread.sleep(1000);
			}
		}
        System.out.println("Start Resources:\n"+resources);
        System.out.println("End Resources:\n"+getResources());


	}

	public static void pool(Thread[] myTheadPool,String deviceToTest) throws InterruptedException {

		for (int i = 0; i < myTheadPool.length; i++) {
			myTheadPool[i]= new Thread(new Suite(repNum, reportFolderString,deviceToTest, deviceQuery));
			myTheadPool[i].start();
		}
	}

	public static void DeleteRecursive(File fileOrDirectory) {

		if (fileOrDirectory.isDirectory())
			for (File child : fileOrDirectory.listFiles())
				DeleteRecursive(child);

		fileOrDirectory.delete();

	}

	private static String getResources() throws IOException {
		String line;
		String savedLine = "";
		Process process = Runtime.getRuntime().exec("tasklist.exe");
		BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
		while ((line = input.readLine()) != null) {
			if (line.startsWith("studio.exe")){ // I only want the processes that have 'studio.exe' for a name.
				savedLine = line;}
		}
		return savedLine;
	}


}
