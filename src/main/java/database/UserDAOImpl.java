package database;
import usermanager.User;
import exception.UserNotFoundException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
    public User fecthUser(String username) {
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
        } finally {
            if(conn != null){
                try {
                    conn.close();
                }catch (SQLException e){
                    System.out.println(e.getMessage());
                }
            }
        }
        return user;
    }

    public void insert(String username, String password, String bio){
        String query = "INSERT INTO users (username, password, bio) VALUES (?,?,?)";
        Connection conn = null;
        try{
            conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2,password);
            pstmt.setString(3,bio);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            if(conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    @Override
    public int fecthFollowing(int user_id) {
        String query = "SELECT COUNT(u.user_id) as following FROM QuackstagramDB.users u INNER JOIN QuackstagramDB.follows f ON u.user_id = f.follower_id WHERE u.user_id = ? GROUP BY (u.user_id);";

        int followingCount = 0;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setInt(1, user_id);

            try (ResultSet rs = preparedStatement.executeQuery()) {  // Add try-with-resources for ResultSet
                while (rs.next()) {
                    followingCount = rs.getInt(1);
                }
            } // rs is automatically closed here

        } catch (SQLException e) {
            System.out.println("Error executing SQL query: " + e.getMessage());
        }

        return followingCount;
    }


    @Override
    public int fecthFollower(int user_id) {
        String query = "SELECT COUNT(u.user_id) as followed FROM QuackstagramDB.users u INNER JOIN QuackstagramDB.follows f ON u.user_id = f.followee_id WHERE u.user_id = ? GROUP BY (u.user_id);";

        Connection conn = null;
        int followedCount = 0;
        try {
            conn = dataSource.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, user_id);
            // Execute query and handle result set
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                followedCount = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error executing SQL query: " + e.getMessage());
        }finally {
            if(conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return followedCount;
    }

    @Override
    public int fecthPostNum(int user_id) throws SQLException {
        String query = "SELECT COUNT(u.user_id) " +
                "FROM QuackstagramDB.users u INNER JOIN QuackstagramDB.image_data id ON u.user_id = id.user_id " +
                "WHERE u.user_id = ? " +      // Placeholder for user ID
                "GROUP BY u.user_id";
        int postNumber = 0;
        try(Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)){
            pstmt.setInt(1, user_id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                postNumber = rs.getInt(1);
            }
        }catch (SQLException e) {
            System.out.println("Error executing SQL query: " + e.getMessage());
        }
        return postNumber;
    }

    public List<User> fecthUsernames(String input) {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users WHERE username LIKE ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + input + "%");
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    User user = new User(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("bio"),
                            rs.getString("password")
                    );
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error executing SQL query: " + e.getMessage());
        }
        return users;
    }





    public static void main(String[] args) throws SQLException {
//        UserDAOImpl.getInstance().insertFollow(3,2);
    }
}
