package Hospital_management_system;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class MyProfile extends JFrame {

    private final String username;
    private final String role;
    private final JTextArea profileArea;

    public MyProfile(String username, String role) {
        this.username = username;
        this.role = role;

        setTitle("My Profile");
        setSize(600, 500);
        setLocation(450, 190);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("My Profile", JLabel.CENTER);
        title.setFont(new Font("Tahoma", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        profileArea = new JTextArea();
        profileArea.setEditable(false);
        profileArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(new JScrollPane(profileArea), BorderLayout.CENTER);

        loadProfile();

        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void loadProfile() {
        String sql;
        switch (role.toLowerCase()) {
            case "doctor": sql = "SELECT * FROM doctors WHERE username=?"; break;
            case "patient": sql = "SELECT * FROM patients WHERE username=?"; break;
            case "receptionist": sql = "SELECT * FROM receptionists WHERE username=?"; break;
            case "admin": sql = "SELECT * FROM login WHERE username=?"; break;
            default:
                JOptionPane.showMessageDialog(this, "Invalid role!");
                return;
        }

        try (Conn c = new Conn();
             PreparedStatement pst = c.getConnection().prepareStatement(sql)) {

            pst.setString(1, username);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    StringBuilder sb = new StringBuilder();
                    ResultSetMetaData meta = rs.getMetaData();
                    int colCount = meta.getColumnCount();
                    for (int i = 1; i <= colCount; i++) {
                        sb.append(String.format("%-20s: %s%n", meta.getColumnName(i), rs.getString(i)));
                    }
                    profileArea.setText(sb.toString());
                } else {
                    profileArea.setText("Profile not found!");
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading profile: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MyProfile("sampleUser", "doctor"));
    }
}