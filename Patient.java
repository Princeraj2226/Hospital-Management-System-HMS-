package Hospital_management_system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Patient extends JFrame implements ActionListener {

    JMenuItem viewDoctor, viewPatient, viewReceptionist, addPatient, addAppointment, viewAppointment;
    JMenuItem myProfile, changePassword;

    
    private String username;    

    public Patient(int patientId, String username) {
        this.username = username;

        setTitle("Patient Panel");
        setSize(1920, 1080);
        setLocationRelativeTo(null);
        setLayout(null);
// menu bar
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.BLACK);
        menuBar.setPreferredSize(new Dimension(1980, 40)); // Taller menu bar
        setJMenuBar(menuBar);

        Font menuFont = new Font("Arial", Font.BOLD, 20);   // Menu titles
        Font itemFont = new Font("Arial", Font.PLAIN, 16);  // Menu items

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
        addAppointment = new JMenuItem("Add Appointment");
        appointmentMenu.add(addAppointment);
        addAppointment.addActionListener(this);

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

        setJMenuBar(menuBar);

        // -------- Apply Fonts to All Menus --------
        setMenuFont(doctorMenu, menuFont, itemFont);
        setMenuFont(patientMenu, menuFont, itemFont);
        setMenuFont(receptionistMenu, menuFont, itemFont);
        setMenuFont(appointmentMenu, menuFont, itemFont);
        setMenuFont(profileMenu, menuFont, itemFont);
        setMenuFont(exitMenu, menuFont, itemFont);


        // Background image
        ImageIcon img = new ImageIcon(ClassLoader.getSystemResource("icon/image.png"));
        Image image = img.getImage().getScaledInstance(1920, 1080, Image.SCALE_SMOOTH);
        JLabel background = new JLabel(new ImageIcon(image));
        background.setBounds(0, 0, 1920, 1080);
        add(background);

        // Title on background
        JLabel title = new JLabel("Evergreen Medical");
        title.setBounds(500,40,900,70);
        title.setFont(new Font("Tahoma",Font.BOLD,60));
        title.setForeground(Color.WHITE);
        background.add(title);

        setVisible(true);
    }

    
        // -------- Helper Method to Set Menu Fonts --------
    private void setMenuFont(JMenu menu, Font menuFont, Font itemFont) {
        menu.setFont(menuFont);
        for (int i = 0; i < menu.getItemCount(); i++) {
            JMenuItem item = menu.getItem(i);
            if (item != null) {
                item.setFont(itemFont);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();

       
        if(source == viewPatient) {
            new viewPatient(this);
        } else if(source == viewDoctor) {
            new viewDoctor(this);
        } else if(source == viewReceptionist) {
            new viewReceptionist(this);
        } else if(source == addAppointment) {
                AddAppointment appointment = new AddAppointment();
                appointment.setVisible(true);
            }else if(source == viewAppointment) {
            new viewAppointment(this);    // Show only this patient's appointments
        } else if(source == myProfile) {
            new MyProfile(username, "patient");      // Show patient profile
        } else if(source == changePassword) {
            new ChangePassword();
        }
    }

    public static void main(String[] args) {
        // Test patient panel: patientId = 1, username = "patient1"
        new Patient(1, "patient1");
    }
}