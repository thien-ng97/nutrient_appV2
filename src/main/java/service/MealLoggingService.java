package service;

import classes.Meal;
import dao.MealDAO;

public class MealLoggingService {
    private MealDAO mealDAO = new MealDAO();

    public boolean logMeal(Meal meal) {
        return mealDAO.logMeal(meal);
    }
}
