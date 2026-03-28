package Hospital_management_system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import com.toedter.calendar.JDateChooser;

public class BookRoom extends JFrame implements ActionListener {

    private JComboBox<String> tUsernameCombo;
    private JTextField tName, tEmail, tFather, tPhone, tBlood, tDOB, tAssignedRoom;
    private JComboBox<String> tMaritalStatus, tGender, tRoomType;
    private JDateChooser bookingDateChooser;
    private JButton bBook, bCancel;

    public BookRoom() {
        setTitle("Book Room");
        setSize(900, 600);
        setLocation(270, 190);
        setLayout(null);
        setResizable(false);
        getContentPane().setBackground(Color.WHITE);

        JLabel head = new JLabel("Room Booking Details");
        head.setBounds(250, 20, 400, 30);
        head.setFont(new Font("Tahoma", Font.BOLD, 24));
        add(head);

        int leftX = 20, rightX = 450;

        // --- LEFT SIDE ---
        addLabel("Patient Username", leftX, 80);
        tUsernameCombo = new JComboBox<>();
        tUsernameCombo.setBounds(leftX + 180, 80, 200, 30);
        add(tUsernameCombo);

        loadPatientUsernames();

        tUsernameCombo.addActionListener(e -> {
            String selectedUser = (String) tUsernameCombo.getSelectedItem();
            if (selectedUser != null) fetchPatientDetails(selectedUser);
        });

        addLabel("Full Name", leftX, 130);
        tName = addTextField(leftX + 180, 130); tName.setEditable(false);

        addLabel("Email", leftX, 180);
        tEmail = addTextField(leftX + 180, 180); tEmail.setEditable(false);

        addLabel("Father Name", leftX, 230);
        tFather = addTextField(leftX + 180, 230); tFather.setEditable(false);

        addLabel("Phone No.", leftX, 280);
        tPhone = addTextField(leftX + 180, 280); tPhone.setEditable(false);

        addLabel("Booking Date", leftX, 330);
        bookingDateChooser = new JDateChooser();
        bookingDateChooser.setBounds(leftX + 180, 330, 200, 30);
        bookingDateChooser.setDateFormatString("yyyy-MM-dd");
        bookingDateChooser.setMinSelectableDate(new java.util.Date());
        add(bookingDateChooser);

        addLabel("Assigned Room", leftX, 380);
        tAssignedRoom = addTextField(leftX + 180, 380);
        tAssignedRoom.setEditable(false);

        // --- RIGHT SIDE ---
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

        addLabel("DOB (YYYY-MM-DD)", rightX, 230);
        tDOB = addTextField(rightX + 180, 230); tDOB.setEditable(false);

        addLabel("Room Type", rightX, 280);
        tRoomType = new JComboBox<>(new String[]{"General", "Private", "Operation Theater", "ICU"});
        tRoomType.setBounds(rightX + 180, 280, 200, 30);
        add(tRoomType);

        tRoomType.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED) updateAssignedRoom();
        });

        // Buttons
        bBook = new JButton("Book");
        bBook.setBounds(250, 450, 120, 30);
        bBook.setBackground(Color.BLACK);
        bBook.setForeground(Color.WHITE);
        bBook.addActionListener(this);
        add(bBook);

        bCancel = new JButton("Cancel");
        bCancel.setBounds(400, 450, 120, 30);
        bCancel.setBackground(Color.BLACK);
        bCancel.setForeground(Color.WHITE);
        bCancel.addActionListener(this);
        add(bCancel);

        updateAssignedRoom();
        setVisible(true);
    }

    private JLabel addLabel(String text,int x,int y){
        JLabel l = new JLabel(text);
        l.setBounds(x,y,200,30);
        l.setFont(new Font("Tahoma",Font.BOLD,14));
        add(l);
        return l;
    }

    private JTextField addTextField(int x,int y){
        JTextField t = new JTextField();
        t.setBounds(x,y,200,30);
        t.setBackground(new Color(255,230,204));
        add(t);
        return t;
    }

    private void loadPatientUsernames() {
        try (Conn c = new Conn();
             Statement st = c.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT username FROM patients")) {

            tUsernameCombo.removeAllItems();
            while (rs.next()) {
                tUsernameCombo.addItem(rs.getString("username"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,"Error loading patient usernames");
        }
    }

    private void fetchPatientDetails(String username){
        if(username.isEmpty()) return;

        String sql = "SELECT * FROM patients WHERE username=?";
        try(Conn c = new Conn();
            PreparedStatement pst = c.getConnection().prepareStatement(sql)) {

            pst.setString(1, username);
            try(ResultSet rs = pst.executeQuery()){
                if(rs.next()){
                    tName.setText(rs.getString("name"));
                    tEmail.setText(rs.getString("email"));
                    tFather.setText(rs.getString("father_name"));
                    tPhone.setText(rs.getString("phone"));
                    tBlood.setText(rs.getString("blood_group"));
                    tDOB.setText(rs.getString("dob"));
                    tGender.setSelectedItem(rs.getString("gender"));
                    tMaritalStatus.setSelectedItem(rs.getString("marital_status"));
                } else {
                    JOptionPane.showMessageDialog(this,"Patient not found!");
                    clearPatientFields();
                }
            }
        } catch(Exception ex){ ex.printStackTrace(); }
    }

    private void clearPatientFields(){
        tName.setText(""); tEmail.setText(""); tFather.setText(""); tPhone.setText("");
        tBlood.setText(""); tDOB.setText("");
    }

    private void updateAssignedRoom(){
        String selectedType = tRoomType.getSelectedItem().toString();
        String roomNo = "No available rooms";

        try(Conn c = new Conn();
            PreparedStatement pst = c.getConnection().prepareStatement(
                    "SELECT room_no FROM rooms WHERE booked=0 AND room_type=? LIMIT 1")) {

            pst.setString(1, selectedType);
            try(ResultSet rs = pst.executeQuery()){
                if(rs.next()) roomNo = rs.getString("room_no");
            }

        } catch(Exception e){
            roomNo = "Error loading room";
        }

        tAssignedRoom.setText(roomNo);
    }

    public void actionPerformed(ActionEvent ae){
        if(ae.getSource() == bBook) bookRoom();
        else if(ae.getSource() == bCancel) setVisible(false);
    }

    private void bookRoom(){
        try{
            String username = (String) tUsernameCombo.getSelectedItem();
            java.util.Date selectedDate = bookingDateChooser.getDate();
            if(selectedDate == null){
                JOptionPane.showMessageDialog(this,"Select booking date");
                return;
            }
            java.sql.Date bookingDate = new java.sql.Date(selectedDate.getTime());
            String roomNo = tAssignedRoom.getText();
            String selectedType = tRoomType.getSelectedItem().toString();

            if(roomNo.equals("No available rooms")){
                JOptionPane.showMessageDialog(this,"No rooms available!");
                return;
            }

            try(Conn c = new Conn()) {
                Connection conn = c.getConnection();
                conn.setAutoCommit(false);

                // 1. Insert booking
                String sqlInsert = "INSERT INTO room_booking(patient_username,name,email,room_no,room_type,booking_date,status) VALUES(?,?,?,?,?,?,?)";
                PreparedStatement pstInsert = conn.prepareStatement(sqlInsert);
                pstInsert.setString(1, username);
                pstInsert.setString(2, tName.getText());
                pstInsert.setString(3, tEmail.getText());
                pstInsert.setString(4, roomNo);
                pstInsert.setString(5, selectedType);
                pstInsert.setDate(6, bookingDate);
                pstInsert.setString(7, "Active");
                pstInsert.executeUpdate();

                // 2. Mark room as booked
                String sqlUpdateRoom = "UPDATE rooms SET booked=1 WHERE room_no=? AND booked=0";
                PreparedStatement pstUpdate = conn.prepareStatement(sqlUpdateRoom);
                pstUpdate.setString(1, roomNo);
                int updated = pstUpdate.executeUpdate();

                if(updated == 0){
                    conn.rollback();
                    JOptionPane.showMessageDialog(this,"Room already booked by someone else!");
                    return;
                }

                conn.commit();
                JOptionPane.showMessageDialog(this,"Room booked successfully!");
                updateAssignedRoom();
            }

        } catch(SQLIntegrityConstraintViolationException ex){
            JOptionPane.showMessageDialog(this,"Room already booked (database constraint)!");
        } catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,"Error booking room!");
        }
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(BookRoom::new);
    }
}