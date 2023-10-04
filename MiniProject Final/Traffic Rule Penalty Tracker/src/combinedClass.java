import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class combinedClass extends JFrame {
    private JPanel userDetailsPanel;
    private JTextArea userDetailsTextArea;
    private JPanel fineImpositionPanel;
    private JCheckBox overspeedingCheckbox;
    private JCheckBox trafficSignalViolationCheckbox;
    private JCheckBox overtakingCheckbox;
    private JCheckBox speedLimitViolationCheckbox;
    private JCheckBox parkingViolationCheckbox;
    private JCheckBox documentIrregularitiesCheckbox;
    private JButton chargeButton;
    private JPanel fineHistoryPanel;
    private JTable fineHistoryTable;
    private DefaultTableModel tableModel;
    private JLabel pendingFinesLabel;

    public combinedClass() {
        setTitle("Traffic Police Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        getContentPane().setBackground(Color.BLACK); // Set the background color to black

        // Initialize UI components and set up layout
        tableModel = new DefaultTableModel();

        // User Details Panel
        userDetailsPanel = new JPanel();
        userDetailsPanel.setLayout(new BorderLayout());
        userDetailsTextArea = new JTextArea(10, 30);
        userDetailsTextArea.setEditable(false);
        userDetailsTextArea.setLineWrap(true);
        userDetailsTextArea.setWrapStyleWord(true);
        userDetailsTextArea.setBackground(Color.BLACK);
        userDetailsTextArea.setForeground(Color.ORANGE);
        userDetailsTextArea.setFont(new Font("Arial", Font.PLAIN, 14));

        // Display dummy user details
        userDetailsTextArea.append("Driver Name: John Doe\n");
        userDetailsTextArea.append("Driving License Status: Active\n");
        userDetailsTextArea.append("Age: 30\n");
        userDetailsTextArea.append("Sex: Male\n");
        userDetailsTextArea.append("Birth Date: 01/15/1993\n");
        userDetailsTextArea.append("Phone No: 123-456-7890\n");
        userDetailsTextArea.append("Residential Address: 123 Main St, City\n");
        userDetailsTextArea.append("Car Name: Toyota\n");
        userDetailsTextArea.append("Model: Camry\n");
        userDetailsTextArea.append("Registration Year: 2021\n");
        userDetailsTextArea.append("Car Colour: Blue\n");
        userDetailsTextArea.append("RTO: XYZ RTO\n");
        userDetailsTextArea.append("Driving License Valid Till: 12/31/2023\n");

        userDetailsPanel.add(new JScrollPane(userDetailsTextArea), BorderLayout.CENTER);

        // Fine Imposition Panel
        fineImpositionPanel = new JPanel();
        fineImpositionPanel.setLayout(new GridLayout(8, 1));
        overspeedingCheckbox = new JCheckBox("Overspeeding (400 INR)");
        trafficSignalViolationCheckbox = new JCheckBox("Traffic Signal Violation (500 INR)");
        overtakingCheckbox = new JCheckBox("Overtaking (300 INR)");
        speedLimitViolationCheckbox = new JCheckBox("Speed Limit Violation (200 INR)");
        parkingViolationCheckbox = new JCheckBox("Parking Violation (400 INR)");
        documentIrregularitiesCheckbox = new JCheckBox("Document Irregularities (700 INR)");

        overspeedingCheckbox.setFont(new Font("Arial", Font.PLAIN, 14));
        trafficSignalViolationCheckbox.setFont(new Font("Arial", Font.PLAIN, 14));
        overtakingCheckbox.setFont(new Font("Arial", Font.PLAIN, 14));
        speedLimitViolationCheckbox.setFont(new Font("Arial", Font.PLAIN, 14));
        parkingViolationCheckbox.setFont(new Font("Arial", Font.PLAIN, 14));
        documentIrregularitiesCheckbox.setFont(new Font("Arial", Font.PLAIN, 14));

        overspeedingCheckbox.setForeground(Color.ORANGE);
        trafficSignalViolationCheckbox.setForeground(Color.ORANGE);
        overtakingCheckbox.setForeground(Color.ORANGE);
        speedLimitViolationCheckbox.setForeground(Color.ORANGE);
        parkingViolationCheckbox.setForeground(Color.ORANGE);
        documentIrregularitiesCheckbox.setForeground(Color.ORANGE);

        overspeedingCheckbox.setBackground(Color.BLACK);
        trafficSignalViolationCheckbox.setBackground(Color.BLACK);
        overtakingCheckbox.setBackground(Color.BLACK);
        speedLimitViolationCheckbox.setBackground(Color.BLACK);
        parkingViolationCheckbox.setBackground(Color.BLACK);
        documentIrregularitiesCheckbox.setBackground(Color.BLACK);

        chargeButton = new JButton("Charge");
        chargeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle fine imposition logic here
            }
        });
        chargeButton.setFont(new Font("Arial", Font.PLAIN, 14));
        chargeButton.setBackground(Color.ORANGE);
        chargeButton.setForeground(Color.BLACK);

        fineImpositionPanel.add(overspeedingCheckbox);
        fineImpositionPanel.add(trafficSignalViolationCheckbox);
        fineImpositionPanel.add(overtakingCheckbox);
        fineImpositionPanel.add(speedLimitViolationCheckbox);
        fineImpositionPanel.add(parkingViolationCheckbox);
        fineImpositionPanel.add(documentIrregularitiesCheckbox);
        fineImpositionPanel.add(chargeButton);

        fineImpositionPanel.setBackground(Color.BLACK);

        // Fine History Panel
        fineHistoryPanel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel();
        fineHistoryTable = new JTable(tableModel);
        fineHistoryTable.setBackground(Color.BLACK);
        fineHistoryTable.setForeground(Color.ORANGE);
        fineHistoryTable.setFont(new Font("Arial", Font.PLAIN, 14));
        fineHistoryPanel.add(new JScrollPane(fineHistoryTable), BorderLayout.CENTER);

        // Pending Fines Panel
        pendingFinesLabel = new JLabel("Pending Fines: 0 INR");
        pendingFinesLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        pendingFinesLabel.setForeground(Color.ORANGE);
        fineHistoryPanel.add(pendingFinesLabel, BorderLayout.SOUTH);

        // Set up the layout for the main frame
        setLayout(new GridLayout(2, 2));

        // Left Top: User Details
        add(userDetailsPanel);

        // Right Top: Fine Imposition
        add(fineImpositionPanel);

        // Left Bottom: Fine History
        add(fineHistoryPanel);
    }

    // Implement methods to update user details, fine history, and pending fines

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new combinedClass().setVisible(true);
            }
        });
    }
}
