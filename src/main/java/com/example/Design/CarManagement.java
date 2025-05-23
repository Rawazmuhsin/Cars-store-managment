package com.example.Design;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

/**
 * Car Management UI class that extends BaseUI to inherit common UI elements
 * Displays cars as cards with enhanced filtering capabilities
 */
public class CarManagement extends BaseUI {
    
    // Filter panels with active state indicators
    private RoundedPanel priceRangeCard;
    private RoundedPanel carTypeCard;
    private RoundedPanel dateCard;
    
    // Labels for active filter indicators
    private JLabel priceRangeFilterLabel;
    private JLabel carTypeFilterLabel;
    private JLabel dateFilterLabel;
    
    // Store active filters
    private Map<String, Object> activeFilters = new HashMap<>();
    
    // Car data structure
    private List<Car> cars;
    
    // Summary statistics
    private int totalCarsCount = 0;
    private int availableCarsCount = 0;
    private int soldCarsCount = 0;
    private int reservedCarsCount = 0;

    /**
     * Constructor with admin ID
     * @param adminId The ID of the admin user
     */
    public CarManagement(int adminId) {
        super(adminId);
    }
    
    /**
     * Default constructor
     */
    public CarManagement() {
        super();
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
     * Create the content panel for car management
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
        
        JLabel headerLabel = new JLabel("Car Inventory Management");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 26));
        headerLabel.setForeground(new Color(50, 50, 50));
        
        // Sub header info
        JLabel subHeaderLabel = new JLabel("View and manage your car inventory");
        subHeaderLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subHeaderLabel.setForeground(new Color(120, 120, 120));
        
        headerPanel.add(headerLabel, BorderLayout.NORTH);
        headerPanel.add(subHeaderLabel, BorderLayout.SOUTH);
        
        // Create buttons panel for top right
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setOpaque(false);
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        
        // Clear filters button
        JButton clearFiltersButton = new JButton("Clear Filters");
        clearFiltersButton.setBackground(PRIMARY_RED);
        clearFiltersButton.setForeground(Color.WHITE);
        clearFiltersButton.setFocusPainted(false);
        clearFiltersButton.setFont(new Font("Arial", Font.BOLD, 14));
        clearFiltersButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        clearFiltersButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearFiltersButton.setOpaque(true);
        clearFiltersButton.setBorderPainted(false);
        
        // Add action listener to clear filters
        clearFiltersButton.addActionListener(e -> clearAllFilters());
        
        // Add hover effects
        clearFiltersButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                clearFiltersButton.setBackground(PRIMARY_RED.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                clearFiltersButton.setBackground(PRIMARY_RED);
            }
        });
        
        // Add Car button
        JButton addCarButton = new JButton("Add New Car");
        addCarButton.setBackground(PRIMARY_BLUE);
        addCarButton.setForeground(Color.WHITE);
        addCarButton.setFocusPainted(false);
        addCarButton.setFont(new Font("Arial", Font.BOLD, 14));
        addCarButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        addCarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addCarButton.setOpaque(true);
        addCarButton.setBorderPainted(false);
        
        // Add action listener to add car button
        addCarButton.addActionListener(e -> navigateToScreen("Add New Car"));
        
        // Add hover effects
        addCarButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                addCarButton.setBackground(PRIMARY_BLUE.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                addCarButton.setBackground(PRIMARY_BLUE);
            }
        });
        
        // Export button
        JButton exportButton = new JButton("Export PDF");
        exportButton.setBackground(PRIMARY_YELLOW);
        exportButton.setForeground(Color.WHITE);
        exportButton.setFocusPainted(false);
        exportButton.setFont(new Font("Arial", Font.BOLD, 14));
        exportButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        exportButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exportButton.setOpaque(true);
        exportButton.setBorderPainted(false);
        
        // Add hover effects
        exportButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                exportButton.setBackground(PRIMARY_YELLOW.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                exportButton.setBackground(PRIMARY_YELLOW);
            }
        });
        
        buttonsPanel.add(clearFiltersButton);
        buttonsPanel.add(addCarButton);
        buttonsPanel.add(exportButton);
        
        // Add header and buttons to top panel
        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new BorderLayout());
        topPanel.add(headerPanel, BorderLayout.WEST);
        topPanel.add(buttonsPanel, BorderLayout.EAST);
        
        // Dashboard content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        
        // Initialize sample data
        initializeSampleData();
        
        // Calculate statistics
        calculateCarStatistics();
        
        // Car statistics overview
        JPanel statsPanel = new JPanel();
        statsPanel.setOpaque(false);
        statsPanel.setLayout(new GridLayout(1, 4, 20, 0));
        statsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsPanel.setMaximumSize(new Dimension(1200, 120));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
        
        // Create overview cards
        JPanel totalCarsCard = createOverviewCard("Total Cars", Integer.toString(totalCarsCount), PRIMARY_BLUE);
        JPanel availableCard = createOverviewCard("Available", Integer.toString(availableCarsCount), PRIMARY_GREEN);
        JPanel soldCard = createOverviewCard("Sold", Integer.toString(soldCarsCount), PRIMARY_RED);
        JPanel reservedCard = createOverviewCard("Reserved", Integer.toString(reservedCarsCount), PRIMARY_YELLOW);
        
        statsPanel.add(totalCarsCard);
        statsPanel.add(availableCard);
        statsPanel.add(soldCard);
        statsPanel.add(reservedCard);
        
        // Filter section title
        JLabel filterLabel = new JLabel("CAR FILTERS");
        filterLabel.setFont(new Font("Arial", Font.BOLD, 16));
        filterLabel.setForeground(new Color(50, 50, 50));
        filterLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 15, 0));
        filterLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Create filter cards panel
        JPanel filterCardsPanel = new JPanel();
        filterCardsPanel.setOpaque(false);
        filterCardsPanel.setLayout(new GridLayout(1, 3, 20, 0));
        filterCardsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterCardsPanel.setMaximumSize(new Dimension(1000, 120));
        
        // Initialize filter indicator labels
        priceRangeFilterLabel = new JLabel("");
        priceRangeFilterLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        priceRangeFilterLabel.setForeground(PRIMARY_BLUE);
        
        carTypeFilterLabel = new JLabel("");
        carTypeFilterLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        carTypeFilterLabel.setForeground(PRIMARY_BLUE);
        
        dateFilterLabel = new JLabel("");
        dateFilterLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        dateFilterLabel.setForeground(PRIMARY_BLUE);
        
        // Create filter cards
        priceRangeCard = createFilterCard("Price Range", "Filter by minimum and maximum price", priceRangeFilterLabel);
        carTypeCard = createFilterCard("Car Type", "Filter by sedan, SUV, truck, etc.", carTypeFilterLabel);
        dateCard = createFilterCard("Date Added", "Filter by date when car was added", dateFilterLabel);
        
        filterCardsPanel.add(priceRangeCard);
        filterCardsPanel.add(carTypeCard);
        filterCardsPanel.add(dateCard);
        
        // Car list section title
        JLabel carsLabel = new JLabel("CAR INVENTORY");
        carsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        carsLabel.setForeground(new Color(50, 50, 50));
        carsLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 15, 0));
        carsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Create cars grid panel
        JPanel carsGridPanel = createCarsGrid();
        
        // Create footer panel
        footerPanel = createFooterPanel();
        
        // Add components to content panel
        contentPanel.add(statsPanel);
        contentPanel.add(filterLabel);
        contentPanel.add(filterCardsPanel);
        contentPanel.add(carsLabel);
        contentPanel.add(carsGridPanel);
        
        // Add all panels to main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    /**
     * Initialize sample data for cars
     */
    private void initializeSampleData() {
        // Get cars from the shared CarStatusManager
        CarStatusManager carManager = CarStatusManager.getInstance();
        cars = carManager.getAvailableCars(); // Only get available and reserved cars
    }
    
    /**
     * Create cars grid panel with car cards
     */
    private JPanel createCarsGrid() {
        JPanel gridContainer = new JPanel();
        gridContainer.setOpaque(false);
        gridContainer.setLayout(new BoxLayout(gridContainer, BoxLayout.Y_AXIS));
        gridContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Create grid layout (4 cars per row)
        JPanel gridPanel = new JPanel(new GridLayout(0, 4, 20, 20));
        gridPanel.setOpaque(false);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Add car cards to grid
        for (Car car : cars) {
            JPanel carCard = createCarCard(car);
            gridPanel.add(carCard);
        }
        
        gridContainer.add(gridPanel);
        return gridContainer;
    }
    
    /**
     * Create a car card with image, name, and details
     */
    private JPanel createCarCard(Car car) {
        RoundedPanel card = new RoundedPanel(15, Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        card.setPreferredSize(new Dimension(280, 380));
        card.setMaximumSize(new Dimension(280, 380));
        
        // Car image panel
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new BorderLayout());
        imagePanel.setBackground(new Color(248, 250, 252));
        imagePanel.setPreferredSize(new Dimension(250, 160));
        imagePanel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));
        
        // Load car image (placeholder for now)
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        
        try {
            // Try to load the actual image
            ImageIcon originalIcon = new ImageIcon(car.getImagePath());
            if (originalIcon.getIconWidth() > 0) {
                Image scaledImage = originalIcon.getImage().getScaledInstance(240, 140, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
            } else {
                throw new Exception("Image not found");
            }
        } catch (Exception e) {
            // Use placeholder if image not found
            imageLabel.setText("🚗");
            imageLabel.setFont(new Font("Arial", Font.PLAIN, 50));
            imageLabel.setForeground(new Color(150, 150, 150));
        }
        
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        
        // Car info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        // Car name
        JLabel nameLabel = new JLabel(car.getModel());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setForeground(new Color(50, 50, 50));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Car year and type
        JLabel yearTypeLabel = new JLabel(car.getYear() + " • " + car.getType());
        yearTypeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        yearTypeLabel.setForeground(new Color(120, 120, 120));
        yearTypeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Car color
        JLabel colorLabel = new JLabel("Color: " + car.getColor());
        colorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        colorLabel.setForeground(new Color(100, 100, 100));
        colorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Price
        JLabel priceLabel = new JLabel(car.getPrice());
        priceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        priceLabel.setForeground(PRIMARY_GREEN);
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Status badge
        JLabel statusLabel = new JLabel(car.getStatus());
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setOpaque(true);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Set status color
        switch (car.getStatus()) {
            case "Available":
                statusLabel.setBackground(new Color(232, 255, 232));
                statusLabel.setForeground(new Color(25, 135, 84));
                break;
            case "Sold":
                statusLabel.setBackground(new Color(255, 236, 236));
                statusLabel.setForeground(new Color(220, 53, 69));
                break;
            case "Reserved":
                statusLabel.setBackground(new Color(255, 250, 230));
                statusLabel.setForeground(new Color(255, 153, 0));
                break;
            default:
                statusLabel.setBackground(new Color(240, 240, 240));
                statusLabel.setForeground(new Color(70, 70, 70));
        }
        
        // Action buttons panel
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        actionsPanel.setOpaque(false);
        
        JButton showButton = new JButton("Show");
        showButton.setBackground(PRIMARY_BLUE);
        showButton.setForeground(Color.WHITE);
        showButton.setFont(new Font("Arial", Font.BOLD, 11));
        showButton.setFocusPainted(false);
        showButton.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        showButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JButton editButton = new JButton("Edit");
        editButton.setBackground(PRIMARY_YELLOW);
        editButton.setForeground(Color.WHITE);
        editButton.setFont(new Font("Arial", Font.BOLD, 11));
        editButton.setFocusPainted(false);
        editButton.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        editButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add action listeners
        showButton.addActionListener(e -> showCarDetails(car));
        editButton.addActionListener(e -> editCarDetails(car));
        
        actionsPanel.add(showButton);
        actionsPanel.add(editButton);
        
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(yearTypeLabel);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(colorLabel);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(priceLabel);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(statusLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(actionsPanel);
        
        card.add(imagePanel, BorderLayout.NORTH);
        card.add(infoPanel, BorderLayout.CENTER);
        
        // Add shadow effect
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(0, 0, 5, 5),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        return card;
    }
    
    /**
     * Creates a filter card with title, description, and filter indicator
     */
    private RoundedPanel createFilterCard(String title, String description, JLabel filterIndicator) {
        RoundedPanel card = new RoundedPanel(15, Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Top colored bar
        JPanel colorBar = new JPanel();
        colorBar.setBackground(PRIMARY_BLUE);
        colorBar.setPreferredSize(new Dimension(50, 5));
        
        // Title label
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(50, 50, 50));
        
        // Description label
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descLabel.setForeground(new Color(120, 120, 120));
        
        // Create a panel for the labels
        JPanel labelPanel = new JPanel();
        labelPanel.setOpaque(false);
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        labelPanel.add(titleLabel);
        labelPanel.add(Box.createVerticalStrut(5));
        labelPanel.add(descLabel);
        labelPanel.add(Box.createVerticalStrut(3));
        labelPanel.add(filterIndicator);
        
        // Add components to card
        card.add(colorBar, BorderLayout.NORTH);
        card.add(labelPanel, BorderLayout.CENTER);
        
        // Add click handler
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleFilterCardClick(title);
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(250, 252, 255));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
            }
        });
        
        // Add shadow effect
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(0, 0, 5, 5),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        return card;
    }
    
    /**
     * Handles filter card click events
     */
    private void handleFilterCardClick(String filterType) {
        switch (filterType) {
            case "Price Range":
                showPriceRangeFilterDialog();
                break;
            case "Car Type":
                showCarTypeFilterDialog();
                break;
            case "Date Added":
                showDateFilterDialog();
                break;
            default:
                // Do nothing
        }
    }
    
    /**
     * Shows price range filter dialog
     */
    private void showPriceRangeFilterDialog() {
        JDialog dialog = new JDialog(this, "Select Price Range", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Min price
        JPanel minPricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        minPricePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel minPriceLabel = new JLabel("Minimum Price:");
        minPriceLabel.setPreferredSize(new Dimension(100, 25));
        
        SpinnerNumberModel minModel = new SpinnerNumberModel(0, 0, 500000, 1000);
        JSpinner minPriceSpinner = new JSpinner(minModel);
        minPriceSpinner.setPreferredSize(new Dimension(150, 25));
        
        minPricePanel.add(minPriceLabel);
        minPricePanel.add(minPriceSpinner);
        
        // Max price
        JPanel maxPricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        maxPricePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel maxPriceLabel = new JLabel("Maximum Price:");
        maxPriceLabel.setPreferredSize(new Dimension(100, 25));
        
        SpinnerNumberModel maxModel = new SpinnerNumberModel(100000, 0, 1000000, 1000);
        JSpinner maxPriceSpinner = new JSpinner(maxModel);
        maxPriceSpinner.setPreferredSize(new Dimension(150, 25));
        
        maxPricePanel.add(maxPriceLabel);
        maxPricePanel.add(maxPriceSpinner);
        
        // Quick price ranges
        JPanel quickRangesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        quickRangesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel quickRangeLabel = new JLabel("Quick Select:");
        quickRangeLabel.setPreferredSize(new Dimension(100, 25));
        
        JButton budgetButton = new JButton("Budget (<$30k)");
        JButton midRangeButton = new JButton("Mid-Range ($30k-$50k)");
        JButton luxuryButton = new JButton("Luxury (>$50k)");
        
        Font quickButtonFont = new Font("Arial", Font.PLAIN, 12);
        budgetButton.setFont(quickButtonFont);
        midRangeButton.setFont(quickButtonFont);
        luxuryButton.setFont(quickButtonFont);
        
        budgetButton.addActionListener(e -> {
            minPriceSpinner.setValue(0);
            maxPriceSpinner.setValue(30000);
        });
        
        midRangeButton.addActionListener(e -> {
            minPriceSpinner.setValue(30000);
            maxPriceSpinner.setValue(50000);
        });
        
        luxuryButton.addActionListener(e -> {
            minPriceSpinner.setValue(50000);
            maxPriceSpinner.setValue(1000000);
        });
        
        JPanel buttonsContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        buttonsContainer.add(budgetButton);
        buttonsContainer.add(midRangeButton);
        buttonsContainer.add(luxuryButton);
        
        quickRangesPanel.add(quickRangeLabel);
        quickRangesPanel.add(buttonsContainer);
        
        // Add panels to main panel
        mainPanel.add(minPricePanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(maxPricePanel);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(quickRangesPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dialog.dispose());
        
        JButton applyButton = new JButton("Apply Filter");
        applyButton.setBackground(PRIMARY_BLUE);
        applyButton.setForeground(Color.WHITE);
        applyButton.addActionListener(e -> {
            int minPrice = (int) minPriceSpinner.getValue();
            int maxPrice = (int) maxPriceSpinner.getValue();
            
            if (maxPrice >= minPrice) {
                DecimalFormat formatter = new DecimalFormat("$#,###");
                priceRangeFilterLabel.setText("Filter: " + formatter.format(minPrice) + " - " + formatter.format(maxPrice));
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, 
                    "Maximum price must be greater than or equal to minimum price", 
                    "Invalid Price Range", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(applyButton);
        
        mainPanel.add(buttonPanel);
        
        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
    
    /**
     * Shows car type filter dialog
     */
    private void showCarTypeFilterDialog() {
        JDialog dialog = new JDialog(this, "Select Car Types", true);
        dialog.setSize(350, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Select Car Types:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Create checkboxes for car types
        JCheckBox sedanCheckBox = new JCheckBox("Sedan");
        sedanCheckBox.setFont(new Font("Arial", Font.PLAIN, 14));
        sedanCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        sedanCheckBox.setSelected(true);
        
        JCheckBox suvCheckBox = new JCheckBox("SUV");
        suvCheckBox.setFont(new Font("Arial", Font.PLAIN, 14));
        suvCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        suvCheckBox.setSelected(true);
        
        JCheckBox truckCheckBox = new JCheckBox("Truck");
        truckCheckBox.setFont(new Font("Arial", Font.PLAIN, 14));
        truckCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        truckCheckBox.setSelected(true);
        
        JCheckBox electricCheckBox = new JCheckBox("Electric");
        electricCheckBox.setFont(new Font("Arial", Font.PLAIN, 14));
        electricCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        electricCheckBox.setSelected(true);
        
        JCheckBox luxuryCheckBox = new JCheckBox("Luxury");
        luxuryCheckBox.setFont(new Font("Arial", Font.PLAIN, 14));
        luxuryCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        luxuryCheckBox.setSelected(true);
        
        JCheckBox sportsCheckBox = new JCheckBox("Sports");
        sportsCheckBox.setFont(new Font("Arial", Font.PLAIN, 14));
        sportsCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        sportsCheckBox.setSelected(true);
        
        // Add components to main panel
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(sedanCheckBox);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(suvCheckBox);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(truckCheckBox);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(electricCheckBox);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(luxuryCheckBox);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(sportsCheckBox);
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dialog.dispose());
        
        JButton applyButton = new JButton("Apply Filter");
        applyButton.setBackground(PRIMARY_BLUE);
        applyButton.setForeground(Color.WHITE);
        applyButton.addActionListener(e -> {
            List<String> selectedTypes = new ArrayList<>();
            
            if (sedanCheckBox.isSelected()) selectedTypes.add("Sedan");
            if (suvCheckBox.isSelected()) selectedTypes.add("SUV");
            if (truckCheckBox.isSelected()) selectedTypes.add("Truck");
            if (electricCheckBox.isSelected()) selectedTypes.add("Electric");
            if (luxuryCheckBox.isSelected()) selectedTypes.add("Luxury");
            if (sportsCheckBox.isSelected()) selectedTypes.add("Sports");
            
            if (selectedTypes.size() == 6 || selectedTypes.isEmpty()) {
                carTypeFilterLabel.setText("");
            } else {
                carTypeFilterLabel.setText("Filter: " + String.join(", ", selectedTypes));
            }
            
            dialog.dispose();
        });
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(applyButton);
        
        mainPanel.add(buttonPanel);
        
        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
    
    /**
     * Shows date filter dialog
     */
    private void showDateFilterDialog() {
        JDialog dialog = new JDialog(this, "Filter by Date Added", true);
        dialog.setSize(400, 280);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Select Date Range:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // From date
        JPanel fromDatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fromDatePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel fromDateLabel = new JLabel("From Date:");
        fromDateLabel.setPreferredSize(new Dimension(100, 25));
        
        Date today = new Date();
        Date pastMonth = new Date(today.getTime() - (30L * 24 * 60 * 60 * 1000));
        SpinnerDateModel fromModel = new SpinnerDateModel(pastMonth, null, today, java.util.Calendar.DAY_OF_MONTH);
        JSpinner fromDateSpinner = new JSpinner(fromModel);
        fromDateSpinner.setEditor(new JSpinner.DateEditor(fromDateSpinner, "MMM dd, yyyy"));
        fromDateSpinner.setPreferredSize(new Dimension(150, 25));
        
        fromDatePanel.add(fromDateLabel);
        fromDatePanel.add(fromDateSpinner);
        
        // To date
        JPanel toDatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toDatePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel toDateLabel = new JLabel("To Date:");
        toDateLabel.setPreferredSize(new Dimension(100, 25));
        
        SpinnerDateModel toModel = new SpinnerDateModel(today, null, null, java.util.Calendar.DAY_OF_MONTH);
        JSpinner toDateSpinner = new JSpinner(toModel);
        toDateSpinner.setEditor(new JSpinner.DateEditor(toDateSpinner, "MMM dd, yyyy"));
        toDateSpinner.setPreferredSize(new Dimension(150, 25));
        
        toDatePanel.add(toDateLabel);
        toDatePanel.add(toDateSpinner);
        
        // Quick date ranges
        JPanel quickRangesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        quickRangesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel quickRangeLabel = new JLabel("Quick Select:");
        quickRangeLabel.setPreferredSize(new Dimension(100, 25));
        
        JButton lastWeekButton = new JButton("Last Week");
        JButton lastMonthButton = new JButton("Last Month");
        JButton last3MonthsButton = new JButton("Last 3 Months");
        
        Font quickButtonFont = new Font("Arial", Font.PLAIN, 12);
        lastWeekButton.setFont(quickButtonFont);
        lastMonthButton.setFont(quickButtonFont);
        last3MonthsButton.setFont(quickButtonFont);
        
        lastWeekButton.addActionListener(e -> {
            Date weekAgo = new Date(today.getTime() - (7L * 24 * 60 * 60 * 1000));
            fromDateSpinner.setValue(weekAgo);
            toDateSpinner.setValue(today);
        });
        
        lastMonthButton.addActionListener(e -> {
            Date monthAgo = new Date(today.getTime() - (30L * 24 * 60 * 60 * 1000));
            fromDateSpinner.setValue(monthAgo);
            toDateSpinner.setValue(today);
        });
        
        last3MonthsButton.addActionListener(e -> {
            Date threeMonthsAgo = new Date(today.getTime() - (90L * 24 * 60 * 60 * 1000));
            fromDateSpinner.setValue(threeMonthsAgo);
            toDateSpinner.setValue(today);
        });
        
        JPanel buttonsContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        buttonsContainer.add(lastWeekButton);
        buttonsContainer.add(lastMonthButton);
        buttonsContainer.add(last3MonthsButton);
        
        quickRangesPanel.add(quickRangeLabel);
        quickRangesPanel.add(buttonsContainer);
        
        // Add panels to main panel
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(fromDatePanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(toDatePanel);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(quickRangesPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dialog.dispose());
        
        JButton applyButton = new JButton("Apply Filter");
        applyButton.setBackground(PRIMARY_BLUE);
        applyButton.setForeground(Color.WHITE);
        applyButton.addActionListener(e -> {
            Date fromDate = (Date) fromDateSpinner.getValue();
            Date toDate = (Date) toDateSpinner.getValue();
            
            if (toDate.after(fromDate) || toDate.equals(fromDate)) {
                java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MMM dd, yyyy");
                dateFilterLabel.setText("Filter: " + formatter.format(fromDate) + " - " + formatter.format(toDate));
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, 
                    "To date must be after or equal to from date", 
                    "Invalid Date Range", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(applyButton);
        
        mainPanel.add(buttonPanel);
        
        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
    
    /**
     * Show car details in a separate window
     */
    private void showCarDetails(Car car) {
        dispose(); // Close current window
        SwingUtilities.invokeLater(() -> new ShowCarUI(car, adminId).setVisible(true));
    }
    
    /**
     * Edit car details in a separate window
     */
    private void editCarDetails(Car car) {
        dispose(); // Close current window
        SwingUtilities.invokeLater(() -> new EditCarUI(car, adminId).setVisible(true));
    }
    
    /**
     * Clear all active filters
     */
    private void clearAllFilters() {
        priceRangeFilterLabel.setText("");
        carTypeFilterLabel.setText("");
        dateFilterLabel.setText("");
        activeFilters.clear();
        
        // In a real application, this would refresh the car list
        JOptionPane.showMessageDialog(this, 
            "All filters have been cleared.", 
            "Filters Cleared", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Calculate car statistics
     */
    private void calculateCarStatistics() {
        if (cars == null) {
            totalCarsCount = 0;
            availableCarsCount = 0;
            soldCarsCount = 0;
            reservedCarsCount = 0;
            return;
        }
        
        totalCarsCount = cars.size();
        availableCarsCount = 0;
        soldCarsCount = 0;
        reservedCarsCount = 0;
        
        for (Car car : cars) {
            switch (car.getStatus()) {
                case "Available":
                    availableCarsCount++;
                    break;
                case "Sold":
                    soldCarsCount++;
                    break;
                case "Reserved":
                    reservedCarsCount++;
                    break;
            }
        }
    }
    
    /**
     * Inner class to represent a Car
     */
    public static class Car {
        private int id;
        private String model;
        private String year;
        private String type;
        private String color;
        private String price;
        private String status;
        private String dateAdded;
        private String imagePath;
        
        public Car(int id, String model, String year, String type, String color, String price, 
                   String status, String dateAdded, String imagePath) {
            this.id = id;
            this.model = model;
            this.year = year;
            this.type = type;
            this.color = color;
            this.price = price;
            this.status = status;
            this.dateAdded = dateAdded;
            this.imagePath = imagePath;
        }
        
        // Getters
        public int getId() { return id; }
        public String getModel() { return model; }
        public String getYear() { return year; }
        public String getType() { return type; }
        public String getColor() { return color; }
        public String getPrice() { return price; }
        public String getStatus() { return status; }
        public String getDateAdded() { return dateAdded; }
        public String getImagePath() { return imagePath; }
        
        // Setters
        public void setId(int id) { this.id = id; }
        public void setModel(String model) { this.model = model; }
        public void setYear(String year) { this.year = year; }
        public void setType(String type) { this.type = type; }
        public void setColor(String color) { this.color = color; }
        public void setPrice(String price) { this.price = price; }
        public void setStatus(String status) { this.status = status; }
        public void setDateAdded(String dateAdded) { this.dateAdded = dateAdded; }
        public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    }
    
    /**
     * Main method to test the Car Management UI
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set look and feel to system look and feel
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new CarManagement().setVisible(true);
        });
    }
}