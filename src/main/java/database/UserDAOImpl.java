package database;
import datahandler.DatabaseConnector;
import usermanager.User;
import exception.UserNotFoundException;

import java.sql.*;

public class UserDAOImpl implements UserDAO {
    String query = "SELECT * FROM users u WHERE username = ?";
    User user;
    static UserDAOImpl userDAOImpl;

    private UserDAOImpl(){
    }

    public static UserDAOImpl getInstance(){
        if(userDAOImpl == null){
            userDAOImpl = new UserDAOImpl();
        }
        return userDAOImpl;
    }

    @Override
    public User fecthUserData(String username) {
        try (Connection connection = DatabaseConnector.setupConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            // Execute query and handle result set
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) { // Check if a user is found
                return new User(rs.getString("username"), rs.getString("bio"), rs.getString("password"));
            } else {
                throw new UserNotFoundException("User not found: " + username);
            }
        } catch (SQLException e) {
            System.out.println("Error executing SQL query: " + e.getMessage());
        }
        return user;
    }

    public static void main(String[] args) {
        User user = UserDAOImpl.getInstance().fecthUserData("Xylo");
        System.out.println("username: " + user.getUsername() + "bio: " + user.getBio());
    }
}
