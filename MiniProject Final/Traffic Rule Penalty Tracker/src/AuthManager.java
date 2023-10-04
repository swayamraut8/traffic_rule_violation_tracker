import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AuthManager {
    public static User authenticateUser(String username, String enteredPassword) {
        Connection connection = DatabaseConfig.getConnection();
        if (connection == null) {
            return null; // Database connection failed
        }

        // Prepare a SQL query to fetch user data
        String sql = "SELECT user_id, password_hash, user_type, active_date FROM Users WHERE username = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int userId = resultSet.getInt("user_id");
                    String storedPasswordHash = resultSet.getString("password_hash");
                    String userType = resultSet.getString("user_type");
                    Date activeDate = resultSet.getDate("active_date");

                    // Check if the account is active
                    Date currentDate = new Date();
                    if (currentDate.after(activeDate) && checkPassword(enteredPassword, storedPasswordHash)) {
                        return new User(userId, username, storedPasswordHash, userType, activeDate);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Authentication failed
    }

    private static boolean checkPassword(String enteredPassword, String storedPasswordHash) {
        // Implement password hashing and verification logic
        // For security reasons, store and compare password hashes
        // This is a simplified example; use a strong hashing library in practice
        return enteredPassword.equals(storedPasswordHash);
    }
}
