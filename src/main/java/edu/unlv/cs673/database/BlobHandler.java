package edu.unlv.cs673.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * This is a class to handle blob upload and downloads from the database.
 * 
 * Base example from: http://lists.mysql.com/java/6017 Code updated and
 * simplified for reuse by: James Oravec (http://www.jamesoravec.com)
 */
public class BlobHandler {

	private static final boolean DEBUG = true;
	private static final String CLASS_NAME = "BlobHandler";

	/**
	 * DB connection stuff.
	 */
	static private String dbUrl = "";
	public static String query = "";
	static private String user = "";
	static private String password = "";
	public static Connection myConn = null;
	public static Connection conn01 = null;

	/**
	 * Default constructor. Creates connection to database, so can insert blobs
	 * into the db, etc.
	 */
	public BlobHandler() {
		makeConnectionToMySQLDatabase();
	}

	/**
	 * Creates the connection to the MySQL Database. Database username and
	 * password are provided in the config.properties file.
	 */
	private void makeConnectionToMySQLDatabase() {
		if (DEBUG) {
			System.out.println("start makeConnectionToMySQLDatabase()");
		}

		Properties prop = new Properties();
		try {
			// load a properties file
			InputStream in = getClass().getResourceAsStream("/config.properties");
			prop.load(in);

			// get the property value and print it out
			dbUrl = prop.getProperty("dbUrl");
			user = prop.getProperty("user");
			password = prop.getProperty("password");

			if (DEBUG) {
				System.out.println("dbUrl: " + dbUrl);
				System.out.println("user: " + user);
				System.out.println("password: " + password);
			}

			// Connect to DB
			Class.forName("com.mysql.jdbc.Driver");
			myConn = DriverManager.getConnection(dbUrl, user, password);
			conn01 = DriverManager.getConnection(dbUrl, user, password);
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (DEBUG) {
			System.out.println("end makeConnectionToMySQLDatabase()");
		}
	}

	/**
	 * Inserts all files from a folder into the databse.
	 * 
	 * @param userId
	 * @param blobBinaryFolderPath
	 */
	public void insertBlobsFromFolder(int userId, String blobBinaryFolderPath, int userSpecificCategoryId) {
		
		System.out.println("userId: " + userId);
		System.out.println("blobBinaryFolderPath: " + blobBinaryFolderPath);
		System.out.println("userSpecificCategoryId: " + userSpecificCategoryId);
		
		// Insert each binary into the db.
		String fileName;
		File folder = new File(blobBinaryFolderPath);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				fileName = listOfFiles[i].getName();
				System.out.println(fileName);
				insertBinaryMin(userId, fileName, blobBinaryFolderPath + "/" + fileName, userSpecificCategoryId);				
			}
		}
	}
	
	/**
	 * Inserts a blob into the database table.
	 * 
	 * @param userId
	 *            Foreign key for user. e.g. 1
	 * @param blobFileName
	 *            e.g. "jamesbtest1.jpg"
	 * @param blobBinaryFilePath
	 *            e.g. "spider/images/jamesbtest1.jpg"
	 * @param blobCaption
	 *            e.g. "Picture of James, when he was skinny."
	 */
	public void insertBinaryMin(int userId, String blobFileName, String blobBinaryFilePath, int userSpecificCategoryId) {
		System.out.println("insertBinaryMin()");
		System.out.println("userId: " + userId);
		System.out.println("blobFileName: " + blobFileName);
		System.out.println("blobBinaryFilePath: " + blobBinaryFilePath);
		System.out.println("userSpecificCategoryId: " + userSpecificCategoryId);
		
		String insertStmt = "INSERT INTO Blobs (userId, blobFileName, blobBinary, userSpecificCategoryId) VALUES(?, ?, ?, ?)";
		String tableName = getTableName(insertStmt);
		File blobFile = null;
		blobFile = new File(blobBinaryFilePath);
		FileInputStream fileInputStream = null;

		/* Insert a single row. */
		PreparedStatement pstmt01 = null;
		try {
			fileInputStream = new FileInputStream(blobFile);
			pstmt01 = myConn.prepareStatement(insertStmt);
			int ix = 1;
			pstmt01.setInt(ix++, userId);
			pstmt01.setString(ix++, blobFileName);
			pstmt01.setBinaryStream(ix++, fileInputStream, blobFile.length());
			pstmt01.setInt(ix++, userSpecificCategoryId);
			pstmt01.executeUpdate();
			pstmt01.close();
		} catch (SQLException sql_excp) {
			if (sql_excp.getSQLState().equals("23505")) {
				System.err.println("Row cannot be added to table " + tableName + "because another row with this key already exists.");
			} 
			sql_excp.printStackTrace();
		} catch (FileNotFoundException fnf) {
			fnf.printStackTrace();
		}
	}
	

	/**
	 * Inserts a blob into the database table.
	 * 
	 * @param userId
	 *            Foreign key for user. e.g. 1
	 * @param blobFileName
	 *            e.g. "jamesbtest1.jpg"
	 * @param blobBinaryFilePath
	 *            e.g. "spider/images/jamesbtest1.jpg"
	 * @param blobCaption
	 *            e.g. "Picture of James, when he was skinny."
	 */
	public void insertRow(int userId, String blobFileName, String blobBinaryFilePath, String blobCaption) {
		String insertStmt = "INSERT INTO Blobs (userId, blobFileName, blobBinary, blobCaption) VALUES(?, ?, ?, ?)";
		String tableName = getTableName(insertStmt);
		File blobFile = null;
		blobFile = new File(blobBinaryFilePath);
		FileInputStream fileInputStream = null;

		/* Insert a single row. */
		PreparedStatement pstmt01 = null;
		try {
			fileInputStream = new FileInputStream(blobFile);
			pstmt01 = myConn.prepareStatement(insertStmt);
			int ix = 1;
			pstmt01.setInt(ix++, userId);
			pstmt01.setString(ix++, blobFileName);
			pstmt01.setBinaryStream(ix++, fileInputStream, (int) blobFile.length());
			pstmt01.setString(ix++, blobCaption);
			pstmt01.executeUpdate();
			
			pstmt01.close();
		} catch (SQLException sql_excp) {
			if (sql_excp.getSQLState().equals("23505")) {
				System.err.println("Row cannot be added to table " + tableName + "because another row with this key already exists.");
				sql_excp.printStackTrace();
			} else {
				System.err.println("Tried to store a picture for a new member. Error: " + sql_excp);
				sql_excp.printStackTrace();
			}
		} catch (FileNotFoundException fnf) {
			fnf.printStackTrace();
		}
	}

	/**
	 * Gets the table name out of an insert statement. Useful for errors.
	 * 
	 * @param insertStmt
	 *            Insert statement to be parsed.
	 * @return The table name.
	 */
	private String getTableName(String insertStmt) {
		String tableName;
		// Table name is 3rd word in the string.
		StringTokenizer st = new StringTokenizer(insertStmt, " ");
		for (short ix = 0; ix < 2; ix++) {
			st.nextToken(); // discard first two words
		}
		tableName = st.nextToken();
		if (DEBUG) {
			System.out.println("tableName is " + tableName);
		}
		return tableName;
	}

	/**
	 * This is the method I use to get a specific row from the Blobs table. -
	 * The blobs that I am getting from the database need to be stored as files
	 * on my file system. They are written to the blobsIn subdirectory, which is
	 * immediately below the directory containing my code.
	 */
	public void getRow(int blobId, String blobOutputDirectory) {
		String METHOD_NAME = "getRow()";
		String blobFileName = "";

		String tableName = "Blobs";
		if (DEBUG) {
			System.out.println(this.getClass().getName() + "." + METHOD_NAME + " -tableName is " + tableName);
		}
		String getStmt = "SELECT userId, blobFileName, blobBinary FROM " + tableName + " WHERE blobId = ?";

		PreparedStatement pstmt01 = null;
		ResultSet rs = null;

		/* Execute the query.
		 * 
		 * Examine the result set, which should be a single row. The values from
		 * the row are stored in Class variables.
		 */
		int rowCount = 0;
		try {
			pstmt01 = conn01.prepareStatement(getStmt);
			pstmt01.setInt(1, blobId);
			rs = pstmt01.executeQuery();
			while (rs.next()) {
				rowCount++;
				blobFileName = rs.getString("blobFileName").trim();
				Blob blobBinary = rs.getBlob("blobBinary");
				String blobOutputFilePath = blobOutputDirectory + "/" + blobFileName;
				if (DEBUG) {
					System.out.println("blobOutputFilePath: " + blobOutputFilePath);
				}

				writeBlobToFile(blobBinary, blobOutputFilePath);
			}
			pstmt01.close();
		} catch (SQLException sql_excp) {
			if (sql_excp.getSQLState().equals("42S02")) {
				System.err.println(CLASS_NAME + "." + METHOD_NAME + " - Desired row of table " + tableName + " not found. Error: " + sql_excp);
				sql_excp.printStackTrace();
			} else {
				System.err.println(CLASS_NAME + "." + METHOD_NAME + " - Error: " + sql_excp);
				sql_excp.printStackTrace();
			}
		}
		
		if (rowCount != 1) {
			System.err.println(CLASS_NAME + "." + METHOD_NAME + " - Query failed to return exactly one result row.");
		}
	}

	/**
	 * Writes any blob to a file.
	 * 
	 * @param myBlob
	 *            The blob handle.
	 * @param blobOutputFilePath
	 *            The output file path.
	 */
	public void writeBlobToFile(Blob myBlob, String blobOutputFilePath) {
		if (DEBUG) {
			System.out.println("start writeBlobToFile()");
		}
		blobOutputFilePath = blobOutputFilePath.replace("\\", "/");
		String METHOD_NAME = "writeBlobToFile()";

		File binaryFile = new File(blobOutputFilePath);
		try {
			// Create the file if it doesn't exist.
			if (!binaryFile.exists()) {
				binaryFile.createNewFile();
			}

			FileOutputStream outstream = new FileOutputStream(binaryFile);
			InputStream instream = myBlob.getBinaryStream();

			// Write binary in 4k chunks.
			int chunk = 4096;
			byte[] buffer = new byte[chunk];
			int length = -1;
			while ((length = instream.read(buffer)) != -1) {
				outstream.write(buffer, 0, length);
			}

			outstream.flush();
			instream.close();
			outstream.close();
		} catch (IOException io_excp) {
			System.err.println(CLASS_NAME + "." + METHOD_NAME + " - Error: " + io_excp);
			io_excp.printStackTrace();
		} catch (SQLException sql_excp) {
			System.err.println(CLASS_NAME + "." + METHOD_NAME + " - Error: " + sql_excp);
			sql_excp.printStackTrace();
		}

		if (DEBUG) {
			System.out.println("end writeBlobToFile()");
		}
	}
	
	/**
	 * Used to close the db connection.
	 */
	public void closeDbConn(){
		try {
			myConn.close();
			conn01.close();
		} catch (Exception e) {
			System.err.println("Exception occured when trying to close db connection.");
			e.printStackTrace();
		} 
	}

}
