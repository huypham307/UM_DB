package entities;

import java.sql.Time;
import java.sql.Timestamp;

public class Image {
    String imageID;
    String username;
    String imageBio;
    Timestamp postTime;
    String filePath;


    public Image(String imageID, String username, String imageBio, Timestamp postTime, String filePath){
        this.imageID = imageID;
        this.username = username;
        this.postTime = postTime;
        this.imageBio = imageBio;
        this.filePath = filePath;
    }

    public String getImageID() {
        return imageID;
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

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setImageBio(String imageBio) {
        this.imageBio = imageBio;
    }

    public void setPostTime(Timestamp postTime) {
        this.postTime = postTime;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
