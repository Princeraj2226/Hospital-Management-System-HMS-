package Hospital_management_system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ViewRoom extends JFrame {

    JTable table;
    DefaultTableModel model;

    public ViewRoom() {
        setTitle("View Rooms");
        setSize(800,500);
        setLocation(250,150);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        JLabel head = new JLabel("Room Details", JLabel.CENTER);
        head.setFont(new Font("Tahoma", Font.BOLD, 24));
        head.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        add(head, BorderLayout.NORTH);

        // Table columns
        String[] columns = {"Room No", "Room Type", "Floor", "Booked"};
        model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);

        table = new JTable(model);
        table.setRowHeight(25);
        table.setFont(new Font("Tahoma", Font.PLAIN, 14));
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        loadRooms();

        setVisible(true);
    }

    private void loadRooms(){
        model.setRowCount(0); // Clear existing rows

        String sql = "SELECT room_no, room_type, floor, booked FROM rooms";
        try (Conn c = new Conn();
             ResultSet rs = c.getStatement().executeQuery(sql)) {

            while(rs.next()){
                String roomNo = rs.getString("room_no");
                String roomType = rs.getString("room_type");
                String floor = rs.getString("floor");
                boolean booked = rs.getBoolean("booked");

                model.addRow(new Object[]{roomNo, roomType, floor, booked ? "Yes" : "No"});
            }

        } catch(Exception e){
            JOptionPane.showMessageDialog(this,"Error loading rooms: "+e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(ViewRoom::new);
    }
}