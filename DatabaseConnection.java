import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseConnection {
    public static void main(String[] args) {
        // Database connection parameters
        String url = "jdbc:mysql://localhost:3306/seminar_management"; // Use your database name
        String username = "root"; // Default MySQL username in XAMPP is "root"
        String password = ""; // Default password is empty in XAMPP

        // JDBC Connection
        Connection conn = null;

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection to the database
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connection successful!");

            // SQL INSERT query
            String sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";

            // Create a PreparedStatement
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            // Set the values for the parameters (replace these with user input or hardcoded values)
            stmt.setString(1, "John Doe");  // Name
            stmt.setString(2, "john.doe@example.com");  // Email
            stmt.setString(3, "password123");  // Password

            // Execute the query
            int rowsAffected = stmt.executeUpdate();

            // Check if the insertion was successful
            if (rowsAffected > 0) {
                System.out.println("User added successfully!");
            } else {
                System.out.println("Failed to add user.");
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Database connection failed!");
        } finally {
            try {
                if (conn != null) {
                    conn.close();  // Close the connection
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
