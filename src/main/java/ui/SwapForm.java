package ui;

import service.FoodSwapService;
import dao.MealDAO;
import dao.FoodSwapDAO;
import factory.*;
import observer.*;
import decorator.*;

import javax.swing.*;

import classes.SwapGoal;
import classes.SwapLoggedEvent;
import classes.SwapSuggestion;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.*;

public class SwapForm extends JFrame {
    private JComboBox<String> userBox;
    private JComboBox<String> dateBox;
    private JComboBox<String> mealBox;
    private JComboBox<String> nutrientBox;
    private JComboBox<String> directionBox;
    private JComboBox<String> percentageBox;
    private JButton swapButton;
    private JButton logButton;
    private JTextArea resultArea;
    private JTextArea caloArea;
    private JCheckBox showCaloriesBox;

    private FoodSwapService service = new FoodSwapService();
    private MealDAO mealDAO = new MealDAO();
    private FoodSwapDAO foodSwapDAO = new FoodSwapDAO();
    
    private final SwapGoalFactory swapGoalFactory = new DefaultSwapGoalFactory();
   
    // observer: to register listeners
    private final List<SwapEventListener> listeners = new ArrayList<>();
    
    
    private SwapSuggestion latestSuggestion;
    private String latestNutrient;

    public SwapForm() {
        setTitle("Precise Food Swap");
        setSize(600, 450);
        setLayout(new GridLayout(10, 2));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        

        // Initialize components
        userBox = new JComboBox<>();
        dateBox = new JComboBox<>();
        mealBox = new JComboBox<>();
        nutrientBox = new JComboBox<>(new String[]{"carbohydrate", "protein", "fibre"});
        directionBox = new JComboBox<>(new String[]{"INCREASE", "DECREASE"});
        percentageBox = new JComboBox<>(new String[]{"10", "25"});
        swapButton = new JButton("Swap");
        logButton = new JButton("Log Swap");
        // for decorator
        showCaloriesBox = new JCheckBox("Show Calorie Impact");
        resultArea = new JTextArea(6, 30);
        
        // for decorator CalorieImpact display
        caloArea = new JTextArea(6, 30);
        caloArea.setLineWrap(true);
        caloArea.setWrapStyleWord(true);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);

        // Add components to layout
        add(new JLabel("Select User:")); add(userBox);
        add(new JLabel("Select Date:")); add(dateBox);
        add(new JLabel("Select Meal Type:")); add(mealBox);
        add(new JLabel("Nutrient to Adjust:")); add(nutrientBox);
        add(new JLabel("Direction:")); add(directionBox);
        add(new JLabel("Percentage:")); add(percentageBox);
        add(swapButton); add(logButton);
        add(new JLabel("Suggestion:")); add(new JScrollPane(resultArea));
        // for decorator pattern
        add(showCaloriesBox); add(new JLabel("")); // align grid
      
        add(new JLabel("Calorie Impact:")); add(new JScrollPane(caloArea));

        populateUsers();

        // Add event listeners
        userBox.addActionListener(this::onUserSelected);
        dateBox.addActionListener(this::onDateSelected);
        swapButton.addActionListener(e -> handleSwap());
        logButton.addActionListener(e -> handleLogSwap());

        setLocationRelativeTo(null);
        setVisible(true);
    }
    // observer DESIGN PATTERN
    public void addSwapEventListener(SwapEventListener listener) {
        listeners.add(listener);
    }
    
    private void fireSwapLoggedEvent(SwapSuggestion suggestion, String nutrientName) {
        SwapLoggedEvent event = new SwapLoggedEvent(suggestion, nutrientName);
        for (SwapEventListener listener : listeners) {
            listener.onSwapLogged(event);
        }
    }

    private void populateUsers() {
        userBox.removeAllItems();
        for (Integer id : mealDAO.getAllUserIds()) {
            userBox.addItem(String.valueOf(id));
        }
    }

    private void onUserSelected(ActionEvent e) {
        String userId = (String) userBox.getSelectedItem();
        if (userId != null) {
            dateBox.removeAllItems();
            List<String> dates = mealDAO.getDatesForUser(Integer.parseInt(userId));
            for (String d : dates) dateBox.addItem(d);
        }
    }

    private void onDateSelected(ActionEvent e) {
        String userId = (String) userBox.getSelectedItem();
        String date = (String) dateBox.getSelectedItem();
        if (userId != null && date != null) {
            mealBox.removeAllItems();
            List<String> types = mealDAO.getMealTypesForUserDate(Integer.parseInt(userId), date);
            for (String m : types) mealBox.addItem(m);
        }
    }

    private void handleSwap() {
        try {
            int userId = Integer.parseInt((String) userBox.getSelectedItem());
            String date = (String) dateBox.getSelectedItem();
            String mealType = (String) mealBox.getSelectedItem();

            String nutrient = (String) nutrientBox.getSelectedItem();
            String direction = (String) directionBox.getSelectedItem();
            int percentage = Integer.parseInt((String) percentageBox.getSelectedItem());
            
            // below is before using factory pattern
            //SwapGoal goal = new SwapGoal(SwapGoal.NutrientType.valueOf(nutrient.toUpperCase()), SwapGoal.GoalDirection.valueOf(direction), percentage);
            
            // using factory DESIGN PATTERN                string    string     in
            SwapGoal goal = swapGoalFactory.createSwapGoal(nutrient, direction, percentage);

            SwapSuggestion suggestion = service.getSwapSuggestion(userId, date, mealType, goal);
            if (suggestion != null) {
                String measure = foodSwapDAO.getMeasureDescription(suggestion.getOriginalFood(), userId, date, mealType);
                double after = foodSwapDAO.getRecommendedNutrientAmount(suggestion.getRecommendedFood(), measure, nutrient);

                latestSuggestion = suggestion;
                latestNutrient = nutrient;

                resultArea.setText("Original: " + suggestion.getOriginalFood() +
                        "\nSuggested: " + suggestion.getRecommendedFood() +
                        "\n" + nutrient + " before: " + suggestion.getOriginalValue() +
                        "\n" + nutrient + " after: " + after);
                if (showCaloriesBox.isSelected()) {
                	CalorieImpactDecorator decorated = new CalorieImpactDecorator(suggestion, userId, date, mealType);
                    caloArea.append("\n\n" + decorated.getDecoratedInfo());
                }
            } else {
                resultArea.setText("⚠ No suitable swap found for your goal.");
            }
        } catch (Exception ex) {
            resultArea.setText("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private void handleLogSwap() {
        try {
            if (latestSuggestion == null || latestNutrient == null) {
                JOptionPane.showMessageDialog(this, "Please perform a swap first.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int userId = Integer.parseInt((String) userBox.getSelectedItem());
            String date = (String) dateBox.getSelectedItem();
            String mealType = (String) mealBox.getSelectedItem();
            int mealId = mealDAO.getMealId(userId, date, mealType);
            if (mealId == -1) {
                JOptionPane.showMessageDialog(this, "Original meal not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String measure = foodSwapDAO.getMeasureDescription(latestSuggestion.getOriginalFood(), userId, date, mealType);
            double after = foodSwapDAO.getRecommendedNutrientAmount(latestSuggestion.getRecommendedFood(), measure, latestNutrient);

            boolean success = foodSwapDAO.logSwapMeal(
                userId, mealId, date, mealType,
                latestSuggestion.getOriginalFood(), latestSuggestion.getOriginalValue(),
                latestSuggestion.getRecommendedFood(), after, latestNutrient.toUpperCase()
            );

            if (success) {
            	
            	fireSwapLoggedEvent(latestSuggestion, latestNutrient); //  Notify all observers

                JOptionPane.showMessageDialog(this, "Swap logged successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to log swap.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            resultArea.setText("Log Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
    	SwapForm form = new SwapForm();
        form.addSwapEventListener(new SwapLogger());
        form.addSwapEventListener(new SwapChartObserver()); // new chart observer
    }
}
