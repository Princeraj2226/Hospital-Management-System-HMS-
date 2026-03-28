package Hospital_management_system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import com.toedter.calendar.JDateChooser;   // ✅ NEW

public class AddAppointment extends JFrame implements ActionListener {

    JTextField tUsername, tName, tEmail, tFather, tPhone, tBlood, tDOB, deceasedNameField;
    JComboBox<String> tMaritalStatus, tGender, tSelectDoctor, tAppointmentTime;
    JCheckBox tDeceased;

    // ✅ Calendar instead of text field
    JDateChooser appointmentDateChooser;

    JButton bSave, bCancel;

    public AddAppointment() {
        setTitle("Add Appointment");
        setSize(1000, 600);
        setLocation(270, 150);
        setLayout(null);
        setResizable(false);
        getContentPane().setBackground(Color.WHITE);

        JLabel head = new JLabel("Add Appointment Details");
        head.setBounds(300, 20, 400, 30);
        head.setFont(new Font("Tahoma", Font.BOLD, 24));
        add(head);

        int leftX = 20, rightX = 500;

        // ----- LEFT SIDE -----
        addLabel("Patient Username", leftX, 80);
        tUsername = addTextField(leftX + 180, 80);
        tUsername.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                fetchPatientDetails(tUsername.getText().trim());
            }
        });

        addLabel("Full Name", leftX, 130);
        tName = addTextField(leftX + 180, 130); tName.setEditable(false);

        addLabel("Email", leftX, 180);
        tEmail = addTextField(leftX + 180, 180); tEmail.setEditable(false);

        addLabel("Father Name", leftX, 230);
        tFather = addTextField(leftX + 180, 230); tFather.setEditable(false);

        addLabel("Phone No.", leftX, 280);
        tPhone = addTextField(leftX + 180, 280); tPhone.setEditable(false);

        // ✅ NEW CALENDAR
        addLabel("Appointment Date", leftX, 330);
        appointmentDateChooser = new JDateChooser();
        appointmentDateChooser.setBounds(leftX + 180, 330, 200, 30);
        appointmentDateChooser.setDateFormatString("yyyy-MM-dd");
        appointmentDateChooser.setMinSelectableDate(new java.util.Date()); // optional
        add(appointmentDateChooser);

        // ----- RIGHT SIDE -----
        addLabel("Marital Status", rightX, 80);
        tMaritalStatus = new JComboBox<>(new String[]{"Single", "Married", "Other"});
        tMaritalStatus.setBounds(rightX + 180, 80, 200, 30);
        tMaritalStatus.setEnabled(false);
        add(tMaritalStatus);

        addLabel("Gender", rightX, 130);
        tGender = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        tGender.setBounds(rightX + 180, 130, 200, 30);
        tGender.setEnabled(false);
        add(tGender);

        addLabel("Blood Group", rightX, 180);
        tBlood = addTextField(rightX + 180, 180); tBlood.setEditable(false);

        addLabel("DOB", rightX, 230);
        tDOB = addTextField(rightX + 180, 230); tDOB.setEditable(false);

        addLabel("Deceased", rightX, 280);
        tDeceased = new JCheckBox("Yes");
        tDeceased.setBounds(rightX + 180, 280, 100, 30);
        tDeceased.setBackground(Color.WHITE);
        add(tDeceased);

        addLabel("Deceased Name", rightX, 320);
        deceasedNameField = addTextField(rightX + 180, 320);
        deceasedNameField.setEditable(false);

        tDeceased.addItemListener(e -> {
            deceasedNameField.setEditable(tDeceased.isSelected());
            if (!tDeceased.isSelected()) deceasedNameField.setText("");
        });

        // ----- SELECTION AREA -----
        addLabel("Select Doctor", leftX, 380);
        tSelectDoctor = new JComboBox<>();
        tSelectDoctor.setBounds(leftX + 180, 380, 200, 30);
        add(tSelectDoctor);

        addLabel("Time Slot", rightX, 380);
        tAppointmentTime = new JComboBox<>();
        tAppointmentTime.setBounds(rightX + 180, 380, 200, 30);
        add(tAppointmentTime);

        loadTimeSlots();
        loadDoctors();

        // ----- BUTTONS -----
        bSave = new JButton("Save Appointment");
        bSave.setBounds(320, 480, 180, 40);
        bSave.setBackground(Color.BLACK);
        bSave.setForeground(Color.WHITE);
        bSave.addActionListener(this);
        add(bSave);

        bCancel = new JButton("Cancel");
        bCancel.setBounds(520, 480, 120, 40);
        bCancel.setBackground(Color.BLACK);
        bCancel.setForeground(Color.WHITE);
        bCancel.addActionListener(this);
        add(bCancel);

        setVisible(true);
    }

    private JLabel addLabel(String text, int x, int y) {
        JLabel l = new JLabel(text);
        l.setBounds(x, y, 200, 30);
        l.setFont(new Font("Tahoma", Font.BOLD, 14));
        add(l);
        return l;
    }

    private JTextField addTextField(int x, int y) {
        JTextField t = new JTextField();
        t.setBounds(x, y, 200, 30);
        t.setBackground(new Color(255, 230, 204));
        add(t);
        return t;
    }

    private void loadDoctors() {
        try (Conn c = new Conn()) {
            ResultSet rs = c.getStatement().executeQuery("SELECT doctor_id, name, specialization FROM doctors");
            tSelectDoctor.removeAllItems();
            while (rs.next()) {
                tSelectDoctor.addItem(rs.getInt("doctor_id") + ": " + rs.getString("name") + " (" + rs.getString("specialization") + ")");
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadTimeSlots() {
        for (int h = 10; h <= 17; h++) {
            tAppointmentTime.addItem(String.format("%02d:00", h));
            tAppointmentTime.addItem(String.format("%02d:30", h));
        }
    }

    private void fetchPatientDetails(String username) {
        if (username.isEmpty()) return;
        try (Conn c = new Conn()) {
            PreparedStatement pst = c.getConnection().prepareStatement("SELECT * FROM patients WHERE username=?");
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                tName.setText(rs.getString("name"));
                tEmail.setText(rs.getString("email"));
                tFather.setText(rs.getString("father_name"));
                tPhone.setText(rs.getString("phone"));
                tBlood.setText(rs.getString("blood_group"));
                tDOB.setText(rs.getString("dob"));
                tGender.setSelectedItem(rs.getString("gender"));
                tMaritalStatus.setSelectedItem(rs.getString("marital_status"));
            } else {
                JOptionPane.showMessageDialog(this, "Patient not found!");
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == bSave) saveAppointment();
        else setVisible(false);
    }

    private void saveAppointment() {
        if (tUsername.getText().isEmpty() || appointmentDateChooser.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Please fill required fields!");
            return;
        }

        try (Conn c = new Conn()) {
            String docData = tSelectDoctor.getSelectedItem().toString();
            int docId = Integer.parseInt(docData.split(":")[0]);

            java.sql.Date appointmentDate = new java.sql.Date(
                    appointmentDateChooser.getDate().getTime()
            );

            String sql = "INSERT INTO appointment(name, email, fathername, phoneno, marital_status, gender, bloodgroup, dob, deceased, deceased_name, doctor_id, appointmentdate, appointmenttime) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

            PreparedStatement pst = c.getConnection().prepareStatement(sql);

            pst.setString(1, tName.getText());
            pst.setString(2, tEmail.getText());
            pst.setString(3, tFather.getText());
            pst.setString(4, tPhone.getText());
            pst.setString(5, tMaritalStatus.getSelectedItem().toString());
            pst.setString(6, tGender.getSelectedItem().toString());
            pst.setString(7, tBlood.getText());
            pst.setDate(8, java.sql.Date.valueOf(tDOB.getText()));
            pst.setBoolean(9, tDeceased.isSelected());
            pst.setString(10, deceasedNameField.getText());
            pst.setInt(11, docId);
            pst.setDate(12, appointmentDate);
            pst.setString(13, tAppointmentTime.getSelectedItem().toString());

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Appointment Booked Successfully!");
            this.dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new AddAppointment();
    }
}