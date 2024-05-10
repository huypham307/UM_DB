package database;

import entities.Image;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;

public class ImageDataDAOImpl implements ImageDataDAO{
    static ImageDataDAOImpl instance;
    private final DataSource dataSource;

    private ImageDataDAOImpl(){
        this.dataSource = DatabaseUtil.setupDataSource();
    };

    public static ImageDataDAOImpl getInstance(){
        if(instance == null){
            instance = new ImageDataDAOImpl();
        }
        return instance;
    }

    @Override
    public ArrayList<Image> fetchImages(int user_id) {
        String query = "SELECT image_id, image_bio, post_time FROM QuackstagramDB.image_data id WHERE user_id = ?;";
        Connection conn;
        ArrayList<Image> images = new ArrayList<>();
        try{
            conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, user_id);
            ResultSet rs = pstmt.executeQuery();
            int columns = rs.getMetaData().getColumnCount();
            while (rs.next()){
                String image_id = "";
                String imageBio ="";
                Timestamp timestamp = null;

                image_id = rs.getString(1);
                imageBio = rs.getString(2);
                timestamp = rs.getTimestamp(3);

                images.add(new Image(image_id, user_id, imageBio, timestamp ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return images;
    }

    public static void main(String[] args) {
        ArrayList<Image> images = ImageDataDAOImpl.getInstance().fetchImages(1);
        for (Image img: images){
            System.out.println("Image id: " + img.getImageBio());
        }
    }
}
