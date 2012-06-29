package sqltest;
import java.sql.*;
public class DBUtil {
	private static String DRIVER="com.mysql.jdbc.Driver";
	private static String URL="jdbc:mysql://localhost:3306/test";
	private static String USERNAME="root";
	private static String PASSWORD="discoveracm";
	
	public static Connection getConnection(){
		Connection conn=null;
		try {
			Class.forName(DRIVER);
			conn=DriverManager.getConnection(URL,USERNAME,PASSWORD);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
	public static void closeConnection(Connection conn){
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
