package strategy;

public class DecreaseStrategy implements NutrientGoalStrategy {
    @Override
    public double calculateTarget(double originalValue, int percentage) {
        return originalValue * (1 - (percentage / 100.0));
    }
}
