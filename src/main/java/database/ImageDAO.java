package database;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import entities.Image;

public interface ImageDAO {
    void insert(String image_id, int user_id, String image_bio, Timestamp post_time, String filepath);
    List<Image> fetchFollowedImage(int user_id);
}
