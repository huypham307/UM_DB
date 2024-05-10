package database;

import exception.UserNotFoundException;
import usermanager.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FollowDAOImpl implements FollowDAO{
    static FollowDAOImpl instance;
    private final DataSource dataSource;

    private FollowDAOImpl(){
        this.dataSource = DatabaseUtil.setupDataSource();
    };

    public static FollowDAOImpl getInstance(){
        if(instance == null){
            instance = new FollowDAOImpl();
        }
        return instance;
    }


    @Override
    public ArrayList<Integer> fetchFollower(int user_id) {
        String query = "SELECT followee_id FROM QuackstagramDB.follows f WHERE follower_id = ?";
        Connection conn = null;
        ArrayList<Integer> followees = new ArrayList<>();
        try{
            conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, user_id);
            ResultSet rs = pstmt.executeQuery();
            int columns = rs.getMetaData().getColumnCount();
            while (rs.next()){
                int followee_id = rs.getInt(1);
                followees.add(followee_id);
            }
            //Do somthing clever
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return followees;
    }

    public static void main(String[] args) {
        ArrayList<Integer> followees = FollowDAOImpl.getInstance().fetchFollower(1);
        for(int i: followees){
            System.out.println("Followee: "  + i);
        }
    }
}
