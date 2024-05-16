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

    public void insert(String image_id, int user_id, String image_bio, Timestamp post_time, String filepath){
        String query = "INSERT INTO image_data (image_id, user_id, image_bio, post_time, file_path) VALUES (?,?,?,?,?)";
        Connection conn;
        try{
            conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, image_id);
            stmt.setInt(2, user_id);
            stmt.setString(3, image_bio);
            stmt.setTimestamp(4, post_time);
            stmt.setString(5, filepath);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Image> fetchFollowedImage(int user_id){
        String query = "SELECT image_id, image_bio, file_path, post_time, username FROM users u INNER JOIN (SELECT * FROM image_data id INNER JOIN (SELECT * FROM follows f WHERE follower_id = ?) as temp ON id.user_id = temp.followee_id) as temp2 ON u.user_id = temp2.user_id";
        ArrayList<Image> images = new ArrayList<>();
        Connection conn = null;
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
        }finally {
            if(conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return images;
    }

    public Image fetchImage(String imageID) throws SQLException {
        String query = "SELECT image_id, u.username, image_bio, post_time, file_path " +
                "FROM image_data id INNER JOIN users u ON id.user_id = u.user_id " +
                "WHERE image_id = ?";
        Image image = null;
        try(Connection conn = dataSource.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)){
            pstmt.setString(1, imageID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                image = new Image(rs.getString(1), rs.getString(2), rs.getString(3), rs.getTimestamp(4), rs.getString(5));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        return image;
    }

    public static void main(String[] args) throws SQLException {
        Image image = ImageDAOImpl.getInstance().fetchImage("Mystar_1");
        System.out.println("Owner: " + image.getUsername()+ " " + image.getImageID());

    }
}
