package usermanager;
import database.SessionsDAOImpl;
import database.UserDAOImpl;
import datahandler.DatabaseConnector;
import datahandler.Encryptor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class UserAuthenticator {

    public User newUser;
 
    public boolean verifyCredentials(String username, String password) {
        User user = UserDAOImpl.getInstance().fecthUserData(username);
        if(user.getUsername().equals(username) && user.checkPassword(password)){
            writeUserToSession(user.getUserID());
            return true;
        }
        return false;
    }

   public void writeUserToSession(int userID) {
       SessionsDAOImpl.getInstance().insert(userID);
    }
}
