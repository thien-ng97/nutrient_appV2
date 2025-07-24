package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import classes.Meal;
import dbManager.DatabaseManager;

public class MealDAO {

    public List<Integer> getAllUserIds() {
        List<Integer> userIds = new ArrayList<>();
        String sql = "SELECT DISTINCT user_id FROM meal_table";
        try (
            Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
        ) {
            while (rs.next()) {
                userIds.add(rs.getInt("user_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userIds;
    }

    public List<String> getDatesForUser(int userId) {
        List<String> dates = new ArrayList<>();
        String sql = "SELECT DISTINCT date FROM meal_table WHERE user_id = ? ORDER BY date DESC";
        try (
            Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    dates.add(rs.getString("date"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dates;
    }

    public List<String> getMealTypesForUserDate(int userId, String date) {
        List<String> mealTypes = new ArrayList<>();
        String sql = "SELECT meal_type FROM meal_table WHERE user_id = ? AND date = ?";
        try (
            Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, userId);
            stmt.setString(2, date);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    mealTypes.add(rs.getString("meal_type"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mealTypes;
    }

    public String getFoodForMeal(int userId, String date, String mealType) {
        String sql = "SELECT ingredient FROM meal_table WHERE user_id = ? AND date = ? AND meal_type = ?";
        try (
            Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, userId);
            stmt.setString(2, date);
            stmt.setString(3, mealType);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getString("ingredient");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getQuantityForMeal(int userId, String date, String mealType) {
        String sql = "SELECT quantity FROM meal_table WHERE user_id = ? AND date = ? AND meal_type = ?";
        try (
            Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, userId);
            stmt.setString(2, date);
            stmt.setString(3, mealType);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getString("quantity");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // log meal to meal_table 
    public boolean logMeal(Meal meal) {
        String sqlCheck = "SELECT COUNT(*) FROM meal_table WHERE user_id = ? AND meal_type = ? AND date = ?";
        String sqlInsert = "INSERT INTO meal_table (user_id, meal_type, date, ingredient, quantity) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(sqlCheck)) {

            checkStmt.setInt(1, meal.getUserId());
            checkStmt.setString(2, meal.getMealType());
            checkStmt.setDate(3, java.sql.Date.valueOf(meal.getDate()));

            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return false; // duplicate
            }

            try (PreparedStatement insertStmt = conn.prepareStatement(sqlInsert)) {
                insertStmt.setInt(1, meal.getUserId());
                insertStmt.setString(2, meal.getMealType());
                insertStmt.setDate(3, java.sql.Date.valueOf(meal.getDate()));
                insertStmt.setString(4, meal.getIngredient());
                insertStmt.setString(5, meal.getQuantity());
                insertStmt.executeUpdate();
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    
    // this helps with getting meal_id for logging swap meal
    public int getMealId(int userId, String date, String mealType) {
        String sql = "SELECT meal_id FROM meal_table WHERE user_id = ? AND date = ? AND meal_type = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, date);
            stmt.setString(3, mealType);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("meal_id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


} 
