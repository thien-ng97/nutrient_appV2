package dao;

import java.sql.*;

import classes.UserProfile;
import dbManager.DatabaseManager;


// With use of AI

public class UserDAO {
	public void saveUser(UserProfile user) {
        String sql = "INSERT INTO users (name, gender, date_of_birth, height_cm, weight_kg) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getGender());
            stmt.setDate(3, Date.valueOf(user.getDateOfBirth()));
            stmt.setDouble(4, user.getHeightCm());
            stmt.setDouble(5, user.getWeightKg());
           

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
