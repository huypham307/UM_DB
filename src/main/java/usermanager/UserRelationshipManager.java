package usermanager;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import database.FollowDAOImpl;
import database.UserDAOImpl;
import datahandler.FileHandler;

public class UserRelationshipManager {
    public void followUser(int followerID, int followeeID) throws IOException {
        FollowDAOImpl.getInstance().insertFollow(followerID, followeeID);
    }

    // Method to check if a user is already following another user
    public boolean isAlreadyFollowing(int followerID, int followeeID) throws IOException {
        ArrayList<Integer> followees = FollowDAOImpl.getInstance().fetchFollower(followerID);
        return Collections.binarySearch(followees, followeeID) >= 0;
    }

    public static void main(String[] args) throws IOException {
//        System.out.println(isAlreadyFollowing(1,5 ));
    }


}