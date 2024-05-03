package usermanager;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

import datahandler.FileHandler;

public class UserRelationshipManager {

    private final String followersFilePath = "data/following.txt";
    private final FileHandler fileHandler;

    public UserRelationshipManager(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    public void followUser(String follower, String followed) throws IOException {
        // Use FileHandler to load all lines from the file
        List<String> lines = fileHandler.readLinesFromFile(followersFilePath);
        boolean foundFollower = false;

        // Update the list of followed users for the follower
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.startsWith(follower + ":")) {
                foundFollower = true;
                if (!isAlreadyFollowing(follower, followed)) {
                    String newValue = line + "; " + followed;
                    lines.set(i, newValue);
                }
                break;
            }
        }

        // If the follower is not found, add a new entry
        if (!foundFollower && !followed.isEmpty()) {
            lines.add(follower + ": " + followed);
        }

        // Overwrite the file with the updated list
        // First, clear the file content
        new PrintWriter(followersFilePath).close();

        // Then, write each line back to the file
        for (String line : lines) {
            fileHandler.appendLineToFile(followersFilePath, line);
        }
    }

    // Method to check if a user is already following another user
    public boolean isAlreadyFollowing(String follower, String followed) throws IOException {
        // Use FileHandler to load all lines from the file
        List<String> lines = fileHandler.readLinesFromFile(followersFilePath);
        for (String line : lines) {
            // Split the line at the colon to separate the follower and followed parts
            String[] parts = line.split(":", 2);
            if (parts.length == 2 && parts[0].trim().equals(follower)) {
                // Split the followed part by semicolons and trim whitespace
                String[] followedUsers = parts[1].trim().split(";\\s*");
                // Check if the followed array contains the followed user
                for (String user : followedUsers) {
                    if (user.trim().equals(followed)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public List<String> getFollowers(String username) throws IOException {
        return fileHandler.readLinesFromFile(followersFilePath).stream()
                .map(line -> line.split(":"))
                .filter(parts -> parts[1].equals(username))
                .map(parts -> parts[0])
                .collect(Collectors.toList());
    }


    public List<String> getFollowing(String username) throws IOException {
        return fileHandler.readLinesFromFile(followersFilePath).stream()
                .map(line -> line.split(":"))
                .filter(parts -> parts[0].equals(username))
                .map(parts -> parts[1])
                .collect(Collectors.toList());
    }}