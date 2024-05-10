package database;

import usermanager.User;

public interface UserDAO {
    User fecthUserData(String username);
    void insert(String username, String password, String bio);
}
