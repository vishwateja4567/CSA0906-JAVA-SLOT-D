import java.sql.*;
public class DBConfig {
    public static final String URL = "jdbc:mysql://localhost:3306/assignment5";
    public static final String USER = "root";
    public static final String PASSWORD = "2235rmsql";
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
