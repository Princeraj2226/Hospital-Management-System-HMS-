package Hospital_management_system;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Doctor extends JFrame implements ActionListener {

    JMenuItem viewDoctor, viewPatient, viewReceptionist, viewAppointment;
    JMenuItem myProfile, changePassword;

    // private int doctorId;
    private String username;

    public Doctor(int doctorId, String username) {
        // this.doctorId = doctorId;
        this.username = username;

        setTitle("Doctor Panel");
        setSize(1920, 1080);
        setLocationRelativeTo(null);
        setLayout(null);

        // Menu bar
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.BLACK);
        menuBar.setPreferredSize(new Dimension(1980, 40));
        setJMenuBar(menuBar);

        Font menuFont = new Font("Arial", Font.BOLD, 20);
        Font itemFont = new Font("Arial", Font.PLAIN, 16);

        // Doctor Menu
        JMenu doctorMenu = new JMenu("Doctor");
        doctorMenu.setForeground(Color.WHITE);
        menuBar.add(doctorMenu);

        viewDoctor = new JMenuItem("View Doctor");
        doctorMenu.add(viewDoctor);
        viewDoctor.addActionListener(this);

        // Patient Menu
        JMenu patientMenu = new JMenu("Patient");
        patientMenu.setForeground(Color.WHITE);
        menuBar.add(patientMenu);

        viewPatient = new JMenuItem("View Patient");
        patientMenu.add(viewPatient);
        viewPatient.addActionListener(this);

        // Receptionist Menu
        JMenu receptionistMenu = new JMenu("Receptionist");
        receptionistMenu.setForeground(Color.WHITE);
        menuBar.add(receptionistMenu);

        viewReceptionist = new JMenuItem("View Receptionist");
        receptionistMenu.add(viewReceptionist);
        viewReceptionist.addActionListener(this);

        // Appointment Menu
        JMenu appointmentMenu = new JMenu("Appointment");
        appointmentMenu.setForeground(Color.WHITE);
        menuBar.add(appointmentMenu);

        viewAppointment = new JMenuItem("View Appointment");
        appointmentMenu.add(viewAppointment);
        viewAppointment.addActionListener(this);

        // Profile Menu
        JMenu profileMenu = new JMenu("Profile");
        profileMenu.setForeground(Color.WHITE);
        menuBar.add(profileMenu);

        myProfile = new JMenuItem("My Profile");
        changePassword = new JMenuItem("Change Password");

        profileMenu.add(myProfile);
        profileMenu.add(changePassword);

        myProfile.addActionListener(this);
        changePassword.addActionListener(this);

        // Exit Menu
        JMenu exitMenu = new JMenu("Exit");
        exitMenu.setForeground(Color.RED);
        menuBar.add(exitMenu);

        exitMenu.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                dispose();
                new index();
            }
        });

        // Apply fonts
        setMenuFont(doctorMenu, menuFont, itemFont);
        setMenuFont(patientMenu, menuFont, itemFont);
        setMenuFont(receptionistMenu, menuFont, itemFont);
        setMenuFont(appointmentMenu, menuFont, itemFont);
        setMenuFont(profileMenu, menuFont, itemFont);
        setMenuFont(exitMenu, menuFont, itemFont);

        // Background
        JLabel background = new JLabel(new ImageIcon(ClassLoader.getSystemResource("icon/image.png")));
        background.setBounds(0, 0, 1920, 1080);
        add(background);

        JLabel titleLabel = new JLabel("Evergreen Medical");
        titleLabel.setBounds(500, 40, 900, 70);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 60));
        titleLabel.setForeground(Color.WHITE);
        background.add(titleLabel);

        setVisible(true);
    }

    private void setMenuFont(JMenu menu, Font menuFont, Font itemFont) {
        menu.setFont(menuFont);
        for (int i = 0; i < menu.getItemCount(); i++) {
            JMenuItem item = menu.getItem(i);
            if (item != null) item.setFont(itemFont);
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();

        if (source == viewPatient)
            new viewPatient(this);
        else if (source == viewDoctor)
            new viewDoctor(this);
        else if (source == viewReceptionist)
            new viewReceptionist(this);
        else if (source == viewAppointment)
            new DoctorAppointment(username); // now valid constructor
        else if (source == myProfile)
            new MyProfile(username, "doctor");
        else if (source == changePassword)
            new ChangePassword();
    }

    public static void main(String[] args) {
        new Doctor(1, "doctor1"); // Example: doctor_id=1
    }
}