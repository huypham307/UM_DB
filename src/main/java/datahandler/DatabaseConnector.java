package datahandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public final class DatabaseConnector {
    private static DatabaseConnector databaseConnector;

    public static DatabaseConnector getInstance(){
        if(databaseConnector == null){
            databaseConnector = new DatabaseConnector();
        }
        return databaseConnector;
    }

    private void createConnection(){
        String databaseUrl = "jdbc:mysql://localhost:3306/quackstagram";
        String username = "BCS1510";
        String password = "BCS1510";

        try (Connection connection = DriverManager.getConnection(databaseUrl, username, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM users")) {

            while (resultSet.next()) {
                String username1 = resultSet.getString("username");  // Assuming you have an 'id' column
                String password1 = resultSet.getString("password"); // Assuming you have a 'name' column
                System.out.println("Username: " + username1 + ", Password: " + password1);
            }
        } catch (Exception e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        DatabaseConnector databaseConnector1 = DatabaseConnector.getInstance();
        databaseConnector1.createConnection();
    }
}

