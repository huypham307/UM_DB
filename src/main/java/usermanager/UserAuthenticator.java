package usermanager;
import datahandler.Encryptor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
public class UserAuthenticator {

    public User newUser;
 
    public boolean verifyCredentials(String username, String password) {
        Encryptor encryptor = Encryptor.getInstance();
        password = encryptor.encrypt(password);
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/java/data/credentials.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(":");
                if (credentials[0].equals(username) && credentials[1].equals(password)) {
                String bio = credentials[2];
                // Create User object and save information
                newUser = new User(username, bio, password);
                saveUserInformation(newUser);
                return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

   public void saveUserInformation(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/data/users.txt", false))) {
            writer.write(user.toString()); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
