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
import java.io.File;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Edit Car UI class for editing car details with photo upload capability
 */
public class EditCarUI extends BaseUI {
    
    private CarManagement.Car car;
    private JTextField modelField;
    private JSpinner yearSpinner;
    private JComboBox<String> typeComboBox;
    private JTextField colorField;
    private JTextField priceField;
    private JComboBox<String> statusComboBox;
    private JSpinner dateAddedSpinner;
    private JTextArea notesArea;
    private JLabel imageLabel;
    private String selectedImagePath = "";
    
    /**
     * Constructor with car data and admin ID
     * @param car The car to edit
     * @param adminId The ID of the admin user
     */
    public EditCarUI(CarManagement.Car car, int adminId) {
        super(adminId);
        this.car = car;
        this.selectedImagePath = car.getImagePath();
    }
    
    /**
     * Constructor with car data
     * @param car The car to edit
     */
    public EditCarUI(CarManagement.Car car) {
        super();
        this.car = car;
        this.selectedImagePath = car.getImagePath();
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
     * Create the content panel for editing car details
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
        
        JLabel headerLabel = new JLabel("Edit " + car.getModel() + " " + car.getYear());
        headerLabel.setFont(new Font("Arial", Font.BOLD, 26));
        headerLabel.setForeground(new Color(50, 50, 50));
        
        // Sub header info
        JLabel subHeaderLabel = new JLabel("Modify vehicle information and specifications");
        subHeaderLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subHeaderLabel.setForeground(new Color(120, 120, 120));
        
        headerPanel.add(headerLabel, BorderLayout.NORTH);
        headerPanel.add(subHeaderLabel, BorderLayout.SOUTH);
        
        // Buttons panel for top right
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setOpaque(false);
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        
        // Back button
        JButton backButton = new JButton("← Back to Details");
        backButton.setBackground(new Color(108, 117, 125));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);
        
        // Add action listener to back button
        backButton.addActionListener(e -> goBackToDetails());
        
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
        
        buttonsPanel.add(backButton);
        
        // Add header and buttons to top panel
        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new BorderLayout());
        topPanel.add(headerPanel, BorderLayout.WEST);
        topPanel.add(buttonsPanel, BorderLayout.EAST);
        
        // Form content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        
        // Create form panel
        JPanel formPanel = createFormPanel();
        
        // Add components to content panel
        contentPanel.add(formPanel);
        
        // Create footer panel
        footerPanel = createFooterPanel();
        
        // Add all panels to main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    /**
     * Create the form panel with all input fields
     */
    private JPanel createFormPanel() {
        RoundedPanel formCard = new RoundedPanel(15, Color.WHITE);
        formCard.setLayout(new BorderLayout());
        formCard.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        formCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel formContent = new JPanel(new GridBagLayout());
        formContent.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Form title
        JLabel formTitle = new JLabel("Vehicle Information");
        formTitle.setFont(new Font("Arial", Font.BOLD, 20));
        formTitle.setForeground(new Color(50, 50, 50));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.insets = new Insets(0, 0, 20, 0);
        formContent.add(formTitle, gbc);
        
        // Reset insets and gridwidth
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridwidth = 1;
        
        // Left column - Vehicle Photo
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 7;
        gbc.fill = GridBagConstraints.BOTH;
        JPanel photoPanel = createPhotoUploadPanel();
        formContent.add(photoPanel, gbc);
        
        // Reset for right column fields
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Model field
        gbc.gridx = 2;
        gbc.gridy = 1;
        formContent.add(new JLabel("Model:"), gbc);
        gbc.gridx = 3;
        modelField = new JTextField(car.getModel(), 20);
        modelField.setFont(new Font("Arial", Font.PLAIN, 14));
        modelField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        formContent.add(modelField, gbc);
        
        // Year field
        gbc.gridx = 2;
        gbc.gridy = 2;
        formContent.add(new JLabel("Year:"), gbc);
        gbc.gridx = 3;
        SpinnerNumberModel yearModel = new SpinnerNumberModel(Integer.parseInt(car.getYear()), 1990, 2030, 1);
        yearSpinner = new JSpinner(yearModel);
        yearSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        formContent.add(yearSpinner, gbc);
        
        // Type field
        gbc.gridx = 2;
        gbc.gridy = 3;
        formContent.add(new JLabel("Type:"), gbc);
        gbc.gridx = 3;
        String[] types = {"Sedan", "SUV", "Truck", "Electric", "Luxury", "Sports", "Hatchback", "Van"};
        typeComboBox = new JComboBox<>(types);
        typeComboBox.setSelectedItem(car.getType());
        typeComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        typeComboBox.setBackground(Color.WHITE);
        formContent.add(typeComboBox, gbc);
        
        // Color field
        gbc.gridx = 2;
        gbc.gridy = 4;
        formContent.add(new JLabel("Color:"), gbc);
        gbc.gridx = 3;
        colorField = new JTextField(car.getColor(), 20);
        colorField.setFont(new Font("Arial", Font.PLAIN, 14));
        colorField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        formContent.add(colorField, gbc);
        
        // Price field
        gbc.gridx = 2;
        gbc.gridy = 5;
        formContent.add(new JLabel("Price:"), gbc);
        gbc.gridx = 3;
        priceField = new JTextField(car.getPrice().replace("$", "").replace(",", ""), 20);
        priceField.setFont(new Font("Arial", Font.PLAIN, 14));
        priceField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        formContent.add(priceField, gbc);
        
        // Status field
        gbc.gridx = 2;
        gbc.gridy = 6;
        formContent.add(new JLabel("Status:"), gbc);
        gbc.gridx = 3;
        String[] statuses = {"Available", "Reserved", "Sold"};
        statusComboBox = new JComboBox<>(statuses);
        statusComboBox.setSelectedItem(car.getStatus());
        statusComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        statusComboBox.setBackground(Color.WHITE);
        formContent.add(statusComboBox, gbc);
        
        // Date Added field
        gbc.gridx = 2;
        gbc.gridy = 7;
        formContent.add(new JLabel("Date Added:"), gbc);
        gbc.gridx = 3;
        Date today = new Date();
        SpinnerDateModel dateModel = new SpinnerDateModel(today, null, today, java.util.Calendar.DAY_OF_MONTH);
        dateAddedSpinner = new JSpinner(dateModel);
        dateAddedSpinner.setEditor(new JSpinner.DateEditor(dateAddedSpinner, "MMM dd, yyyy"));
        dateAddedSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        formContent.add(dateAddedSpinner, gbc);
        
        // Notes field (spans full width)
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 4;
        gbc.insets = new Insets(20, 10, 10, 10);
        formContent.add(new JLabel("Notes:"), gbc);
        
        gbc.gridy = 9;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.3;
        gbc.insets = new Insets(5, 0, 0, 0);
        
        String initialNotes = generateInitialNotes();
        notesArea = new JTextArea(initialNotes, 4, 50);
        notesArea.setFont(new Font("Arial", Font.PLAIN, 14));
        notesArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        JScrollPane notesScrollPane = new JScrollPane(notesArea);
        notesScrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        formContent.add(notesScrollPane, gbc);
        
        // Buttons panel
        gbc.gridy = 10;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.insets = new Insets(30, 10, 0, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonsPanel = createFormButtonsPanel();
        formContent.add(buttonsPanel, gbc);
        
        formCard.add(formContent, BorderLayout.CENTER);
        
        return formCard;
    }
    
    /**
     * Create photo upload panel
     */
    private JPanel createPhotoUploadPanel() {
        JPanel photoPanel = new JPanel();
        photoPanel.setLayout(new BoxLayout(photoPanel, BoxLayout.Y_AXIS));
        photoPanel.setOpaque(false);
        photoPanel.setPreferredSize(new Dimension(300, 400));
        photoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 30));
        
        JLabel photoTitle = new JLabel("Vehicle Photo");
        photoTitle.setFont(new Font("Arial", Font.BOLD, 16));
        photoTitle.setForeground(new Color(50, 50, 50));
        photoTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Image display area
        RoundedPanel imageContainer = new RoundedPanel(10, new Color(248, 250, 252));
        imageContainer.setLayout(new BorderLayout());
        imageContainer.setPreferredSize(new Dimension(280, 200));
        imageContainer.setMaximumSize(new Dimension(280, 200));
        imageContainer.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2, true));
        imageContainer.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        
        // Load existing image
        loadExistingImage();
        
        imageContainer.add(imageLabel, BorderLayout.CENTER);
        
        // Upload button
        JButton uploadButton = new JButton("Change Photo");
        uploadButton.setBackground(PRIMARY_BLUE);
        uploadButton.setForeground(Color.WHITE);
        uploadButton.setFocusPainted(false);
        uploadButton.setFont(new Font("Arial", Font.BOLD, 14));
        uploadButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        uploadButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        uploadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        uploadButton.setMaximumSize(new Dimension(200, 40));
        
        // Upload button action
        uploadButton.addActionListener(e -> choosePhoto());
        
        // Image container click action
        imageContainer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                choosePhoto();
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                imageContainer.setBackground(new Color(240, 245, 250));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                imageContainer.setBackground(new Color(248, 250, 252));
            }
        });
        
        // Remove photo button
        JButton removeButton = new JButton("Remove Photo");
        removeButton.setBackground(PRIMARY_RED);
        removeButton.setForeground(Color.WHITE);
        removeButton.setFocusPainted(false);
        removeButton.setFont(new Font("Arial", Font.PLAIN, 12));
        removeButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        removeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        removeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeButton.setMaximumSize(new Dimension(150, 30));
        removeButton.setVisible(!selectedImagePath.isEmpty() && !selectedImagePath.startsWith("placeholder"));
        
        removeButton.addActionListener(e -> removePhoto(removeButton));
        
        // Add components to photo panel
        photoPanel.add(photoTitle);
        photoPanel.add(Box.createVerticalStrut(15));
        photoPanel.add(imageContainer);
        photoPanel.add(Box.createVerticalStrut(15));
        photoPanel.add(uploadButton);
        photoPanel.add(Box.createVerticalStrut(10));
        photoPanel.add(removeButton);
        
        return photoPanel;
    }
    
    /**
     * Load existing image
     */
    private void loadExistingImage() {
        try {
            if (!selectedImagePath.isEmpty() && !selectedImagePath.startsWith("placeholder")) {
                ImageIcon originalIcon = new ImageIcon(selectedImagePath);
                if (originalIcon.getIconWidth() > 0) {
                    Image scaledImage = originalIcon.getImage().getScaledInstance(260, 180, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImage));
                    imageLabel.setText("");
                    return;
                }
            }
        } catch (Exception e) {
            // Fall through to placeholder
        }
        
        // Use placeholder if no image or image not found
        imageLabel.setIcon(null);
        imageLabel.setText("<html><div style='text-align: center;'>"
                + "<div style='font-size: 48px; color: #888;'>🚗</div>"
                + "<div style='margin-top: 10px; color: #666;'>Click to upload image</div>"
                + "</html>");
    }
    
    /**
     * Handle photo selection
     */
    private void choosePhoto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Car Photo");
        
        // Set file filter for images
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Image files", "jpg", "jpeg", "png", "gif", "bmp");
        fileChooser.setFileFilter(filter);
        
        int result = fileChooser.showOpenDialog(this);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            selectedImagePath = selectedFile.getAbsolutePath();
            
            try {
                // Load and display the image
                ImageIcon originalIcon = new ImageIcon(selectedImagePath);
                Image scaledImage = originalIcon.getImage().getScaledInstance(260, 180, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
                imageLabel.setText(""); // Remove placeholder text
                
                // Show remove button
                Component removeButton = ((JPanel) imageLabel.getParent().getParent()).getComponent(4);
                removeButton.setVisible(true);
                
                // Refresh the panel
                imageLabel.getParent().revalidate();
                imageLabel.getParent().repaint();
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error loading image: " + e.getMessage(), 
                    "Image Error", 
                    JOptionPane.ERROR_MESSAGE);
                selectedImagePath = car.getImagePath(); // Revert to original
            }
        }
    }
    
    /**
     * Remove selected photo
     */
    private void removePhoto(JButton removeButton) {
        selectedImagePath = "";
        imageLabel.setIcon(null);
        imageLabel.setText("<html><div style='text-align: center;'>"
                + "<div style='font-size: 48px; color: #888;'>🚗</div>"
                + "<div style='margin-top: 10px; color: #666;'>Click to upload image</div>"
                + "</html>");
        removeButton.setVisible(false);
        
        // Refresh the panel
        imageLabel.getParent().revalidate();
        imageLabel.getParent().repaint();
    }
    
    /**
     * Create form buttons panel
     */
    private JPanel createFormButtonsPanel() {
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonsPanel.setOpaque(false);
        
        // Cancel button
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(108, 117, 125));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        cancelButton.addActionListener(e -> goBackToDetails());
        
        // Sell Car button (only show if car is available or reserved)
        JButton sellButton = null;
        if ("Available".equals(car.getStatus()) || "Reserved".equals(car.getStatus())) {
            sellButton = new JButton("Sell Car");
            sellButton.setBackground(PRIMARY_YELLOW);
            sellButton.setForeground(Color.WHITE);
            sellButton.setFocusPainted(false);
            sellButton.setFont(new Font("Arial", Font.BOLD, 14));
            sellButton.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
            sellButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            sellButton.addActionListener(e -> sellCar());
        }
        
        // Delete button
        JButton deleteButton = new JButton("Delete Vehicle");
        deleteButton.setBackground(PRIMARY_RED);
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.setFont(new Font("Arial", Font.BOLD, 14));
        deleteButton.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        deleteButton.addActionListener(e -> deleteVehicle());
        
        // Save button
        JButton saveButton = new JButton("Save Changes");
        saveButton.setBackground(PRIMARY_GREEN);
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveButton.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        saveButton.addActionListener(e -> saveChanges());
        
        buttonsPanel.add(cancelButton);
        if (sellButton != null) {
            buttonsPanel.add(sellButton);
        }
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(saveButton);
        
        return buttonsPanel;
    }
    
    /**
     * Sell the car - open sell car dialog
     */
    private void sellCar() {
        SellCarDialog sellDialog = new SellCarDialog(this, car);
        sellDialog.setVisible(true);
        
        // Check if sale was completed
        if (sellDialog.isSaleCompleted()) {
            // Car has been sold, go back to inventory
            JOptionPane.showMessageDialog(this,
                "Vehicle sold successfully!\nReturning to inventory...",
                "Sale Complete",
                JOptionPane.INFORMATION_MESSAGE);
            
            goBackToInventory();
        }
    }
    
    /**
     * Generate initial notes for the text area
     */
    private String generateInitialNotes() {
        StringBuilder notes = new StringBuilder();
        
        if ("Electric".equals(car.getType())) {
            notes.append("Electric vehicle with advanced technology and zero emissions. ");
        } else if ("Luxury".equals(car.getType())) {
            notes.append("Luxury vehicle with premium features and materials. ");
        } else if ("Sports".equals(car.getType())) {
            notes.append("High-performance sports vehicle for enthusiasts. ");
        } else if ("Truck".equals(car.getType())) {
            notes.append("Heavy-duty truck with excellent capabilities. ");
        } else {
            notes.append("Reliable and efficient vehicle for daily use. ");
        }
        
        notes.append("Vehicle includes full manufacturer warranty and has been thoroughly inspected. ");
        notes.append("All maintenance records available upon request.");
        
        return notes.toString();
    }
    
    /**
 * Save changes to the car
 * This method should be in your EditCarUI class
 */
private void saveChanges() {
    // Validate required fields
    if (modelField.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter the car model.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        modelField.requestFocus();
        return;
    }
    
    if (colorField.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter the color.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        colorField.requestFocus();
        return;
    }
    
    if (priceField.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter the price.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        priceField.requestFocus();
        return;
    }
    
    // Validate price format
    try {
        double price = Double.parseDouble(priceField.getText().replace(",", ""));
        if (price <= 0) {
            throw new NumberFormatException("Price must be positive");
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Please enter a valid price.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        priceField.requestFocus();
        return;
    }
    
    // Update car object with new values
    car.setModel(modelField.getText().trim());
    car.setYear(yearSpinner.getValue().toString());
    car.setType((String) typeComboBox.getSelectedItem());
    car.setColor(colorField.getText().trim());
    
    // Format price
    try {
        double priceValue = Double.parseDouble(priceField.getText().replace(",", ""));
        car.setPrice(String.format("$%,.0f", priceValue));
    } catch (NumberFormatException e) {
        car.setPrice("$" + priceField.getText().trim());
    }
    
    car.setStatus((String) statusComboBox.getSelectedItem());
    
    // Format date
    Date selectedDate = (Date) dateAddedSpinner.getValue();
    java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MMM dd, yyyy");
    car.setDateAdded(formatter.format(selectedDate));
    
    // Update image path
    car.setImagePath(selectedImagePath);
    
    // Use CarManagementIntegration to update the car in the database
    CarManagementIntegration integration = CarManagementIntegration.getInstance();
    boolean success = integration.updateCar(car);
    
    if (success) {
        // Go back to details page
        goBackToDetails();
    }
    // Error messages are handled by the integration class
}
    
    /**
     * Delete the vehicle
     */
    private void deleteVehicle() {
        int result = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this vehicle?\n\n" +
            "Vehicle: " + car.getModel() + " " + car.getYear() + "\n" +
            "This action cannot be undone.", 
            "Confirm Deletion", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (result == JOptionPane.YES_OPTION) {
            // In a real application, this would delete from the database
            JOptionPane.showMessageDialog(this, 
                "Vehicle has been deleted successfully.", 
                "Deleted", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Go back to inventory
            goBackToInventory();
        }
    }
    
    /**
     * Go back to car details page
     */
    private void goBackToDetails() {
        dispose(); // Close current window
        SwingUtilities.invokeLater(() -> new ShowCarUI(car, adminId).setVisible(true));
    }
    
    /**
     * Go back to inventory page
     */
    private void goBackToInventory() {
        dispose(); // Close current window
        SwingUtilities.invokeLater(() -> new CarManagement(adminId).setVisible(true));
    }
    
    /**
     * Main method to test the Edit Car UI
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
            CarManagement.Car sampleCar = new CarManagement.Car(
                1, "Tesla Model 3", "2023", "Electric", "Black", "$47,000", "Available", "May 12, 2025", "placeholder_tesla.jpg"
            );
            
            new EditCarUI(sampleCar).setVisible(true);
        });
    }
}