package ui;

import service.MealLoggingService;

import javax.swing.*;

import classes.Meal;

import java.awt.*;
import java.time.LocalDate;

public class MealLoggingForm2 extends JFrame {
    private JTextField userIdField = new JTextField();
    private JComboBox<String> mealTypeBox = new JComboBox<>(new String[]{"breakfast", "lunch", "dinner", "snack"});
    private JTextField dateField = new JTextField("YYYY-MM-DD");

    private JTextField ingredientField = new JTextField();
    private JTextField quantityField = new JTextField();

    private JButton submitButton = new JButton("Log Meal");

    public MealLoggingForm2() {
        setTitle("Meal Logging Form (Simplified)");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2));

        add(new JLabel("User ID:")); add(userIdField);
        add(new JLabel("Meal Type:")); add(mealTypeBox);
        add(new JLabel("Date (YYYY-MM-DD):")); add(dateField);

        add(new JLabel("Ingredient:")); add(ingredientField);
        add(new JLabel("Quantity (e.g., 100ml):")); add(quantityField);

        add(new JLabel("")); add(submitButton);

        submitButton.addActionListener(e -> {
            try {
                int userId = Integer.parseInt(userIdField.getText().trim());
                String mealType = mealTypeBox.getSelectedItem().toString();
                LocalDate date = LocalDate.parse(dateField.getText().trim());
                String ingredient = ingredientField.getText().trim();
                String quantity = quantityField.getText().trim();

                Meal meal = new Meal(userId, mealType, date, ingredient, quantity);
                MealLoggingService service = new MealLoggingService();
                boolean success = service.logMeal(meal);

                if (success) {
                    JOptionPane.showMessageDialog(this, "Meal logged successfully!");

                    // Clear all fields
                    userIdField.setText("");
                    mealTypeBox.setSelectedIndex(0);
                    dateField.setText("YYYY-MM-DD");
                    ingredientField.setText("");
                    quantityField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this,
                            "You have already logged this meal type for the selected day.",
                            "Duplicate Meal", JOptionPane.WARNING_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid user ID format.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new MealLoggingForm2();
    }
}
