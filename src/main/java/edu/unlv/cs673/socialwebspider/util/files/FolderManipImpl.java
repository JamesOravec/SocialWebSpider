package edu.unlv.cs673.socialwebspider.util.files;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import edu.unlv.cs673.socialwebspider.uuid.UUIDFactoryImpl;

/**
 * Used to do basic operations with folders.
 * 
 * @author James Oravec (http://www.jamesoravec.com)
 */
public class FolderManipImpl implements FolderManip {

	@Override
	public final void createFolder(final String relativePath, final String folderName) {
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
	public final void deleteFolder(final String path) {
		System.out.println("Deleting folder: " + path);
		try {
			String absPath = new File("").getAbsolutePath();
			String fullPath = absPath + "/" + path;
			fullPath = fullPath.replace("\\", "/");		// Make all slashes forward slash
			fullPath = fullPath.replace("/", "\\"); 	// Put back into native windows directory slash form.
			System.out.println("fullPath: " + fullPath);
					
			File folder = new File(fullPath);
			FileUtils.deleteDirectory(folder);
			
			// The above apache common-io isn't working for the delete. 
			// After looking on google, it might be an issue with how crawler4j is coded.
			//		Reference: http://stackoverflow.com/questions/2143217/unable-to-delete-a-file-after-reading-it
			// If they don't close the file handle, then I won't be able to delete the file.
			// This is resuling in me trying a different method to delet the files/folder.
			
			// Reference: http://www.exampledepot.com/egs/java.lang/Exec.html
			//
			// Our hack to get around this issue is to use the native dos command for the delete.
//		    String command = "rmdir \"" + fullPath + "\" /s /q";
//		    System.out.println("command: " + command);
//		    try{
//			    Process child = Runtime.getRuntime().exec(command);
//			    int exit = child.exitValue();
//			    if(exit!=0){
//			    	System.out.println("Error in executing delete command.");
//			    }
//		    } catch (IOException e){
//		    	System.err.println("IOException occured when trying to delete folder.");
//		    	e.printStackTrace();
//		    }
			
		} catch (Exception e) {
			System.err.println("Error deleting folder: " + path);
			e.printStackTrace();
		}
	}

	@Override
	public final String createUniqueFolder(final String relativePath) {
		UUIDFactoryImpl uuid = new UUIDFactoryImpl();
		String folderName = uuid.generateUUID();
		createFolder(relativePath, folderName);
		return folderName;
	}

	/**
	 * Deletes all files and subdirectories under dir. Returns true if all
	 * deletions were successful. If a deletion fails, the method stops
	 * attempting to delete and returns false.
	 * 
	 * Recursive file delete from:
	 * http://www.theserverside.com/discussions/thread.tss?thread_id=32492
	 * 
	 * @param dir
	 * @return
	 */
	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		// The directory is now empty so delete it
		return dir.delete();
	}

}
