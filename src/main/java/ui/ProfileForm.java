package ui;

import javax.swing.*;

import classes.UserProfile;

import java.awt.*;
import java.time.LocalDate;
import service.ProfileService;


// With use of AI

public class ProfileForm extends JFrame {
	public ProfileForm() {
        setTitle("Create User Profile");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(8, 2));

        JTextField nameField = new JTextField();
        JComboBox<String> genderBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        JTextField dobField = new JTextField("YYYY-MM-DD");
        JTextField heightField = new JTextField();
        JTextField weightField = new JTextField();
        

        JButton saveBtn = new JButton("Save");

        add(new JLabel("Name")); add(nameField);
        add(new JLabel("Gender")); add(genderBox);
        add(new JLabel("Date of Birth")); add(dobField);
        add(new JLabel("Height")); add(heightField);
        add(new JLabel("Weight")); add(weightField);
        
        add(new JLabel("")); add(saveBtn);

        saveBtn.addActionListener(e -> {
            UserProfile user = new UserProfile(
                nameField.getText(),
                (String) genderBox.getSelectedItem(),
                LocalDate.parse(dobField.getText()),
                Double.parseDouble(heightField.getText()),
                Double.parseDouble(weightField.getText())
                
            );

            new ProfileService().createProfile(user);
            JOptionPane.showMessageDialog(this, "Profile saved!");
        });

        setVisible(true);
    }
}
