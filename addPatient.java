package Hospital_management_system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import com.toedter.calendar.JDateChooser;   // ✅ NEW

public class addPatient extends JFrame implements ActionListener {

    private JTextField t1, t2, t3, t4, t5, t6, t10;
    private JComboBox<String> genderBox, maritalBox, bloodBox;

    // ✅ Calendar instead of text field
    private JDateChooser dobChooser;

    private JButton b1, b2;

    public addPatient() {
        setTitle("Add Patient Panel");
        setSize(1000, 600);
        setLocation(270, 190);
        setLayout(null);
        setResizable(false);
        getContentPane().setBackground(Color.WHITE);

        JLabel head = new JLabel("Add Patient Details");
        head.setBounds(180, 20, 700, 30);
        head.setFont(new Font("Tahoma", Font.BOLD, 24));
        add(head);

        // Name and Username
        addLabel("Patient Name", 20, 70); t1 = addTextField(200, 73);
        addLabel("Username", 480, 70); t2 = addTextField(600, 73);
        addLabel("Password", 20, 120); t3 = addTextField(200, 123);
        addLabel("Phone No.", 480, 120); t4 = addTextField(600, 123);
        addLabel("Email Id", 20, 170); t5 = addTextField(200, 173);
        addLabel("Address", 480, 170); t6 = addTextField(600, 173);

        // Gender
        addLabel("Gender", 20, 220);
        genderBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        genderBox.setBounds(200, 223, 200, 30);
        add(genderBox);

        // ✅ DOB Calendar
        addLabel("Date of Birth", 480, 220);
        dobChooser = new JDateChooser();
        dobChooser.setBounds(600, 223, 200, 30);
        dobChooser.setDateFormatString("yyyy-MM-dd");
        dobChooser.setMaxSelectableDate(new java.util.Date()); // prevent future DOB
        add(dobChooser);

        // Blood group
        addLabel("Blood Group", 20, 270);
        bloodBox = new JComboBox<>(new String[]{"A+","A-","B+","B-","O+","O-","AB+","AB-"});
        bloodBox.setBounds(200, 273, 200, 30);
        add(bloodBox);

        // Father's Name
        addLabel("Father's Name", 480, 270); t10 = addTextField(600, 273);

        // Marital Status
        addLabel("Marital Status", 20, 320);
        maritalBox = new JComboBox<>(new String[]{"Single", "Married", "Divorced"});
        maritalBox.setBounds(200, 323, 200, 30);
        add(maritalBox);

        // Buttons
        b1 = new JButton("Save");
        b1.setBounds(300, 420, 120, 40);
        b1.setBackground(Color.BLACK);
        b1.setForeground(Color.WHITE);
        b1.addActionListener(this);
        add(b1);

        b2 = new JButton("Cancel");
        b2.setBounds(500, 420, 120, 40);
        b2.setBackground(Color.BLACK);
        b2.setForeground(Color.WHITE);
        b2.addActionListener(this);
        add(b2);

        setVisible(true);
    }

    private JLabel addLabel(String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, 200, 30);
        lbl.setFont(new Font("Tahoma", Font.BOLD, 16));
        add(lbl);
        return lbl;
    }

    private JTextField addTextField(int x, int y) {
        JTextField t = new JTextField();
        t.setBounds(x, y, 200, 30);
        add(t);
        return t;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource() == b1) savePatient();
        else if(ae.getSource() == b2) setVisible(false);
    }

    private void savePatient() {

        if(t1.getText().isEmpty() || t2.getText().isEmpty() || dobChooser.getDate() == null){
            JOptionPane.showMessageDialog(this,"Please fill all required fields and select DOB.");
            return;
        }

        try (Conn c = new Conn()) {

            java.sql.Date dob = new java.sql.Date(dobChooser.getDate().getTime());

            // Insert patient
            String sql = "INSERT INTO patients(name, username, password, phone, email, address, gender, dob, blood_group, father_name, marital_status) VALUES(?,?,?,?,?,?,?,?,?,?,?)";

            try (PreparedStatement pst = c.getConnection().prepareStatement(sql)) {

                pst.setString(1, t1.getText());
                pst.setString(2, t2.getText());
                pst.setString(3, t3.getText());
                pst.setString(4, t4.getText());
                pst.setString(5, t5.getText());
                pst.setString(6, t6.getText());
                pst.setString(7, genderBox.getSelectedItem().toString());
                pst.setDate(8, dob);
                pst.setString(9, bloodBox.getSelectedItem().toString());
                pst.setString(10, t10.getText());
                pst.setString(11, maritalBox.getSelectedItem().toString());

                pst.executeUpdate();
            }

            // Login table
            String sqlLogin = "INSERT INTO login(username, password, role) VALUES(?,?,?)";
            try (PreparedStatement pstLogin = c.getConnection().prepareStatement(sqlLogin)) {
                pstLogin.setString(1, t2.getText());
                pstLogin.setString(2, t3.getText());
                pstLogin.setString(3, "patient");
                pstLogin.executeUpdate();
            }

            JOptionPane.showMessageDialog(this,"Patient added successfully!");
            this.dispose();

        } catch(SQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(this,"Username already exists!");
        } catch(Exception e) {
            JOptionPane.showMessageDialog(this,"Error: "+e.getMessage());
        }
    }

    public static void main(String[] args){
        new addPatient();
    }
}