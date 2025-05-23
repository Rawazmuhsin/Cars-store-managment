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
 * Show Coming Soon UI class that displays detailed information about a coming soon car
 */
public class ShowComingSoonUI extends BaseUI {
    
    private ComingSoonUI.ComingSoonCar car;
    
    /**
     * Constructor with car data and admin ID
     * @param car The coming soon car to display
     * @param adminId The ID of the admin user
     */
    public ShowComingSoonUI(ComingSoonUI.ComingSoonCar car, int adminId) {
        super(adminId);
        this.car = car;
    }
    
    /**
     * Constructor with car data
     * @param car The coming soon car to display
     */
    public ShowComingSoonUI(ComingSoonUI.ComingSoonCar car) {
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
        // Add menu items - set Coming Soon as active
        addMenuItem(menuPanel, "Dashboard", false);
        addMenuItem(menuPanel, "Car Inventory", false);
        addMenuItem(menuPanel, "Add New Car", false);
        addMenuItem(menuPanel, "Coming Soon", true);
        addMenuItem(menuPanel, "Sold Cars", false);
        addMenuItem(menuPanel, "Economic", false);
        addMenuItem(menuPanel, "Audit Logs", false);
    }
    
    /**
     * Create the content panel for showing coming soon car details
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
        JButton backButton = new JButton("← Back to Coming Soon");
        backButton.setBackground(new Color(108, 117, 125));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);
        
        // Add action listener to back button
        backButton.addActionListener(e -> goBackToComingSoon());
        
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
        editButton.setBackground(PRIMARY_BLUE);
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
        
        // Delete button
        JButton deleteButton = new JButton("Delete");
        deleteButton.setBackground(PRIMARY_RED);
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
        
        buttonsPanel.add(backButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(deleteButton);
        
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
     * Create the details panel showing car information
     */
    private JPanel createDetailsPanel() {
        JPanel mainContainer = new JPanel();
        mainContainer.setOpaque(false);
        mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.X_AXIS));
        mainContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Left panel - Car image
        RoundedPanel imagePanel = createImagePanel();
        
        // Right panel - Car details
        RoundedPanel infoPanel = createInfoPanel();
        
        mainContainer.add(imagePanel);
        mainContainer.add(Box.createHorizontalStrut(30));
        mainContainer.add(infoPanel);
        
        return mainContainer;
    }
    
    /**
     * Create image panel
     */
    private RoundedPanel createImagePanel() {
        RoundedPanel imageCard = new RoundedPanel(15, Color.WHITE);
        imageCard.setLayout(new BorderLayout());
        imageCard.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        imageCard.setPreferredSize(new Dimension(450, 400));
        imageCard.setMaximumSize(new Dimension(450, 400));
        
        JLabel titleLabel = new JLabel("Vehicle Photo");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(50, 50, 50));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Image container
        JPanel imageContainer = new JPanel();
        imageContainer.setLayout(new BorderLayout());
        imageContainer.setBackground(new Color(248, 250, 252));
        imageContainer.setPreferredSize(new Dimension(390, 280));
        imageContainer.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 2));
        
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        
        try {
            // Try to load the car image
            ImageIcon originalIcon = new ImageIcon(car.getImagePath());
            if (originalIcon.getIconWidth() > 0) {
                Image scaledImage = originalIcon.getImage().getScaledInstance(380, 270, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
            } else {
                throw new Exception("Image not found");
            }
        } catch (Exception e) {
            // Use placeholder if image not found
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
        switch (car.getStatus()) {
            case "Ordered":
                statusBadge.setBackground(new Color(255, 250, 230));
                statusBadge.setForeground(new Color(255, 153, 0));
                break;
            case "In Transit":
                statusBadge.setBackground(new Color(232, 255, 232));
                statusBadge.setForeground(new Color(25, 135, 84));
                break;
            case "In Production":
                statusBadge.setBackground(new Color(230, 255, 250));
                statusBadge.setForeground(new Color(13, 110, 253));
                break;
            default:
                statusBadge.setBackground(new Color(240, 240, 240));
                statusBadge.setForeground(new Color(70, 70, 70));
        }
        
        imageCard.add(titleLabel, BorderLayout.NORTH);
        imageCard.add(Box.createVerticalStrut(15), BorderLayout.NORTH);
        imageCard.add(imageContainer, BorderLayout.CENTER);
        imageCard.add(statusBadge, BorderLayout.SOUTH);
        
        return imageCard;
    }
    
    /**
     * Create information panel
     */
    private RoundedPanel createInfoPanel() {
        RoundedPanel infoCard = new RoundedPanel(15, Color.WHITE);
        infoCard.setLayout(new BorderLayout());
        infoCard.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        infoCard.setPreferredSize(new Dimension(500, 400));
        
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
        addDetailRow(infoContent, gbc, 3, "Color:", car.getColor());
        addDetailRow(infoContent, gbc, 4, "Expected Price:", car.getExpectedPrice());
        addDetailRow(infoContent, gbc, 5, "Arrival Date:", car.getArrivalDate());
        addDetailRow(infoContent, gbc, 6, "Current Status:", car.getStatus());
        
        // Additional specifications (sample data)
        addDetailRow(infoContent, gbc, 7, "Engine:", "2.0L Turbocharged");
        addDetailRow(infoContent, gbc, 8, "Transmission:", "8-Speed Automatic");
        addDetailRow(infoContent, gbc, 9, "Fuel Type:", "Premium Gasoline");
        addDetailRow(infoContent, gbc, 10, "Drive Type:", "All-Wheel Drive");
        
        // Notes section
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 10, 0);
        JLabel notesLabel = new JLabel("Notes:");
        notesLabel.setFont(new Font("Arial", Font.BOLD, 14));
        notesLabel.setForeground(new Color(50, 50, 50));
        infoContent.add(notesLabel, gbc);
        
        gbc.gridy = 12;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(5, 0, 0, 0);
        
        JTextArea notesArea = new JTextArea("This vehicle has been specially ordered with premium features including heated seats, panoramic sunroof, and advanced driver assistance package. Expected delivery includes full manufacturer warranty and complimentary first service.");
        notesArea.setFont(new Font("Arial", Font.PLAIN, 13));
        notesArea.setForeground(new Color(80, 80, 80));
        notesArea.setBackground(new Color(248, 250, 252));
        notesArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        notesArea.setEditable(false);
        infoContent.add(notesArea, gbc);
        
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
        
        // Special formatting for price
        if (label.contains("Price")) {
            valueComponent.setFont(new Font("Arial", Font.BOLD, 14));
            valueComponent.setForeground(PRIMARY_GREEN);
        }
        
        // Special formatting for status
        if (label.contains("Status")) {
            valueComponent.setFont(new Font("Arial", Font.BOLD, 14));
            switch (value) {
                case "Ordered":
                    valueComponent.setForeground(new Color(255, 153, 0));
                    break;
                case "In Transit":
                    valueComponent.setForeground(PRIMARY_GREEN);
                    break;
                case "In Production":
                    valueComponent.setForeground(PRIMARY_BLUE);
                    break;
            }
        }
        
        panel.add(valueComponent, gbc);
    }
    
    /**
     * Edit car details
     */
    private void editCarDetails() {
        JOptionPane.showMessageDialog(this, 
            "Edit functionality would open a form to modify the car details.\n" +
            "In a real application, this would allow updating:\n" +
            "• Expected price\n" +
            "• Arrival date\n" +
            "• Status\n" +
            "• Notes\n" +
            "• Vehicle photo", 
            "Edit Car Details", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Delete car entry
     */
    private void deleteCarEntry() {
        int result = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this coming soon vehicle?\n\n" +
            "Vehicle: " + car.getModel() + " " + car.getYear() + "\n" +
            "This action cannot be undone.", 
            "Confirm Deletion", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (result == JOptionPane.YES_OPTION) {
            // In a real application, this would delete from the database
            JOptionPane.showMessageDialog(this, 
                "Vehicle has been removed from the coming soon list.", 
                "Deleted", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Go back to coming soon page
            goBackToComingSoon();
        }
    }
    
    /**
     * Go back to coming soon page
     */
    private void goBackToComingSoon() {
        dispose(); // Close current window
        SwingUtilities.invokeLater(() -> new ComingSoonUI(adminId).setVisible(true));
    }
    
    /**
     * Main method to test the Show Coming Soon UI
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set look and feel to system look and feel
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // Create a sample car for testing
            ComingSoonUI.ComingSoonCar sampleCar = new ComingSoonUI.ComingSoonCar(
                1, "Tesla Model Y", "2025", "White", "$55,000", "Jun 15, 2025", "Ordered", "placeholder_tesla.jpg"
            );
            
            new ShowComingSoonUI(sampleCar).setVisible(true);
        });
    }
}