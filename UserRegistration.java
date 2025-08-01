import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Base64;
public class UserRegistration {
    //add user with username and hashed password
    public static boolean addUser(String username, String password) {
        // Generate a random salt
        String salt = generateSalt();
        if (salt == null) {
            return false;  // Fail if salt generation fails
        }
        //hash the password with the salt
        String hashedPassword = hashPassword(password, salt);
        if (hashedPassword == null) {
            return false;  // Fail if hashing fails
        }
        //connect to the database and insert the user
        String insertQuery = "INSERT INTO users (username, password, salt) VALUES (?, ?, ?)";
        try (Connection connection = bbaDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(insertQuery)) {

            statement.setString(1, username);  //set the username
            statement.setString(2, hashedPassword);  //set the hashed password
            statement.setString(3, salt);  //store the salt
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;  //return true if insertion was successful

        } catch (SQLException e) {
            System.out.println("Error inserting user: " + e.getMessage());
            return false;
        }
    }
    // Generate a random salt
    private static String generateSalt() {
        try {
            SecureRandom random = new SecureRandom();
            byte[] saltBytes = new byte[16];
            random.nextBytes(saltBytes);
            return Base64.getEncoder().encodeToString(saltBytes);  //encode to Base64 for storage
        } catch (Exception e) {
            System.out.println("Error generating salt: " + e.getMessage());
            return null;
        }
    }
    //hash password with SHA-256 and salt
    private static String hashPassword(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String saltedPassword = password + salt;  //combine password with salt
            byte[] hash = digest.digest(saltedPassword.getBytes());  //hash the salted password
            StringBuilder hexString = new StringBuilder();

            //convert hash bytes to hexadecimal format
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();  //return hashed password as hex string

        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error hashing password: " + e.getMessage());
            return null;  //rturn null if hashing fails
        }
    }
    // TEST FUNCTION
    /*
    public static void main(String[] args) {
        String testUsername = "ron";  // Replace with a test username
        String testPassword = "securepassword";  // Replace with a test password
        if (addUser(testUsername, testPassword)) {
            System.out.println("User '" + testUsername + "' added successfully!");
        } else {
            System.out.println("Failed to add user '" + testUsername + "'.");
        }
    }
    */
}
