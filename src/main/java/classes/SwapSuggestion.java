package classes;

public class SwapSuggestion {
    private String originalFood;
    private String recommendedFood;
    private double originalValue;
    private double recommendedValue;
    

    // Constructor when both original and recommended food are known
    public SwapSuggestion(String originalFood, String recommendedFood, double originalValue, double recommendedValue) {
        this.originalFood = originalFood;
        this.recommendedFood = recommendedFood;
        this.originalValue = originalValue;
        this.recommendedValue = recommendedValue;
    }

    // Alternative constructor for when only recommended food is known
    // Added an explicit 'boolean placeholder' to differentiate
    public SwapSuggestion(String recommendedFood, double originalValue, double recommendedValue, boolean isPartial) {
        this.originalFood = null;
        this.recommendedFood = recommendedFood;
        this.originalValue = originalValue;
        this.recommendedValue = recommendedValue;
    }

    public String getOriginalFood() {
        return originalFood;
    }

    public String getRecommendedFood() {
        return recommendedFood;
    }

    public double getOriginalValue() {
        return originalValue;
    }

    public double getRecommendedValue() {
        return recommendedValue;
    }

    @Override
    public String toString() {
        return "Swap " + originalFood + " with " + recommendedFood +
               " | Original: " + originalValue + " → Recommended: " + recommendedValue;
    }
}
