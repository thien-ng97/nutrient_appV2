package factory;

import classes.SwapGoal;

public interface SwapGoalFactory {
	SwapGoal createSwapGoal(String nutrient, String direction, int percentage);
}
