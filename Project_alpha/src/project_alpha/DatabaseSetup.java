package project_alpha;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
public class DatabaseSetup {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/e_museum";
        String user = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            createTables(connection);
            insertData(connection);
            System.out.println("Tables and data inserted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTables(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Artists (" +
                    "  ArtistID INT AUTO_INCREMENT PRIMARY KEY," +
                    "  Name VARCHAR(255)," +
                    "  Nationality VARCHAR(255)," +
                    "  BirthDate DATE," +
                    "  DeathDate DATE" +
                    ")");
            
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Exhibitions (" +
                    "  ExhibitionID INT AUTO_INCREMENT PRIMARY KEY," +
                    "  Name VARCHAR(255)," +
                    "  StartDate DATETIME," +
                    "  EndDate DATETIME" +
                    ")");
            
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS ItemCategories (" +
                    "  CategoryID INT AUTO_INCREMENT PRIMARY KEY," +
                    "  Name VARCHAR(255)," +
                    "  Description TEXT" +
                    ")");
                    
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Visitors (" +
                    "  VisitorID INT AUTO_INCREMENT PRIMARY KEY," +
                    "  Name VARCHAR(255)," +
                    "  Age INT," +
                    "  Status VARCHAR(50)," +
                    "  GroupSize INT," +
                    "  Email VARCHAR(255)," +
                    "  Phone VARCHAR(20)," +
                    "  RegistrationDate DATETIME" +
                    ")");
            
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS TicketTypes (" +
                    "  TicketTypeID INT AUTO_INCREMENT PRIMARY KEY," +
                    "  Name VARCHAR(255)," +
                    "  BasePrice FLOAT," +
                    "  DiscountForStudents FLOAT," +
                    "  DiscountForSoldiers FLOAT," +
                    "  DiscountForRetirees FLOAT," +
                    "  DiscountForGroups FLOAT," +
                    "  TaxForPhotography FLOAT," +
                    "  TaxForFilming FLOAT," +
                    "  Description TEXT," +
                    "  AvailableFrom TIME," +
                    "  AvailableUntil TIME" +
                    ")");
                    
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Reservations (" +
                    "  ReservationID INT AUTO_INCREMENT PRIMARY KEY," +
                    "  VisitorID INT," +
                    "  ReservationDateTime DATETIME," +
                    "  ExpiryDateTime DATETIME," +
                    "  ReservedTicketsCount INT," +
                    "  FOREIGN KEY (VisitorID) REFERENCES Visitors(VisitorID)" +
                    ")");
            
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS VisitorFeedback (" +
                    "  FeedbackID INT AUTO_INCREMENT PRIMARY KEY," +
                    "  VisitorID INT," +
                    "  ExhibitionID INT," +
                    "  Feedback TEXT," +
                    "  Rating INT," +
                    "  FeedbackDateTime DATETIME," +
                    "  FOREIGN KEY (VisitorID) REFERENCES Visitors(VisitorID)," +
                    "  FOREIGN KEY (ExhibitionID) REFERENCES Exhibitions(ExhibitionID)" +
                    ")");
            
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS ExhibitionItems (" +
                    "  ItemID INT AUTO_INCREMENT PRIMARY KEY," +
                    "  Type VARCHAR(255)," +
                    "  Description TEXT," +
                    "  ArtistID INT," +
                    "  RegionOfOrigin VARCHAR(255)," +
                    "  YearProduced INT," +
                    "  Position VARCHAR(255)," +
                    "  CategoryID INT," +
                    "  FOREIGN KEY (ArtistID) REFERENCES Artists(ArtistID)," +
                    "  FOREIGN KEY (CategoryID) REFERENCES ItemCategories(CategoryID)" +
                    ")");
                    
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS ExhibitionItemMappings (" +
                    "  MappingID INT AUTO_INCREMENT PRIMARY KEY," +
                    "  ExhibitionID INT," +
                    "  ItemID INT," +
                    "  DisplayOrder INT," +
                    "  FOREIGN KEY (ExhibitionID) REFERENCES Exhibitions(ExhibitionID)," +
                    "  FOREIGN KEY (ItemID) REFERENCES ExhibitionItems(ItemID)" +
                    ")");
                    
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Tickets (" +
                    "  TicketID INT AUTO_INCREMENT PRIMARY KEY," +
                    "  VisitorID INT," +
                    "  TicketTypeID INT," +
                    "  PurchaseDateTime DATETIME," +
                    "  ReservationID INT," +
                    "  PaymentReference VARCHAR(50)," +
                    "  FOREIGN KEY (VisitorID) REFERENCES Visitors(VisitorID)," +
                    "  FOREIGN KEY (TicketTypeID) REFERENCES TicketTypes(TicketTypeID)," +
                    "  FOREIGN KEY (ReservationID) REFERENCES Reservations(ReservationID)" +
                    ")");
                    
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Payments (" +
                    "  PaymentID INT AUTO_INCREMENT PRIMARY KEY," +
                    "  Amount FLOAT," +
                    "  PaymentMethod VARCHAR(50)," +
                    "  PaymentDateTime DATETIME" +
                    ")");
                    
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Transactions (" +
                    "  TransactionID INT AUTO_INCREMENT PRIMARY KEY," +
                    "  PaymentID INT," +
                    "  TransactionType VARCHAR(50)," +
                    "  TransactionDateTime DATETIME," +
                    "  FOREIGN KEY (PaymentID) REFERENCES Payments(PaymentID)" +
                    ")");
                    
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS TicketPayments (" +
                    "  TicketPaymentID INT AUTO_INCREMENT PRIMARY KEY," +
                    "  TicketID INT," +
                    "  PaymentID INT," +
                    "  FOREIGN KEY (TicketID) REFERENCES Tickets(TicketID)," +
                    "  FOREIGN KEY (PaymentID) REFERENCES Payments(PaymentID)" +
                    ")");
        }
    }

    private static void insertData(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            // Insert data into Artists table
            statement.executeUpdate("INSERT INTO Artists (Name, Nationality, BirthDate, DeathDate) VALUES " +
                    "('Leonardo da Vinci', 'Italian', '1452-04-15', '1519-05-02'), " +
                    "('Vincent van Gogh', 'Dutch', '1853-03-30', '1890-07-29'), " +
                    "('Pablo Picasso', 'Spanish', '1881-10-25', '1973-04-08')");

            // Insert data into other tables similarly
            statement.executeUpdate("INSERT INTO Exhibitions (Name, StartDate, EndDate) VALUES " +
                    "('Masters of Renaissance', '2024-06-01', '2024-08-31'), " +
                    "('Modern Art Revolution', '2024-09-01', '2024-11-30'), " +
                    "('Capturing Moments: A Photography Exhibition', '2024-12-01', '2025-02-28')");

            statement.executeUpdate("INSERT INTO ItemCategories (Name, Description) VALUES " +
                    "('Painting', 'Works of art created with paint on a canvas or other surfaces'), " +
                    "('Sculpture', 'Three-dimensional artworks created by shaping or combining materials'), " +
                    "('Photography', 'Images captured by a camera')");

            statement.executeUpdate("INSERT INTO Visitors (Name, Age, Status, GroupSize, Email, Phone, RegistrationDate) VALUES " +
                    "('John Doe', 25, 'Regular', 1, 'john.doe@example.com', '1234567890', NOW()), " +
                    "('Alice Smith', 20, 'Student', 1, 'alice.smith@example.com', '0987654321', NOW()), " +
                    "('Bob Johnson', 30, 'Soldier', 1, 'bob.johnson@example.com', '9876543210', NOW()), " +
                    "('Elderly Person', 65, 'Retiree', 1, 'elderly@example.com', '1231231234', NOW())");

            statement.executeUpdate("INSERT INTO TicketTypes (Name, BasePrice, DiscountForStudents, DiscountForSoldiers, DiscountForRetirees, DiscountForGroups, TaxForPhotography, TaxForFilming, Description, AvailableFrom, AvailableUntil) VALUES " +
                    "('Gold Ticket', 30.00, 0.20, 0.20, 0.20, 0.25, 5.00, 10.00, 'Access to all rooms in the museum', '09:00:00', '20:00:00'), " +
                    "('Silver Ticket', 20.00, 0.15, 0.15, 0.15, 0.20, 3.00, 7.00, 'Access to the first 8 rooms of the museum', '09:00:00', '20:00:00'), " +
                    "('Bronze Ticket', 15.00, 0.10, 0.10, 0.10, 0.15, 2.00, 5.00, 'Access to the first 4 rooms of the museum', '09:00:00', '20:00:00')");

            statement.executeUpdate("INSERT INTO Reservations (VisitorID, ReservationDateTime, ExpiryDateTime, ReservedTicketsCount) VALUES " +
                    "(1, NOW(), NOW() + INTERVAL 1 DAY, 2), " +
                    "(2, NOW(), NOW() + INTERVAL 1 DAY, 3)");

            statement.executeUpdate("INSERT INTO ExhibitionItems (Type, Description, ArtistID, RegionOfOrigin, YearProduced, Position, CategoryID) VALUES " +
                    "('Painting', 'Mona Lisa is a portrait painting by Leonardo da Vinci.', 1, 'Florence, Italy', 1503, 'Room 1', 1), " +
                    "('Painting', 'Starry Night depicts the view from the window of Van Gogh’s asylum room.', 2, 'Saint-Rémy-de-Provence, France', 1889, 'Room 2', 1), " +
                    "('Sculpture', 'Guernica is a large mural by Pablo Picasso.', 3, 'Paris, France', 1937, 'Room 3', 1), " +
                    "('Photography', 'The Falling Soldier is a photograph by Robert Capa.', NULL, NULL, 1936, 'Room 4', 3)");

            statement.executeUpdate("INSERT INTO ExhibitionItemMappings (ExhibitionID, ItemID, DisplayOrder) VALUES " +
                    "(1, 1, 1), " +
                    "(1, 2, 2), " +
                    "(2, 3, 1), " +
                    "(3, 4, 1)");

            statement.executeUpdate("INSERT INTO Payments (Amount, PaymentMethod, PaymentDateTime) VALUES " +
                    "(60.00, 'Credit Card', NOW()), " +
                    "(45.00, 'Cash', NOW() - INTERVAL 1 DAY)");

            statement.executeUpdate("INSERT INTO Tickets (VisitorID, TicketTypeID, PurchaseDateTime, ReservationID, PaymentReference) VALUES " +
                    "(1, 1, NOW(), NULL, 'ABC123'), " +
                    "(2, 2, NOW(), 1, 'DEF456'), " +
                    "(3, 3, NOW(), NULL, 'GHI789')");

            statement.executeUpdate("INSERT INTO Transactions (PaymentID, TransactionType, TransactionDateTime) VALUES " +
                    "(1, 'Sale', NOW()), " +
                    "(2, 'Sale', NOW() - INTERVAL 1 DAY)");

            statement.executeUpdate("INSERT INTO TicketPayments (TicketID, PaymentID) VALUES " +
                    "(1, 1), " +
                    "(2, 1), " +
                    "(3, 2)");

            statement.executeUpdate("INSERT INTO VisitorFeedback (VisitorID, ExhibitionID, Feedback, Rating, FeedbackDateTime) VALUES " +
                    "(1, 1, 'The exhibition was fantastic! I particularly enjoyed the paintings by Leonardo da Vinci.', 5, NOW()), " +
                    "(2, 2, 'Great exhibition showcasing modern artworks. The organization was excellent.', 4, NOW() - INTERVAL 1 DAY), " +
                    "(3, 3, 'The photography exhibition was captivating. I wish there were more exhibits like this.', 4, NOW())");
        }
    }
}
