package classes;

public class SwapGoal {
    public enum NutrientType {
        CARBOHYDRATE, PROTEIN, FIBRE
    }

    public enum GoalDirection {
        INCREASE, DECREASE
    }

    private NutrientType nutrientType;
    private GoalDirection direction;
    private int percentage;

    public SwapGoal(NutrientType nutrientType, GoalDirection direction, int percentage) {
        this.nutrientType = nutrientType;
        this.direction = direction;
        this.percentage = percentage;
    }
    public SwapGoal(String nutrientType, GoalDirection direction, int percentage) {
        this.nutrientType = NutrientType.valueOf(nutrientType.toUpperCase());
        this.direction = direction;
        this.percentage = percentage;
    }

    public NutrientType getNutrientType() {
        return nutrientType;
    }

    public GoalDirection getDirection() {
        return direction;
    }

    public int getPercentage() {
        return percentage;
    }

    public int getNutrientId() {
        return switch (nutrientType) {
            case PROTEIN -> 203;
            case CARBOHYDRATE -> 205;
            case FIBRE -> 291;
        };
    }
    

    @Override
    public String toString() {
        return direction + " " + percentage + "% " + nutrientType.name().toLowerCase();
    }
}
