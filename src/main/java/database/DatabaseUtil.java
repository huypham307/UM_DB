package database;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;

public class DatabaseUtil {
    private static BasicDataSource dataSource;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/QuackstagramDB";
    private static final String USERNAME = "BCS1510";
    private static final String PASSWORD = "BCS1510";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    public static DataSource setupDataSource(){
        if(dataSource == null){
            BasicDataSource ds = new BasicDataSource();
            ds.setDriverClassName(DRIVER); // Replace with your database driver
            ds.setUrl(DB_URL);
            ds.setUsername(USERNAME);
            ds.setPassword(PASSWORD);

            // Optional pooling configurations
            ds.setInitialSize(5);  // Initial connections in the pool
            ds.setMaxTotal(10);    // Maximum number of connections
            dataSource = ds;
        }
        return dataSource;
    }


}
