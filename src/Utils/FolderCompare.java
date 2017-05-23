package Utils;

import java.io.File;
import java.util.ArrayList;

public class FolderCompare {

	private ArrayList<String> removedFiles;
	private ArrayList<String> addedFiles;

	public static void main(String[] args) {
		FolderCompare fc = new FolderCompare("C:\\Program Files (x86)\\Experitest\\SeeTest 10.6","C:\\Program Files (x86)\\Experitest\\SeeTest 10.81");
		System.out.println(fc.getAddedFileNUmber() +" New Files:");
		ArrayList<String> stringArrayAdded = fc.getAddedFiles();
		for (int i = 0; i < stringArrayAdded.size(); i++) {
			System.out.println(stringArrayAdded.get(i));
		}
        System.out.println("-------------------------------------------------------------------------------------------------------");
        System.out.println(fc.getRemovedFileNumber() +" Removed Files:");
        ArrayList<String> stringArrayRemoved = fc. getRemovedFiles();
        for (int i = 0; i < stringArrayRemoved.size(); i++) {
            System.out.println(stringArrayRemoved.get(i));
        }

	}
	
	public  FolderCompare(String OldInstallationPath, String newInstallationPath){
		removedFiles = travelInFolder(OldInstallationPath);
		addedFiles = travelInFolder(newInstallationPath);
		
		ArrayList<String> tempFiles = (ArrayList<String>) removedFiles.clone();
		this.removedFiles.removeAll(addedFiles);
		this.addedFiles.removeAll(tempFiles);
	}
	
	private ArrayList<String> travelInFolder(String path){
		File baseDir = new File(path);
		ArrayList<String> fileList = new  ArrayList<String>();
		doTravel(baseDir, fileList);
		ArrayList<String> reletiveList = new  ArrayList<String>();
		for(java.util.Iterator<String> iter = fileList.iterator(); iter.hasNext();){
			String filepath = iter.next();
			reletiveList.add("<base directory>" + filepath.replace(path, ""));
		}
		return reletiveList;
	}

	private void doTravel(File directory, ArrayList<String> fileList){
		for(File file: directory.listFiles()){
			if(file.isDirectory()){
				fileList.add(file.getPath());
				doTravel(file, fileList);
			} else{
				fileList.add(file.getPath());
			}
		}
	}

	public ArrayList<String> getRemovedFiles() {
		return removedFiles;
	}

	public int getRemovedFileNumber() {
		return removedFiles.size();
	}

	public ArrayList<String> getAddedFiles() {
		return addedFiles;
	}

	public int getAddedFileNUmber() {
		return addedFiles.size();
	}
}
