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

    private User authorizedUser;
    private static UserAuthenticator instance;

    public static UserAuthenticator getInstance(){
        if(instance == null){
            instance = new UserAuthenticator();
        }
        return instance;
    }
 
    public boolean verifyCredentials(String username, String password) {
        User user = UserDAOImpl.getInstance().fecthUserData(username);
        if(user.getUsername().equals(username) && user.checkPassword(password)){
            writeUserToSession(user.getUserID());
            authorizedUser = user;
            return true;
        }
        return false;
    }

    public User getAuthorizedUser(){
        return authorizedUser;
    }

   public void writeUserToSession(int userID) {
       SessionsDAOImpl.getInstance().insert(userID);
    }
}
