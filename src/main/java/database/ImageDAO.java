package database;

import java.sql.Timestamp;

public interface ImageDAO {
    void insert(int userID, String imageBio, Timestamp time);
}
