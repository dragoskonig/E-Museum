package project_alpha;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Gui4 {

    public static class MuseumGUI4 extends JFrame {
        private JTextField nameField, basePriceField, discountStudentField, discountSoldierField, discountRetireeField,
                discountGroupField, taxPhotoField, taxVideoField, descriptionField, availableFromField, availableUntilField;
        private Connection connection;
        private JTextArea textArea;

        public MuseumGUI4() {
            setTitle("New Ticket Type");
            setSize(400, 600);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new BorderLayout());

            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/e_museum", "root", "");
            } catch (SQLException e) {
                e.printStackTrace();
            }

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new GridLayout(0, 2));

            JLabel nameLabel = new JLabel("Name:");
            nameField = new JTextField();

            JLabel basePriceLabel = new JLabel("Base Price:");
            basePriceField = new JTextField();

            JLabel discountStudentLabel = new JLabel("Discount for Students:");
            discountStudentField = new JTextField();

            JLabel discountSoldierLabel = new JLabel("Discount for Soldiers:");
            discountSoldierField = new JTextField();

            JLabel discountRetireeLabel = new JLabel("Discount for Retirees:");
            discountRetireeField = new JTextField();

            JLabel discountGroupLabel = new JLabel("Discount for Groups:");
            discountGroupField = new JTextField();

            JLabel taxPhotoLabel = new JLabel("Tax for Photography:");
            taxPhotoField = new JTextField();

            JLabel taxVideoLabel = new JLabel("Tax for Filming:");
            taxVideoField = new JTextField();

            JLabel descriptionLabel = new JLabel("Description:");
            descriptionField = new JTextField();

            JLabel availableFromLabel = new JLabel("Available From:");
            availableFromField = new JTextField();

            JLabel availableUntilLabel = new JLabel("Available Until:");
            availableUntilField = new JTextField();

            mainPanel.add(nameLabel);
            mainPanel.add(nameField);
            mainPanel.add(basePriceLabel);
            mainPanel.add(basePriceField);
            mainPanel.add(discountStudentLabel);
            mainPanel.add(discountStudentField);
            mainPanel.add(discountSoldierLabel);
            mainPanel.add(discountSoldierField);
            mainPanel.add(discountRetireeLabel);
            mainPanel.add(discountRetireeField);
            mainPanel.add(discountGroupLabel);
            mainPanel.add(discountGroupField);
            mainPanel.add(taxPhotoLabel);
            mainPanel.add(taxPhotoField);
            mainPanel.add(taxVideoLabel);
            mainPanel.add(taxVideoField);
            mainPanel.add(descriptionLabel);
            mainPanel.add(descriptionField);
            mainPanel.add(availableFromLabel);
            mainPanel.add(availableFromField);
            mainPanel.add(availableUntilLabel);
            mainPanel.add(availableUntilField);

            JButton saveButton = new JButton("Save");
            saveButton.addActionListener(e -> saveTicketType());

            JPanel bottomPanel = new JPanel(new BorderLayout());

            textArea = new JTextArea();
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(getWidth(), getHeight() * 2 / 10));
            bottomPanel.add(scrollPane, BorderLayout.CENTER);

            bottomPanel.add(saveButton, BorderLayout.SOUTH);

            add(mainPanel, BorderLayout.CENTER);
            add(bottomPanel, BorderLayout.SOUTH);
        }

        private void saveTicketType() {
            StringBuilder message = new StringBuilder();
            boolean hasError = false;

            String name = nameField.getText();
            String basePriceStr = basePriceField.getText();
            String discountStudentStr = discountStudentField.getText();
            String discountSoldierStr = discountSoldierField.getText();
            String discountRetireeStr = discountRetireeField.getText();
            String discountGroupStr = discountGroupField.getText();
            String taxPhotoStr = taxPhotoField.getText();
            String taxVideoStr = taxVideoField.getText();
            String description = descriptionField.getText();
            String availableFrom = availableFromField.getText();
            String availableUntil = availableUntilField.getText();

            if (name.isEmpty()) {
                message.append("The name field is empty!\n");
                hasError = true;
            }
            if (basePriceStr.isEmpty()) {
                message.append("The base price field is empty!\n");
                hasError = true;
            }
            if (discountStudentStr.isEmpty()) {
                message.append("The discount for students field is empty!\n");
                hasError = true;
            }
            if (discountSoldierStr.isEmpty()) {
                message.append("The discount for soldiers field is empty!\n");
                hasError = true;
            }
            if (discountRetireeStr.isEmpty()) {
                message.append("The discount for retirees field is empty!\n");
                hasError = true;
            }
            if (discountGroupStr.isEmpty()) {
                message.append("The discount for groups field is empty!\n");
                hasError = true;
            }
            if (taxPhotoStr.isEmpty()) {
                message.append("The tax for photography field is empty!\n");
                hasError = true;
            }
            if (taxVideoStr.isEmpty()) {
                message.append("The tax for filming field is empty!\n");
                hasError = true;
            }
            if (description.isEmpty()) {
                message.append("The description field is empty!\n");
                hasError = true;
            }
            if (availableFrom.isEmpty()) {
                message.append("The available from field is empty!\n");
                hasError = true;
            }
            if (availableUntil.isEmpty()) {
                message.append("The available until field is empty!\n");
                hasError = true;
            }

            if (hasError) {
                textArea.setText(message.toString());
                return;
            }

            try {
                double basePrice = Double.parseDouble(basePriceStr);
                double discountStudent = Double.parseDouble(discountStudentStr);
                double discountSoldier = Double.parseDouble(discountSoldierStr);
                double discountRetiree = Double.parseDouble(discountRetireeStr);
                double discountGroup = Double.parseDouble(discountGroupStr);
                double taxPhoto = Double.parseDouble(taxPhotoStr);
                double taxVideo = Double.parseDouble(taxVideoStr);

                String sql = "INSERT INTO TicketTypes (Name, BasePrice, DiscountForStudents, DiscountForSoldiers, " +
                        "DiscountForRetirees, DiscountForGroups, TaxForPhotography, TaxForFilming, Description, " +
                        "AvailableFrom, AvailableUntil) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, name);
                statement.setDouble(2, basePrice);
                statement.setDouble(3, discountStudent);
                statement.setDouble(4, discountSoldier);
                statement.setDouble(5, discountRetiree);
                statement.setDouble(6, discountGroup);
                statement.setDouble(7, taxPhoto);
                statement.setDouble(8, taxVideo);
                statement.setString(9, description);
                statement.setString(10, availableFrom);
                statement.setString(11, availableUntil);

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    textArea.setText("New ticket type saved successfully.");
                } else {
                    textArea.setText("Failed to save new ticket type.");
                }
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                textArea.setText("Invalid number format in one of the fields.");
            } catch (SQLException ex) {
                ex.printStackTrace();
                textArea.setText("Error saving new ticket type.");
            }
        }

        public static void main(String[] args) {
            MuseumGUI4 museumGUI4 = new MuseumGUI4();
            museumGUI4.setVisible(true);
        }
    }
}
