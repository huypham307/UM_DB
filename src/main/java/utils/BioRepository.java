package utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import usermanager.User;

public class BioRepository {
    private String bio;
    private final User currentUser;
    public BioRepository(User user){
        this.currentUser = user;
    }

    public String readBio(){
        bio = "";
        final Path bioDetailsFilePath = Paths.get("src/main/java/data", "credentials.txt");

        bio = "";
        try (BufferedReader bioDetailsReader = Files.newBufferedReader(bioDetailsFilePath)) {
            String line;
            while ((line = bioDetailsReader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts[0].equals(currentUser.getUsername()) && parts.length >= 3) {
                    bio = parts[2];
                    break; // Exit the loop once the matching bio is found
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Bio for " + currentUser.getUsername() + ": " + bio);

        return bio;
    }
}
