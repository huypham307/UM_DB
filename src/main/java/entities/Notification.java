package entities;

import java.sql.Timestamp;

public class Notification {
    String likername;
    String username;
    Timestamp likeTime;

    public Notification(String likername, String username, Timestamp likeTime) {
        this.likername = likername;
        this.username = username;
        this.likeTime = likeTime;
    }

    public String getLikername() {
        return likername;
    }

    public String getUsername() {
        return username;
    }

    public Timestamp getLikeTime() {
        return likeTime;
    }
}