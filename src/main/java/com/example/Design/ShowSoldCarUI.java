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
 * Show Sold Car UI class that displays detailed information about a sold car
 */
public class ShowSoldCarUI extends BaseUI {
    
    private SoldCarsUI.SoldCar car;
    
    /**
     * Constructor with car data and admin ID
     * @param car The sold car to display
     * @param adminId The ID of the admin user
     */
    public ShowSoldCarUI(SoldCarsUI.SoldCar car, int adminId) {
        super(adminId);
        this.car = car;
    }
    
    /**
     * Constructor with car data
     * @param car The sold car to display
     */
    public ShowSoldCarUI(SoldCarsUI.SoldCar car) {
        super();
        this.car = car;
    }
    
    /**
     * Add menu items to the sidebar
     * Overrides the method in BaseUI
     * @param menuPanel The panel to add menu items to
     */
    @Override
    protected void addMenuItems(JPanel menuPanel) {
        // Add menu items - set Sold Cars as active
        addMenuItem(menuPanel, "Dashboard", false);
        addMenuItem(menuPanel, "Car Inventory", false);
        addMenuItem(menuPanel, "Add New Car", false);
        addMenuItem(menuPanel, "Coming Soon", false);
        addMenuItem(menuPanel, "Sold Cars", true);
        addMenuItem(menuPanel, "Economic", false);
        addMenuItem(menuPanel, "Audit Logs", false);
    }
    
    /**
     * Create the content panel for showing sold car details
     * Overrides the method in BaseUI
     * @return JPanel containing the content
     */
    @Override
    protected JPanel createContentPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(LIGHT_GRAY_BG);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new BorderLayout());
        
        JLabel headerLabel = new JLabel(car.getModel() + " " + car.getYear() + " - SOLD");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 26));
        headerLabel.setForeground(new Color(50, 50, 50));
        
        // Sub header info
        JLabel subHeaderLabel = new JLabel("Sale Transaction Details and History");
        subHeaderLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subHeaderLabel.setForeground(new Color(120, 120, 120));
        
        headerPanel.add(headerLabel, BorderLayout.NORTH);
        headerPanel.add(subHeaderLabel, BorderLayout.SOUTH);
        
        // Buttons panel for top right
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setOpaque(false);
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        
        // Back button
        JButton backButton = new JButton("← Back to Sold Cars");
        backButton.setBackground(new Color(108, 117, 125));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);
        
        // Add action listener to back button
        backButton.addActionListener(e -> goBackToSoldCars());
        
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
        
        // Print Receipt button
        JButton receiptButton = new JButton("Print Receipt");
        receiptButton.setBackground(PRIMARY_GREEN);
        receiptButton.setForeground(Color.WHITE);
        receiptButton.setFocusPainted(false);
        receiptButton.setFont(new Font("Arial", Font.BOLD, 14));
        receiptButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        receiptButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        receiptButton.setOpaque(true);
        receiptButton.setBorderPainted(false);
        
        // Add action listener to receipt button
        receiptButton.addActionListener(e -> printReceipt());
        
        // Add hover effects
        receiptButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                receiptButton.setBackground(PRIMARY_GREEN.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                receiptButton.setBackground(PRIMARY_GREEN);
            }
        });
        
        // Email Receipt button
        JButton emailButton = new JButton("Email Receipt");
        emailButton.setBackground(PRIMARY_BLUE);
        emailButton.setForeground(Color.WHITE);
        emailButton.setFocusPainted(false);
        emailButton.setFont(new Font("Arial", Font.BOLD, 14));
        emailButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        emailButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        emailButton.setOpaque(true);
        emailButton.setBorderPainted(false);
        
        // Add action listener to email button
        emailButton.addActionListener(e -> emailReceipt());
        
        // Add hover effects
        emailButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                emailButton.setBackground(PRIMARY_BLUE.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                emailButton.setBackground(PRIMARY_BLUE);
            }
        });
        
        buttonsPanel.add(backButton);
        buttonsPanel.add(receiptButton);
        buttonsPanel.add(emailButton);
        
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
        footerPanel = createFooterPanel();
        
        // Add all panels to main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    /**
     * Create the details panel showing car and sale information
     */
    private JPanel createDetailsPanel() {
        JPanel mainContainer = new JPanel();
        mainContainer.setOpaque(false);
        mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.X_AXIS));
        mainContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Left panel - Car image
        RoundedPanel imagePanel = createImagePanel();
        
        // Middle panel - Vehicle details
        RoundedPanel vehicleInfoPanel = createVehicleInfoPanel();
        
        // Right panel - Sale details
        RoundedPanel saleInfoPanel = createSaleInfoPanel();
        
        mainContainer.add(imagePanel);
        mainContainer.add(Box.createHorizontalStrut(20));
        mainContainer.add(vehicleInfoPanel);
        mainContainer.add(Box.createHorizontalStrut(20));
        mainContainer.add(saleInfoPanel);
        
        return mainContainer;
    }
    
    /**
     * Create image panel
     */
    private RoundedPanel createImagePanel() {
        RoundedPanel imageCard = new RoundedPanel(15, Color.WHITE);
        imageCard.setLayout(new BorderLayout());
        imageCard.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        imageCard.setPreferredSize(new Dimension(380, 450));
        imageCard.setMaximumSize(new Dimension(380, 450));
        
        JLabel titleLabel = new JLabel("Vehicle Photo");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(50, 50, 50));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Image container
        JPanel imageContainer = new JPanel();
        imageContainer.setLayout(new BorderLayout());
        imageContainer.setBackground(new Color(248, 250, 252));
        imageContainer.setPreferredSize(new Dimension(320, 240));
        imageContainer.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 2));
        
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        
        try {
            // Try to load the car image
            ImageIcon originalIcon = new ImageIcon(car.getImagePath());
            if (originalIcon.getIconWidth() > 0) {
                Image scaledImage = originalIcon.getImage().getScaledInstance(310, 230, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
            } else {
                throw new Exception("Image not found");
            }
        } catch (Exception e) {
            // Use placeholder if image not found
            imageLabel.setText("🚗");
            imageLabel.setFont(new Font("Arial", Font.PLAIN, 80));
            imageLabel.setForeground(new Color(150, 150, 150));
        }
        
        imageContainer.add(imageLabel, BorderLayout.CENTER);
        
        // Status badge
        JLabel statusBadge = new JLabel("SOLD");
        statusBadge.setFont(new Font("Arial", Font.BOLD, 16));
        statusBadge.setHorizontalAlignment(SwingConstants.CENTER);
        statusBadge.setOpaque(true);
        statusBadge.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        statusBadge.setBackground(new Color(255, 236, 236));
        statusBadge.setForeground(new Color(220, 53, 69));
        
        // Sale price display
        JLabel salePriceLabel = new JLabel(car.getSalePrice());
        salePriceLabel.setFont(new Font("Arial", Font.BOLD, 24));
        salePriceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        salePriceLabel.setForeground(PRIMARY_GREEN);
        
        // Sale date display
        JLabel saleDateLabel = new JLabel("Sold on " + car.getSaleDate());
        saleDateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        saleDateLabel.setHorizontalAlignment(SwingConstants.CENTER);
        saleDateLabel.setForeground(new Color(100, 100, 100));
        
        JPanel bottomInfo = new JPanel();
        bottomInfo.setLayout(new BoxLayout(bottomInfo, BoxLayout.Y_AXIS));
        bottomInfo.setOpaque(false);
        bottomInfo.add(statusBadge);
        bottomInfo.add(Box.createVerticalStrut(10));
        bottomInfo.add(salePriceLabel);
        bottomInfo.add(Box.createVerticalStrut(5));
        bottomInfo.add(saleDateLabel);
        
        imageCard.add(titleLabel, BorderLayout.NORTH);
        imageCard.add(Box.createVerticalStrut(15), BorderLayout.NORTH);
        imageCard.add(imageContainer, BorderLayout.CENTER);
        imageCard.add(bottomInfo, BorderLayout.SOUTH);
        
        return imageCard;
    }
    
    /**
     * Create vehicle information panel
     */
    private RoundedPanel createVehicleInfoPanel() {
        RoundedPanel infoCard = new RoundedPanel(15, Color.WHITE);
        infoCard.setLayout(new BorderLayout());
        infoCard.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        infoCard.setPreferredSize(new Dimension(380, 450));
        
        JPanel infoContent = new JPanel(new GridBagLayout());
        infoContent.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 0, 8, 20);
        
        // Title
        JLabel infoTitle = new JLabel("Vehicle Information");
        infoTitle.setFont(new Font("Arial", Font.BOLD, 18));
        infoTitle.setForeground(new Color(50, 50, 50));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        infoContent.add(infoTitle, gbc);
        
        // Reset gridwidth and insets
        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 0, 8, 20);
        
        // Vehicle details
        addDetailRow(infoContent, gbc, 1, "Model:", car.getModel());
        addDetailRow(infoContent, gbc, 2, "Year:", car.getYear());
        addDetailRow(infoContent, gbc, 3, "Type:", car.getType());
        addDetailRow(infoContent, gbc, 4, "Color:", car.getColor());
        addDetailRow(infoContent, gbc, 5, "VIN:", "VIN" + String.format("%06d", car.getId()));
        addDetailRow(infoContent, gbc, 6, "Engine:", getEngineByType(car.getType()));
        addDetailRow(infoContent, gbc, 7, "Transmission:", "Automatic CVT");
        addDetailRow(infoContent, gbc, 8, "Fuel Type:", getFuelTypeByType(car.getType()));
        addDetailRow(infoContent, gbc, 9, "Mileage:", "15,000 mi");
        
        // Features section
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 10, 0);
        JLabel featuresLabel = new JLabel("Features:");
        featuresLabel.setFont(new Font("Arial", Font.BOLD, 14));
        featuresLabel.setForeground(new Color(50, 50, 50));
        infoContent.add(featuresLabel, gbc);
        
        gbc.gridy = 11;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(5, 0, 0, 0);
        
        String featuresText = generateFeaturesText(car);
        JTextArea featuresArea = new JTextArea(featuresText);
        featuresArea.setFont(new Font("Arial", Font.PLAIN, 13));
        featuresArea.setForeground(new Color(80, 80, 80));
        featuresArea.setBackground(new Color(248, 250, 252));
        featuresArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        featuresArea.setLineWrap(true);
        featuresArea.setWrapStyleWord(true);
        featuresArea.setEditable(false);
        infoContent.add(featuresArea, gbc);
        
        infoCard.add(infoContent, BorderLayout.CENTER);
        
        return infoCard;
    }
    
    /**
     * Create sale information panel
     */
    private RoundedPanel createSaleInfoPanel() {
        RoundedPanel saleCard = new RoundedPanel(15, Color.WHITE);
        saleCard.setLayout(new BorderLayout());
        saleCard.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        saleCard.setPreferredSize(new Dimension(380, 450));
        
        JPanel saleContent = new JPanel(new GridBagLayout());
        saleContent.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 0, 8, 20);
        
        // Title
        JLabel saleTitle = new JLabel("Sale Information");
        saleTitle.setFont(new Font("Arial", Font.BOLD, 18));
        saleTitle.setForeground(new Color(50, 50, 50));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        saleContent.add(saleTitle, gbc);
        
        // Reset gridwidth and insets
        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 0, 8, 20);
        
        // Sale details
        addDetailRow(saleContent, gbc, 1, "Sale Date:", car.getSaleDate());
        addDetailRow(saleContent, gbc, 2, "Sale Price:", car.getSalePrice());
        
        // Calculate tax and total
        String priceStr = car.getSalePrice().replace("$", "").replace(",", "");
        double price = 0;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            price = 30000; // Default value
        }
        double tax = price * 0.08;
        double total = price + tax;
        
        addDetailRow(saleContent, gbc, 3, "Tax (8%):", String.format("$%.2f", tax));
        addDetailRow(saleContent, gbc, 4, "Total Amount:", String.format("$%.2f", total));
        addDetailRow(saleContent, gbc, 5, "Payment Method:", car.getPaymentMethod());
        
        // Buyer Information Section
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 0, 15, 0);
        JLabel buyerTitle = new JLabel("Buyer Information");
        buyerTitle.setFont(new Font("Arial", Font.BOLD, 18));
        buyerTitle.setForeground(new Color(50, 50, 50));
        saleContent.add(buyerTitle, gbc);
        
        // Reset gridwidth and insets
        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 0, 8, 20);
        
        // Buyer details
        addDetailRow(saleContent, gbc, 7, "Buyer Name:", car.getBuyerName());
        addDetailRow(saleContent, gbc, 8, "Contact:", car.getBuyerContact());
        addDetailRow(saleContent, gbc, 9, "Email:", car.getBuyerName().toLowerCase().replace(" ", ".") + "@example.com");
        addDetailRow(saleContent, gbc, 10, "Address:", "123 Main St, City, State");
        
        // Transaction Notes
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 10, 0);
        JLabel notesLabel = new JLabel("Transaction Notes:");
        notesLabel.setFont(new Font("Arial", Font.BOLD, 14));
        notesLabel.setForeground(new Color(50, 50, 50));
        saleContent.add(notesLabel, gbc);
        
        gbc.gridy = 12;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(5, 0, 0, 0);
        
        String notesText = "Vehicle sold and delivered successfully. All paperwork completed and signed. " +
                          "Extended warranty documentation provided to buyer. " +
                          "Keys, manuals, and spare key fob handed over. " +
                          "Customer satisfaction survey completed with excellent rating.";
        
        JTextArea notesArea = new JTextArea(notesText);
        notesArea.setFont(new Font("Arial", Font.PLAIN, 13));
        notesArea.setForeground(new Color(80, 80, 80));
        notesArea.setBackground(new Color(248, 250, 252));
        notesArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        notesArea.setEditable(false);
        saleContent.add(notesArea, gbc);
        
        saleCard.add(saleContent, BorderLayout.CENTER);
        
        return saleCard;
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
        gbc.weightx = 0;
        gbc.weighty = 0;
        
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Arial", Font.BOLD, 14));
        labelComponent.setForeground(new Color(50, 50, 50));
        labelComponent.setPreferredSize(new Dimension(120, 25));
        panel.add(labelComponent, gbc);
        
        // Value
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Arial", Font.PLAIN, 14));
        valueComponent.setForeground(new Color(80, 80, 80));
        
        // Special formatting for price-related fields
        if (label.contains("Price") || label.contains("Amount") || label.contains("Tax")) {
            valueComponent.setFont(new Font("Arial", Font.BOLD, 14));
            valueComponent.setForeground(PRIMARY_GREEN);
        }
        
        panel.add(valueComponent, gbc);
    }
    
    /**
     * Get engine type based on car type
     */
    private String getEngineByType(String type) {
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
     * Generate features text based on car information
     */
    private String generateFeaturesText(SoldCarsUI.SoldCar car) {
        StringBuilder features = new StringBuilder();
        
        if ("Electric".equals(car.getType())) {
            features.append("• Advanced electric drivetrain\n")
                    .append("• Fast charging capability\n")
                    .append("• Regenerative braking\n")
                    .append("• Autopilot features\n");
        } else if ("Luxury".equals(car.getType())) {
            features.append("• Premium leather interior\n")
                    .append("• Heated and ventilated seats\n")
                    .append("• Premium sound system\n")
                    .append("• Advanced navigation\n");
        } else if ("Sports".equals(car.getType())) {
            features.append("• Sport-tuned suspension\n")
                    .append("• Performance brakes\n")
                    .append("• Sport steering wheel\n")
                    .append("• Launch control\n");
        } else if ("Truck".equals(car.getType())) {
            features.append("• 4WD capability\n")
                    .append("• Towing package\n")
                    .append("• Bed liner\n")
                    .append("• Heavy-duty suspension\n");
        } else {
            features.append("• Air conditioning\n")
                    .append("• Power windows\n")
                    .append("• Bluetooth connectivity\n")
                    .append("• Backup camera\n");
        }
        
        features.append("• Advanced safety features\n")
                .append("• Comprehensive warranty\n")
                .append("• Roadside assistance");
        
        return features.toString();
    }
    
    /**
     * Print receipt for the sold car
     */
    private void printReceipt() {
        JOptionPane.showMessageDialog(this,
            "Printing receipt for:\n\n" +
            "Vehicle: " + car.getModel() + " " + car.getYear() + "\n" +
            "Buyer: " + car.getBuyerName() + "\n" +
            "Sale Price: " + car.getSalePrice() + "\n" +
            "Date: " + car.getSaleDate() + "\n\n" +
            "Receipt will be sent to the default printer.",
            "Print Receipt",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Email receipt for the sold car
     */
    private void emailReceipt() {
        String buyerEmail = car.getBuyerName().toLowerCase().replace(" ", ".") + "@example.com";
        
        JOptionPane.showMessageDialog(this,
            "Emailing receipt to:\n\n" +
            "Recipient: " + car.getBuyerName() + "\n" +
            "Email: " + buyerEmail + "\n" +
            "Vehicle: " + car.getModel() + " " + car.getYear() + "\n" +
            "Sale Price: " + car.getSalePrice() + "\n\n" +
            "Receipt email will be sent shortly.",
            "Email Receipt",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Go back to sold cars page
     */
    private void goBackToSoldCars() {
        dispose(); // Close current window
        SwingUtilities.invokeLater(() -> new SoldCarsUI(adminId).setVisible(true));
    }
    
    /**
     * Main method to test the Show Sold Car UI
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set look and feel to system look and feel
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // Create a sample sold car for testing
            SoldCarsUI.SoldCar sampleCar = new SoldCarsUI.SoldCar(
                1, "Toyota Camry", "2023", "Sedan", "Silver", "$29,500", "Jan 15, 2025", 
                "John Smith", "(555) 101-2001", "Financing", "placeholder_toyota.jpg"
            );
            
            new ShowSoldCarUI(sampleCar).setVisible(true);
        });
    }
}