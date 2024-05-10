package database;

import usermanager.User;

import java.util.ArrayList;
import java.util.List;

public interface FollowDAO {
    ArrayList<Integer> fetchFollower(int user_id);
}
