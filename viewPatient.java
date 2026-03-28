package Hospital_management_system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class viewPatient extends JFrame implements ActionListener {

    private final JTable table;
    private final JButton backButton;
    private final JFrame previousFrame;

    public viewPatient(JFrame prev) {
        this.previousFrame = prev;

        setTitle("View Patient Records");
        setSize(900, 600);
        setLocation(270, 190);
        setLayout(new BorderLayout());

        // Header
        JLabel title = new JLabel("All Patients", JLabel.CENTER);
        title.setFont(new Font("Tahoma", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // Table columns
        String[] columns = {"ID", "Name", "Gender", "DOB", "Phone", "Email", "Address"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        // Table setup
        table = new JTable(model);
        table.setFont(new Font("Tahoma", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.setFillsViewportHeight(true);
        table.setDefaultEditor(Object.class, null); // read-only
        table.setAutoCreateRowSorter(true); // enable sorting
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Load data
        loadPatientData(model);

        // Back button
        backButton = new JButton("Back");
        backButton.setFont(new Font("Tahoma", Font.BOLD, 16));
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(this);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void loadPatientData(DefaultTableModel model) {
        String query = "SELECT patient_id, name, gender, dob, phone, email, address FROM patients";

        try (Conn c = new Conn();
             ResultSet rs = c.getStatement().executeQuery(query)) {

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("patient_id"),
                        rs.getString("name"),
                        rs.getString("gender"),
                        rs.getDate("dob") != null ? rs.getDate("dob").toString() : "",
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address")
                };
                model.addRow(row);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading patient data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == backButton) {
            dispose();
            if (previousFrame != null) {
                previousFrame.setVisible(true);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new viewPatient(null));
    }
}