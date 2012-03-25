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
 * Base example from: http://lists.mysql.com/java/6017
 * 
 * Code updated by: JamesOravec
 * 
 * Notes from original post: And lastly one very important fact: when you have
 * stored the blob successfully in the table, don't expect to see much in the
 * table when you do a select on it! In DB2, which I have been using for years,
 * a blob shows up as a string of hex bytes, e.g. X'A1B3D490'..... etc. In
 * MySQL, you get only 4 bytes that often look like nonsense. That's because
 * MySQL actually stores the blob(s) outside of the table and just leaves a
 * locator in the table to point to the actual blob. I wish I had known *that*
 * from the beginning! As it turned out, my very first execution of my program
 * which stores Blobs worked perfectly but when I didn't see the data looking
 * the way it did in DB2, I thought I had messed something up and spend a number
 * of hours researchng and rewriting code to make it store the blob correctly. I
 * could have saved that time if I had known that only the locator appears in
 * the table when you do a select on it, e.g. select audio on Blobs;
 * 
 * This code depends on the following table: create table Blobs (member_name
 * char(20) not null, picture_filename char(50), picture mediumblob,
 * picture_caption blob, audio_filename char(50), audio mediumblob,
 * audio_caption blob, primary key(member_name));
 */
public class BlobHandler {

	private static final boolean DEBUG = true;
	private static final String CLASS_NAME = "BlobHandler";

	static private String dbUrl = "";
	public static String query = "";
	static private String user = "";
	static private String password = "";
	public static Connection myConn = null;
	public static Connection conn01 = null;

	/* Set the host variables. */
	String MemberName = null;
	String PictureFilename = null;
	String DecodedBlobInPath = null;
	File PictureFile = null;
	FileInputStream PFStream = null;
	String PictureCaption = null;

	/**
	 * Default constructor. Creates connection to database, so can insert blobs
	 * into the db, etc.
	 */
	public BlobHandler() {
		makeConnectionToMySQLDatabase();
	}

	/**
	 * Creates the connection to the MySQL Database. Database username and password
	 * are provided in the config.properties file.
	 */
	private void makeConnectionToMySQLDatabase() {
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
	}

	public static void main(String[] args) {
		System.out.println("Start connection creation");
		BlobHandler b = new BlobHandler();
		b.insertRow();

		System.out.println("End connection creation");
	}

	/**
	 * This is the method I used to insert a new row into the Blobs table. Note
	 * the following: - The blobs that I am trying to store in the database
	 * exist as files on my file system. They are in the blobsIn subdirectory,
	 * which is immediately below the directory containing my code. - I am using
	 * the current MySQL/J driver which supports JDBC 3.0. Therefore, I am not
	 * using setBytes() or even setBlob() to insert blob values into a blob
	 * column; I am using setBinaryStream(). However, I used to use setBytes()
	 * and still have the code for that if you need it: it needs a fairly
	 * lengthy additional method to convert the files to byte arrays.
	 */
	public void insertRow() {
		if (DEBUG) {
			System.out.println("start insertRow()");
		}

		String METHOD_NAME = "insertRow()";
		String insertStmt = "INSERT INTO Blobs (member_name, picture_filename, picture, picture_caption) VALUES(?, ?, ?, ?)";

		// Get table name for errors.
		String tableName = getTableName(insertStmt);

		String currentFile = new File("spider/images/jamesbtest1.jpg").getAbsolutePath();

		/* Set the host variables. */
		MemberName = "Big Daddy O";
		PictureFilename = "jamesbtest1.jpg";
		PictureFile = new File(currentFile);
		PFStream = null;
		PictureCaption = "Picture of James, when he was skinny.";

		try {
			PFStream = new FileInputStream(PictureFile);
		} catch (FileNotFoundException fnf) {
			fnf.printStackTrace();
		}

		/* Insert a single row. */
		PreparedStatement pstmt01 = null;
		int insertResponse = 0;
		try {
			pstmt01 = myConn.prepareStatement(insertStmt);
			int ix = 1;
			pstmt01.setString(ix++, MemberName);
			pstmt01.setString(ix++, PictureFilename);
			pstmt01.setBinaryStream(ix++, PFStream, (int) PictureFile.length());
			pstmt01.setString(ix++, PictureCaption);
			insertResponse = pstmt01.executeUpdate();
		} catch (SQLException sql_excp) {
			if (sql_excp.getSQLState().equals("23505")) {
				System.err.println("Row cannot be added to table " + tableName + "because another row with this key already exists.");
				sql_excp.printStackTrace();
			} else {
				System.err.println("Tried to store a picture for a new member. Error: " + sql_excp);
				sql_excp.printStackTrace();
			}
		}

		/* Dispose of the statement and commit. */
		try {
			pstmt01.close();
			myConn.close();
		} catch (SQLException sql_excp) {
			String msg = "Tried to close the statement which inserted a picture file for a new member, commit the transaction, and close the connection. Error: " + sql_excp;
			System.err.println(msg);
			sql_excp.printStackTrace();
		}

		if (DEBUG) {
			System.out.println("end insertRow()");
		}
	}

	/**
	 * Gets the table name out of an insert statement.
	 * 
	 * @param insertStmt	Insert statement to be parsed.
	 * @return				The table name.
	 */
	private String getTableName(String insertStmt) {
		String tableName;
		//Table name is 3rd word in the string.
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
	public void getRow() {
		if (DEBUG) {
			System.out.println("start getRow()");
		}

		String METHOD_NAME = "getRow()";

		String tableName = "Blobs";
		if (DEBUG) {
			System.out.println(this.getClass().getName() + "." + METHOD_NAME + " -tableName is " + tableName);
		}
		String getStmt = "select member_name, picture_filename, picture, picture_caption, audio_filename, audio, audio_caption from " + tableName + " where member_name = ?";

		/* Determine path to which blobs should be written. */
		URL urlBlobOutPath = this.getClass().getResource("/blobOut");
		if (DEBUG) {
			System.out.println("urlBlobOutPath: " + urlBlobOutPath.toString());
		}
		String EncodedBlobOutPath = urlBlobOutPath.getFile(); // String version
																// of file name
		if (DEBUG) {
			System.out.println(CLASS_NAME + "." + METHOD_NAME + " - EncodedBlobOutPath: " + EncodedBlobOutPath);
		}
		String DecodedBlobOutPath = null;
		try {
			DecodedBlobOutPath = URLDecoder.decode(EncodedBlobOutPath, "UTF-8");
			if (DEBUG) {
				System.out.println(CLASS_NAME + "." + METHOD_NAME + " - DecodedBlobOutPath: " + DecodedBlobOutPath);
			}
		} catch (UnsupportedEncodingException ue_excp) {
			System.out.println(CLASS_NAME + " - Failed to decode file name " + EncodedBlobOutPath + ". Error: " + ue_excp);
			System.exit(16);
		}

		/* Set the WHERE variables. */
		String DesiredMemberName = "Dave G.";

		/* Execute the query. */
		PreparedStatement pstmt01 = null;
		ResultSet rs = null;
		try {
			pstmt01 = conn01.prepareStatement(getStmt);
			pstmt01.setString(1, DesiredMemberName);
			rs = pstmt01.executeQuery();
		} catch (SQLException sql_excp) {
			if (sql_excp.getSQLState().equals("42S02")) {
				String msg = CLASS_NAME + "." + METHOD_NAME + " - Desired row of table " + tableName + " not found. Error: " + sql_excp;
				System.err.println(msg);
				sql_excp.printStackTrace();
				return;
			} else {
				String msg = CLASS_NAME + "." + METHOD_NAME + " - Failed to retrieve desired row. Error: " + sql_excp;
				System.err.println(msg);
				sql_excp.printStackTrace();
				return;
			}
		}

		/* Prepare to count the number of rows in the result set. */
		int rowCount = 0;

		/*
		 * Examine the result set, which should be a single row. The values from
		 * the row are stored in Class variables.
		 */
		try {
			while (rs.next()) {
				rowCount++;
				MemberName = rs.getString("MEMBER_NAME").trim();
				PictureFilename = rs.getString("PICTURE_FILENAME").trim();
				Blob PictureBlob = rs.getBlob("PICTURE");
				writeBlobToFile(PictureBlob, DecodedBlobOutPath, PictureFilename);
				PictureCaption = rs.getString("PICTURE_CAPTION").trim();
			}
		} catch (SQLException sql_excp) {
			String msg = CLASS_NAME + "." + METHOD_NAME + " - Encountered SQLException while reading query result set. Error: " + sql_excp;
			System.err.println(msg);
			sql_excp.printStackTrace();
			return;
		}

		if (rowCount != 1) {
			String msg = CLASS_NAME + "." + METHOD_NAME + " - Query failed to return exactly one result row.";
			System.err.println(msg);
			return;
		}

		/* Dispose of the statement and commit. */
		try {
			pstmt01.close();
			conn01.commit();
			conn01.close();
		} catch (SQLException sql_excp) {
			String msg = CLASS_NAME + "." + METHOD_NAME + " - Tried to close the statement which got a picture of a new member, commit the transaction, and close the connection. Error: " + sql_excp;
			System.err.println(msg);
			sql_excp.printStackTrace();
			return;
		}

		if (DEBUG) {
			System.out.println("end getRow()");
		}
	}

	/**
	 * [This is a utility method to handle the writing of any blob to a file.]
	 * 
	 * @param myBlob
	 * @param FilePath
	 * @param FileName
	 */
	public void writeBlobToFile(Blob myBlob, String FilePath, String FileName) {
		if (DEBUG) {
			System.out.println("start writeBlobToFile()");
		}

		String METHOD_NAME = "writeBlobToFile()";

		File binaryFile = new File(FilePath, FileName);

		try {
			FileOutputStream outstream = new FileOutputStream(binaryFile);
			InputStream instream = myBlob.getBinaryStream();

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
			String msg = CLASS_NAME + "." + METHOD_NAME + " - Error: " + io_excp;
			System.err.println(msg);
			io_excp.printStackTrace();
			return;
		} catch (SQLException sql_excp) {
			String msg = CLASS_NAME + "." + METHOD_NAME + " - Error: " + sql_excp;
			System.err.println(msg);
			sql_excp.printStackTrace();
		}

		if (DEBUG) {
			System.out.println("end writeBlobToFile()");
		}
	}

}
