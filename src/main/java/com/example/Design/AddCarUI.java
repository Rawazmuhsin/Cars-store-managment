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
import java.text.SimpleDateFormat;
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
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * AddCarUI - Form for adding new cars to the inventory
 * This class handles the UI for adding a new car and saving it to the database
 */
public class AddCarUI extends BaseUI {
    
    private JTextField vinField;
    private JTextField manufacturerField;
    private JTextField modelField;
    private JSpinner yearSpinner;
    private JComboBox<String> categoryComboBox;
    private JTextField colorField;
    private JSpinner priceSpinner;
    private JSpinner costSpinner;
    private JSpinner mileageSpinner;
    private JTextField locationField;
    private JComboBox<String> statusComboBox;
    private JTextField engineTypeField;
    private JComboBox<String> transmissionComboBox;
    private JComboBox<String> fuelTypeComboBox;
    private JSpinner horsepowerSpinner;
    private JSpinner seatsSpinner;
    private JTextField fuelEconomyField;
    private JTextArea featuresArea;
    private JTextArea notesArea;
    private JLabel imageLabel;
    private String selectedImagePath = "";
    private JLabel dateAddedLabel;
    private String currentDate;
    
    /**
     * Constructor with admin ID
     * @param adminId The ID of the admin user
     */
    public AddCarUI(int adminId) {
        super(adminId);
        initializeCurrentDate();
    }
    
    /**
     * Default constructor
     */
    public AddCarUI() {
        super();
        initializeCurrentDate();
    }
    
    /**
     * Initialize the current date for the form
     */
    private void initializeCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy 'at' HH:mm");
        currentDate = dateFormat.format(new Date());
    }
    
    /**
     * Create a styled text field with consistent appearance
     * @return A styled JTextField
     */
    private JTextField createStyledTextField() {
        JTextField field = new JTextField(15);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setPreferredSize(new Dimension(180, 32));
        field.setMinimumSize(new Dimension(180, 32));
        return field;
    }
    
    /**
     * Add menu items to the sidebar
     * Overrides the method in BaseUI
     * @param menuPanel The panel to add menu items to
     */
    @Override
    protected void addMenuItems(JPanel menuPanel) {
        addMenuItem(menuPanel, "Dashboard", false);
        addMenuItem(menuPanel, "Car Inventory", false);
        addMenuItem(menuPanel, "Add New Car", true);
        addMenuItem(menuPanel, "Coming Soon", false);
        addMenuItem(menuPanel, "Sold Cars", false);
        addMenuItem(menuPanel, "Economic", false);
        addMenuItem(menuPanel, "Audit Logs", false);
    }
    
    /**
     * Create the content panel for the form
     * Overrides the method in BaseUI
     * @return JPanel containing the content
     */
    @Override
    protected JPanel createContentPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(LIGHT_GRAY_BG);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new BorderLayout());
        
        JLabel headerLabel = new JLabel("Add New Vehicle to Inventory");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 26));
        headerLabel.setForeground(new Color(50, 50, 50));
        
        JLabel subHeaderLabel = new JLabel("Enter complete vehicle information and specifications");
        subHeaderLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subHeaderLabel.setForeground(new Color(120, 120, 120));
        
        headerPanel.add(headerLabel, BorderLayout.NORTH);
        headerPanel.add(subHeaderLabel, BorderLayout.SOUTH);
        
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setOpaque(false);
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        
        JButton backButton = new JButton("← Back to Inventory");
        backButton.setBackground(new Color(108, 117, 125));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);
        
        backButton.addActionListener(e -> goBackToInventory());
        
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
        
        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new BorderLayout());
        topPanel.add(headerPanel, BorderLayout.WEST);
        topPanel.add(buttonsPanel, BorderLayout.EAST);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        
        JPanel formPanel = createFormPanel();
        contentPanel.add(formPanel);
        
        JPanel footerPanel = createFooterPanel();
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    /**
     * Create the main form panel with all input fields
     * @return JPanel containing the form
     */
    private JPanel createFormPanel() {
        RoundedPanel formCard = new RoundedPanel(15, Color.WHITE);
        formCard.setLayout(new BorderLayout());
        formCard.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        formCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setOpaque(false);
        mainContainer.setPreferredSize(new Dimension(1200, 900));
        mainContainer.setMinimumSize(new Dimension(1200, 900));
        
        JPanel formContent = new JPanel(new GridBagLayout());
        formContent.setOpaque(false);
        formContent.setPreferredSize(new Dimension(1150, 850));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        JLabel formTitle = new JLabel("Vehicle Information");
        formTitle.setFont(new Font("Arial", Font.BOLD, 20));
        formTitle.setForeground(new Color(50, 50, 50));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 6;
        gbc.insets = new Insets(0, 0, 10, 0);
        formContent.add(formTitle, gbc);
        
        dateAddedLabel = new JLabel("Date Added: " + currentDate);
        dateAddedLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        dateAddedLabel.setForeground(new Color(100, 100, 100));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 20, 0);
        formContent.add(dateAddedLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridheight = 8;
        gbc.fill = GridBagConstraints.BOTH;
        JPanel photoPanel = createPhotoUploadPanel();
        formContent.add(photoPanel, gbc);
        
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // VIN and Manufacturer
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(8, 20, 8, 10);
        formContent.add(new JLabel("VIN:"), gbc);
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(8, 10, 8, 30);
        vinField = createStyledTextField();
        formContent.add(vinField, gbc);
        
        gbc.gridx = 4;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(8, 20, 8, 10);
        formContent.add(new JLabel("Manufacturer:"), gbc);
        gbc.gridx = 5;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(8, 10, 8, 20);
        manufacturerField = createStyledTextField();
        formContent.add(manufacturerField, gbc);
        
        // Model and Year
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(8, 20, 8, 10);
        formContent.add(new JLabel("Model:"), gbc);
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(8, 10, 8, 30);
        modelField = createStyledTextField();
        formContent.add(modelField, gbc);
        
        gbc.gridx = 4;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(8, 20, 8, 10);
        formContent.add(new JLabel("Year:"), gbc);
        gbc.gridx = 5;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(8, 10, 8, 20);
        yearSpinner = new JSpinner(new SpinnerNumberModel(2024, 1990, 2030, 1));
        yearSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        yearSpinner.setPreferredSize(new Dimension(180, 32));
        formContent.add(yearSpinner, gbc);
        
        // Category and Color
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(8, 20, 8, 10);
        formContent.add(new JLabel("Category:"), gbc);
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(8, 10, 8, 30);
        String[] categories = {"sedan", "suv", "truck", "sports", "hatchback", "van", "luxury", "electric"};
        categoryComboBox = new JComboBox<>(categories);
        categoryComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        categoryComboBox.setPreferredSize(new Dimension(180, 32));
        formContent.add(categoryComboBox, gbc);
        
        gbc.gridx = 4;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(8, 20, 8, 10);
        formContent.add(new JLabel("Color:"), gbc);
        gbc.gridx = 5;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(8, 10, 8, 20);
        colorField = createStyledTextField();
        formContent.add(colorField, gbc);
        
        // Price and Cost
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(8, 20, 8, 10);
        formContent.add(new JLabel("Sale Price:"), gbc);
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(8, 10, 8, 30);
        priceSpinner = new JSpinner(new SpinnerNumberModel(25000.0, 0.0, 1000000.0, 100.0));
        priceSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        priceSpinner.setPreferredSize(new Dimension(180, 32));
        formContent.add(priceSpinner, gbc);
        
        gbc.gridx = 4;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(8, 20, 8, 10);
        formContent.add(new JLabel("Cost Price:"), gbc);
        gbc.gridx = 5;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(8, 10, 8, 20);
        costSpinner = new JSpinner(new SpinnerNumberModel(20000.0, 0.0, 1000000.0, 100.0));
        costSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        costSpinner.setPreferredSize(new Dimension(180, 32));
        formContent.add(costSpinner, gbc);
        
        // Mileage and Location
        gbc.gridx = 2;
        gbc.gridy = 6;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(8, 20, 8, 10);
        formContent.add(new JLabel("Mileage:"), gbc);
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(8, 10, 8, 30);
        mileageSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 500000, 1));
        mileageSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        mileageSpinner.setPreferredSize(new Dimension(180, 32));
        formContent.add(mileageSpinner, gbc);
        
        gbc.gridx = 4;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(8, 20, 8, 10);
        formContent.add(new JLabel("Location:"), gbc);
        gbc.gridx = 5;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(8, 10, 8, 20);
        locationField = createStyledTextField();
        locationField.setText("Main Lot");
        formContent.add(locationField, gbc);
        
        // Status
        gbc.gridx = 2;
        gbc.gridy = 7;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(8, 20, 8, 10);
        formContent.add(new JLabel("Status:"), gbc);
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(8, 10, 8, 20);
        String[] statuses = {"available", "reserved", "maintenance"};
        statusComboBox = new JComboBox<>(statuses);
        statusComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        statusComboBox.setPreferredSize(new Dimension(180, 32));
        formContent.add(statusComboBox, gbc);
        
        // Engine Specifications Section
        gbc.gridwidth = 1;
        gbc.gridx = 2;
        gbc.gridy = 8;
        gbc.gridwidth = 4;
        gbc.insets = new Insets(25, 20, 15, 20);
        JLabel engineTitle = new JLabel("Engine & Specifications");
        engineTitle.setFont(new Font("Arial", Font.BOLD, 18));
        engineTitle.setForeground(new Color(50, 50, 50));
        formContent.add(engineTitle, gbc);
        
        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 10, 8, 10);
        
        // Engine Type and Transmission
        gbc.gridx = 2;
        gbc.gridy = 9;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(8, 20, 8, 10);
        formContent.add(new JLabel("Engine Type:"), gbc);
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(8, 10, 8, 30);
        engineTypeField = createStyledTextField();
        engineTypeField.setText("2.5L 4-Cylinder");
        formContent.add(engineTypeField, gbc);
        
        gbc.gridx = 4;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(8, 20, 8, 10);
        formContent.add(new JLabel("Transmission:"), gbc);
        gbc.gridx = 5;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(8, 10, 8, 20);
        String[] transmissions = {"automatic", "manual", "semi-automatic"};
        transmissionComboBox = new JComboBox<>(transmissions);
        transmissionComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        transmissionComboBox.setPreferredSize(new Dimension(180, 32));
        formContent.add(transmissionComboBox, gbc);
        
        // Fuel Type and Horsepower
        gbc.gridx = 2;
        gbc.gridy = 10;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(8, 20, 8, 10);
        formContent.add(new JLabel("Fuel Type:"), gbc);
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(8, 10, 8, 30);
        String[] fuelTypes = {"gasoline", "diesel", "electric", "hybrid", "plugin_hybrid"};
        fuelTypeComboBox = new JComboBox<>(fuelTypes);
        fuelTypeComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        fuelTypeComboBox.setPreferredSize(new Dimension(180, 32));
        formContent.add(fuelTypeComboBox, gbc);
        
        gbc.gridx = 4;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(8, 20, 8, 10);
        formContent.add(new JLabel("Horsepower:"), gbc);
        gbc.gridx = 5;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(8, 10, 8, 20);
        horsepowerSpinner = new JSpinner(new SpinnerNumberModel(200, 50, 1000, 10));
        horsepowerSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        horsepowerSpinner.setPreferredSize(new Dimension(180, 32));
        formContent.add(horsepowerSpinner, gbc);
        
        // Seats and Fuel Economy
        gbc.gridx = 2;
        gbc.gridy = 11;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(8, 20, 8, 10);
        formContent.add(new JLabel("Seats:"), gbc);
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(8, 10, 8, 30);
        seatsSpinner = new JSpinner(new SpinnerNumberModel(5, 2, 8, 1));
        seatsSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        seatsSpinner.setPreferredSize(new Dimension(180, 32));
        formContent.add(seatsSpinner, gbc);
        
        gbc.gridx = 4;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(8, 20, 8, 10);
        formContent.add(new JLabel("Fuel Economy (MPG):"), gbc);
        gbc.gridx = 5;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(8, 10, 8, 20);
        fuelEconomyField = createStyledTextField();
        fuelEconomyField.setText("28.5");
        formContent.add(fuelEconomyField, gbc);
        
        // Features
        gbc.gridx = 2;
        gbc.gridy = 12;
        gbc.gridwidth = 4;
        gbc.insets = new Insets(20, 20, 10, 20);
        formContent.add(new JLabel("Features:"), gbc);
        
        gbc.gridy = 13;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.3;
        gbc.insets = new Insets(5, 20, 10, 20);
        
        featuresArea = new JTextArea(3, 50);
        featuresArea.setFont(new Font("Arial", Font.PLAIN, 14));
        featuresArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        featuresArea.setLineWrap(true);
        featuresArea.setWrapStyleWord(true);
        featuresArea.setText("Air Conditioning, Power Windows, Bluetooth, Backup Camera");
        JScrollPane featuresScrollPane = new JScrollPane(featuresArea);
        featuresScrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        featuresScrollPane.setPreferredSize(new Dimension(500, 80));
        formContent.add(featuresScrollPane, gbc);
        
        // Notes
        gbc.gridy = 14;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weighty = 0;
        gbc.insets = new Insets(20, 20, 10, 20);
        formContent.add(new JLabel("Notes:"), gbc);
        
        gbc.gridy = 15;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 0.3;
        gbc.insets = new Insets(5, 20, 20, 20);
        
        notesArea = new JTextArea(3, 50);
        notesArea.setFont(new Font("Arial", Font.PLAIN, 14));
        notesArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        JScrollPane notesScrollPane = new JScrollPane(notesArea);
        notesScrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        notesScrollPane.setPreferredSize(new Dimension(500, 80));
        formContent.add(notesScrollPane, gbc);
        
        // Buttons
        gbc.gridy = 16;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.insets = new Insets(30, 20, 0, 20);
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonsPanel = createFormButtonsPanel();
        formContent.add(buttonsPanel, gbc);
        
        mainContainer.add(formContent, BorderLayout.CENTER);
        
        JScrollPane scrollPane = new JScrollPane(mainContainer);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        formCard.add(scrollPane, BorderLayout.CENTER);
        
        return formCard;
    }
    
    /**
     * Create the photo upload panel
     * @return JPanel for uploading car photos
     */
    private JPanel createPhotoUploadPanel() {
        JPanel photoPanel = new JPanel();
        photoPanel.setLayout(new BoxLayout(photoPanel, BoxLayout.Y_AXIS));
        photoPanel.setOpaque(false);
        photoPanel.setPreferredSize(new Dimension(250, 350));
        photoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        
        JLabel photoTitle = new JLabel("Vehicle Photo");
        photoTitle.setFont(new Font("Arial", Font.BOLD, 16));
        photoTitle.setForeground(new Color(50, 50, 50));
        photoTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        RoundedPanel imageContainer = new RoundedPanel(10, new Color(248, 250, 252));
        imageContainer.setLayout(new BorderLayout());
        imageContainer.setPreferredSize(new Dimension(230, 180));
        imageContainer.setMaximumSize(new Dimension(230, 180));
        imageContainer.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2, true));
        imageContainer.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setText("<html><div style='text-align: center;'>"
                + "<div style='font-size: 48px; color: #888;'>🚗</div>"
                + "<div style='margin-top: 10px; color: #666;'>Click to upload image</div>"
                + "</html>");
        imageLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        imageContainer.add(imageLabel, BorderLayout.CENTER);
        
        JButton uploadButton = new JButton("Choose Photo");
        uploadButton.setBackground(PRIMARY_BLUE);
        uploadButton.setForeground(Color.WHITE);
        uploadButton.setFocusPainted(false);
        uploadButton.setFont(new Font("Arial", Font.BOLD, 12));
        uploadButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        uploadButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        uploadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        uploadButton.setMaximumSize(new Dimension(150, 35));
         uploadButton.setOpaque(true);  // Add this
        uploadButton.setBorderPainted(false);  // Add this
        
        
        uploadButton.addActionListener(e -> choosePhoto());
        
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
        
        JButton removeButton = new JButton("Remove Photo");
        removeButton.setBackground(PRIMARY_RED);
        removeButton.setForeground(Color.WHITE);
        removeButton.setFocusPainted(false);
        removeButton.setFont(new Font("Arial", Font.PLAIN, 11));
        removeButton.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        removeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        removeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeButton.setMaximumSize(new Dimension(120, 25));
        removeButton.setVisible(false);
          removeButton.setOpaque(true);  // Add this
        removeButton.setBorderPainted(false);  // Add this
        
        
        removeButton.addActionListener(e -> removePhoto(removeButton));
        
        photoPanel.add(photoTitle);
        photoPanel.add(Box.createVerticalStrut(10));
        photoPanel.add(imageContainer);
        photoPanel.add(Box.createVerticalStrut(10));
        photoPanel.add(uploadButton);
        photoPanel.add(Box.createVerticalStrut(8));
        photoPanel.add(removeButton);
        photoPanel.add(Box.createVerticalGlue());
        
        return photoPanel;
    }
    
    /**
     * Choose a photo using file chooser
     */
    private void choosePhoto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Car Photo");
        
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Image files", "jpg", "jpeg", "png", "gif", "bmp");
        fileChooser.setFileFilter(filter);
        
        int result = fileChooser.showOpenDialog(this);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            selectedImagePath = selectedFile.getAbsolutePath();
            
            try {
                ImageIcon originalIcon = new ImageIcon(selectedImagePath);
                Image scaledImage = originalIcon.getImage().getScaledInstance(210, 160, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
                imageLabel.setText("");
                
                Component removeButton = ((JPanel) imageLabel.getParent().getParent()).getComponent(4);
                removeButton.setVisible(true);
                
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
     * Remove the selected photo
     */
    private void removePhoto(JButton removeButton) {
        selectedImagePath = "";
        imageLabel.setIcon(null);
        imageLabel.setText("<html><div style='text-align: center;'>"
                + "<div style='font-size: 48px; color: #888;'>🚗</div>"
                + "<div style='margin-top: 10px; color: #666;'>Click to upload image</div>"
                + "</html>");
        removeButton.setVisible(false);
        
        imageLabel.getParent().revalidate();
        imageLabel.getParent().repaint();
    }
    
    /**
     * Create the form buttons panel
     */
    private JPanel createFormButtonsPanel() {
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonsPanel.setOpaque(false);
        
        JButton clearButton = new JButton("Clear Form");
        clearButton.setBackground(new Color(108, 117, 125));
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        clearButton.setFont(new Font("Arial", Font.BOLD, 14));
        clearButton.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        clearButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        clearButton.addActionListener(e -> clearForm());
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(PRIMARY_RED);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
         cancelButton.setOpaque(true);  // Add this
        cancelButton.setBorderPainted(false);  // Add this
        
        
        cancelButton.addActionListener(e -> goBackToInventory());
        
        JButton saveButton = new JButton("Add Vehicle to Inventory");
        saveButton.setBackground(PRIMARY_GREEN);
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveButton.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
         saveButton.setOpaque(true);  // Add this
        saveButton.setBorderPainted(false);  // Add this
        
        
        saveButton.addActionListener(e -> saveNewCar());
        
        buttonsPanel.add(clearButton);
        buttonsPanel.add(cancelButton);
        buttonsPanel.add(saveButton);
        
        return buttonsPanel;
    }
    
    /**
     * Clear the form fields
     */
    private void clearForm() {
        int result = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to clear all form data?", 
            "Clear Form", 
            JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            vinField.setText("");
            manufacturerField.setText("");
            modelField.setText("");
            colorField.setText("");
            locationField.setText("Main Lot");
            engineTypeField.setText("2.5L 4-Cylinder");
            fuelEconomyField.setText("28.5");
            
            yearSpinner.setValue(2024);
            priceSpinner.setValue(25000.0);
            costSpinner.setValue(20000.0);
            mileageSpinner.setValue(0);
            horsepowerSpinner.setValue(200);
            seatsSpinner.setValue(5);
            
            categoryComboBox.setSelectedIndex(0);
            statusComboBox.setSelectedIndex(0);
            transmissionComboBox.setSelectedIndex(0);
            fuelTypeComboBox.setSelectedIndex(0);
            
            featuresArea.setText("Air Conditioning, Power Windows, Bluetooth, Backup Camera");
            notesArea.setText("");
            
            selectedImagePath = "";
            imageLabel.setIcon(null);
            imageLabel.setText("<html><div style='text-align: center;'>"
                    + "<div style='font-size: 48px; color: #888;'>🚗</div>"
                    + "<div style='margin-top: 10px; color: #666;'>Click to upload image</div>"
                    + "</html>");
            
            try {
                Component removeButton = ((JPanel) imageLabel.getParent().getParent()).getComponent(4);
                removeButton.setVisible(false);
            } catch (Exception e) {
                // Ignore if button not found
            }
            
            initializeCurrentDate();
            dateAddedLabel.setText("Date Added: " + currentDate);
        }
    }
    
    /**
     * Validate form fields before saving
     */
    private boolean validateForm() {
        if (vinField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "VIN is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            vinField.requestFocus();
            return false;
        }
        
        if (manufacturerField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Manufacturer is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            manufacturerField.requestFocus();
            return false;
        }
        
        if (modelField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Model is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            modelField.requestFocus();
            return false;
        }
        
        if (colorField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Color is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            colorField.requestFocus();
            return false;
        }
        
        String vin = vinField.getText().trim();
        if (vin.length() != 17) {
            JOptionPane.showMessageDialog(this, "VIN must be exactly 17 characters.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            vinField.requestFocus();
            return false;
        }
        
        Integer year = (Integer) yearSpinner.getValue();
        if (year < 1990 || year > 2030) {
            JOptionPane.showMessageDialog(this, "Year must be between 1990 and 2030.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            yearSpinner.requestFocus();
            return false;
        }
        
        Double salePrice = (Double) priceSpinner.getValue();
        Double costPrice = (Double) costSpinner.getValue();
        
        if (salePrice <= 0) {
            JOptionPane.showMessageDialog(this, "Sale price must be greater than 0.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            priceSpinner.requestFocus();
            return false;
        }
        
        if (costPrice <= 0) {
            JOptionPane.showMessageDialog(this, "Cost price must be greater than 0.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            costSpinner.requestFocus();
            return false;
        }
        
        if (costPrice >= salePrice) {
            int result = JOptionPane.showConfirmDialog(this, 
                "Cost price is greater than or equal to sale price. This will result in no profit or a loss. Continue?", 
                "Price Warning", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.WARNING_MESSAGE);
            if (result != JOptionPane.YES_OPTION) {
                return false;
            }
        }
        
        try {
            Double fuelEcon = Double.parseDouble(fuelEconomyField.getText().trim());
            if (fuelEcon <= 0 || fuelEcon > 100) {
                JOptionPane.showMessageDialog(this, "Fuel economy must be between 0 and 100 MPG.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                fuelEconomyField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Fuel economy must be a valid number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            fuelEconomyField.requestFocus();
            return false;
        }
        
        return true;
    }
    
    /**
     * Save a new car to the database
     */
    private void saveNewCar() {
        if (!validateForm()) {
            return;
        }
        
        try {
            // Get all form data
            String vin = vinField.getText().trim();
            String manufacturer = manufacturerField.getText().trim();
            String model = modelField.getText().trim();
            Integer year = (Integer) yearSpinner.getValue();
            String category = (String) categoryComboBox.getSelectedItem();
            String color = colorField.getText().trim();
            Double salePrice = (Double) priceSpinner.getValue();
            Double costPrice = (Double) costSpinner.getValue();
            Integer mileage = (Integer) mileageSpinner.getValue();
            String location = locationField.getText().trim();
            String status = (String) statusComboBox.getSelectedItem();
            
            String engineType = engineTypeField.getText().trim();
            String transmission = (String) transmissionComboBox.getSelectedItem();
            String fuelType = (String) fuelTypeComboBox.getSelectedItem();
            Integer horsepower = (Integer) horsepowerSpinner.getValue();
            Integer seats = (Integer) seatsSpinner.getValue();
            String fuelEconomy = fuelEconomyField.getText().trim();
            String features = featuresArea.getText().trim();
            String notes = notesArea.getText().trim();
            
            // === SAVE TO DATABASE ===
            CarManagementIntegration integration = CarManagementIntegration.getInstance();
            integration.setCurrentStaffId(adminId); // Set current user
            
            boolean success = integration.saveCarFromAddCarUI(
                vin, manufacturer, model, year, category, color,
                salePrice, costPrice, mileage, location, engineType,
                transmission, fuelType, horsepower, seats,
                fuelEconomy, features, notes, selectedImagePath
            );
            
            if (success) {
                // === SUCCESS - SAVED TO DATABASE ===
                String successMessage = "Vehicle Added Successfully to Database!\n\n" +
                        "VIN: " + vin + "\n" +
                        "Vehicle: " + manufacturer + " " + model + " " + year + "\n" +
                        "Color: " + color + "\n" +
                        "Category: " + category + "\n" +
                        "Sale Price: $" + String.format("%.2f", salePrice) + "\n" +
                        "Cost Price: $" + String.format("%.2f", costPrice) + "\n" +
                        "Status: " + status + "\n" +
                        "Date Added: " + currentDate + "\n" +
                        (selectedImagePath.isEmpty() ? "" : "Image: " + new File(selectedImagePath).getName()) +
                        "\n\n✅ Car has been saved to database and will appear in inventory.";
                
                JOptionPane.showMessageDialog(this, 
                    successMessage, 
                    "Success - Database Updated", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                int addAnother = JOptionPane.showConfirmDialog(this, 
                    "Vehicle added successfully to database! Would you like to add another vehicle?", 
                    "Add Another Vehicle", 
                    JOptionPane.YES_NO_OPTION);
                
                if (addAnother == JOptionPane.YES_OPTION) {
                    clearForm();
                } else {
                    goBackToInventory(); // Will now show the new car from database
                }
            } else {
                // === FAILURE - NOT SAVED ===
                JOptionPane.showMessageDialog(this, 
                    "❌ Failed to save vehicle to database.\n" +
                    "Please check your database connection and try again.", 
                    "Database Save Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error saving vehicle to database: " + e.getMessage() + 
                "\n\nPlease check:\n" +
                "- Database connection\n" +
                "- VIN format (17 characters)\n" +
                "- All required fields", 
                "Save Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /**
     * Navigate back to the inventory screen
     */
    private void goBackToInventory() {
        if (hasUnsavedChanges()) {
            int result = JOptionPane.showConfirmDialog(this, 
                "You have unsaved changes. Are you sure you want to leave?", 
                "Unsaved Changes", 
                JOptionPane.YES_NO_OPTION);
            if (result != JOptionPane.YES_OPTION) {
                return;
            }
        }
        
        dispose();
        CarManagement carManagement = new CarManagement(adminId);
        carManagement.setVisible(true);
    }
    
    /**
     * Check if the form has unsaved changes
     */
    private boolean hasUnsavedChanges() {
        return !vinField.getText().trim().isEmpty() || 
               !manufacturerField.getText().trim().isEmpty() || 
               !modelField.getText().trim().isEmpty() || 
               !colorField.getText().trim().isEmpty() ||
               !selectedImagePath.isEmpty();
    }
    
    /**
     * Main method to test the Add Car UI
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new AddCarUI().setVisible(true);
        });
    }
}