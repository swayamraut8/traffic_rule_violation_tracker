import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class TrafficPoliceDashboard {
    private JFrame frame;
    private JPanel panel;
    private JLabel titleLabel, searchLabel;
    private JTextField searchField;
    private JButton searchButton;
    private JTextArea userDetailsTextArea;

    public TrafficPoliceDashboard() {
        // Create the main frame for the Traffic Police Dashboard
        frame = new JFrame("Traffic Police Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(Color.BLACK); // Set background color to black

        // Create the panel for components with a black background color
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.BLACK);

        // Create fonts for title and labels
        Font titleFont = new Font("Arial", Font.BOLD, 24);
        Font labelFont = new Font("Arial", Font.PLAIN, 14);
        Color labelColor = Color.ORANGE;

        // Create components with updated UI
        titleLabel = new JLabel("Traffic Police Dashboard");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(labelColor);

        searchLabel = new JLabel("Enter Car Number:");
        searchLabel.setFont(labelFont);
        searchLabel.setForeground(labelColor);

        searchField = new JTextField(20);
        searchField.setFont(labelFont);

        searchButton = new JButton("Search");
        searchButton.setFont(labelFont);
        searchButton.setBackground(labelColor);
        searchButton.setForeground(Color.BLACK);

        userDetailsTextArea = new JTextArea();
        userDetailsTextArea.setEditable(false);
        userDetailsTextArea.setFont(labelFont);
        userDetailsTextArea.setBackground(Color.BLACK);
        userDetailsTextArea.setForeground(labelColor);

        // Create GridBagConstraints for layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add components to the panel with GridBagConstraints
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(searchLabel, gbc);

        gbc.gridx = 1;
        panel.add(searchField, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(new JLabel(), gbc);

        gbc.gridy = 3;
        panel.add(searchButton, gbc);

        // Add the panel to the frame
        frame.add(panel, BorderLayout.NORTH);

        // Create a scroll pane for the user details text area
        JScrollPane scrollPane = new JScrollPane(userDetailsTextArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Center the frame on the screen
        frame.setLocationRelativeTo(null);

        // Attach an action listener to the search button
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String carNumber = searchField.getText();

                // Database interaction logic
                String userData = fetchUserDataFromDatabase(carNumber);

                // Display user data
                userDetailsTextArea.setText(userData);
            }
        });

        // Display the frame
        frame.setVisible(true);
    }

    // Sample method to fetch user data from the database based on car number
    private String fetchUserDataFromDatabase(String carNumber) {
        try {
            // Establish a database connection
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/traffic_violation_tracker", "root", "Pass@1234");

            // Prepare an SQL query
            String sql = "SELECT * FROM UserTable WHERE car_number = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, carNumber);

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Process the result set and build user data
            StringBuilder userData = new StringBuilder();
            while (resultSet.next()) {
                userData.append("Driver Name: ").append(resultSet.getString("driver_name")).append("\n");
                userData.append("License Status: ").append(resultSet.getString("license_status")).append("\n");
                // Add more fields as needed
            }

            // Close resources
            resultSet.close();
            preparedStatement.close();
            connection.close();

            // Return the user data as a string
            return userData.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error retrieving user data.";
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TrafficPoliceDashboard();
            }
        });
    }
}
