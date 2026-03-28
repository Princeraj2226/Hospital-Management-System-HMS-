package Hospital_management_system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class PatientLogin extends JFrame implements ActionListener {

    private final JTextField textField;
    private final JPasswordField jPasswordField;
    private final JButton b1, b2, b3;

    public PatientLogin() {
        setTitle("Patient Login");

        // Username label & field
        JLabel nameLabel = new JLabel("Username");
        nameLabel.setBounds(40, 20, 200, 30);
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        add(nameLabel);

        textField = new JTextField();
        textField.setBounds(150, 20, 200, 30);
        textField.setFont(new Font("Tahoma", Font.PLAIN, 15));
        textField.setBackground(new Color(255, 179, 0));
        add(textField);

        // Password label & field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(40, 70, 200, 30);
        passwordLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        add(passwordLabel);

        jPasswordField = new JPasswordField();
        jPasswordField.setBounds(150, 70, 200, 30);
        jPasswordField.setFont(new Font("Tahoma", Font.PLAIN, 15));
        jPasswordField.setBackground(new Color(255, 179, 0));
        add(jPasswordField);

        // Buttons
        b1 = new JButton("Login");
        b1.setBounds(40, 140, 120, 30);
        b1.setFont(new Font("Serif", Font.BOLD, 15));
        b1.setBackground(Color.BLACK);
        b1.setForeground(Color.WHITE);
        b1.addActionListener(this);
        add(b1);

        b2 = new JButton("Cancel");
        b2.setBounds(180, 140, 120, 30);
        b2.setFont(new Font("Serif", Font.BOLD, 15));
        b2.setBackground(Color.BLACK);
        b2.setForeground(Color.WHITE);
        b2.addActionListener(this);
        add(b2);

        b3 = new JButton("Sign up");
        b3.setBounds(120, 190, 120, 30);
        b3.setFont(new Font("Serif", Font.BOLD, 15));
        b3.setBackground(Color.BLUE);
        b3.setForeground(Color.WHITE);
        b3.addActionListener(this);
        add(b3);

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
            loginPatient();
        } else if (e.getSource() == b2) {
            // Close login frame
            this.dispose();
        }else if (e.getSource() == b3) {
            setVisible(false);
            new addPatient(); // Open sign-up page
        }
    }

    private void loginPatient() {
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
                    int patientId = rs.getInt("id");

                    if ("patient".equalsIgnoreCase(role)) {
                        new Patient(patientId, username); // Open patient panel
                        setVisible(false);
                    } else {
                        JOptionPane.showMessageDialog(this, "You are not a patient!");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password!");
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PatientLogin::new);
    }
}