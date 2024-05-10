package entities;

import java.sql.Time;
import java.sql.Timestamp;

public class Image {
    String imageID;
    int user_id;
    String imageBio;
    Timestamp postTime;

    public Image(String imageID, int user_id, String imageBio, Timestamp postTime){
        this.imageID = imageID;
        this.user_id = user_id;
        this.postTime = postTime;
        this.imageBio = imageBio;
    }

    public String getImageID() {
        return imageID;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getImageBio() {
        return imageBio;
    }

    public Timestamp getPostTime() {
        return postTime;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setImageBio(String imageBio) {
        this.imageBio = imageBio;
    }

    public void setPostTime(Timestamp postTime) {
        this.postTime = postTime;
    }
}
