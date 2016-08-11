package FrameWork;

import FrameWork.Suite;

import java.io.File;
import java.io.FileNotFoundException;

public class Runner {
	static int iOSDevicesNum = 0;
	static int androidDevicesNum =2;
	static int repNum = 500;
	static String reportFolderString = "c:\\temp\\Reports";
	static String deviceQuery= "";

	public static void main(String[] args) throws InterruptedException, FileNotFoundException {
		//deviceQuery= " and contains(@version,'10.')";


		try {
			File Report = new File("Reports");
			for (File file : Report.listFiles()) file.delete();
			File ReportFolder = new File(reportFolderString);
			for (File file : ReportFolder.listFiles()) DeleteRecursive(file);
		}catch(Exception e){
			e.printStackTrace();
		}

		Thread[] iOSTheadPool = new Thread[iOSDevicesNum];
		pool(iOSTheadPool,"ios");
		Thread[] androidTheadPool = new Thread[androidDevicesNum];
		pool(androidTheadPool,"android");
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

}
