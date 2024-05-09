package database;

import usermanager.User;

public interface UserDAO {
    User fecthUserData(String username);
}
