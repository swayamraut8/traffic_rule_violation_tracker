import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DriverDashboard {
    private JFrame frame;
    private JPanel panel;
    private JLabel titleLabel, userDetailsLabel;
    private JTextField usernameField;
    private JButton searchButton;
    private JTextArea userDetailsTextArea;

    public DriverDashboard() {
        // Create the main frame for the Driver Dashboard
        frame = new JFrame("Driver Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Create the panel for components
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        // Create fonts for title and labels
        Font titleFont = new Font("Arial", Font.BOLD, 24);
        Font labelFont = new Font("Arial", Font.PLAIN, 14);

        // Create components
        titleLabel = new JLabel("Driver Dashboard");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(titleFont);

        userDetailsLabel = new JLabel("Enter Username:");
        userDetailsLabel.setFont(labelFont);

        usernameField = new JTextField(20);

        searchButton = new JButton("Search");
        searchButton.setFont(labelFont);

        userDetailsTextArea = new JTextArea();
        userDetailsTextArea.setEditable(false);
        userDetailsTextArea.setFont(labelFont);

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
        panel.add(userDetailsLabel, gbc);

        gbc.gridx = 1;
        panel.add(usernameField, gbc);

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
                // Dummy user data
                String userData = "Driver Name: John Doe\n";
                userData += "License Status: Active\n";
                userData += "Age: 30\n";
                userData += "Sex: Male\n";
                userData += "Birth Date: 1993-05-15\n";
                userData += "Phone No: 123-456-7890\n";
                userData += "Residential Address: 123 Main St\n";
                userData += "Car Name: Toyota Camry\n";
                userData += "Model: 2021\n";
                userData += "Registration Year: 2021\n";
                userData += "Car Color: Silver\n";
                userData += "RTO: Mumbai\n";
                userData += "License Valid Till: 2025-05-15\n";

                // Display user data
                userDetailsTextArea.setText(userData);
            }
        });

        // Display the frame
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new DriverDashboard();
            }
        });
    }
}
