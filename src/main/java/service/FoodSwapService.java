package service;
import classes.SwapGoal;
import classes.SwapSuggestion;
import dao.FoodSwapDAO;
import strategy.*;
public class FoodSwapService {
   private FoodSwapDAO dao = new FoodSwapDAO();
   public SwapSuggestion getSwapSuggestion(int userId, String date, String mealType, SwapGoal goal) {
       try {
           // Step 1: Get the original food
           String originalFood = dao.getOriginalFood(userId, date, mealType);
           if (originalFood == null) return null;
           // Step 2: Get measure description used in the meal
           String measureDescription = dao.getMeasureDescription(originalFood, userId, date, mealType);
           if (measureDescription == null) return null;
           // Step 3: Get original nutrient amount
           double originalAmount = dao.getNutrientAmount(originalFood, measureDescription, goal.getNutrientType().name());
           if (originalAmount == 0.0) return null;
          
           // strategy DESIGN PATTERN
           NutrientGoalStrategy strategy = goal.getDirection() == SwapGoal.GoalDirection.INCREASE ? new IncreaseStrategy()
                   : new DecreaseStrategy();


           // Step 4: Calculate target amount using strategy pattern
           double targetAmount = strategy.calculateTarget(originalAmount, goal.getPercentage());
           // Step 5: Find the best matching food
           String recommendedFood = dao.findBestSwap(originalFood, measureDescription, goal.getNutrientType().name(), targetAmount);
           if (recommendedFood == null) return null;
           // Step 6: Get nutrient amount of recommended food
           double recommendedAmount = dao.getNutrientAmount(recommendedFood, measureDescription, goal.getNutrientType().name());
           return new SwapSuggestion(originalFood, recommendedFood, originalAmount, recommendedAmount);
       } catch (Exception e) {
           e.printStackTrace();
           return null;
       }
   }
}
