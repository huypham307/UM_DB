package database;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

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
}
