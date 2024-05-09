package usermanager;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import mediahandler.Picture;

import java.util.ArrayList;

// Represents a user on Quackstagram
public class User {
    private final String username;
    private String bio;
    private String password;
    private int postsCount;
    private int followersCount;
    private int followingCount;
    private List<Picture> pictures;

    public User(String username, String bio, String password) {
        this.username = username;
        this.bio = bio;
        this.password = password;
        this.pictures = new ArrayList<>();
    }

    public User(String username){
        this.username = username;
    }

    // Getter methods for user details
    public String getUsername() { return username; }
    public String getBio() {
        return bio;
    }
    public void setBio(String bio) {this.bio = bio; }

    public int getFollowersCount() { return followersCount; }
    public int getFollowingCount() { return followingCount; }
    public List<Picture> getPictures() { return pictures; }

    public int getPostCount() {
        Path imageDetailsFilePath = Paths.get("src/main/java/img", "image_details.txt");
        postsCount = 0;
        try (BufferedReader reader = Files.newBufferedReader(imageDetailsFilePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Assuming each line in the file corresponds to an image and starts with the username
                if (line.contains("Username: " + username)) {
                    postsCount++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately (e.g., log it, throw a runtime exception, etc.)
        }
        return postsCount;
    }

    // Setter methods for followers and the following counts
    public void setFollowersCount(int followersCount) { this.followersCount = followersCount; }
    public void setFollowingCount(int followingCount) { this.followingCount = followingCount; }
    public void setPostCount(int postCount) { this.postsCount = postCount;}
    // Implement the toString method for saving user information
    @Override
    public String toString() {
        return username + ":" + bio + ":" + password; // Format as needed
    }

}