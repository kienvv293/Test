/**
 * 
 */
package common;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author nguyenhuytan
 *
 */
public class SqliteCommon {

	private static Connection conn = null;
	private static String url = null;

	public static Connection initDatabase(String fileName) {
		url = "jdbc:sqlite:" + fileName;
		try {
		    Connection conn = DriverManager.getConnection(url);
			if (conn != null) {
				DatabaseMetaData meta = conn.getMetaData();
				System.out.println("The driver name is " + meta.getDriverName());
				System.out.println("A new database has been created.");
				return conn;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return null;
	}

	public static Connection getConnection() {
		try {
			if (conn == null || conn.isClosed()) {
				openConnection();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		if (conn == null) {
			throw new RuntimeException("không thể tạo kết nối DB");
		}
		return conn;
	}

	private static void openConnection() {
		try {
			// create a connection to the database
			conn = DriverManager.getConnection(url);
			System.out.println("Connection to SQLite has been established.");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}