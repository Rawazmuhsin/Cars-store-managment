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
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Add Coming Soon UI class for adding new coming soon cars with photo upload
 */
public class AddComingSoonUI extends BaseUI {
    
    private JTextField modelField;
    private JTextField yearField;
    private JTextField colorField;
    private JTextField priceField;
    private JSpinner arrivalDateSpinner;
    private JComboBox<String> statusComboBox;
    private JTextArea notesArea;
    private JLabel imageLabel;
    private String selectedImagePath = "";
    
    /**
     * Constructor with admin ID
     * @param adminId The ID of the admin user
     */
    public AddComingSoonUI(int adminId) {
        super(adminId);
    }
    
    /**
     * Default constructor
     */
    public AddComingSoonUI() {
        super();
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
     * Create the content panel for adding coming soon cars
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
        
        JLabel headerLabel = new JLabel("Add Coming Soon Vehicle");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 26));
        headerLabel.setForeground(new Color(50, 50, 50));
        
        // Sub header info
        JLabel subHeaderLabel = new JLabel("Add a new vehicle to the coming soon list");
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
        gbc.gridheight = 6;
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
        modelField = new JTextField(20);
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
        yearField = new JTextField(20);
        yearField.setFont(new Font("Arial", Font.PLAIN, 14));
        yearField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        formContent.add(yearField, gbc);
        
        // Color field
        gbc.gridx = 2;
        gbc.gridy = 3;
        formContent.add(new JLabel("Color:"), gbc);
        gbc.gridx = 3;
        colorField = new JTextField(20);
        colorField.setFont(new Font("Arial", Font.PLAIN, 14));
        colorField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        formContent.add(colorField, gbc);
        
        // Expected Price field
        gbc.gridx = 2;
        gbc.gridy = 4;
        formContent.add(new JLabel("Expected Price:"), gbc);
        gbc.gridx = 3;
        priceField = new JTextField(20);
        priceField.setFont(new Font("Arial", Font.PLAIN, 14));
        priceField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        formContent.add(priceField, gbc);
        
        // Arrival Date field
        gbc.gridx = 2;
        gbc.gridy = 5;
        formContent.add(new JLabel("Expected Arrival:"), gbc);
        gbc.gridx = 3;
        Date today = new Date();
        Date futureDate = new Date(today.getTime() + (30L * 24 * 60 * 60 * 1000)); // 30 days from now
        SpinnerDateModel dateModel = new SpinnerDateModel(futureDate, today, null, java.util.Calendar.DAY_OF_MONTH);
        arrivalDateSpinner = new JSpinner(dateModel);
        arrivalDateSpinner.setEditor(new JSpinner.DateEditor(arrivalDateSpinner, "MMM dd, yyyy"));
        arrivalDateSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        formContent.add(arrivalDateSpinner, gbc);
        
        // Status field
        gbc.gridx = 2;
        gbc.gridy = 6;
        formContent.add(new JLabel("Status:"), gbc);
        gbc.gridx = 3;
        String[] statuses = {"Ordered", "In Production", "In Transit"};
        statusComboBox = new JComboBox<>(statuses);
        statusComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        statusComboBox.setBackground(Color.WHITE);
        formContent.add(statusComboBox, gbc);
        
        // Notes field (spans full width)
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 4;
        gbc.insets = new Insets(20, 10, 10, 10);
        formContent.add(new JLabel("Notes:"), gbc);
        
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.3;
        notesArea = new JTextArea(4, 50);
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
        gbc.gridy = 9;
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
        imageLabel.setText("<html><div style='text-align: center;'>"
                + "<div style='font-size: 48px; color: #888;'>📷</div>"
                + "<div style='margin-top: 10px; color: #666;'>Click to upload image</div>"
                + "</html>");
        imageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        imageContainer.add(imageLabel, BorderLayout.CENTER);
        
        // Upload button
        JButton uploadButton = new JButton("Choose Photo");
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
        removeButton.setVisible(false); // Initially hidden
        
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
                selectedImagePath = "";
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
                + "<div style='font-size: 48px; color: #888;'>📷</div>"
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
        
        cancelButton.addActionListener(e -> goBackToComingSoon());
        
        // Save button
        JButton saveButton = new JButton("Add Coming Soon Vehicle");
        saveButton.setBackground(PRIMARY_GREEN);
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveButton.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        saveButton.addActionListener(e -> saveComingSoonCar());
        
        buttonsPanel.add(cancelButton);
        buttonsPanel.add(saveButton);
        
        return buttonsPanel;
    }
    
    /**
     * Save the coming soon car
     */
    private void saveComingSoonCar() {
        // Validate required fields
        if (modelField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the car model.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            modelField.requestFocus();
            return;
        }
        
        if (yearField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the year.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            yearField.requestFocus();
            return;
        }
        
        if (colorField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the color.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            colorField.requestFocus();
            return;
        }
        
        if (priceField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the expected price.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            priceField.requestFocus();
            return;
        }
        
        // In a real application, this would save to the database
        String successMessage = "Coming Soon Vehicle Added Successfully!\n\n" +
                "Model: " + modelField.getText() + "\n" +
                "Year: " + yearField.getText() + "\n" +
                "Color: " + colorField.getText() + "\n" +
                "Expected Price: " + priceField.getText() + "\n" +
                "Status: " + statusComboBox.getSelectedItem() + "\n" +
                (selectedImagePath.isEmpty() ? "" : "Image: " + selectedImagePath);
        
        JOptionPane.showMessageDialog(this, 
            successMessage, 
            "Success", 
            JOptionPane.INFORMATION_MESSAGE);
        
        // Go back to coming soon page
        goBackToComingSoon();
    }
    
    /**
     * Go back to coming soon page
     */
    private void goBackToComingSoon() {
        dispose(); // Close current window
        SwingUtilities.invokeLater(() -> new ComingSoonUI(adminId).setVisible(true));
    }
    
    /**
     * Main method to test the Add Coming Soon UI
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set look and feel to system look and feel
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new AddComingSoonUI().setVisible(true);
        });
    }
}