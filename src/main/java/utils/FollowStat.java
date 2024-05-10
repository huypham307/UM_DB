package utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
public class FollowStat {
    private final Path followingFilePath;
    private int follwingCount;
    private int followerCount;

    public FollowStat(String userName){
        this.followingFilePath = Paths.get("src/main/java/data", "following.txt");
        calculateFollow(userName);
    }

    public void calculateFollow(String userName){
        try (BufferedReader followingReader = Files.newBufferedReader(followingFilePath)) {
            String line;
            while ((line = followingReader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String username = parts[0].trim();
                    String[] followingUsers = parts[1].split(";");
                    if (username.equals(userName)) {
                        follwingCount = followingUsers.length;
                    } else {
                        for (String followingUser : followingUsers) {
                            if (followingUser.trim().equals(userName)) {
                                followerCount++;
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getFollowerCount(){
        return followerCount;
    }

    public int getFollwingCount(){
        return follwingCount;
    }

}
