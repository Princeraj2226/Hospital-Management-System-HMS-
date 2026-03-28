package Hospital_management_system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ChangePassword extends JFrame implements ActionListener {

    JTextField usernameField;
    JPasswordField oldPassField, newPassField;
    JButton changeBtn, cancelBtn;

    public ChangePassword() {
        setTitle("Change Password");
        setSize(500, 350);
        setLocation(450, 200);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel head = new JLabel("Change Password");
        head.setBounds(140, 20, 300, 30);
        head.setFont(new Font("Tahoma", Font.BOLD, 24));
        add(head);

        addLabel("Username:", 50, 80);
        usernameField = new JTextField();
        usernameField.setBounds(200, 80, 200, 30);
        add(usernameField);

        addLabel("Old Password:", 50, 130);
        oldPassField = new JPasswordField();
        oldPassField.setBounds(200, 130, 200, 30);
        add(oldPassField);

        addLabel("New Password:", 50, 180);
        newPassField = new JPasswordField();
        newPassField.setBounds(200, 180, 200, 30);
        add(newPassField);

        changeBtn = new JButton("Change");
        changeBtn.setBounds(120, 240, 100, 30);
        changeBtn.setBackground(Color.BLACK);
        changeBtn.setForeground(Color.WHITE);
        changeBtn.addActionListener(this);
        add(changeBtn);

        cancelBtn = new JButton("Cancel");
        cancelBtn.setBounds(250, 240, 100, 30);
        cancelBtn.setBackground(Color.BLACK);
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.addActionListener(this);
        add(cancelBtn);

        setVisible(true);
    }

    private void addLabel(String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, 150, 30);
        lbl.setFont(new Font("Tahoma", Font.BOLD, 14));
        add(lbl);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == changeBtn) changePassword();
        else if (ae.getSource() == cancelBtn) setVisible(false);
    }

    private void changePassword() {
        String username = usernameField.getText().trim();
        String oldPass = new String(oldPassField.getPassword()).trim();
        String newPass = new String(newPassField.getPassword()).trim();

        if (username.isEmpty() || oldPass.isEmpty() || newPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        try (Conn c = new Conn()) {

            String checkSql = "SELECT * FROM login WHERE username=? AND password=?";
            try (PreparedStatement pstCheck = c.getConnection().prepareStatement(checkSql)) {
                pstCheck.setString(1, username);
                pstCheck.setString(2, oldPass);

                try (ResultSet rs = pstCheck.executeQuery()) {
                    if (rs.next()) {
                        // Update password
                        String updateSql = "UPDATE login SET password=? WHERE username=?";
                        try (PreparedStatement pstUpdate = c.getConnection().prepareStatement(updateSql)) {
                            pstUpdate.setString(1, newPass);
                            pstUpdate.setString(2, username);
                            pstUpdate.executeUpdate();
                        }
                        JOptionPane.showMessageDialog(this, "Password changed successfully!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid username or old password!");
                    }
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChangePassword::new);
    }
}