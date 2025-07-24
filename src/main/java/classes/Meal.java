package classes;

import java.time.LocalDate;

public class Meal {
    private int userId;
    private String mealType;
    private LocalDate date;
    private String ingredient;
    private String quantity;

    public Meal(int userId, String mealType, LocalDate date, String ingredient, String quantity) {
        this.userId = userId;
        this.mealType = mealType;
        this.date = date;
        this.ingredient = ingredient;
        this.quantity = quantity;
    }

    public int getUserId() {
        return userId;
    }

    public String getMealType() {
        return mealType;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getIngredient() {
        return ingredient;
    }

    public String getQuantity() {
        return quantity;
    }
}
