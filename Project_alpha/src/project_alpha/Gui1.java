package project_alpha;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Gui1 {

    public static class MuseumGUI1 extends JFrame {
        private JTextArea textArea;
        private JRadioButton hour1, hour2, soldier, student, elder, adult, largeGroupYes, largeGroupNo, photo, video, bronze, silver, gold, cash, card;
        private Connection connection;

        public MuseumGUI1() {
            setTitle("Museum Ticketing System");
            setSize(400, 600);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new BorderLayout());

            // Initialize database connection
            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/e_museum", "root", "");
            } catch (SQLException e) {
                e.printStackTrace();
            }

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new GridLayout(0, 1));

            // Hour
            JLabel hourLabel = new JLabel("Hour:");
            hour1 = new JRadioButton("9-17");
            hour2 = new JRadioButton("17-20");
            ButtonGroup hourGroup = new ButtonGroup();
            hourGroup.add(hour1);
            hourGroup.add(hour2);

            // Customer Type
            JLabel customerTypeLabel = new JLabel("Customer type:");
            soldier = new JRadioButton("Soldier");
            student = new JRadioButton("Student");
            elder = new JRadioButton("Elder");
            adult = new JRadioButton("Adult");
            ButtonGroup customerTypeGroup = new ButtonGroup();
            customerTypeGroup.add(soldier);
            customerTypeGroup.add(student);
            customerTypeGroup.add(elder);
            customerTypeGroup.add(adult);

            // Large Group
            JLabel largeGroupLabel = new JLabel("Large group (>10):");
            largeGroupYes = new JRadioButton("Yes");
            largeGroupNo = new JRadioButton("No");
            ButtonGroup largeGroupGroup = new ButtonGroup();
            largeGroupGroup.add(largeGroupYes);
            largeGroupGroup.add(largeGroupNo);

            // Extras
            JLabel extrasLabel = new JLabel("Extras:");
            photo = new JRadioButton("Photo");
            video = new JRadioButton("Video");
            ButtonGroup extrasGroup = new ButtonGroup();
            extrasGroup.add(photo);
            extrasGroup.add(video);

            // Ticket Type
            JLabel ticketTypeLabel = new JLabel("Ticket Type:");
            bronze = new JRadioButton("Bronze Ticket");
            silver = new JRadioButton("Silver Ticket");
            gold = new JRadioButton("Gold Ticket");
            ButtonGroup ticketTypeGroup = new ButtonGroup();
            ticketTypeGroup.add(bronze);
            ticketTypeGroup.add(silver);
            ticketTypeGroup.add(gold);

            // Payment Method
            JLabel paymentMethodLabel = new JLabel("Payment method:");
            cash = new JRadioButton("Cash");
            card = new JRadioButton("Card");
            ButtonGroup paymentMethodGroup = new ButtonGroup();
            paymentMethodGroup.add(cash);
            paymentMethodGroup.add(card);

            mainPanel.add(hourLabel);
            mainPanel.add(hour1);
            mainPanel.add(hour2);
            mainPanel.add(customerTypeLabel);
            mainPanel.add(soldier);
            mainPanel.add(student);
            mainPanel.add(elder);
            mainPanel.add(adult);
            mainPanel.add(largeGroupLabel);
            mainPanel.add(largeGroupYes);
            mainPanel.add(largeGroupNo);
            mainPanel.add(extrasLabel);
            mainPanel.add(photo);
            mainPanel.add(video);
            mainPanel.add(ticketTypeLabel);
            mainPanel.add(bronze);
            mainPanel.add(silver);
            mainPanel.add(gold);
            mainPanel.add(paymentMethodLabel);
            mainPanel.add(cash);
            mainPanel.add(card);


            JPanel bottomPanel = new JPanel(new BorderLayout());

            textArea = new JTextArea();
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(getWidth(), getHeight() * 2 / 10));
            bottomPanel.add(scrollPane, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout());
            JButton monthlyReportButton = new JButton("Monthly report");
            JButton printButton = new JButton("Print");
            JButton saveButton = new JButton("Save");
            JButton deleteButton = new JButton("Delete");
            buttonPanel.add(monthlyReportButton);
            buttonPanel.add(printButton);
            buttonPanel.add(saveButton);
            buttonPanel.add(deleteButton);
            bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

            add(mainPanel, BorderLayout.CENTER);
            add(bottomPanel, BorderLayout.SOUTH);

            printButton.addActionListener(e -> printTicket());

            deleteButton.addActionListener(e -> deleteTicket());

            monthlyReportButton.addActionListener(e -> showMonthlyReport());
            saveButton.addActionListener(e -> saveTicket());
        }

        private void printTicket() {
            StringBuilder message = new StringBuilder();
            boolean hasError = false;

                    // Hour
                    if (!hour1.isSelected() && !hour2.isSelected()) {
                        message.append("The hour field is empty!\n");
                        hasError = true;
                    }

                    // Customer Type
                    if (!soldier.isSelected() && !student.isSelected() && !elder.isSelected() && !adult.isSelected()) {
                        message.append("The customer type field is empty!\n");
                        hasError = true;
                    }

                    // Large Group
                    if (!largeGroupYes.isSelected() && !largeGroupNo.isSelected()) {
                        message.append("The large group field is empty!\n");
                        hasError = true;
                    }

                    // Extras
                    if (!photo.isSelected() && !video.isSelected()) {
                        message.append("The extras field is empty!\n");
                        hasError = true;
                    }

                    // Ticket Type
                    if (!bronze.isSelected() && !silver.isSelected() && !gold.isSelected()) {
                        message.append("The ticket type field is empty!\n");
                        hasError = true;
                    }

                    // Payment Method
                    if (!cash.isSelected() && !card.isSelected()) {
                        message.append("The payment method field is empty!\n");
                        hasError = true;
                    }

                    if (hasError) {
                        textArea.setText(message.toString());
                    }else
                    {
            String hour = hour1.isSelected() ? "9-17" : "17-20";
            String customerType = soldier.isSelected() ? "Soldier" :
                    student.isSelected() ? "Student" :
                            elder.isSelected() ? "Elder" : "Adult";
            boolean isLargeGroup = largeGroupYes.isSelected();
            boolean isPhoto = photo.isSelected();
            boolean isVideo = video.isSelected();
            String ticketType = bronze.isSelected() ? "Bronze Ticket" :
                    silver.isSelected() ? "Silver Ticket" : "Gold Ticket";
            String paymentMethod = cash.isSelected() ? "Cash" : "Card";

            double price = calculatePrice(hour, customerType, isLargeGroup, isPhoto, isVideo, ticketType);

            String ticketDetails = String.format("Hour: %s\nCustomer type: %s\nLarge group: %s\nExtras: %s %s\nTicket Type: %s\nPayment method: %s\nPrice: %.2f",
                    hour, customerType, isLargeGroup ? "Yes" : "No", isPhoto ? "Photo" : "", isVideo ? "Video" : "", ticketType, paymentMethod, price);
            textArea.setText(ticketDetails);
                    }
        }
        

        private double calculatePrice(String hour, String customerType, boolean isLargeGroup, boolean isPhoto, boolean isVideo, String ticketType) {
            double basePrice = (hour.equals("9-17")) ? 30 : 20;

            if (customerType.equals("Soldier")) basePrice *= 0.7;
            else if (customerType.equals("Student")) basePrice *= 0.8;
            else if (customerType.equals("Elder")) basePrice *= 0.5;

            if (isLargeGroup) basePrice *= 0.9;

            if (isPhoto) basePrice += 5;
            if (isVideo) basePrice += 10;

            if (ticketType.equals("Silver Ticket")) basePrice *= 1.5;
            else if (ticketType.equals("Gold Ticket")) basePrice *= 2;

            return basePrice;
        }
        
        private int getTicketTypeID() {
    try {
        String ticketType = null;
        int ticketTypeID = -1;

        if (bronze.isSelected()) {
            ticketType = "Bronze Ticket";
        } else if (silver.isSelected()) {
            ticketType = "Silver Ticket";
        } else if (gold.isSelected()) {
            ticketType = "Gold Ticket";
        } else {
            textArea.setText("Please select a ticket type.");
            return -1;
        }

        String sqlSelect = "SELECT TicketTypeID FROM TicketTypes WHERE Name = ?";
        PreparedStatement selectStatement = connection.prepareStatement(sqlSelect);
        selectStatement.setString(1, ticketType);

        ResultSet resultSet = selectStatement.executeQuery();

        if (!resultSet.next()) {
            textArea.setText("Ticket type does not exist in the database.");
            return -1;
        }

        return resultSet.getInt("TicketTypeID");
    } catch (SQLException ex) {
        ex.printStackTrace();
        textArea.setText("Error retrieving ticket type ID.");
        return -1;
    }
}


        private void saveTicket() {
            try {
                int visitorID = getVisitorID();

                if (visitorID == -1) {
                    return;
                }

                int ticketTypeID = getTicketTypeID();

                if (ticketTypeID == -1) {
                    return;
                }

                Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                // SQL statement insert ticket
                String sql = "INSERT INTO Tickets (VisitorID, TicketTypeID, PurchaseDateTime) VALUES (?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, visitorID);
                statement.setInt(2, ticketTypeID);
                statement.setTimestamp(3, timestamp);

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    textArea.setText("Ticket saved successfully.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                textArea.setText("Error saving ticket.");
            }
        }


        private int getVisitorID() {
            try {
                String visitorName = "";
                String visitorEmail = "";
                String visitorPhone = "";
                int visitorAge = 0;
                String customerType = "";

                JTextField nameField = new JTextField();
                JTextField emailField = new JTextField();
                JTextField phoneField = new JTextField();
                JTextField ageField = new JTextField();

                JPanel panel = new JPanel(new GridLayout(5, 2));
                panel.add(new JLabel("Name:"));
                panel.add(nameField);
                panel.add(new JLabel("Email:"));
                panel.add(emailField);
                panel.add(new JLabel("Phone:"));
                panel.add(phoneField);
                panel.add(new JLabel("Age:"));
                panel.add(ageField);

                int result = JOptionPane.showConfirmDialog(null, panel, "Enter Visitor Details",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    visitorName = nameField.getText();
                    visitorEmail = emailField.getText();
                    visitorPhone = phoneField.getText();
                    try {
                        visitorAge = Integer.parseInt(ageField.getText());
                    } catch (NumberFormatException e) {
                        textArea.setText("Invalid age input. Please enter a valid number.");
                        return -1;
                    }
                } else {
                    return -1;
                }

                if (soldier.isSelected()) {
                    customerType = "Soldier";
                } else if (student.isSelected()) {
                    customerType = "Student";
                } else if (elder.isSelected()) {
                    customerType = "Elder";
                } else if (adult.isSelected()) {
                    customerType = "Adult";
                } else {
                    textArea.setText("Please select a customer type.");
                    return -1;
                }

                String sqlCheck = "SELECT VisitorID FROM Visitors WHERE Name = ? AND Email = ? AND Phone = ?";
                PreparedStatement statementCheck = connection.prepareStatement(sqlCheck);
                statementCheck.setString(1, visitorName);
                statementCheck.setString(2, visitorEmail);
                statementCheck.setString(3, visitorPhone);
                ResultSet resultSetCheck = statementCheck.executeQuery();

                if (resultSetCheck.next()) {
                    return resultSetCheck.getInt("VisitorID");
                } else {
                    String sqlInsert = "INSERT INTO Visitors (Name, Age, Status, GroupSize, Email, Phone, RegistrationDate) VALUES (?, ?, ?, ?, ?, ?, NOW())";
                    PreparedStatement statementInsert = connection.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
                    statementInsert.setString(1, visitorName);
                    statementInsert.setInt(2, visitorAge);
                    statementInsert.setString(3, customerType);
                    statementInsert.setInt(4, 1); 
                    statementInsert.setString(5, visitorEmail);
                    statementInsert.setString(6, visitorPhone);
                    int rowsInserted = statementInsert.executeUpdate();

                    if (rowsInserted > 0) {
                        ResultSet generatedKeys = statementInsert.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            return generatedKeys.getInt(1);
                        } else {
                            return -1;
                        }
                    } else {
                        return -1;
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                textArea.setText("Error retrieving visitor ID.");
                return -1;
            }
        }
        
        private void deleteTicket() {
            try {
                String input = JOptionPane.showInputDialog(this, "Enter Ticket ID to delete:");
                if (input != null && !input.isEmpty()) {
                    int ticketID = Integer.parseInt(input);

                    String sqlDelete = "DELETE FROM Tickets WHERE TicketID = ?";
                    PreparedStatement statement = connection.prepareStatement(sqlDelete);
                    statement.setInt(1, ticketID);

                    int rowsDeleted = statement.executeUpdate();

                    if (rowsDeleted > 0) {
                        textArea.setText("Ticket with ID " + ticketID + " deleted successfully.");
                    } else {
                        textArea.setText("No ticket found with ID " + ticketID + ".");
                    }
                }
            } catch (NumberFormatException | SQLException ex) {
                ex.printStackTrace();
                textArea.setText("Error deleting ticket.");
            }
        }

        private void showMonthlyReport() {
            try {
                String sqlSelect = "SELECT t.TicketID, t.PurchaseDateTime, COUNT(t.TicketID) AS TicketCount, tt.Name AS TicketTypeName, tt.BasePrice, tt.DiscountForStudents, tt.DiscountForSoldiers, tt.DiscountForRetirees, tt.DiscountForGroups, tt.TaxForPhotography, tt.TaxForFilming " +
                                   "FROM Tickets t " +
                                   "INNER JOIN TicketTypes tt ON t.TicketTypeID = tt.TicketTypeID " +
                                   "GROUP BY t.TicketID";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlSelect);

                StringBuilder reportBuilder = new StringBuilder();
                double totalTicketPrice = 0;
                int totalTicketCount = 0;

                while (resultSet.next()) {
                    int ticketID = resultSet.getInt("TicketID");
                    Timestamp purchaseDateTime = resultSet.getTimestamp("PurchaseDateTime");
                    int ticketCount = resultSet.getInt("TicketCount");
                    String ticketTypeName = resultSet.getString("TicketTypeName");
                    double basePrice = resultSet.getDouble("BasePrice");
                    double discountForStudents = resultSet.getDouble("DiscountForStudents");
                    double discountForSoldiers = resultSet.getDouble("DiscountForSoldiers");
                    double discountForRetirees = resultSet.getDouble("DiscountForRetirees");
                    double discountForGroups = resultSet.getDouble("DiscountForGroups");
                    double taxForPhotography = resultSet.getDouble("TaxForPhotography");
                    double taxForFilming = resultSet.getDouble("TaxForFilming");

                    double totalPrice = ticketCount * basePrice;
                    if (discountForStudents > 0 && (student.isSelected() || elder.isSelected())) {
                        totalPrice -= ticketCount * basePrice * discountForStudents;
                    }
                    if (discountForSoldiers > 0 && soldier.isSelected()) {
                        totalPrice -= ticketCount * basePrice * discountForSoldiers;
                    }
                    if (discountForRetirees > 0 && elder.isSelected()) {
                        totalPrice -= ticketCount * basePrice * discountForRetirees;
                    }
                    if (discountForGroups > 0 && largeGroupYes.isSelected()) {
                        totalPrice -= ticketCount * basePrice * discountForGroups;
                    }
                    if (photo.isSelected()) {
                        totalPrice += ticketCount * taxForPhotography;
                    }
                    if (video.isSelected()) {
                        totalPrice += ticketCount * taxForFilming;
                    }

                    totalTicketPrice += totalPrice;
                    totalTicketCount += ticketCount;

                    reportBuilder.append("Ticket ID: ").append(ticketID).append(", ");
                    reportBuilder.append("Date: ").append(purchaseDateTime).append(", ");
                    reportBuilder.append("Ticket Type: ").append(ticketTypeName).append(", ");
                    reportBuilder.append("Total Price: ").append(totalPrice).append("\n");
                }

                reportBuilder.append("\nTotal Price of all tickets: ").append(totalTicketPrice).append("\n");
                reportBuilder.append("Total Tickets Sold: ").append(totalTicketCount).append("\n");

                textArea.setText(reportBuilder.toString());
            } catch (SQLException ex) {
                ex.printStackTrace();
                textArea.setText("Error retrieving monthly report.");
            }
        }

    public static void main(String[] args) {
        MuseumGUI1 museumGUI1 = new MuseumGUI1();
        museumGUI1.setVisible(true);
    }
}
}