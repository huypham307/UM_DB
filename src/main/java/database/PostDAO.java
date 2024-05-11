package database;

import java.sql.Timestamp;

public interface PostDAO {
    boolean insert(int likerID, String imageID, Timestamp time);
}
