package Hospital_management_system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class viewReceptionist extends JFrame implements ActionListener {

    private final JTable table;
    private final JButton backButton;
    private final JFrame previousFrame;

    public viewReceptionist(JFrame prev) {
        this.previousFrame = prev;

        setTitle("View Receptionists");
        setSize(900, 600);
        setLocation(270, 190);
        setLayout(new BorderLayout());

        // Title label
        JLabel title = new JLabel("All Receptionists", JLabel.CENTER);
        title.setFont(new Font("Tahoma", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // Table columns
        String[] columns = {"ID", "Name", "Username", "Phone", "Email", "Address",
                            "Gender", "DOB", "Blood Group", "Joining Date", "Qualification", "Marital Status"};

        DefaultTableModel model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setFont(new Font("Tahoma", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.setFillsViewportHeight(true);
        table.setDefaultEditor(Object.class, null); // read-only
        table.setAutoCreateRowSorter(true); // enable sorting
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Load receptionist data
        loadReceptionists(model);

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

    // Load receptionists from database
    private void loadReceptionists(DefaultTableModel model) {
        String sql = "SELECT * FROM receptionists";

        try (Conn c = new Conn();
             ResultSet rs = c.getStatement().executeQuery(sql)) {

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("username"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getString("gender"),
                        rs.getDate("dob") != null ? rs.getDate("dob").toString() : "",
                        rs.getString("blood_group"),
                        rs.getDate("joining_date") != null ? rs.getDate("joining_date").toString() : "",
                        rs.getString("qualification"),
                        rs.getString("marital_status")
                };
                model.addRow(row);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading receptionists: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Refresh table after updates
    public void refreshReceptionists() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        loadReceptionists(model);
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
        SwingUtilities.invokeLater(() -> new viewReceptionist(null));
    }
}