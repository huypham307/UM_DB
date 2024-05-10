package database;

import java.util.ArrayList;
import entities.Image;

public interface ImageDataDAO {
    ArrayList<Image> fetchImages(int user_id);
}
