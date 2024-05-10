package database;

import java.sql.Timestamp;

public interface PostDAO {
    void insert(int userID, int likerID, String imageID, Timestamp time);
}
