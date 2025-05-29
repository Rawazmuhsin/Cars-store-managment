package com.example.Design;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

/**
 * Show Car UI class that displays detailed information about a car
 */
public class ShowCarUI extends BaseUI {

    private CarManagement.Car car;
    private int adminId; // Store adminId if needed by SellCarDialog or other actions

    /**
     * Constructor with car data and admin ID
     * @param car The car to display
     * @param adminId The ID of the admin user
     */
    public ShowCarUI(CarManagement.Car car, int adminId) {
        super(adminId);
        
        // Check if car is null to prevent NullPointerException
        if (car == null) {
            System.err.println("ERROR: Car object is null in ShowCarUI constructor");
            JOptionPane.showMessageDialog(null, 
                "Error: Could not retrieve car details.", 
                "Data Error", JOptionPane.ERROR_MESSAGE);
                
            // Redirect back to inventory after error message
            SwingUtilities.invokeLater(() -> {
                dispose();
                new CarManagement(adminId).setVisible(true);
            });
        } else {
            this.car = car;
            this.adminId = adminId; // Store adminId
            System.out.println("Showing car details: " + car.getModel() + " (ID: " + car.getId() + ")");
        }
    }

    /**
     * Constructor with car data
     * @param car The car to display
     */
    public ShowCarUI(CarManagement.Car car) {
        super(); // Assuming BaseUI has a default constructor that handles adminId if needed, or sets a default
        
        // Check if car is null to prevent NullPointerException
        if (car == null) {
            System.err.println("ERROR: Car object is null in ShowCarUI constructor");
            JOptionPane.showMessageDialog(null, 
                "Error: Could not retrieve car details.", 
                "Data Error", JOptionPane.ERROR_MESSAGE);
                
            // Redirect back to inventory after error message
            SwingUtilities.invokeLater(() -> {
                dispose();
                new CarManagement().setVisible(true);
            });
        } else {
            this.car = car;
            // this.adminId = getDefaultAdminId(); // Or handle adminId appropriately
            System.out.println("Showing car details: " + car.getModel() + " (ID: " + car.getId() + ")");
        }
    }

    /**
     * Add menu items to the sidebar
     * Overrides the method in BaseUI
     * @param menuPanel The panel to add menu items to
     */
    @Override
    protected void addMenuItems(JPanel menuPanel) {
        // Add menu items - set Car Inventory as active
        addMenuItem(menuPanel, "Dashboard", false);
        addMenuItem(menuPanel, "Car Inventory", true);
        addMenuItem(menuPanel, "Add New Car", false);
        addMenuItem(menuPanel, "Coming Soon", false);
        addMenuItem(menuPanel, "Sold Cars", false);
        addMenuItem(menuPanel, "Economic", false);
        addMenuItem(menuPanel, "Audit Logs", false);
    }

    /**
     * Create the content panel for showing car details
     * Overrides the method in BaseUI
     * @return JPanel containing the content
     */
    @Override
    protected JPanel createContentPanel() {
        // Check if car is null before proceeding
        if (car == null) {
            JPanel errorPanel = new JPanel();
            errorPanel.setBackground(LIGHT_GRAY_BG);
            errorPanel.setLayout(new BorderLayout());
            
            JLabel errorLabel = new JLabel("Error: Car data is unavailable", SwingConstants.CENTER);
            errorLabel.setFont(new Font("Arial", Font.BOLD, 18));
            errorLabel.setForeground(Color.RED);
            
            JButton backButton = new JButton("Back to Inventory");
            backButton.addActionListener(e -> goBackToInventory());
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.setOpaque(false);
            buttonPanel.add(backButton);
            
            errorPanel.add(errorLabel, BorderLayout.CENTER);
            errorPanel.add(buttonPanel, BorderLayout.SOUTH);
            
            return errorPanel;
        }
    
        // Normal content creation if car is not null
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(LIGHT_GRAY_BG); // Assumed from BaseUI
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new BorderLayout());

        JLabel headerLabel = new JLabel(car.getModel() + " " + car.getYear());
        headerLabel.setFont(new Font("Arial", Font.BOLD, 26));
        headerLabel.setForeground(new Color(50, 50, 50));

        // Sub header info
        JLabel subHeaderLabel = new JLabel("Vehicle Details and Specifications");
        subHeaderLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subHeaderLabel.setForeground(new Color(120, 120, 120));

        headerPanel.add(headerLabel, BorderLayout.NORTH);
        headerPanel.add(subHeaderLabel, BorderLayout.SOUTH);

        // Buttons panel for top right
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setOpaque(false);
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        // Back button
        JButton backButton = new JButton("← Back to Inventory");
        backButton.setBackground(new Color(108, 117, 125));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);

        // Add action listener to back button
        backButton.addActionListener(e -> goBackToInventory());

        // Add hover effects
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backButton.setBackground(new Color(108, 117, 125).darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backButton.setBackground(new Color(108, 117, 125));
            }
        });

        // Edit button
        JButton editButton = new JButton("Edit Details");
        editButton.setBackground(PRIMARY_BLUE); // Assumed from BaseUI
        editButton.setForeground(Color.WHITE);
        editButton.setFocusPainted(false);
        editButton.setFont(new Font("Arial", Font.BOLD, 14));
        editButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        editButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editButton.setOpaque(true);
        editButton.setBorderPainted(false);

        // Add action listener to edit button
        editButton.addActionListener(e -> editCarDetails());

        // Add hover effects
        editButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                editButton.setBackground(PRIMARY_BLUE.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                editButton.setBackground(PRIMARY_BLUE);
            }
        });

        buttonsPanel.add(backButton);
        buttonsPanel.add(editButton);

        // --- Start of "Sell Car" button ---
        // Sell button (only show if car is available or reserved)
        if (car.getStatus() != null && 
            (car.getStatus().equalsIgnoreCase("Available") || 
             car.getStatus().equalsIgnoreCase("Reserved"))) {
            
            JButton sellButton = new JButton("Sell Car");
            sellButton.setBackground(PRIMARY_GREEN); // Assumed from BaseUI
            sellButton.setForeground(Color.WHITE);
            sellButton.setFocusPainted(false);
            sellButton.setFont(new Font("Arial", Font.BOLD, 14));
            sellButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            sellButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            sellButton.setOpaque(true);
            sellButton.setBorderPainted(false);

            // Add action listener to sell button
            sellButton.addActionListener(e -> sellCar());

            // Add hover effects
            sellButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    sellButton.setBackground(PRIMARY_GREEN.darker());
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    sellButton.setBackground(PRIMARY_GREEN);
                }
            });

            buttonsPanel.add(sellButton);
        }
        // --- End of "Sell Car" button ---

        // Delete button (only show if car is not sold)
        if (car.getStatus() != null && !car.getStatus().equalsIgnoreCase("Sold")) {
            JButton deleteButton = new JButton("Delete");
            deleteButton.setBackground(PRIMARY_RED); // Assumed from BaseUI
            deleteButton.setForeground(Color.WHITE);
            deleteButton.setFocusPainted(false);
            deleteButton.setFont(new Font("Arial", Font.BOLD, 14));
            deleteButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            deleteButton.setOpaque(true);
            deleteButton.setBorderPainted(false);

            // Add action listener to delete button
            deleteButton.addActionListener(e -> deleteCarEntry());

            // Add hover effects
            deleteButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    deleteButton.setBackground(PRIMARY_RED.darker());
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    deleteButton.setBackground(PRIMARY_RED);
                }
            });
            buttonsPanel.add(deleteButton);
        }

        // Add header and buttons to top panel
        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new BorderLayout());
        topPanel.add(headerPanel, BorderLayout.WEST);
        topPanel.add(buttonsPanel, BorderLayout.EAST);

        // Details content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        // Create details panel
        JPanel detailsPanel = createDetailsPanel();

        // Add components to content panel
        contentPanel.add(detailsPanel);

        // Create footer panel
        footerPanel = createFooterPanel(); // Assumed from BaseUI

        // Add all panels to main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    /**
     * Create the details panel showing car information
     */
    private JPanel createDetailsPanel() {
        JPanel mainContainer = new JPanel();
        mainContainer.setOpaque(false);
        mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.X_AXIS));
        mainContainer.setAlignmentX(Component.LEFT_ALIGNMENT); // Align main container to the left

        // Left panel - Car image
        RoundedPanel imagePanel = createImagePanel(); // Assumed RoundedPanel is defined

        // Right panel - Car details
        RoundedPanel infoPanel = createInfoPanel(); // Assumed RoundedPanel is defined

        mainContainer.add(imagePanel);
        mainContainer.add(Box.createHorizontalStrut(30));
        mainContainer.add(infoPanel);
        // To ensure it doesn't expand beyond its preferred size if the window is larger
        mainContainer.add(Box.createHorizontalGlue());

        return mainContainer;
    }

    /**
     * Create image panel
     */
    private RoundedPanel createImagePanel() {
        RoundedPanel imageCard = new RoundedPanel(15, Color.WHITE); // Assumed RoundedPanel
        imageCard.setLayout(new BorderLayout(0, 15)); // Added vertical gap
        imageCard.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        imageCard.setPreferredSize(new Dimension(450, 400));
        imageCard.setMaximumSize(new Dimension(450, 450)); // Allow a bit more height for status badge
        imageCard.setMinimumSize(new Dimension(400, 350));

        JLabel titleLabel = new JLabel("Vehicle Photo");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(50, 50, 50));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Image container
        JPanel imageContainer = new JPanel();
        imageContainer.setLayout(new BorderLayout());
        imageContainer.setBackground(new Color(248, 250, 252));
        // imageContainer.setPreferredSize(new Dimension(390, 280)); // Let it be flexible
        imageContainer.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 2));

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);

        try {
            // Try to load the car image
            String imagePath = car.getImagePath();
            if (imagePath != null && !imagePath.isEmpty()) {
                ImageIcon originalIcon = new ImageIcon(imagePath); // Consider ClassLoader for resource path
                if (originalIcon.getIconWidth() > 0 && originalIcon.getIconHeight() > 0) {
                    Image scaledImage = originalIcon.getImage().getScaledInstance(380, 270, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImage));
                } else {
                    throw new Exception("Image not found or zero dimensions: " + imagePath);
                }
            } else {
                throw new Exception("Image path is null or empty");
            }
        } catch (Exception e) {
            System.err.println("Image loading error: " + e.getMessage());
            imageLabel.setText("🚗");
            imageLabel.setFont(new Font("Arial", Font.PLAIN, 100));
            imageLabel.setForeground(new Color(150, 150, 150));
        }

        imageContainer.add(imageLabel, BorderLayout.CENTER);

        // Status badge
        JLabel statusBadge = new JLabel(car.getStatus());
        statusBadge.setFont(new Font("Arial", Font.BOLD, 14));
        statusBadge.setHorizontalAlignment(SwingConstants.CENTER);
        statusBadge.setOpaque(true);
        statusBadge.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Set status color
        String status = car.getStatus() != null ? car.getStatus().toLowerCase() : "";
        switch (status) {
            case "available":
                statusBadge.setBackground(new Color(232, 255, 232)); // Light Green
                statusBadge.setForeground(new Color(25, 135, 84));   // Dark Green
                break;
            case "sold":
                statusBadge.setBackground(new Color(255, 236, 236)); // Light Red
                statusBadge.setForeground(new Color(220, 53, 69));   // Dark Red
                break;
            case "reserved":
                statusBadge.setBackground(new Color(255, 250, 230)); // Light Yellow
                statusBadge.setForeground(new Color(255, 153, 0));  // Orange/Dark Yellow
                break;
            default:
                statusBadge.setBackground(new Color(240, 240, 240)); // Light Gray
                statusBadge.setForeground(new Color(70, 70, 70));    // Dark Gray
        }

        imageCard.add(titleLabel, BorderLayout.NORTH);
        imageCard.add(imageContainer, BorderLayout.CENTER);
        imageCard.add(statusBadge, BorderLayout.SOUTH);

        return imageCard;
    }

    /**
     * Create information panel
     */
    private RoundedPanel createInfoPanel() {
        RoundedPanel infoCard = new RoundedPanel(15, Color.WHITE); // Assumed RoundedPanel
        infoCard.setLayout(new BorderLayout());
        infoCard.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        // infoCard.setPreferredSize(new Dimension(500, 400)); // Let it be more flexible

        JPanel infoContent = new JPanel(new GridBagLayout());
        infoContent.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 0, 8, 20); // top, left, bottom, right

        // Title
        JLabel infoTitle = new JLabel("Vehicle Information");
        infoTitle.setFont(new Font("Arial", Font.BOLD, 18));
        infoTitle.setForeground(new Color(50, 50, 50));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0); // Reset insets for title
        infoContent.add(infoTitle, gbc);

        // Reset gridwidth and insets for detail rows
        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 0, 8, 20);

        // Vehicle details
        addDetailRow(infoContent, gbc, 1, "Model:", car.getModel());
        addDetailRow(infoContent, gbc, 2, "Year:", car.getYear());
        addDetailRow(infoContent, gbc, 3, "Type:", car.getType());
        addDetailRow(infoContent, gbc, 4, "Color:", car.getColor());
        addDetailRow(infoContent, gbc, 5, "Price:", car.getPrice());
        addDetailRow(infoContent, gbc, 6, "Status:", car.getStatus());
        addDetailRow(infoContent, gbc, 7, "Date Added:", car.getDateAdded());

        // Additional specifications (sample data)
        addDetailRow(infoContent, gbc, 8, "VIN:", "VIN" + String.format("%06d", car.getId()));
        addDetailRow(infoContent, gbc, 9, "Engine:", getEngineByType(car.getType()));
        addDetailRow(infoContent, gbc, 10, "Transmission:", "Automatic CVT"); // Example
        addDetailRow(infoContent, gbc, 11, "Fuel Type:", getFuelTypeByType(car.getType()));
        addDetailRow(infoContent, gbc, 12, "Mileage:", getMileageByStatus(car.getStatus()));

        // Notes section
        gbc.gridx = 0;
        gbc.gridy = 13;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.NORTHWEST; // Anchor label to top-left
        gbc.insets = new Insets(20, 0, 10, 0); // Space above notes label
        JLabel notesLabel = new JLabel("Notes:");
        notesLabel.setFont(new Font("Arial", Font.BOLD, 14));
        notesLabel.setForeground(new Color(50, 50, 50));
        infoContent.add(notesLabel, gbc);

        gbc.gridy = 14;
        gbc.fill = GridBagConstraints.BOTH; // Make JTextArea fill its allocated space
        gbc.weightx = 1.0; // Allow horizontal expansion
        gbc.weighty = 1.0; // Allow vertical expansion
        gbc.insets = new Insets(0, 0, 0, 0); // No insets for text area itself

        String notesText = generateNotesText(car);
        JTextArea notesArea = new JTextArea(notesText);
        notesArea.setFont(new Font("Arial", Font.PLAIN, 13));
        notesArea.setForeground(new Color(80, 80, 80));
        notesArea.setBackground(new Color(248, 250, 252));
        notesArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220,220,220)), // Light border for text area
            BorderFactory.createEmptyBorder(10, 10, 10, 10) // Padding inside text area
        ));
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        notesArea.setEditable(false);
        // Wrap JTextArea in JScrollPane for scrollability if content is too long
        // JScrollPane notesScrollPane = new JScrollPane(notesArea);
        // notesScrollPane.setMinimumSize(new Dimension(100, 80)); // Example minimum size
        // infoContent.add(notesScrollPane, gbc);
        infoContent.add(notesArea, gbc); // Adding JTextArea directly for now

        infoCard.add(infoContent, BorderLayout.CENTER);

        return infoCard;
    }

    /**
     * Add a detail row to the info panel
     */
    private void addDetailRow(JPanel panel, GridBagConstraints gbc, int row, String label, String value) {
        // Label
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0; // Labels don't expand
        gbc.weighty = 0;

        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Arial", Font.BOLD, 14));
        labelComponent.setForeground(new Color(50, 50, 50));
        labelComponent.setPreferredSize(new Dimension(120, 25)); // Give label a preferred width
        panel.add(labelComponent, gbc);

        // Value
        gbc.gridx = 1;
        gbc.weightx = 1.0; // Values can expand if needed
        gbc.fill = GridBagConstraints.HORIZONTAL; // Allow value to fill horizontal space

        JLabel valueComponent = new JLabel(value != null ? value : "N/A"); // Handle null values
        valueComponent.setFont(new Font("Arial", Font.PLAIN, 14));
        valueComponent.setForeground(new Color(80, 80, 80));

        // Special formatting for price
        if (label.contains("Price")) {
            valueComponent.setFont(new Font("Arial", Font.BOLD, 14));
            valueComponent.setForeground(PRIMARY_GREEN); // Assumed from BaseUI
        }

        // Special formatting for status
        if (label.contains("Status")) {
            valueComponent.setFont(new Font("Arial", Font.BOLD, 14));
            switch (value != null ? value.toLowerCase() : "") { // Handle null value for switch
                case "available":
                    valueComponent.setForeground(PRIMARY_GREEN); // Assumed from BaseUI
                    break;
                case "sold":
                    valueComponent.setForeground(PRIMARY_RED); // Assumed from BaseUI
                    break;
                case "reserved":
                    valueComponent.setForeground(PRIMARY_YELLOW); // Assumed from BaseUI
                    break;
            }
        }

        panel.add(valueComponent, gbc);
    }

    /**
     * Get engine type based on car type
     */
    private String getEngineByType(String type) {
        if (type == null) return "N/A";
        switch (type.toLowerCase()) {
            case "electric":
                return "Electric Motor (250kW)";
            case "truck":
                return "5.0L V8 Engine";
            case "suv":
                return "3.5L V6 Turbo";
            case "luxury":
                return "4.0L V8 Twin Turbo";
            case "sports":
                return "3.0L V6 Twin Turbo";
            default:
                return "2.5L 4-Cylinder";
        }
    }

    /**
     * Get fuel type based on car type
     */
    private String getFuelTypeByType(String type) {
        if (type == null) return "N/A";
        switch (type.toLowerCase()) {
            case "electric":
                return "Electric";
            case "truck":
                return "Regular Gasoline";
            case "luxury":
            case "sports":
                return "Premium Gasoline";
            default:
                return "Regular Gasoline";
        }
    }

    /**
     * Get mileage based on status
     */
    private String getMileageByStatus(String status) {
        if (status == null) return "N/A";
        if ("sold".equalsIgnoreCase(status)) {
            // This might be dynamic in a real app, or set at time of sale
            return "15,000 mi (Example)";
        } else {
            return "New (0 mi)";
        }
    }

    /**
     * Generate notes text based on car information
     */
    private String generateNotesText(CarManagement.Car car) {
        StringBuilder notes = new StringBuilder();
        String carType = car.getType() != null ? car.getType() : "";
        String carStatus = car.getStatus() != null ? car.getStatus() : "";

        if ("electric".equalsIgnoreCase(carType)) {
            notes.append("This electric vehicle features cutting-edge technology with fast charging capabilities, ")
                  .append("advanced autopilot features, and zero emissions. ")
                  .append("Includes comprehensive warranty covering battery and drivetrain. ");
        } else if ("luxury".equalsIgnoreCase(carType)) {
            notes.append("Luxury vehicle with premium leather interior, advanced infotainment system, ")
                  .append("premium sound system, and comprehensive safety features. ")
                  .append("Includes extended warranty and complimentary maintenance package. ");
        } else if ("sports".equalsIgnoreCase(carType)) {
            notes.append("High-performance sports vehicle with sport-tuned suspension, ")
                  .append("performance brakes, and advanced traction control. ")
                  .append("Perfect for enthusiasts seeking thrilling driving experience. ");
        } else if ("truck".equalsIgnoreCase(carType)) {
            notes.append("Heavy-duty truck with excellent towing capacity, ")
                  .append("4WD capability, and rugged construction. ")
                  .append("Ideal for work and recreational activities. ");
        } else {
            notes.append("Reliable and fuel-efficient vehicle with modern safety features, ")
                  .append("comfortable interior, and excellent build quality. ")
                  .append("Perfect for daily commuting and family use. ");
        }

        if ("available".equalsIgnoreCase(carStatus)) {
            notes.append("Vehicle is ready for immediate delivery with full manufacturer warranty.");
        } else if ("reserved".equalsIgnoreCase(carStatus)) {
            notes.append("Vehicle is currently reserved pending final paperwork completion.");
        } else if ("sold".equalsIgnoreCase(carStatus)) {
            notes.append("Vehicle has been sold and delivered to customer. All documentation completed.");
        }

        return notes.toString();
    }

    /**
     * Edit car details
     */
    private void editCarDetails() {
        dispose(); // Close current window
        SwingUtilities.invokeLater(() -> {
            try {
                // Get the most up-to-date car information before opening the edit screen
                CarManagementIntegration integration = CarManagementIntegration.getInstance();
                CarManagement.Car refreshedCar = integration.getCarById(car.getId());
                
                if (refreshedCar != null) {
                    new EditCarUI(refreshedCar, adminId).setVisible(true);
                } else {
                    // Fall back to the current car object if refresh fails
                    new EditCarUI(car, adminId).setVisible(true);
                }
            } catch (Exception e) {
                System.err.println("Error navigating to EditCarUI: " + e.getMessage());
                e.printStackTrace();
                // Fall back to the current car object if an exception occurs
                new EditCarUI(car, adminId).setVisible(true);
            }
        });
    }

    /**
     * Delete car entry
     */
    private void deleteCarEntry() {
        int result = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this vehicle from inventory?\n\n" +
            "Vehicle: " + car.getModel() + " " + car.getYear() + "\n" +
            "This action cannot be undone.",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            // In a real application, delete from the database
            CarManagementIntegration integration = CarManagementIntegration.getInstance();
            boolean success = integration.deleteCar(car.getId());
            
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Vehicle has been removed from inventory.",
                    "Deleted",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to delete vehicle. Please try again.",
                    "Delete Failed",
                    JOptionPane.ERROR_MESSAGE);
            }

            // Go back to inventory page
            goBackToInventory();
        }
    }

    /**
     * Sell the car - open sell car dialog
     */
    private void sellCar() {
        // Create the sell car dialog
        SellCarDialog sellDialog = new SellCarDialog(this, car);
        sellDialog.setVisible(true);

        // Check if sale was completed
        if (sellDialog.isSaleCompleted()) {
            // Update the car status in the UI
            car.setStatus("Sold");

            // Show success message
            JOptionPane.showMessageDialog(this,
                "Vehicle sold successfully!\nReturning to inventory...",
                "Sale Complete",
                JOptionPane.INFORMATION_MESSAGE);

            // Go back to inventory which will show updated data
            goBackToInventory();
        }
    }

    /**
     * Go back to inventory page
     */
    private void goBackToInventory() {
        dispose(); // Close current window
        SwingUtilities.invokeLater(() -> new CarManagement(adminId).setVisible(true));
    }
}