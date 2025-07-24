package decorator;

import classes.SwapSuggestion;
import dao.FoodSwapDAO;

public class CalorieImpactDecorator {
    private final SwapSuggestion suggestion;
    private final int userId;
    private final String date;
    private final String mealType;
    private final FoodSwapDAO dao = new FoodSwapDAO();

    public CalorieImpactDecorator(SwapSuggestion suggestion, int userId, String date, String mealType) {
        this.suggestion = suggestion;
        this.userId = userId;
        this.date = date;
        this.mealType = mealType;
    }

    public String getDecoratedInfo() {
        try {
            String measure = dao.getMeasureDescription(suggestion.getOriginalFood(), userId, date, mealType);
            if (measure == null) return "Calorie impact: unavailable";

            double originalCalories = dao.getNutrientAmount(suggestion.getOriginalFood(), measure, "ENERGY (KILOCALORIES)");
            double recommendedCalories = dao.getCalorieAmount(suggestion.getRecommendedFood(), measure, "ENERGY (KILOCALORIES)");

            double difference = recommendedCalories - originalCalories;
            double percentage = (Math.abs(difference)/originalCalories)*100;
            String symbol = difference > 0 ? "+" : "-";

            return "Calorie impact: " + symbol + String.format("%.2f", Math.abs(difference)) + " kcal. Percentage change: " + symbol + String.format("%.2f", percentage) + " % kcal";
        } catch (Exception e) {
            return "Calorie impact: unavailable";
        }
    }
}
