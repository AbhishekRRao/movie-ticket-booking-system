package singleton;

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

    // Private constructor - prevents direct instantiation
    private DBConnection() {
        this.connectionStatus = "CONNECTED";
        System.out.println("[DBConnection] Database connection established.");
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

    // Simulate saving a record to DB
    public void save(String record) {
        System.out.println("[DBConnection] Saving to DB: " + record);
    }

    // Simulate fetching from DB
    public void fetch(String query) {
        System.out.println("[DBConnection] Fetching from DB: " + query);
    }
}
