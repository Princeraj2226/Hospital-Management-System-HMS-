package Hospital_management_system;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Admin extends JFrame implements ActionListener {

    JMenu doctorMenu, patientMenu, receptionistMenu, appointmentMenu, profileMenu, exitMenu;
    JMenuItem addDoctors, viewDoctor, addPatient, viewPatient;
    JMenuItem addReceptionist, viewReceptionist;
    JMenuItem addAppointment, viewAppointment;
    JMenuItem myProfile, changePassword;

    private String username;

    public Admin(int adminId, String username) {

        this.username = username;

        setTitle("Admin Panel");
        setSize(1920,1080);
        setLocationRelativeTo(null);
        setLayout(null);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.BLACK);
        menuBar.setPreferredSize(new Dimension(1980,40));
        setJMenuBar(menuBar);

        Font menuFont = new Font("Arial",Font.BOLD,20);
        Font itemFont = new Font("Arial",Font.PLAIN,16);

        // -------- Doctor Menu --------
        doctorMenu = new JMenu("Doctor");
        doctorMenu.setForeground(Color.WHITE);
        menuBar.add(doctorMenu);

        addDoctors = new JMenuItem("Add Doctor");
        viewDoctor = new JMenuItem("View Doctor");

        doctorMenu.add(addDoctors);
        doctorMenu.add(viewDoctor);

        addDoctors.addActionListener(this);
        viewDoctor.addActionListener(this);

        // -------- Patient Menu --------
        patientMenu = new JMenu("Patient");
        patientMenu.setForeground(Color.WHITE);
        menuBar.add(patientMenu);

        addPatient = new JMenuItem("Add Patient");
        viewPatient = new JMenuItem("View Patient");

        patientMenu.add(addPatient);
        patientMenu.add(viewPatient);

        addPatient.addActionListener(this);
        viewPatient.addActionListener(this);

        // -------- Receptionist Menu --------
        receptionistMenu = new JMenu("Receptionist");
        receptionistMenu.setForeground(Color.WHITE);
        menuBar.add(receptionistMenu);

        addReceptionist = new JMenuItem("Add Receptionist");
        viewReceptionist = new JMenuItem("View Receptionist");

        receptionistMenu.add(addReceptionist);
        receptionistMenu.add(viewReceptionist);

        addReceptionist.addActionListener(this);
        viewReceptionist.addActionListener(this);

        // -------- Appointment Menu --------
        appointmentMenu = new JMenu("Appointment");
        appointmentMenu.setForeground(Color.WHITE);
        menuBar.add(appointmentMenu);

        addAppointment = new JMenuItem("Add Appointment");
        viewAppointment = new JMenuItem("View Appointment");

        appointmentMenu.add(addAppointment);
        appointmentMenu.add(viewAppointment);

        addAppointment.addActionListener(this);
        viewAppointment.addActionListener(this);

        // -------- Profile Menu --------
        profileMenu = new JMenu("Profile");
        profileMenu.setForeground(Color.WHITE);
        menuBar.add(profileMenu);

        myProfile = new JMenuItem("My Profile");
        changePassword = new JMenuItem("Change Password");

        profileMenu.add(myProfile);
        profileMenu.add(changePassword);

        myProfile.addActionListener(this);
        changePassword.addActionListener(this);

        // -------- Exit Menu --------
        exitMenu = new JMenu("Exit");
        exitMenu.setForeground(Color.RED);
        menuBar.add(exitMenu);

        exitMenu.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                dispose();
                new index();
            }
        });

        setMenuFont(doctorMenu,menuFont,itemFont);
        setMenuFont(patientMenu,menuFont,itemFont);
        setMenuFont(receptionistMenu,menuFont,itemFont);
        setMenuFont(appointmentMenu,menuFont,itemFont);
        setMenuFont(profileMenu,menuFont,itemFont);
        setMenuFont(exitMenu,menuFont,itemFont);

        // -------- Background --------
        ImageIcon img = new ImageIcon(ClassLoader.getSystemResource("icon/image.png"));
        Image image = img.getImage().getScaledInstance(1920,1080,Image.SCALE_SMOOTH);

        JLabel background = new JLabel(new ImageIcon(image));
        background.setBounds(0,0,1920,1080);
        add(background);

        JLabel title = new JLabel("Evergreen Medical");
        title.setBounds(500,40,900,70);
        title.setFont(new Font("Tahoma",Font.BOLD,60));
        title.setForeground(Color.WHITE);
        background.add(title);

        setVisible(true);
    }

    private void setMenuFont(JMenu menu, Font menuFont, Font itemFont) {

        menu.setFont(menuFont);

        for(int i=0;i<menu.getItemCount();i++) {

            JMenuItem item = menu.getItem(i);

            if(item != null) {
                item.setFont(itemFont);
            }
        }
    }

    public void actionPerformed(ActionEvent ae) {

        Object source = ae.getSource();

        try {

            if(source == addDoctors) {

                new addDoctor();

            } else if(source == viewDoctor) {

                new viewDoctor(this);

            } else if(source == addPatient) {

                new addPatient();

            } else if(source == viewPatient) {

                new viewPatient(this);

            } else if(source == addReceptionist) {

                new addReceptionist();

            } else if(source == viewReceptionist) {

                new viewReceptionist(this);

            } else if(source == addAppointment) {

                AddAppointment appointment = new AddAppointment();
                appointment.setVisible(true);

            } else if(source == viewAppointment) {

                new viewAppointment(this);

            } else if(source == myProfile) {

                new MyProfile(username,"admin");

            } else if(source == changePassword) {

                new ChangePassword();
            }

        } catch(Exception e) {

            e.printStackTrace();
            JOptionPane.showMessageDialog(this,"Error opening window");
        }
    }

    public static void main(String[] args) {

        new Admin(1,"admin1");
    }
}