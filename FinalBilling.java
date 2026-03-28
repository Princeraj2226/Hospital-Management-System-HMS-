package Hospital_management_system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class FinalBilling extends JFrame implements ActionListener {

    JComboBox<String> tSelectPatient;
    JTextField tAppointmentFee, tRoomCharge, tTotal, tDaysStayed, tRoomNo, tAmountPaid;
    JButton bPay, bClose;
    JLabel lRemaining;

    private int appointmentFee = 0;
    private int roomCharge = 0;
    private int total = 0;
    private int paidAmount = 0;

    public FinalBilling() {
        setTitle("Patient Final Billing System");
        setSize(950, 500);
        setLocation(250, 200);
        setLayout(null);
        setResizable(false);
        getContentPane().setBackground(Color.WHITE);

        JLabel head = new JLabel("Final Billing & Check-out");
        head.setBounds(350, 20, 400, 30);
        head.setFont(new Font("Tahoma", Font.BOLD, 26));
        add(head);

        int leftX = 50, rightX = 500;
        int labelWidth = 180, fieldWidth = 200, height = 30;

        addLabel("Select Patient ID", leftX, 80, labelWidth, height);
        tSelectPatient = new JComboBox<>();
        tSelectPatient.setBounds(leftX + labelWidth, 80, fieldWidth, height);
        add(tSelectPatient);

        tSelectPatient.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String user = (String) tSelectPatient.getSelectedItem();
                loadRoomDetails(user);
                loadBilling(user);
            }
        });

        addLabel("Appointment Fee", leftX, 140, labelWidth, height);
        tAppointmentFee = addTextField(leftX + labelWidth, 140, fieldWidth, height);
        tAppointmentFee.setEditable(false);

        addLabel("Room No", leftX, 200, labelWidth, height);
        tRoomNo = addTextField(leftX + labelWidth, 200, fieldWidth, height);
        tRoomNo.setEditable(false);

        addLabel("Days Stayed", rightX, 140, labelWidth, height);
        tDaysStayed = addTextField(rightX + labelWidth, 140, fieldWidth, height);
        tDaysStayed.setEditable(false);

        addLabel("Room Charge", rightX, 200, labelWidth, height);
        tRoomCharge = addTextField(rightX + labelWidth, 200, fieldWidth, height);
        tRoomCharge.setEditable(false);

        addLabel("Total Bill", leftX, 260, labelWidth, height);
        tTotal = addTextField(leftX + labelWidth, 260, fieldWidth, height);
        tTotal.setEditable(false);

        lRemaining = new JLabel("Balance Due: 0");
        lRemaining.setBounds(rightX, 260, 250, height);
        lRemaining.setFont(new Font("Tahoma", Font.BOLD, 16));
        lRemaining.setForeground(new Color(200, 0, 0));
        add(lRemaining);

        addLabel("Amount to Pay", leftX, 320, labelWidth, height);
        tAmountPaid = addTextField(leftX + labelWidth, 320, fieldWidth, height);

        bPay = new JButton("Confirm Payment");
        bPay.setBounds(500, 380, 180, 40);
        bPay.setBackground(Color.BLACK);
        bPay.setForeground(Color.WHITE);
        bPay.addActionListener(this);
        add(bPay);

        bClose = new JButton("Close");
        bClose.setBounds(700, 380, 150, 40);
        bClose.setBackground(Color.BLACK);
        bClose.setForeground(Color.WHITE);
        bClose.addActionListener(this);
        add(bClose);

        loadPatients();
        setVisible(true);
    }

    private JLabel addLabel(String text, int x, int y, int w, int h) {
        JLabel l = new JLabel(text);
        l.setBounds(x, y, w, h);
        l.setFont(new Font("Tahoma", Font.BOLD, 14));
        add(l);
        return l;
    }

    private JTextField addTextField(int x, int y, int w, int h) {
        JTextField t = new JTextField();
        t.setBounds(x, y, w, h);
        t.setBackground(new Color(245, 245, 245));
        add(t);
        return t;
    }

    private void loadPatients() {
        tSelectPatient.removeAllItems();
        try (Conn c = new Conn();
             ResultSet rs = c.getStatement().executeQuery("SELECT username FROM patients")) {
            while (rs.next()) {
                tSelectPatient.addItem(rs.getString("username"));
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadRoomDetails(String username) {
        if (username == null) return;
        try (Conn c = new Conn()) {
            PreparedStatement pst = c.getConnection().prepareStatement(
                "SELECT room_no, booking_date FROM room_booking WHERE patient_username=? ORDER BY booking_id DESC LIMIT 1");
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                tRoomNo.setText(rs.getString("room_no"));
                LocalDate bookingDate = rs.getDate("booking_date").toLocalDate();
                long days = ChronoUnit.DAYS.between(bookingDate, LocalDate.now());
                if (days <= 0) days = 1;
                tDaysStayed.setText(String.valueOf(days));
            } else {
                tRoomNo.setText("N/A");
                tDaysStayed.setText("0");
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadBilling(String username) {
        if (username == null) return;
        paidAmount = 0;
        appointmentFee = 0;

        try (Conn c = new Conn()) {
            // Count all active (not canceled) appointments
            PreparedStatement pstAppt = c.getConnection().prepareStatement(
                "SELECT COUNT(*) FROM appointment " +
                "WHERE name = (SELECT name FROM patients WHERE username=?) " +
                "AND status <> 'Canceled'"
            );
            pstAppt.setString(1, username);
            ResultSet rsAppt = pstAppt.executeQuery();
            if (rsAppt.next()) {
                int activeAppointments = rsAppt.getInt(1);
                appointmentFee = activeAppointments * 500; // 500 per active appointment
            }

            // Previous payment history
            PreparedStatement pstBill = c.getConnection().prepareStatement(
                "SELECT paid_amount FROM billing WHERE patient_username=? ORDER BY bill_id DESC LIMIT 1"
            );
            pstBill.setString(1, username);
            ResultSet rsBill = pstBill.executeQuery();
            if (rsBill.next()) {
                paidAmount = rsBill.getInt("paid_amount");
            }

        } catch (Exception e) { e.printStackTrace(); }

        tAppointmentFee.setText(String.valueOf(appointmentFee));
        recalc();
    }

    private void recalc() {
        String roomNo = tRoomNo.getText();
        int days = 0;
        try { days = Integer.parseInt(tDaysStayed.getText()); } catch (Exception e) { days = 0; }

        roomCharge = 0;
        if (!roomNo.equals("N/A")) {
            try (Conn c = new Conn()) {
                PreparedStatement pst = c.getConnection().prepareStatement("SELECT room_type FROM rooms WHERE room_no=?");
                pst.setString(1, roomNo);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    String type = rs.getString("room_type");
                    switch (type) {
                        case "General": roomCharge = 1000 * days; break;
                        case "Private": roomCharge = 2000 * days; break;
                        case "Operation Theater": roomCharge = 5000 * days; break;
                        case "ICU": roomCharge = 3000 * days; break;
                    }
                }
            } catch (Exception e) { e.printStackTrace(); }
        }

        total = appointmentFee + roomCharge;
        tRoomCharge.setText(String.valueOf(roomCharge));
        tTotal.setText(String.valueOf(total));
        lRemaining.setText("Balance Due: " + (total - paidAmount));
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == bPay) {
            String username = (String) tSelectPatient.getSelectedItem();
            if (username == null) return;

            try {
                int payNow = Integer.parseInt(tAmountPaid.getText());
                int remaining = total - paidAmount;

                if (payNow <= 0 || payNow > remaining) {
                    JOptionPane.showMessageDialog(this, "Invalid Payment Amount!");
                    return;
                }

                try (Conn c = new Conn()) {
                    PreparedStatement pst = c.getConnection().prepareStatement(
                        "INSERT INTO billing(patient_username, appointment_fee, room_charge, total, billing_date, paid_amount, paid) VALUES(?,?,?,?,CURDATE(),?,?)");
                    pst.setString(1, username);
                    pst.setInt(2, appointmentFee);
                    pst.setInt(3, roomCharge);
                    pst.setInt(4, total);
                    pst.setInt(5, paidAmount + payNow);
                    pst.setBoolean(6, (paidAmount + payNow == total));
                    pst.executeUpdate();
                }

                JOptionPane.showMessageDialog(this, "Payment Successful!");
                tAmountPaid.setText("");
                loadBilling(username);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid numeric amount.");
            }
        } else if (ae.getSource() == bClose) {
            dispose();
        }
    }

    public static void main(String[] args) {
        new FinalBilling();
    }
}