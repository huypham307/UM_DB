package database;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import entities.Image;

public interface ImageDAO {
    void insert(int userID, String imageBio, Timestamp time);
    List<Image> fetchFollowedImage(int user_id);
}
