import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
//import java.sql.PreparedStatement; //required for test function
//import java.sql.ResultSet;

// connects to the database
public class bbaDatabase {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/bba_db"; //specify the database IP address, port, and name here
    private static final String USER = "publicUser";  //user with read/write access only for schema (this wont work if there is not a "publicUser" in bba_db
    private static final String PASSWORD = "BasicBank0327"; //password for user

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    //TEST FUNCTION
    /*
    //here is a function to check if a user name exists in the 'users' table that was used for testing
    public static boolean checkUsername(String username) {
        String query = "SELECT 1 FROM users WHERE username = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);  // Set the username parameter in the query
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();  // If a result is returned, the username exists
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while checking the username: " + e.getMessage());
        }
        return false;
    }


    /*
    public static void main(String[] args) {
        try {
            if (checkUsername("bob")) {
                System.out.println("Username 'bob' exists in the database.");
            } else {
                System.out.println("Username 'bob' does not exist in the database.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
    */
}
