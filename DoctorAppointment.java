package Hospital_management_system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class DoctorAppointment extends JFrame implements ActionListener {

    private JTable table;
    private JButton doneButton, cancelButton, refreshButton;
    private int doctorId;

    // Constructor using username (from login)
    public DoctorAppointment(String username) {

        doctorId = getDoctorId(username);

        if (doctorId == -1) {
            JOptionPane.showMessageDialog(null, "Doctor not found!");
            return;
        }

        setTitle("My Appointments");
        setSize(1000, 600);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Title
        JLabel title = new JLabel("My Appointments", JLabel.CENTER);
        title.setFont(new Font("Tahoma", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Patient Name", "Date", "Time", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(25);
        table.setAutoCreateRowSorter(true);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Buttons
        JPanel panel = new JPanel();

        doneButton = new JButton("Mark Done");
        cancelButton = new JButton("Cancel Appointment");
        refreshButton = new JButton("Refresh");

        doneButton.addActionListener(this);
        cancelButton.addActionListener(this);
        refreshButton.addActionListener(this);

        panel.add(doneButton);
        panel.add(cancelButton);
        panel.add(refreshButton);

        add(panel, BorderLayout.SOUTH);

        // Load data
        loadAppointments(model);

        setVisible(true);
    }

    // Get doctor_id from username
    private int getDoctorId(String username) {
        String sql = "SELECT doctor_id FROM doctors WHERE username=?";
        try (Conn c = new Conn();
             PreparedStatement pst = c.getConnection().prepareStatement(sql)) {

            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getInt("doctor_id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Load appointments
    private void loadAppointments(DefaultTableModel model) {
        model.setRowCount(0);

        String sql = "SELECT id, name, appointmentdate, appointmenttime, status " +
                     "FROM appointment WHERE doctor_id=? ORDER BY appointmentdate, appointmenttime";

        try (Conn c = new Conn();
             PreparedStatement pst = c.getConnection().prepareStatement(sql)) {

            pst.setInt(1, doctorId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDate("appointmentdate"),
                        rs.getTime("appointmenttime"),
                        rs.getString("status")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Button actions
    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == refreshButton) {
            loadAppointments((DefaultTableModel) table.getModel());
            return;
        }

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(null, "Select appointment first!");
            return;
        }

        int id = (int) table.getValueAt(row, 0);
        String status = (ae.getSource() == doneButton) ? "Done" : "Cancelled";

        String sql = "UPDATE appointment SET status=? WHERE id=?";

        try (Conn c = new Conn();
             PreparedStatement pst = c.getConnection().prepareStatement(sql)) {

            pst.setString(1, status);
            pst.setInt(2, id);
            pst.executeUpdate();

            loadAppointments((DefaultTableModel) table.getModel());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Main (simulate login)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DoctorAppointment("doctor1"));
    }
}