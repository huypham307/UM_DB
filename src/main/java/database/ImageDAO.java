package database;

import usermanager.User;

import java.sql.Timestamp;

public interface ImageDAO {
    void insert(String image_id, int user_id, String image_bio, Timestamp post_time);

}
