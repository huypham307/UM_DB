package database;

import entities.Notification;
import exception.LikeDuplicateException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;

public class PostDAOImpl implements PostDAO {
    static PostDAOImpl instance;
    private final DataSource dataSource;

    private PostDAOImpl(){
        this.dataSource = DatabaseUtil.setupDataSource();
    };

    public static PostDAOImpl getInstance(){
        if(instance == null){
            instance = new PostDAOImpl();
        }
        return instance;
    }

    public boolean insert(int likerID, String imageID, Timestamp time){
        String query = "INSERT INTO QuackstagramDB.posts (liker_id, image_id, time) VALUES (?,?,?)";
        Connection conn = null;
        try{
            conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, likerID);
            pstmt.setString(2,imageID);
            pstmt.setTimestamp(3, time);
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new LikeDuplicateException("User already liked it");
        }finally {
            if(conn != null){
                try {
                    conn.close();
                }catch (SQLException e){
                    System.out.println(e.getMessage());
                }
            }
        }

    }

    public int fetchLikeCounts(String imageID) {
        String query = "SELECT COUNT(*) as like_counts  FROM posts p WHERE image_id = ?";
        Connection conn = null;
        int likeCount = 0;
        try {
            conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, imageID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                likeCount = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            if(conn != null){
                try {
                    conn.close();
                }catch (SQLException e){
                    System.out.println(e.getMessage());
                }
            }
        }
        return likeCount;
    }

    public ArrayList<Notification> fectchNotifications(int userID){
        String query = "SELECT u.username as liker, temp2.username as owner, time " +
                "FROM QuackstagramDB.users u " +
                "INNER JOIN (" +
                "SELECT u.user_id, username, liker_id, time " +
                "FROM QuackstagramDB.users u " +
                "INNER JOIN (" +
                "SELECT user_id, liker_id, time " +
                "FROM QuackstagramDB.posts p " +
                "INNER JOIN QuackstagramDB.image_data id ON p.image_id = id.image_id " +
                "WHERE user_id = ?" +
                ") as temp ON u.user_id = temp.user_id" +
                ") as temp2 ON u.user_id = temp2.liker_id";

        ArrayList<Notification> notifications = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)){

            pstmt.setInt(1, userID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                notifications.add(new Notification(rs.getString(1), rs.getString(2), rs.getTimestamp(3)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return notifications;
    }

    public static void main(String[] args) {
        ArrayList<Notification> notifications = PostDAOImpl.getInstance().fectchNotifications(4);
        for (Notification noti: notifications){
            System.out.println(noti.getLikername() + " has liked " + "image of " + noti.getUsername());
        }
    }

}
