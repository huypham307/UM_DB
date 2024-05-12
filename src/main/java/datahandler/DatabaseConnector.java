package datahandler;


import java.sql.*;

public final class DatabaseConnector {
    private static DatabaseConnector databaseConnector;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/QuackstagramDB";
    private static final String USERNAME = "BCS1510";
    private static final String PASSWORD = "BCS1510";
    private static Connection connection;

    private DatabaseConnector() {
        try
        {
            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        }
        catch (Exception e)
        {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    public static DatabaseConnector getInstance(){
        if(databaseConnector == null){
            databaseConnector = new DatabaseConnector();
        }
        return databaseConnector;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null){
            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        }
        return connection;
    }
}

