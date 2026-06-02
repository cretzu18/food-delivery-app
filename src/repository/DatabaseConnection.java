package repository;
import model.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private String url;
    private String user;
    private String password;

    private DatabaseConnection() {
        url = "jdbc:postgresql://localhost:5432/food_delivery_bd";
        user = "postgres";
        password = "YOUR_PASSWORD";
        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Conexiunea la baza de date a fost realizata cu succes!");
        } catch (SQLException e) {
            System.err.println("Eroare la conectarea cu baza de date: " + e.getMessage());
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}

