package database;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

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

    public void insert(int userID, int likerID, int imageID, Timestamp time){
        String query = "INSERT INTO posts (userID, likerID, imageID, time) VALUES (?,?,?,?)";
        Connection conn;
        try{
            conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, userID);
            pstmt.setInt(2, likerID);
            pstmt.setInt(3,imageID);
            pstmt.setTimestamp(4, time);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
