package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import dbManager.DatabaseManager;

public class FoodSwapDAO {

    public String getOriginalFood(int userId, String date, String mealType) {
        String sql = "SELECT ingredient FROM meal_table WHERE user_id = ? AND date = ? AND meal_type = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, date);
            stmt.setString(3, mealType);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("ingredient");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getMeasureDescription(String foodName, int userId, String date, String mealType) {
        String sql = "SELECT quantity FROM meal_table WHERE user_id = ? AND date = ? AND meal_type = ? AND ingredient = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, date);
            stmt.setString(3, mealType);
            stmt.setString(4, foodName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("quantity");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    // To match the nutrient description in the database 
    private String normalizeNutrientName(String nutrient) {
        return switch (nutrient.toUpperCase()) {
            case "PROTEIN" -> "PROTEIN";
            case "CARBOHYDRATE" -> "CARBOHYDRATE";
            case "FIBRE" -> "FIBRE, TOTAL DIETARY";
            default -> nutrient.toUpperCase();
        };
    }

    public double getNutrientAmount(String foodName, String measureDescription, String nutrientName) {
        String sql = "SELECT nutrient_amount FROM master_data WHERE food_description = ? AND measure_description = ? AND nutrient_name = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, foodName);
            stmt.setString(2, measureDescription);
            stmt.setString(3, normalizeNutrientName(nutrientName));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("nutrient_amount");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }
    
    public double getCalorieAmount(String foodName, String measureDescription, String nutrientName) {
        String sql = "SELECT nutrient_amount FROM master_data WHERE food_description = ? AND nutrient_name = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, foodName);

            stmt.setString(2, normalizeNutrientName(nutrientName));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("nutrient_amount");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public String findBestSwap(String originalFood, String measureDescription, String nutrientName, double targetAmount) {
        String sql = """
            SELECT food_description, measure_description, nutrient_amount
            FROM master_data
            WHERE nutrient_name = ?
              AND nutrient_amount BETWEEN ? AND ?
              AND food_description != ?
            ORDER BY 
                CASE WHEN measure_description = ? THEN 0 ELSE 1 END,
                ABS(nutrient_amount - ?) ASC
            LIMIT 1
            """;

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, normalizeNutrientName(nutrientName));
            stmt.setDouble(2, targetAmount * 0.95);
            stmt.setDouble(3, targetAmount * 1.05);
            stmt.setString(4, originalFood);
            stmt.setString(5, measureDescription);  // for ordering preference
            stmt.setDouble(6, targetAmount);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("food_description");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    
    // get the nutrient amount from master table after swap
    public double getRecommendedNutrientAmount(String foodName, String measureDescription, String nutrientName) {
        String sql = "SELECT nutrient_amount FROM master_data WHERE food_description = ?  AND nutrient_name = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, foodName);
            
            stmt.setString(2, normalizeNutrientName(nutrientName));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("nutrient_amount");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }
    
    // log recommended meal to swap_meal table
    public boolean logSwapMeal(int userId, int mealId, String date, String mealType,
            String originalFood, double originalVal,
            String swappedFood, double swappedVal,
            String nutrient) {
    	String sql = "INSERT INTO swap_meal (user_id, meal_id, date, meal_type, original_food, original_nutrient, swapped_food, swapped_nutrient, nutrient_name) " +
    			"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    	try (Connection conn = DatabaseManager.getInstance().getConnection();
    			PreparedStatement stmt = conn.prepareStatement(sql)) {
    				stmt.setInt(1, userId);
    				stmt.setInt(2, mealId); // new param
    				stmt.setString(3, date);
    				stmt.setString(4, mealType);	
    				stmt.setString(5, originalFood);
    				stmt.setDouble(6, originalVal);
    				stmt.setString(7, swappedFood);
    				stmt.setDouble(8, swappedVal);
    				stmt.setString(9, nutrient);
    				return stmt.executeUpdate() > 0;	
    	} catch (Exception e) {
    		e.printStackTrace();
    		return false;
    	}
}

    

}
