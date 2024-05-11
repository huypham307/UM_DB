package usermanager;
import database.SessionsDAOImpl;
import database.UserDAOImpl;

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
        User user = UserDAOImpl.getInstance().fecthUser(username);
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
