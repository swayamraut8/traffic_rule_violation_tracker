import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class TrafficLoginApp {
    private JFrame frame;
    private JPanel panel;
    private JLabel titleLabel, usernameLabel, passwordLabel, userTypeLabel; // Added userTypeLabel
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> userTypeComboBox;
    private JButton loginButton;

    public TrafficLoginApp() {
        // Create the main frame
        frame = new JFrame("Traffic Rule Violation Tracker"); // Changed the project title
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 220);
        frame.setLayout(new BorderLayout());

        // Load the background image
        try {
            BufferedImage backgroundImage = ImageIO.read(new File("background.jpg"));
            frame.setContentPane(new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(backgroundImage, 0, 0, null);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create the panel for components with a black background color
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.BLACK);

        // Create fonts for title and labels
        Font titleFont = new Font("Arial", Font.BOLD, 24);
        Font labelFont = new Font("Arial", Font.PLAIN, 14);

        // Create components with improved visuals
        titleLabel = new JLabel("Traffic Rule Violation Tracker"); // Changed the project title
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(Color.ORANGE); // Orange font color
        usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(labelFont);
        usernameLabel.setForeground(Color.ORANGE); // Orange font color
        passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(labelFont);
        passwordLabel.setForeground(Color.ORANGE); // Orange font color
        userTypeLabel = new JLabel("User Type:"); // Added userTypeLabel
        userTypeLabel.setFont(labelFont);
        userTypeLabel.setForeground(Color.ORANGE); // Orange font color
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        userTypeComboBox = new JComboBox<>(new String[]{"Traffic Police", "Driver"});
        userTypeComboBox.setFont(labelFont);
        loginButton = new JButton("Login");
        loginButton.setFont(labelFont);
        loginButton.setBackground(Color.ORANGE); // Orange button background color
        loginButton.setForeground(Color.BLACK); // Black font color

        // Create GridBagConstraints for layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add components to the panel with GridBagConstraints
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        panel.add(userTypeLabel, gbc); // Added userTypeLabel

        gbc.gridx = 1;
        panel.add(userTypeComboBox, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(new JLabel(), gbc); // Empty label for spacing

        gbc.gridy = 5;
        panel.add(loginButton, gbc);

        // Add the panel to the frame
        frame.add(panel, BorderLayout.CENTER);

        // Center the frame on the screen
        frame.setLocationRelativeTo(null);

        // Attach an action listener to the login button
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String userType = (String) userTypeComboBox.getSelectedItem();

                // Add your authentication logic here

                // For demonstration, show a message with login details
                String message = "Logged in as " + userType + ": " + username;
                JOptionPane.showMessageDialog(frame, message, "Login Successful", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Display the frame
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TrafficLoginApp();
            }
        });
    }
}
