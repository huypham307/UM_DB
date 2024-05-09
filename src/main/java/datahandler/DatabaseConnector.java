package datahandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public final class DatabaseConnector {
    private static DatabaseConnector databaseConnector;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/QuackstagramDB";
    private static final String USERNAME = "BCS1510";
    private static final String PASSWORD = "BCS1510";
    public Connection connection;

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

    public static Connection setupConnection(){
        return getInstance().connection;
    }


//    private void createConnection(){
//        String databaseUrl = "jdbc:mysql://localhost:3306/QuackstagramDB";
//        String username = "BCS1510";
//        String password = "BCS1510";
//
//        try (Connection connection = DriverManager.getConnection(databaseUrl, username, password);
//             Statement statement = connection.createStatement();
//             ResultSet resultSet = statement.executeQuery("SELECT * FROM users")) {
//
//            while (resultSet.next()) {
//                String username1 = resultSet.getString("username");  // Assuming you have an 'id' column
//                String password1 = resultSet.getString("password"); // Assuming you have a 'name' column
//                System.out.println("Username: " + username1 + ", Password: " + password1);
//            }
//        } catch (Exception e) {
//            System.out.println("Database connection error: " + e.getMessage());
//        }
//    }
//
//    public static void main(String[] args) {
//        DatabaseConnector databaseConnector1 = DatabaseConnector.getInstance();
//        databaseConnector1.createConnection();
//    }
}

