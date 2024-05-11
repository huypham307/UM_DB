package database;

import entities.Image;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImageDAOImpl implements ImageDAO {
    static ImageDAOImpl instance;
    private final DataSource dataSource;

    private ImageDAOImpl(){
        this.dataSource = DatabaseUtil.setupDataSource();
    };

    public static ImageDAOImpl getInstance(){
        if(instance == null){
            instance = new ImageDAOImpl();
        }
        return instance;
    }

    public void insert(int userID, String imageBio, Timestamp time){
        String query = "INSERT INTO image_data (userID, imageBio, time) VALUES (?,?,?)";
        Connection conn;
        try{
            conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userID);
            stmt.setString(2, imageBio);
            stmt.setTimestamp(3, time);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Image> fetchFollowedImage(int user_id){
        String query = "SELECT image_id, image_bio, file_path, post_time, username FROM users u INNER JOIN (SELECT * FROM image_data id INNER JOIN (SELECT * FROM follows f WHERE follower_id = ?) as temp ON id.user_id = temp.followee_id) as temp2 ON u.user_id = temp2.user_id";
        ArrayList<Image> images = new ArrayList<>();
        Connection conn;
        try{
            conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, user_id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                String imageID = rs.getString(1);
                String imageBio = rs.getString(2);
                String filePath = rs.getString(3);
                Timestamp postTime = rs.getTimestamp(4);
                String username = rs.getString(5);
                images.add(new Image(imageID, username, imageBio, postTime, filePath));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return images;
    }

    public static void main(String[] args) {
        ArrayList<Image> images = ImageDAOImpl.getInstance().fetchFollowedImage(1);
        for(Image img: images){
            System.out.println("ImageID: " + img.getImageID() + " " + img.getUsername());
        }

    }
}
