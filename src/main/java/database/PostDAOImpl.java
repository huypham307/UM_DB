package database;

import exception.LikeDuplicateException;

import javax.sql.DataSource;
import java.sql.*;

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

    public static void main(String[] args) {
        PostDAOImpl.getInstance().insert(4, "Mystar_1", null);
    }
}
