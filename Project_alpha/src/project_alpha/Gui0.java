package project_alpha;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Gui0 {

    public static class TicketReservationSystem extends JFrame {
        private JTextField nameTextField, emailTextField, phoneTextField; 
        private JComboBox<String> numberOfTicketsComboBox;
        private JButton reserveTicketsButton;
        private JRadioButton cash, card; 

        public TicketReservationSystem() {
            setTitle("Ticket Reservation System");
            setSize(400, 350); 
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new BorderLayout());

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

            // Name
            JLabel nameLabel = new JLabel("Visitor Name:");
            nameTextField = new JTextField(20);
            JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            namePanel.add(nameLabel);
            namePanel.add(nameTextField);

            // Email
            JLabel emailLabel = new JLabel("Email:");
            emailTextField = new JTextField(20);
            JPanel emailPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            emailPanel.add(emailLabel);
            emailPanel.add(emailTextField);

            // Phone
            JLabel phoneLabel = new JLabel("Phone:");
            phoneTextField = new JTextField(20);
            JPanel phonePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            phonePanel.add(phoneLabel);
            phonePanel.add(phoneTextField);

            // Number of Tickets
            JLabel numberOfTicketsLabel = new JLabel("Number of Tickets:");
            String[] ticketNumbers = {"1", "2", "3", "4", "5"};
            numberOfTicketsComboBox = new JComboBox<>(ticketNumbers);
            JPanel numberOfTicketsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            numberOfTicketsPanel.add(numberOfTicketsLabel);
            numberOfTicketsPanel.add(numberOfTicketsComboBox);

            // Payment Method
            JLabel paymentMethodLabel = new JLabel("Payment Method:");
            cash = new JRadioButton("Cash");
            card = new JRadioButton("Card");
            ButtonGroup paymentMethodGroup = new ButtonGroup();
            paymentMethodGroup.add(cash);
            paymentMethodGroup.add(card);
            JPanel paymentMethodPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            paymentMethodPanel.add(paymentMethodLabel);
            paymentMethodPanel.add(cash);
            paymentMethodPanel.add(card);

            reserveTicketsButton = new JButton("Reserve Tickets");

            mainPanel.add(namePanel);
            mainPanel.add(emailPanel); 
            mainPanel.add(phonePanel);
            mainPanel.add(numberOfTicketsPanel);
            mainPanel.add(paymentMethodPanel);
            mainPanel.add(reserveTicketsButton);

            add(mainPanel, BorderLayout.CENTER);

            reserveTicketsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    reserveTickets();
                }
            });
        }

        private void reserveTickets() {
            StringBuilder message = new StringBuilder();
            boolean hasError = false;

            String name = nameTextField.getText();
            String email = emailTextField.getText(); 
            String phone = phoneTextField.getText();

            if (name.isEmpty()) {
                message.append("The name field is empty!\n");
                hasError = true;
            }
            if (email.isEmpty()) {
                message.append("The email field is empty!\n");
                hasError = true;
            }
            if (phone.isEmpty()) {
                message.append("The phone field is empty!\n");
                hasError = true;
            }

            if (!cash.isSelected() && !card.isSelected()) {
                message.append("The payment method is empty!\n");
                hasError = true;
            }

            if (hasError) {
                JOptionPane.showMessageDialog(this, message.toString(), "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int numberOfTickets = Integer.parseInt((String) numberOfTicketsComboBox.getSelectedItem());
            String[] ticketTypes = {"Gold Ticket", "Silver Ticket", "Bronze Ticket"}; 

            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/e_museum", "root", "");
                 PreparedStatement ps = con.prepareStatement("INSERT INTO Reservations (VisitorID, ReservationDateTime, ExpiryDateTime, ReservedTicketsCount) VALUES (?, NOW(), NOW() + INTERVAL 1 DAY, ?)")) {
                // Fetch VisitorID 
                int visitorID = getVisitorID(con, name, email, phone);

                // Update GroupSize
                updateVisitorGroupSize(con, visitorID, numberOfTickets);

                // Reserve tickets
                for (int i = 0; i < numberOfTickets; i++) {
                    String selectedTicketType = (String) JOptionPane.showInputDialog(this, "Choose ticket type for ticket " + (i + 1), "Select Ticket Type", JOptionPane.QUESTION_MESSAGE, null, ticketTypes, ticketTypes[0]);
                    reserveTicket(con, visitorID, selectedTicketType);
                }

                JOptionPane.showMessageDialog(this, "Tickets reserved successfully!");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error reserving tickets: " + ex.getMessage());
            }
        }

        private void updateVisitorGroupSize(Connection con, int visitorID, int numberOfTickets) throws SQLException {
            String updateQuery = "UPDATE Visitors SET GroupSize = ? WHERE VisitorID = ?";
            try (PreparedStatement ps = con.prepareStatement(updateQuery)) {
                ps.setInt(1, numberOfTickets);
                ps.setInt(2, visitorID);
                ps.executeUpdate();
            }
        }

        private void reserveTicket(Connection con, int visitorID, String ticketType) throws SQLException {
            try (PreparedStatement ps = con.prepareStatement("INSERT INTO Tickets (VisitorID, TicketTypeID, PurchaseDateTime) VALUES (?, ?, NOW())")) {
                int ticketTypeID = getTicketTypeID(con, ticketType);
                ps.setInt(1, visitorID);
                ps.setInt(2, ticketTypeID);
                ps.executeUpdate();
            }
        }

        private int getTicketTypeID(Connection con, String ticketType) throws SQLException {
            int ticketTypeID = -1;
            String query = "SELECT TicketTypeID FROM TicketTypes WHERE Name = ?";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, ticketType);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        ticketTypeID = rs.getInt("TicketTypeID");
                    }
                }
            }
            return ticketTypeID;
        }

        private int getVisitorID(Connection con, String name, String email, String phone) throws SQLException {
            // Retrieve visitor ID
            int visitorID = -1;

            // Query for fetching visitor ID
            String query = "SELECT VisitorID FROM Visitors WHERE Name = ?";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, name);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        visitorID = rs.getInt("VisitorID");
                    } else {
                        // If visitor not found, insert a new visitor record and get the generated ID
                        visitorID = insertNewVisitor(con, name, email, phone);
                    }
                }
            }

            return visitorID;
        }

        private int insertNewVisitor(Connection con, String name, String email, String phone) throws SQLException {
            int visitorID = -1;
            // Insert visitor record
            String insertQuery = "INSERT INTO Visitors (Name, Age, Status, GroupSize, Email, Phone, RegistrationDate) VALUES (?, ?, ?, ?, ?, ?, NOW())";
            try (PreparedStatement ps = con.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, name);
                ps.setInt(2, 0); 
                ps.setString(3, "Regular"); 
                ps.setInt(4, 1); 
                ps.setString(5, email); 
                ps.setString(6, phone); 
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected == 1) {
                    // Retrieve generated visitor ID
                    ResultSet generatedKeys = ps.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        visitorID = generatedKeys.getInt(1);
                    }
                }
            }
            return visitorID;
        }

        public static void main(String[] args) {
            TicketReservationSystem ticketReservationSystem = new TicketReservationSystem();
            ticketReservationSystem.setVisible(true);
        }
    }
}
