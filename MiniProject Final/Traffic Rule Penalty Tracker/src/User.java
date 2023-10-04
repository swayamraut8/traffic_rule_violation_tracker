import java.util.Date;

public class User {
    private int user_id;
    private String username;
    private String password_hash;
    private String user_type;
    private Date active_date;

    // Constructor
    public User(int user_id, String username, String password_hash, String user_type, Date active_date) {
        this.user_id = user_id;
        this.username = username;
        this.password_hash = password_hash;
        this.user_type = user_type;
        this.active_date = active_date;
    }

    // Getter methods
    public int getUserId() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return password_hash;
    }

    public String getUserType() {
        return user_type;
    }

    public Date getActiveDate() {
        return active_date;
    }

    // Add setter methods if needed
}
