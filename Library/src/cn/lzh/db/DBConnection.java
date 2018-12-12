package cn.lzh.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnection {
	private final String Driver = "com.mysql.cj.jdbc.Driver";
	
	private final String URL  = "jdbc:mysql://localhost:3306/library?serverTimezone=UTC&characterEncoding=utf8&useUnicode=true&useSSL=false";
	
	private final String USER = "root";
	
	private final String PASSWORD = "sakura";
	
	private Connection conn = null;
	
	public DBConnection() throws SQLException {
		try {
			Class.forName(Driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		this.conn = DriverManager.getConnection(URL, USER, PASSWORD);
	}

	public Connection getConnection() {
		return conn;
	}

	public void close() {
		if (this.conn != null) {
			try {
				this.conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
