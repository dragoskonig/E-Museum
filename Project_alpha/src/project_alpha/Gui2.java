package project_alpha;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class Gui2 {

    public static class MuseumGUI2 extends JFrame {

        private Connection connection;
        private ArrayList<ExhibitionItem> exhibitionItems;

        public MuseumGUI2() {
            setTitle("Museum Exhibit Entry");
            setSize(400, 600);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new BorderLayout());

            exhibitionItems = new ArrayList<>();

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/e_museum", "root", "");
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to connect to the database.", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }

            JPanel mainPanel = new JPanel(new BorderLayout());

            // Type
            JPanel radioPanel = new JPanel(new GridLayout(0, 1));
            JLabel typeLabel = new JLabel("Type:");
            JRadioButton paintingButton = new JRadioButton("Painting");
            JRadioButton sculptureButton = new JRadioButton("Sculpture");
            JRadioButton photographyButton = new JRadioButton("Photography");
            JRadioButton fossilButton = new JRadioButton("Fossil");
            JRadioButton relicButton = new JRadioButton("Relic");
            ButtonGroup typeGroup = new ButtonGroup();
            typeGroup.add(paintingButton);
            typeGroup.add(sculptureButton);
            typeGroup.add(photographyButton);
            typeGroup.add(fossilButton);
            typeGroup.add(relicButton);
            radioPanel.add(typeLabel);
            radioPanel.add(paintingButton);
            radioPanel.add(sculptureButton);
            radioPanel.add(photographyButton);
            radioPanel.add(fossilButton);
            radioPanel.add(relicButton);

            // Short desc + Region + Year + Room
            JPanel textFieldPanel = new JPanel(new GridLayout(0, 1));
            JLabel shortDescriptionLabel = new JLabel("Description:");
            JTextField shortDescriptionTextField = new JTextField();
            JLabel regionLabel = new JLabel("Region:");
            JTextField regionTextField = new JTextField();
            JLabel yearLabel = new JLabel("Year:");
            JTextField yearTextField = new JTextField();
            JLabel roomLabel = new JLabel("Room:");
            String[] rooms = {"Room 1", "Room 2", "Room 3", "Room 4", "Room 5", "Room 6", "Room 7", "Room 8", "Room 9", "Room 10"};
            JComboBox<String> roomComboBox = new JComboBox<>(rooms);
            JLabel artistLabel = new JLabel("Artist:");
            JTextField artistTextField = new JTextField(); 
            textFieldPanel.add(shortDescriptionLabel);
            textFieldPanel.add(shortDescriptionTextField);
            textFieldPanel.add(regionLabel);
            textFieldPanel.add(regionTextField);
            textFieldPanel.add(yearLabel);
            textFieldPanel.add(yearTextField);
            textFieldPanel.add(artistLabel);
            textFieldPanel.add(artistTextField);
            textFieldPanel.add(roomLabel);
            textFieldPanel.add(roomComboBox);

            // output
            JPanel output = new JPanel(new BorderLayout());
            JTextArea output_text = new JTextArea();
            output_text.setLineWrap(true);
            output_text.setWrapStyleWord(true);
            output_text.setEditable(false);
            output_text.setRows(5);
            JScrollPane additionalScrollPane = new JScrollPane(output_text);
            output.add(additionalScrollPane, BorderLayout.CENTER);

            // the 4 buttons
            JPanel buttonPanel = new JPanel(new FlowLayout());
            JButton showItemsButton = new JButton("Show items");
            JButton printButton = new JButton("Print");
            JButton saveButton = new JButton("Save");
            JButton deleteButton = new JButton("Delete");
            buttonPanel.add(showItemsButton);
            buttonPanel.add(printButton);
            buttonPanel.add(saveButton);
            buttonPanel.add(deleteButton);

            mainPanel.add(radioPanel, BorderLayout.NORTH);
            mainPanel.add(textFieldPanel, BorderLayout.CENTER);
            mainPanel.add(output, BorderLayout.SOUTH);

            add(mainPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);

            showItemsButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        String query = "SELECT ExhibitionItems.ItemID, ExhibitionItems.Type, ExhibitionItems.Description, Artists.Name AS ArtistName, ItemCategories.Name AS CategoryName FROM ExhibitionItems INNER JOIN Artists ON ExhibitionItems.ArtistID = Artists.ArtistID INNER JOIN ItemCategories ON ExhibitionItems.CategoryID = ItemCategories.CategoryID";
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(query);

                        exhibitionItems.clear();
                        StringBuilder itemsText = new StringBuilder();
                        while (resultSet.next()) {
                            int itemId = resultSet.getInt("ItemID");
                            String type = resultSet.getString("Type");
                            String description = resultSet.getString("Description");
                            String artistName = resultSet.getString("ArtistName");
                            String categoryName = resultSet.getString("CategoryName");

                            ExhibitionItem item = new ExhibitionItem(itemId, type, description, artistName, categoryName);
                            exhibitionItems.add(item);

                            itemsText.append("ItemID: ").append(itemId)
                                     .append(", Type: ").append(type)
                                     .append(", Description: ").append(description)
                                     .append(", Artist: ").append(artistName).append("\n");
                        }
                        output_text.setText(itemsText.toString());
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Failed to fetch items from the database.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            printButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    StringBuilder message = new StringBuilder();
                    boolean hasError = false;

                    // Type
                    if (!paintingButton.isSelected() && !sculptureButton.isSelected() && !photographyButton.isSelected() && !fossilButton.isSelected() && !relicButton.isSelected()) {
                        message.append("The type field is empty!\n");
                        hasError = true;
                    }

                    // Short description
                    if (shortDescriptionTextField.getText().trim().isEmpty()) {
                        message.append("The short description field is empty!\n");
                        hasError = true;
                    }

                    // Region
                    if (regionTextField.getText().trim().isEmpty()) {
                        message.append("The region field is empty!\n");
                        hasError = true;
                    }

                    // Year
                    if (yearTextField.getText().trim().isEmpty()) {
                        message.append("The year field is empty!\n");
                        hasError = true;
                    }
                    
                    // Artist
                    if (artistTextField.getText().trim().isEmpty()) {
                        message.append("The artist field is empty!\n");
                        hasError = true;
                    }

                    if (hasError) {
                        output_text.setText(message.toString());
                    } else {
                        String type = paintingButton.isSelected() ? "Painting" : sculptureButton.isSelected() ? "Sculpture" : photographyButton.isSelected() ? "Photography" : fossilButton.isSelected() ? "Fossil" :  "Relic";
                        String shortDescription = shortDescriptionTextField.getText();
                        String region = regionTextField.getText();
                        String year = yearTextField.getText();
                        String artist = artistTextField.getText();
                        String room = (String) roomComboBox.getSelectedItem();
                        
                        output_text.setText("Type: " + type + "\n" +
                                            "Short Description: " + shortDescription + "\n" + 
                                            "Region: " + region + "\n" +
                                            "Year: " + year + "\n" +
                                            "Artist: " + artist + "\n" +
                                            "Room: " + room);               
                    }
                }
            });

            saveButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String type = paintingButton.isSelected() ? "Painting" : sculptureButton.isSelected() ? "Sculpture" : photographyButton.isSelected() ? "Photography" : fossilButton.isSelected() ? "Fossil" : "Relic";
                    String shortDescription = shortDescriptionTextField.getText();
                    String region = regionTextField.getText();
                    String year = yearTextField.getText();
                    String room = (String) roomComboBox.getSelectedItem();
                    String artist = artistTextField.getText(); 

                    try {
                        int artistId = getArtistID(artist);
                        if (artistId == -1) {
                            artistId = insertNewArtist(artist);
                        }

                        int categoryId = getCategoryID(type);
                        String sql = "INSERT INTO ExhibitionItems (Type, Description, RegionOfOrigin, YearProduced, Position, CategoryID, ArtistID) VALUES (?, ?, ?, ?, ?, ?, ?)";
                        PreparedStatement statement = connection.prepareStatement(sql);
                        statement.setString(1, type);
                        statement.setString(2, shortDescription);
                        statement.setString(3, region);
                        statement.setInt(4, Integer.parseInt(year));
                        statement.setString(5, room);
                        statement.setInt(6, categoryId);
                        statement.setInt(7, artistId);
                        statement.executeUpdate();

                        ExhibitionItem newItem = new ExhibitionItem(0, type, shortDescription, artist, getCategoryName(categoryId));
                        exhibitionItems.add(newItem);

                        JOptionPane.showMessageDialog(null, "Data saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Failed to save data into the database.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            deleteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String input = JOptionPane.showInputDialog(null, "Enter ItemID to delete:");
                    if (input != null && !input.isEmpty()) {
                        try {
                            int itemId = Integer.parseInt(input);
                            String sql = "DELETE FROM ExhibitionItems WHERE ItemID = ?";
                            PreparedStatement statement = connection.prepareStatement(sql);
                            statement.setInt(1, itemId);
                            int rowsDeleted = statement.executeUpdate();
                            if (rowsDeleted > 0) {

                                exhibitionItems.removeIf(item -> item.getItemId() == itemId);

                                JOptionPane.showMessageDialog(null, "Exhibition item deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "No exhibition item found with the specified ItemID.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (NumberFormatException | SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Failed to delete exhibition item.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });      
        }
   
        private int getCategoryID(String type) throws SQLException {
            int categoryId = -1;
            String sql = "SELECT CategoryID FROM ItemCategories WHERE Name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, type);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                categoryId = resultSet.getInt("CategoryID");
            }
            return categoryId;
        }
        
        private int getArtistID(String artistName) throws SQLException {
            int artistId = -1;
            String sql = "SELECT ArtistID FROM Artists WHERE Name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, artistName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                artistId = resultSet.getInt("ArtistID");
            }
            return artistId;
        }

        private int insertNewArtist(String artistName) throws SQLException {
            String sql = "INSERT INTO Artists (Name) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, artistName);
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Failed to insert new artist, no ID obtained.");
            }
        }

        private String getCategoryName(int categoryId) throws SQLException {
            String categoryName = null;
            String sql = "SELECT Name FROM ItemCategories WHERE CategoryID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, categoryId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                categoryName = resultSet.getString("Name");
            }
            return categoryName;
        }

        private static class ExhibitionItem {
            private int itemId;
            private String type;
            private String description;
            private String artist;
            private String category;

            public ExhibitionItem(int itemId, String type, String description, String artist, String category) {
                this.itemId = itemId;
                this.type = type;
                this.description = description;
                this.artist = artist;
                this.category = category;
            }

            public int getItemId() {
                return itemId;
            }

            public String getType() {
                return type;
            }

            public String getDescription() {
                return description;
            }

            public String getArtist() {
                return artist;
            }

            public String getCategory() {
                return category;
            }
        }
    }

    public static void main(String[] args) {
        MuseumGUI2 museumGUI2 = new MuseumGUI2();
        museumGUI2.setVisible(true);
    }
}