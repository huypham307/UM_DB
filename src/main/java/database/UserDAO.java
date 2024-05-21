package database;

import usermanager.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDAO {
    User fecthUser(String username);
    void insert(String username, String password, String bio);
    int fecthFollowing(int user_id);
    int fecthFollower(int user_id);
    int fecthPostNum(int user_id) throws SQLException;
    List<User> fecthUsernames(String input);
}
