package edu.unlv.cs673.socialwebspider.util.files;

import java.io.File;

import edu.unlv.cs673.socialwebspider.uuid.UUIDFactoryImpl;

/**
 * Used to do basic operations with folders.
 * 
 * @author James Oravec (http://www.jamesoravec.com)
 */
public class FolderManipImpl implements FolderManip {

	@Override
	public void createFolder(String relativePath, String folderName) {
		String fullPath = relativePath + "/" + folderName;
		try {
			File folder = new File(fullPath);
			folder.mkdir();
		} catch (Exception e) {
			System.err.println("Error creating folder: " + fullPath);
			e.printStackTrace();
		}
	}

	@Override
	public void deleteFolder(String relativePath) {
		try {
			File folder = new File(relativePath);
			folder.delete();
		} catch (Exception e) {
			System.err.println("Error deleting folder: " + relativePath);
			e.printStackTrace();
		}
	}

	@Override
	public String createUniqueFolder(String relativePath) {
		UUIDFactoryImpl uuid = new UUIDFactoryImpl();
		String folderName = uuid.generateUUID();
		createFolder(relativePath, folderName);		
		return folderName;
	}
	
}
