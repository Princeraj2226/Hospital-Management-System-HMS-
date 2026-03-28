package Hospital_management_system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class DischargePatient extends JFrame implements ActionListener {

    JComboBox<String> tSelectPatient;
    JTextField tName, tEmail, tPhone, tGender, tBlood, tRoomNo, tRoomType, tBookingDate;
    JButton bDischarge, bCancel;

    public DischargePatient() {
        setTitle("Discharge Patient");
        setSize(1000,550);
        setLocation(250,150);
        setLayout(null);
        setResizable(false);
        getContentPane().setBackground(Color.WHITE);

        JLabel head = new JLabel("Discharge Patient");
        head.setBounds(350,20,400,30);
        head.setFont(new Font("Tahoma",Font.BOLD,26));
        add(head);

        int leftX = 50, rightX = 550;
        int labelWidth = 180, fieldWidth = 200, height = 30;

        // Patient Selection
        addLabel("Select Patient", leftX, 80, labelWidth, height);
        tSelectPatient = new JComboBox<>();
        tSelectPatient.setBounds(leftX + labelWidth, 80, fieldWidth, height);
        add(tSelectPatient);

        tSelectPatient.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED){
                loadPatientDetails(tSelectPatient.getSelectedItem().toString());
            }
        });

        // Left column fields
        addLabel("Name", leftX, 150, labelWidth, height);
        tName = addTextField(leftX + labelWidth, 150, fieldWidth, height); tName.setEditable(false);

        addLabel("Email", leftX, 200, labelWidth, height);
        tEmail = addTextField(leftX + labelWidth, 200, fieldWidth, height); tEmail.setEditable(false);

        addLabel("Phone", leftX, 250, labelWidth, height);
        tPhone = addTextField(leftX + labelWidth, 250, fieldWidth, height); tPhone.setEditable(false);

        addLabel("Gender", leftX, 300, labelWidth, height);
        tGender = addTextField(leftX + labelWidth, 300, fieldWidth, height); tGender.setEditable(false);

        addLabel("Blood Group", leftX, 350, labelWidth, height);
        tBlood = addTextField(leftX + labelWidth, 350, fieldWidth, height); tBlood.setEditable(false);

        // Right column fields
        addLabel("Room No", rightX, 150, labelWidth, height);
        tRoomNo = addTextField(rightX + labelWidth, 150, fieldWidth, height); tRoomNo.setEditable(false);

        addLabel("Room Type", rightX, 200, labelWidth, height);
        tRoomType = addTextField(rightX + labelWidth, 200, fieldWidth, height); tRoomType.setEditable(false);

        addLabel("Booking Date", rightX, 250, labelWidth, height);
        tBookingDate = addTextField(rightX + labelWidth, 250, fieldWidth, height); tBookingDate.setEditable(false);

        // Buttons
        bDischarge = new JButton("Discharge");
        bDischarge.setBounds(350, 450, 120, 35);
        bDischarge.setBackground(Color.BLACK); bDischarge.setForeground(Color.WHITE);
        bDischarge.addActionListener(this); add(bDischarge);

        bCancel = new JButton("Cancel");
        bCancel.setBounds(500, 450, 120, 35);
        bCancel.setBackground(Color.BLACK); bCancel.setForeground(Color.WHITE);
        bCancel.addActionListener(this); add(bCancel);

        loadPatients();
        setVisible(true);
    }

    private JLabel addLabel(String text,int x,int y,int w,int h){
        JLabel l = new JLabel(text);
        l.setBounds(x,y,w,h);
        l.setFont(new Font("Tahoma",Font.BOLD,14));
        add(l);
        return l;
    }

    private JTextField addTextField(int x,int y,int w,int h){
        JTextField t = new JTextField();
        t.setBounds(x,y,w,h);
        t.setBackground(new Color(255,230,204));
        add(t);
        return t;
    }

    private void loadPatients(){
        tSelectPatient.removeAllItems();
        try(Conn c = new Conn();
            ResultSet rs = c.getStatement().executeQuery("SELECT patient_username FROM room_booking")) {
            while(rs.next()){
                tSelectPatient.addItem(rs.getString("patient_username"));
            }
        } catch(Exception e){ e.printStackTrace(); }
    }

    private void loadPatientDetails(String username){
        if(username == null || username.isEmpty()) return;

        try(Conn c = new Conn();
            PreparedStatement pst = c.getConnection().prepareStatement(
                    "SELECT rb.*, p.phone, p.gender, p.blood_group " +
                    "FROM room_booking rb JOIN patients p ON rb.patient_username=p.username " +
                    "WHERE rb.patient_username=?")) {
            pst.setString(1, username);
            try(ResultSet rs = pst.executeQuery()){
                if(rs.next()){
                    tName.setText(rs.getString("name"));
                    tEmail.setText(rs.getString("email"));
                    tPhone.setText(rs.getString("phone"));
                    tGender.setText(rs.getString("gender"));
                    tBlood.setText(rs.getString("blood_group"));
                    tRoomNo.setText(rs.getString("room_no"));
                    tRoomType.setText(rs.getString("room_type"));
                    tBookingDate.setText(rs.getString("booking_date"));
                }
            }
        } catch(Exception e){ e.printStackTrace(); }
    }

    @Override
    public void actionPerformed(ActionEvent ae){
        if(ae.getSource() == bDischarge){
            dischargePatient();
        } else if(ae.getSource() == bCancel){
            setVisible(false);
        }
    }

    private void dischargePatient(){
        String username = tSelectPatient.getSelectedItem().toString();
        String roomNo = tRoomNo.getText();

        if(username.isEmpty() || roomNo.isEmpty()){
            JOptionPane.showMessageDialog(this,"Select a patient to discharge!");
            return;
        }

        try{
            // Delete booking
            try(Conn c = new Conn();
                PreparedStatement pst = c.getConnection().prepareStatement(
                        "DELETE FROM room_booking WHERE patient_username=?")) {
                pst.setString(1, username);
                pst.executeUpdate();
            }

            // Free room
            try(Conn c = new Conn();
                PreparedStatement pst = c.getConnection().prepareStatement(
                        "UPDATE rooms SET booked=0 WHERE room_no=?")) {
                pst.setString(1, roomNo);
                pst.executeUpdate();
            }

            // Delete all payments of this patient
            try(Conn c = new Conn();
                PreparedStatement pst = c.getConnection().prepareStatement(
                        "DELETE FROM billing WHERE patient_username=?")) {
                pst.setString(1, username);
                pst.executeUpdate();
            }

            JOptionPane.showMessageDialog(this,"Patient discharged successfully! All payments deleted.");

            // Clear fields and reload patient list
            tName.setText(""); tEmail.setText(""); tPhone.setText("");
            tGender.setText(""); tBlood.setText(""); tRoomNo.setText("");
            tRoomType.setText(""); tBookingDate.setText("");
            loadPatients();

        } catch(Exception e){
            JOptionPane.showMessageDialog(this,"Error: "+e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(DischargePatient::new);
    }
}