package Hospital_management_system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class index extends JFrame implements ActionListener {

    JButton bDoctor, bPatient, bReception, bAdmin;

    public index() {

        setTitle("Index Page");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(null);

        // Background Label
        JLabel bg = new JLabel();
        bg.setBounds(0, 0, 1920, 1080);
        bg.setLayout(null);
        add(bg);

        // ===== Load and Blur Background Image =====
        try {
            ImageIcon bgImg = new ImageIcon(getClass().getResource("/icon/aa.png"));
            Image img = bgImg.getImage();

            // Convert to BufferedImage
            BufferedImage buffered = new BufferedImage(
                    img.getWidth(null),
                    img.getHeight(null),
                    BufferedImage.TYPE_INT_ARGB
            );
            Graphics2D g2 = buffered.createGraphics();
            g2.drawImage(img, 0, 0, null);
            g2.dispose();

            // Apply blur effect (3x3 kernel)
            float[] matrix = {
    1f/9f, 1f/9f, 1f/9f,
    1f/9f, 1f/9f, 1f/9f,
    1f/9f, 1f/9f, 1f/9f
};
            BufferedImageOp op = new ConvolveOp(new Kernel(3, 3, matrix));
            BufferedImage blurred = op.filter(buffered, null);

            // Apply dark overlay AFTER blur
            Graphics2D g3 = blurred.createGraphics();
            g3.setColor(new Color(0, 0, 0, 100)); // adjust alpha for darkness
            g3.fillRect(0, 0, blurred.getWidth(), blurred.getHeight());
            g3.dispose();

            // Scale and set background
            Image finalImg = blurred.getScaledInstance(1920, 1080, Image.SCALE_SMOOTH);
            bg.setIcon(new ImageIcon(finalImg));

        } catch (Exception e) {
            e.printStackTrace();
        }

        // ===== LOGO =====
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/icon/image2.png"));
            Image img1 = icon.getImage().getScaledInstance(300, 80, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(img1));
            imageLabel.setBounds(620, 50, 300, 80); // top-left logo
            bg.add(imageLabel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ===== Slogan =====
        JLabel slogan = new JLabel("Provide Best facility for You");
        slogan.setBounds(600, 150, 700, 40);
        slogan.setFont(new Font("Arial", Font.BOLD, 30));
        slogan.setForeground(Color.BLACK);
        bg.add(slogan);

        // ===== Buttons =====
        bDoctor = createButton("Doctor", 200, 650, bg);
        bPatient = createButton("Patient", 550, 650, bg);
        bReception = createButton("Receptionist", 900, 650, bg);
        bAdmin = createButton("Admin", 1250, 650, bg);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JButton createButton(String text, int x, int y, JLabel parent) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, 180, 50);
        btn.setBackground(Color.BLACK);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(this);
        parent.add(btn);
        return btn;
    }

    public void actionPerformed(ActionEvent ae) {
        JButton src = (JButton) ae.getSource();
        if (src == bDoctor) {
            new DoctorLogin();
        } else if (src == bPatient) {
            new PatientLogin();
        } else if (src == bReception) {
            new ReceptionLogin();
        } else if (src == bAdmin) {
            new AdminLogin();
        }
    }

    public static void main(String[] args) {
        new index();
    }
}