package utils;
import database.UserDAOImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
public class FollowStat {
    private int follwingCount;
    private int followerCount;
    private static int userID;

    public FollowStat(int userID){
        this.userID = userID;
    }

    public int getFollowerCount(){
        return UserDAOImpl.getInstance().fecthFollower(userID);
    }

    public int getFollwingCount(){
        return follwingCount;
    }

}
