package Hospital_management_system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class viewAppointment extends JFrame implements ActionListener {

    private final JTable table;
    private final JButton backButton;
    private final JFrame previousFrame;

    public viewAppointment(JFrame prev) {
        this.previousFrame = prev;

        setTitle("View Appointments");
        setSize(900, 600);
        setLocation(270, 190);
        setLayout(new BorderLayout());

        // Title
        JLabel title = new JLabel("All Appointments", JLabel.CENTER);
        title.setFont(new Font("Tahoma", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // Table columns
        String[] columns = { "Name", "Email", "Father Name", "Phone",
                             "Marital Status", "Gender", "Blood Group", "DOB", "Deceased",
                             "Deceased Name",
                             "Appointment Date", "Time" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setFont(new Font("Tahoma", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.setFillsViewportHeight(true);
        table.setDefaultEditor(Object.class, null); // Make table read-only
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setAutoCreateRowSorter(true); // Enable sorting
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Load data
        refreshAppointments();

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

    // Load appointments from database
    private void loadAppointments(DefaultTableModel model) {
        String sql = "SELECT * FROM appointment";

        try (Conn c = new Conn();
             ResultSet rs = c.getStatement().executeQuery(sql)) {

            while (rs.next()) {
                Object[] row = {
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("fathername"),
                        rs.getString("phoneno"),
                        rs.getString("marital_status"),
                        rs.getString("gender"),
                        rs.getString("bloodgroup"),
                        rs.getDate("dob") != null ? rs.getDate("dob").toString() : "",
                        rs.getBoolean("deceased") ? "Yes" : "No",
                        rs.getString("deceased_name"),
                       
                        rs.getDate("appointmentdate") != null ? rs.getDate("appointmentdate").toString() : "",
                        rs.getTime("appointmenttime") != null ? rs.getTime("appointmenttime").toString() : ""
                };
                model.addRow(row);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading appointments: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Refresh table after updates
    public void refreshAppointments() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Clear existing rows
        loadAppointments(model);
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
        SwingUtilities.invokeLater(() -> new viewAppointment(null)); // Standalone testing
    }
}