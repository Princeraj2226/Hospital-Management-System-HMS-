package Hospital_management_system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import com.toedter.calendar.JDateChooser;   // ✅ NEW

public class addDoctor extends JFrame implements ActionListener {

    JTextField t1, t2, t3, t4, t5, t6, t11, t12;
    JComboBox<String> t7, t9;

    // ✅ Calendar components
    JDateChooser dobChooser, joiningDateChooser;

    JButton b1, b2;

    public addDoctor() {
        setTitle("Add Doctor Panel");
        setSize(1000, 600);
        setLocation(270, 190);
        setLayout(null);
        setResizable(false);
        getContentPane().setBackground(Color.WHITE);

        JLabel head = new JLabel("Add Doctor Details");
        head.setBounds(180, 20, 700, 30);
        head.setFont(new Font("Tahoma", Font.BOLD, 24));
        add(head);

        // Column 1
        addLabel("Doctor Name", 20, 70); t1 = addTextField(200, 73);
        addLabel("Password", 20, 120); t3 = addTextField(200, 123);
        addLabel("Email Id", 20, 170); t5 = addTextField(200, 173);

        addLabel("Gender", 20, 220);
        t7 = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        t7.setBounds(200, 223, 200, 30);
        add(t7);

        addLabel("Blood Group", 20, 270);
        t9 = new JComboBox<>(new String[]{"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"});
        t9.setBounds(200, 273, 200, 30);
        add(t9);

        addLabel("Specialization", 20, 320); t11 = addTextField(200, 323);

        // Column 2
        addLabel("Username", 480, 70); t2 = addTextField(600, 73);
        addLabel("Phone No.", 480, 120); t4 = addTextField(600, 123);
        addLabel("Address", 480, 170); t6 = addTextField(600, 173);

        // ✅ DOB Calendar
        addLabel("Date of Birth", 480, 220);
        dobChooser = new JDateChooser();
        dobChooser.setBounds(600, 223, 200, 30);
        dobChooser.setDateFormatString("yyyy-MM-dd");
        add(dobChooser);

        // ✅ Joining Date Calendar
        addLabel("Joining Date", 480, 270);
        joiningDateChooser = new JDateChooser();
        joiningDateChooser.setBounds(600, 273, 200, 30);
        joiningDateChooser.setDateFormatString("yyyy-MM-dd");
        joiningDateChooser.setMinSelectableDate(new java.util.Date()); // optional
        add(joiningDateChooser);

        addLabel("Clinic No.", 480, 320); t12 = addTextField(600, 323);

        // Buttons
        b1 = new JButton("Save");
        b1.setBounds(250, 420, 120, 40);
        b1.setBackground(Color.BLACK);
        b1.setForeground(Color.WHITE);
        b1.addActionListener(this);
        add(b1);

        b2 = new JButton("Cancel");
        b2.setBounds(450, 420, 120, 40);
        b2.setBackground(Color.BLACK);
        b2.setForeground(Color.WHITE);
        b2.addActionListener(this);
        add(b2);

        setVisible(true);
    }

    private JLabel addLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 200, 30);
        label.setFont(new Font("Tahoma", Font.BOLD, 16));
        add(label);
        return label;
    }

    private JTextField addTextField(int x, int y) {
        JTextField t = new JTextField();
        t.setBounds(x, y, 200, 30);
        add(t);
        return t;
    }

    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource() == b1) saveDoctor();
        else setVisible(false);
    }

    private void saveDoctor() {

        String email = t5.getText().trim();

        // ✅ Validation
        if(t1.getText().isEmpty() || t2.getText().isEmpty() || email.isEmpty()
                || dobChooser.getDate() == null || joiningDateChooser.getDate() == null) {

            JOptionPane.showMessageDialog(this, "Please fill all required fields!");
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Invalid Email format!");
            return;
        }

        try (Conn c = new Conn()) {

            java.sql.Date dob = new java.sql.Date(dobChooser.getDate().getTime());
            java.sql.Date joiningDate = new java.sql.Date(joiningDateChooser.getDate().getTime());

            String sql = "INSERT INTO doctors(name, username, password, phone, email, address, gender, dob, blood_group, joining_date, specialization, clinic_no) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";

            PreparedStatement pst = c.getConnection().prepareStatement(sql);

            pst.setString(1, t1.getText());
            pst.setString(2, t2.getText());
            pst.setString(3, t3.getText());
            pst.setString(4, t4.getText());
            pst.setString(5, email);
            pst.setString(6, t6.getText());
            pst.setString(7, t7.getSelectedItem().toString());
            pst.setDate(8, dob);
            pst.setString(9, t9.getSelectedItem().toString());
            pst.setDate(10, joiningDate);
            pst.setString(11, t11.getText());
            pst.setString(12, t12.getText());

            pst.executeUpdate();

            // Login table
            String sqlLogin = "INSERT INTO login(username, password, role) VALUES(?,?,?)";
            PreparedStatement pstL = c.getConnection().prepareStatement(sqlLogin);
            pstL.setString(1, t2.getText());
            pstL.setString(2, t3.getText());
            pstL.setString(3, "doctor");
            pstL.executeUpdate();

            JOptionPane.showMessageDialog(this, "Doctor Added Successfully!");
            setVisible(false);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new addDoctor();
    }
}