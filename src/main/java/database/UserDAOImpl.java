package database;
import datahandler.DatabaseConnector;
import usermanager.User;
import exception.UserNotFoundException;

import javax.sql.DataSource;
import java.sql.*;

public class UserDAOImpl implements UserDAO {
    User user;
    static UserDAOImpl userDAOImpl;
    private final DataSource dataSource;

    private UserDAOImpl(){
        this.dataSource = DatabaseUtil.setupDataSource();
    }

    public static UserDAOImpl getInstance(){
        if(userDAOImpl == null){
            userDAOImpl = new UserDAOImpl();
        }
        return userDAOImpl;
    }

    @Override
    public User fecthUserData(String username) {
        String query = "SELECT * FROM users u WHERE username = ?";
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, username);
            // Execute query and handle result set
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) { // Check if a user is found
                return new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("bio"), rs.getString("password"));
            } else {
                throw new UserNotFoundException("User not found: " + username);
            }
        } catch (SQLException e) {
            System.out.println("Error executing SQL query: " + e.getMessage());
        }
        return user;
    }

    public void insert(String username, String password, String bio){
        String query = "INSERT INTO users (username, password, bio) VALUES (?,?,?)";
        Connection conn;
        try{
            conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2,password);
            pstmt.setString(3,bio);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public static void main(String[] args) {
        User user = UserDAOImpl.getInstance().fecthUserData("Xylo");
        System.out.println("username: " + user.getUserID() + "bio: " + user.getBio());
    }
}
