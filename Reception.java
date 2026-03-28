package Hospital_management_system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Reception extends JFrame implements ActionListener {

    JMenuItem viewDoctor, viewPatient, viewReceptionist, addPatient, addAppointment, viewAppointment;
    JMenuItem bookRoom, viewRoom, billing;
    JMenuItem myProfile, changePassword, discharge;

    private String username;

    public Reception(int receptionistId, String username) {

        this.username = username;

        setTitle("Reception Panel");
        setSize(1920, 1080);
        setLocationRelativeTo(null);
        setLayout(null);

        // -------- Menu Bar --------
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.BLACK);
        menuBar.setPreferredSize(new Dimension(1980, 40));
        setJMenuBar(menuBar);

        Font menuFont = new Font("Arial", Font.BOLD, 20);
        Font itemFont = new Font("Arial", Font.PLAIN, 16);

        // -------- Menus and Items --------

        // Doctor Menu
        JMenu doctorMenu = new JMenu("Doctor");
        viewDoctor = new JMenuItem("View Doctor");
        doctorMenu.add(viewDoctor);

        // Patient Menu
        JMenu patientMenu = new JMenu("Patient");
        viewPatient = new JMenuItem("View Patient");
        addPatient = new JMenuItem("Add Patient");
        patientMenu.add(viewPatient);
        patientMenu.add(addPatient);

        // Receptionist Menu
        JMenu receptionistMenu = new JMenu("Receptionist");
        viewReceptionist = new JMenuItem("View Receptionist");
        receptionistMenu.add(viewReceptionist);

        // Appointment Menu
        JMenu appointmentMenu = new JMenu("Appointment");
        viewAppointment = new JMenuItem("View Appointment");
        addAppointment = new JMenuItem("Add Appointment");
        appointmentMenu.add(viewAppointment);
        appointmentMenu.add(addAppointment);

        // Room Menu
        JMenu roomMenu = new JMenu("Room");
        viewRoom = new JMenuItem("View Room");
        bookRoom = new JMenuItem("Book Room");
        roomMenu.add(viewRoom);
        roomMenu.add(bookRoom);

        // Billing Menu
        JMenu billingMenu = new JMenu("Billing");
        billing = new JMenuItem("Billing");
        billingMenu.add(billing);

        // Discharge Menu
        JMenu dischargeMenu = new JMenu("Discharge");
        discharge = new JMenuItem("Discharge");
        dischargeMenu.add(discharge);

        // Profile Menu
        JMenu profileMenu = new JMenu("Profile");
        myProfile = new JMenuItem("My Profile");
        changePassword = new JMenuItem("Change Password");
        profileMenu.add(myProfile);
        profileMenu.add(changePassword);

        // Exit Menu
        JMenu exitMenu = new JMenu("Exit");

        // Add menus to menu bar
        menuBar.add(doctorMenu);
        menuBar.add(patientMenu);
        menuBar.add(receptionistMenu);
        menuBar.add(appointmentMenu);
        menuBar.add(roomMenu);
        menuBar.add(billingMenu);
        menuBar.add(dischargeMenu);
        menuBar.add(profileMenu);
        menuBar.add(exitMenu);

        // -------- Apply uniform fonts to all menus and items --------
        setMenuBarFont(menuBar, menuFont, itemFont);

        // -------- Action Listeners --------
        viewDoctor.addActionListener(this);
        viewPatient.addActionListener(this);
        addPatient.addActionListener(this);
        viewReceptionist.addActionListener(this);
        viewAppointment.addActionListener(this);
        addAppointment.addActionListener(this);
        viewRoom.addActionListener(this);
        bookRoom.addActionListener(this);
        billing.addActionListener(this);
        myProfile.addActionListener(this);
        changePassword.addActionListener(this);
        discharge.addActionListener(this);

        exitMenu.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                dispose();
                new index(); // Go back to main page
            }
        });

        // -------- Background Image --------
        JLabel background = new JLabel();
        background.setBounds(0, 0, 1920, 1080);
        background.setLayout(null);
        try {
            ImageIcon img = new ImageIcon(ClassLoader.getSystemResource("icon/image.png"));
            Image image = img.getImage().getScaledInstance(1920, 1080, Image.SCALE_SMOOTH);
            background.setIcon(new ImageIcon(image));
        } catch (Exception e) {
            System.out.println("Background image not found.");
        }
        add(background);

        // Title
        JLabel title = new JLabel("Evergreen Medical");
        title.setBounds(500, 40, 900, 70);
        title.setFont(new Font("Tahoma", Font.BOLD, 60));
        title.setForeground(Color.WHITE);
        background.add(title);

        setVisible(true);
    }

    // -------- Helper Method to Set Fonts --------
    private void setMenuBarFont(JMenuBar menuBar, Font menuFont, Font itemFont) {
        for (int i = 0; i < menuBar.getMenuCount(); i++) {
            JMenu menu = menuBar.getMenu(i);
            if (menu != null) {
                menu.setForeground(menu.getText().equals("Exit") ? Color.RED : Color.WHITE);
                menu.setFont(menuFont);
                for (int j = 0; j < menu.getItemCount(); j++) {
                    JMenuItem item = menu.getItem(j);
                    if (item != null) {
                        item.setFont(itemFont);
                    }
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();

        if (source == addPatient) new addPatient();
        else if (source == viewPatient) new viewPatient(this);
        else if (source == viewDoctor) new viewDoctor(this);
        else if (source == viewReceptionist) new viewReceptionist(this);
        else if (source == addAppointment) new AddAppointment().setVisible(true);
        else if (source == viewAppointment) new viewAppointment(this);
        else if (source == bookRoom) new BookRoom().setVisible(true);
        else if (source == viewRoom) new ViewRoom();
        else if (source == billing) new FinalBilling();
        else if (source == myProfile) new MyProfile(username, "receptionist");
        else if (source == discharge) new DischargePatient();
        else if (source == changePassword) new ChangePassword();
    }

    public static void main(String[] args) {
        new Reception(1, "reception1");
    }
}