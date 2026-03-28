package Hospital_management_system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import com.toedter.calendar.JDateChooser;   // ✅ NEW

public class addReceptionist extends JFrame implements ActionListener {

    JTextField t1, t2, t3, t4, t5, t6, t9, t11;
    JComboBox<String> cbGender, cbMarital;

    // ✅ Calendar components
    JDateChooser dobChooser, joiningDateChooser;

    JButton b1, b2;

    public addReceptionist() {
        setTitle("Add Receptionist Panel");
        setSize(1000, 600);
        setLocation(270, 190);
        setLayout(null);
        setResizable(false);
        getContentPane().setBackground(Color.WHITE);

        JLabel head = new JLabel("Add Receptionist Details");
        head.setBounds(180, 20, 700, 30);
        head.setFont(new Font("Tahoma", Font.BOLD, 24));
        add(head);

        // Column 1
        addLabel("Receptionist Name", 20, 70); t1 = addTextField(200, 73);
        addLabel("Password", 20, 120); t3 = addTextField(200, 123);
        addLabel("Email Id", 20, 170); t5 = addTextField(200, 173);

        addLabel("Gender", 20, 220);
        cbGender = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        cbGender.setBounds(200, 223, 200, 30);
        add(cbGender);

        addLabel("Blood Group", 20, 270); t9 = addTextField(200, 273);
        addLabel("Qualification", 20, 320); t11 = addTextField(200, 323);

        // Column 2
        addLabel("Username", 480, 70); t2 = addTextField(600, 73);
        addLabel("Phone No.", 480, 120); t4 = addTextField(600, 123);
        addLabel("Address", 480, 170); t6 = addTextField(600, 173);

        // ✅ DOB Calendar
        addLabel("Date of Birth", 480, 220);
        dobChooser = new JDateChooser();
        dobChooser.setBounds(600, 223, 200, 30);
        dobChooser.setDateFormatString("yyyy-MM-dd");
        dobChooser.setMaxSelectableDate(new java.util.Date()); // no future DOB
        add(dobChooser);

        // ✅ Joining Date Calendar
        addLabel("Joining Date", 480, 270);
        joiningDateChooser = new JDateChooser();
        joiningDateChooser.setBounds(600, 273, 200, 30);
        joiningDateChooser.setDateFormatString("yyyy-MM-dd");
        joiningDateChooser.setMinSelectableDate(new java.util.Date()); // no past joining
        add(joiningDateChooser);

        addLabel("Marital Status", 480, 320);
        cbMarital = new JComboBox<>(new String[]{"Single", "Married", "Divorced", "Widowed"});
        cbMarital.setBounds(600, 323, 200, 30);
        add(cbMarital);

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
        lbl.setBounds(x, y, 250, 30);
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
        if(ae.getSource() == b1) saveReceptionist();
        else setVisible(false);
    }

    private void saveReceptionist() {

        if(t1.getText().isEmpty() || t2.getText().isEmpty()
                || dobChooser.getDate() == null || joiningDateChooser.getDate() == null){

            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        try (Conn c = new Conn()) {

            // Check username
            String checkSql = "SELECT username FROM login WHERE username=?";
            try (PreparedStatement checkUser = c.getConnection().prepareStatement(checkSql)) {
                checkUser.setString(1, t2.getText());
                ResultSet rs = checkUser.executeQuery();
                if(rs.next()){
                    JOptionPane.showMessageDialog(this, "Username already exists!");
                    return;
                }
            }

            java.sql.Date dob = new java.sql.Date(dobChooser.getDate().getTime());
            java.sql.Date joiningDate = new java.sql.Date(joiningDateChooser.getDate().getTime());

            // Insert receptionist
            String sql = "INSERT INTO receptionists(name,username,password,phone,email,address,gender,dob,blood_group,joining_date,qualification,marital_status) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";

            PreparedStatement pst = c.getConnection().prepareStatement(sql);

            pst.setString(1, t1.getText());
            pst.setString(2, t2.getText());
            pst.setString(3, t3.getText());
            pst.setString(4, t4.getText());
            pst.setString(5, t5.getText());
            pst.setString(6, t6.getText());
            pst.setString(7, (String) cbGender.getSelectedItem());
            pst.setDate(8, dob);
            pst.setString(9, t9.getText());
            pst.setDate(10, joiningDate);
            pst.setString(11, t11.getText());
            pst.setString(12, (String) cbMarital.getSelectedItem());

            pst.executeUpdate();

            // Login table
            String sqlLogin = "INSERT INTO login(username,password,role) VALUES(?,?,?)";
            PreparedStatement pstLogin = c.getConnection().prepareStatement(sqlLogin);
            pstLogin.setString(1, t2.getText());
            pstLogin.setString(2, t3.getText());
            pstLogin.setString(3, "receptionist");
            pstLogin.executeUpdate();

            JOptionPane.showMessageDialog(this, "Receptionist added successfully!");
            setVisible(false);

        } catch(Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    public static void main(String[] args){
        new addReceptionist();
    }
}