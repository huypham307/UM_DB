package database;

import datahandler.DatabaseConnector;
import exception.UserNotFoundException;
import usermanager.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SessionsDAOImpl implements SessionsDAO{

    static SessionsDAOImpl instance;
    private final DataSource dataSource;

    private SessionsDAOImpl(){
        this.dataSource = DatabaseUtil.setupDataSource();
    };

    public static SessionsDAOImpl getInstance(){
        if(instance == null){
            instance = new SessionsDAOImpl();
        }
        return instance;
    }

    @Override
    public void insert(int user_id) {
        String query = "INSERT INTO sessions (user_id) VALUES (?)";
        Connection conn;
        try{
            conn = dataSource.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, user_id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        SessionsDAOImpl sessionsDAO = new SessionsDAOImpl();
        sessionsDAO.insert(3);
    }
}

