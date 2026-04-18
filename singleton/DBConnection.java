package singleton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * SINGLETON PATTERN - DBConnection
 * 
 * Ensures only ONE database connection exists throughout the application.
 * Design Pattern: Singleton (Thread-safe using synchronized)
 * Design Principle: SRP - Only responsible for managing DB connection.
 */
public class DBConnection {

    // The single instance - volatile ensures visibility across threads
    private static volatile DBConnection instance = null;

    private String connectionStatus;
    private Connection connection;

    // Private constructor - prevents direct instantiation
    private DBConnection() {
        initialize();
    }

    // Global access point - Thread-safe Singleton
    public static DBConnection getInstance() {
        if (instance == null) {
            synchronized (DBConnection.class) {
                if (instance == null) {
                    instance = new DBConnection();
                }
            }
        }
        return instance;
    }

    public String getConnectionStatus() {
        return connectionStatus;
    }

    public synchronized Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlite:movie_ticket_booking.db");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to open SQLite connection", e);
        }
        return connection;
    }

    // Simulate saving a record to DB
    public void save(String record) {
        System.out.println("[DBConnection] " + record);
    }

    // Simulate fetching from DB
    public void fetch(String query) {
        System.out.println("[DBConnection] " + query);
        }

        private void initialize() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:movie_ticket_booking.db");
            connectionStatus = "CONNECTED";
            createSchema(connection);
            System.out.println("[DBConnection] SQLite database initialized.");
        } catch (SQLException e) {
            connectionStatus = "FAILED";
            throw new RuntimeException("Failed to initialize SQLite database", e);
        }
        }

        private void createSchema(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users ("
                + "user_id INTEGER PRIMARY KEY,"
                + "name TEXT NOT NULL,"
                + "email TEXT NOT NULL UNIQUE,"
                + "password TEXT NOT NULL,"
                + "user_type TEXT NOT NULL"
                + ")");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS movies ("
                + "movie_id INTEGER PRIMARY KEY,"
                + "title TEXT NOT NULL,"
                + "genre TEXT NOT NULL,"
                + "language TEXT NOT NULL,"
                + "rating REAL NOT NULL,"
                + "active INTEGER NOT NULL,"
                + "duration REAL,"
                + "director TEXT,"
                + "cast_info TEXT,"
                + "release_date TEXT,"
                + "description TEXT"
                + ")");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS shows ("
                + "show_id INTEGER PRIMARY KEY,"
                + "movie_id INTEGER,"
                + "show_time TEXT,"
                + "show_date TEXT,"
                + "show_time_datetime TEXT,"
                + "auditorium TEXT,"
                + "total_seats INTEGER,"
                + "available_seats INTEGER,"
                + "base_price REAL,"
                + "language TEXT,"
                + "format TEXT,"
                + "is_active INTEGER NOT NULL,"
                + "FOREIGN KEY(movie_id) REFERENCES movies(movie_id)"
                + ")");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS seats ("
                + "seat_id INTEGER,"
                + "show_id INTEGER NOT NULL,"
                + "seat_number TEXT NOT NULL,"
                + "seat_type TEXT NOT NULL,"
                + "price REAL NOT NULL,"
                + "status TEXT NOT NULL,"
                + "PRIMARY KEY(show_id, seat_number),"
                + "FOREIGN KEY(show_id) REFERENCES shows(show_id)"
                + ")");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS bookings ("
                + "booking_id INTEGER PRIMARY KEY,"
                + "customer_id INTEGER NOT NULL,"
                + "booking_date TEXT NOT NULL,"
                + "total_amount REAL NOT NULL,"
                + "status TEXT NOT NULL,"
                + "show_id INTEGER,"
                + "FOREIGN KEY(customer_id) REFERENCES users(user_id),"
                + "FOREIGN KEY(show_id) REFERENCES shows(show_id)"
                + ")");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS booking_seats ("
                + "booking_id INTEGER NOT NULL,"
                + "show_id INTEGER NOT NULL,"
                + "seat_number TEXT NOT NULL,"
                + "seat_id INTEGER,"
                + "seat_type TEXT NOT NULL,"
                + "price REAL NOT NULL,"
                + "PRIMARY KEY(booking_id, seat_number),"
                + "FOREIGN KEY(booking_id) REFERENCES bookings(booking_id),"
                + "FOREIGN KEY(show_id, seat_number) REFERENCES seats(show_id, seat_number)"
                + ")");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS payments ("
                + "payment_id INTEGER PRIMARY KEY,"
                + "amount REAL NOT NULL,"
                + "payment_date INTEGER NOT NULL,"
                + "status TEXT NOT NULL,"
                + "method TEXT NOT NULL,"
                + "booking_id INTEGER NOT NULL,"
                + "FOREIGN KEY(booking_id) REFERENCES bookings(booking_id)"
                + ")");
        }
    }
}
