import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {

    private static Connection dbConn = null;
    private static String dbName = "[DATABASE_PATH]";

    public static Connection connect(){

        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("PostgreSQL driver registered.");

            try{
                dbConn = DriverManager.getConnection(dbName, "[DATABASE_USERNAME]", "[DATABASE_PASSWORD]");
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (dbConn != null) {
                System.out.println("Database accessed!");
                return dbConn;
            } else {
                System.out.println("Failed to make connection");
            }

        } catch (ClassNotFoundException ex) {
            System.out.println("Driver not found");
        }

        return null;
    }
}
