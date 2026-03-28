package Hospital_management_system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class viewDoctor extends JFrame implements ActionListener {

    private final JTable table;
    private final JButton backButton;
    private final JFrame previousFrame;

    public viewDoctor(JFrame prev) {
        this.previousFrame = prev;

        setTitle("View Doctor Records");
        setSize(1000, 600);
        setLocation(270, 190);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("All Doctors", JLabel.CENTER);
        title.setFont(new Font("Tahoma", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        String[] columns = {"ID", "Name", "Username", "Phone", "Email", "Address", "Gender", "DOB", "Blood Group", "Joining Date", "Specialization", "Clinic No"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        table = new JTable(model);
        table.setFont(new Font("Tahoma", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.setFillsViewportHeight(true);
        table.setDefaultEditor(Object.class, null); // read-only
        table.setAutoCreateRowSorter(true); // enable sorting

        add(new JScrollPane(table), BorderLayout.CENTER);

        loadDoctorData(model);

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

    private void loadDoctorData(DefaultTableModel model) {
        String query = "SELECT doctor_id, name, username, phone, email, address, gender, dob, blood_group, joining_date, specialization, clinic_no FROM doctors";

        try (Conn c = new Conn();
             ResultSet rs = c.getStatement().executeQuery(query)) {

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("doctor_id"),
                        rs.getString("name"),
                        rs.getString("username"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getString("gender"),
                        rs.getDate("dob") != null ? rs.getDate("dob").toString() : "",
                        rs.getString("blood_group"),
                        rs.getDate("joining_date") != null ? rs.getDate("joining_date").toString() : "",
                        rs.getString("specialization"),
                        rs.getString("clinic_no")
                };
                model.addRow(row);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading doctor data: " + e.getMessage());
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
        SwingUtilities.invokeLater(() -> new viewDoctor(null));
    }
}