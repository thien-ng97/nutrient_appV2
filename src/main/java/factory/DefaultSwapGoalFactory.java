package factory;

import classes.SwapGoal;

public class DefaultSwapGoalFactory implements SwapGoalFactory {
	@Override
    public SwapGoal createSwapGoal(String nutrient, String direction, int percentage) {
        SwapGoal.NutrientType nutrientType = SwapGoal.NutrientType.valueOf(nutrient.toUpperCase());
        SwapGoal.GoalDirection goalDirection = SwapGoal.GoalDirection.valueOf(direction.toUpperCase());
        return new SwapGoal(nutrientType, goalDirection, percentage);
    }
}
