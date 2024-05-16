package database;

import usermanager.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

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

    public void insert(String image_id, int user_id, String image_bio, Timestamp post_time){
        String query = "INSERT INTO image_data (image_id, user_id, image_bio, post_time) VALUES (?,?,?,?)";
        Connection conn;
        try{
            conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, image_id);
            stmt.setInt(2, user_id);
            stmt.setString(3, image_bio);
            stmt.setTimestamp(4, post_time);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
