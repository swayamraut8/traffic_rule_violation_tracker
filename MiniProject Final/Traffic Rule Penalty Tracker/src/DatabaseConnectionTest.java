import java.sql.Connection;

public class DatabaseConnectionTest {
    public static void main(String[] args) {
        Connection connection = DatabaseConfig.getConnection();
        if (connection != null) {
            System.out.println("Database connection established successfully.");
        } else {
            System.out.println("Failed to connect to the database.");
        }
    }
}
