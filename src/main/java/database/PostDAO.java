package database;

import java.sql.Timestamp;

public interface PostDAO {
    void insert(int userID, int likerID, int imageID, Timestamp time);
}
