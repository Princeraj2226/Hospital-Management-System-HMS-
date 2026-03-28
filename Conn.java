package Hospital_management_system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Conn class handles database connection for Hospital Management System.
 * Implements AutoCloseable to allow try-with-resources usage.
 */
public class Conn implements AutoCloseable {

    private Connection connection;
    private Statement statement;

    // Constructor to initialize the database connection
    public Conn() {
        try {
            // Load MySQL JDBC driver (optional in modern Java)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to database
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/hms", // Database URL
                    "root",                            // DB username
                    "Your password"                          // DB password (use config/env in production)
            );

            // Create statement for executing queries
            statement = connection.createStatement();

            System.out.println("Database connected successfully!");

        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error connecting to the database.");
            e.printStackTrace();
        }
    }

    // Getter for Connection
    public Connection getConnection() {
        return connection;
    }

    // Getter for Statement
    public Statement getStatement() {
        return statement;
    }

    // Close connection and statement safely
    @Override
    public void close() {
        try {
            if (statement != null && !statement.isClosed()) statement.close();
            if (connection != null && !connection.isClosed()) connection.close();
            System.out.println("Database connection closed.");
        } catch (SQLException e) {
            System.err.println("Error closing the database connection.");
            e.printStackTrace();
        }
    }
}