package service;

import classes.UserProfile;
import dao.UserDAO;


// With use of AI

public class ProfileService {
	private UserDAO userDAO = new UserDAO();

    public void createProfile(UserProfile user) {
        userDAO.saveUser(user);
    }
}	
