package usermanager;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
public class UserProfileManager{
    private String loggedInUsername = "";

    public UserProfileManager(){
        readUserName();
    }

    public String readUserName(){
        // Read the logged-in user's username from users.txt
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("src/main/java/data", "users.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                loggedInUsername = line.split(":")[0].trim();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loggedInUsername;
    }

    public boolean isCurrentUser(String profileUserName) {
        return profileUserName.equals(loggedInUsername);
    }

}
