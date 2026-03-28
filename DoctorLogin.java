package Hospital_management_system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class DoctorLogin extends JFrame implements ActionListener {

    JTextField textField;
    JPasswordField jPasswordField;
    JButton b1, b2;

    public DoctorLogin() {
        setTitle("Doctor Login");
        JLabel nameLabel = new JLabel("Username");
        nameLabel.setBounds(40, 40, 200, 30);
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        add(nameLabel);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(40, 90, 200, 30);
        passwordLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        add(passwordLabel);

        textField = new JTextField();
        textField.setBounds(150, 40, 200, 30);
        textField.setFont(new Font("Tahoma", Font.PLAIN, 15));
        textField.setBackground(new Color(255, 179, 0));
        add(textField);

        jPasswordField = new JPasswordField();
        jPasswordField.setBounds(150, 90, 200, 30);
        jPasswordField.setFont(new Font("Tahoma", Font.PLAIN, 15));
        jPasswordField.setBackground(new Color(255, 179, 0));
        add(jPasswordField);

        b1 = new JButton("Login");
        b1.setBounds(40, 160, 120, 30);
        b1.setFont(new Font("serif", Font.BOLD, 15));
        b1.setBackground(Color.BLACK);
        b1.setForeground(Color.WHITE);
        b1.addActionListener(this);
        add(b1);

        b2 = new JButton("Cancel");
        b2.setBounds(180, 160, 120, 30);
        b2.setFont(new Font("serif", Font.BOLD, 15));
        b2.setBackground(Color.BLACK);
        b2.setForeground(Color.WHITE);
        b2.addActionListener(this);
        add(b2);

        ImageIcon icon = new ImageIcon("icon/image1.png"); // put your image path here
        Image img = icon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(img);

        JLabel imageLabel = new JLabel(scaledIcon);
        imageLabel.setBounds(400, 10, 300, 250); // RIGHT SIDE position
        add(imageLabel);


        getContentPane().setBackground(new Color(109, 164, 170));
        setSize(750, 300);
        setLocation(400, 270);
        setLayout(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == b1) {
            String username = textField.getText().trim();
            String password = new String(jPasswordField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both username and password!");
                return;
            }

            try (Conn c = new Conn()) {
                String sql = "SELECT role, id FROM login WHERE username=? AND password=?";
                try (PreparedStatement pst = c.getConnection().prepareStatement(sql)) {
                    pst.setString(1, username);
                    pst.setString(2, password);

                    try (ResultSet rs = pst.executeQuery()) {
                        if (rs.next()) {
                            String role = rs.getString("role");
                            int doctorId = rs.getInt("id");
                            if ("doctor".equalsIgnoreCase(role)) {
                                new Doctor(doctorId, username); // Open Doctor panel
                                setVisible(false);
                            } else {
                                JOptionPane.showMessageDialog(this, "You are not a doctor!");
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "Invalid username or password!");
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        } else if (e.getSource() == b2) {
            // Close login frame
            this.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DoctorLogin::new);
    }
}