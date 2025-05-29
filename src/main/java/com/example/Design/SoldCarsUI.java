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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Timestamp;
import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

/**
 * Sold Cars UI class that extends BaseUI to inherit common UI elements
 * Displays sold cars as cards and integrates with database through SoldCarsIntegration
 */
public class SoldCarsUI extends BaseUI {
    
    // Integration with backend
    private SoldCarsIntegration integration;
    
    // Filter panels with active state indicators
    private RoundedPanel datePeriodCard;
    private RoundedPanel priceRangeCard;
    private RoundedPanel carTypeCard;
    
    // Labels for active filter indicators
    private JLabel datePeriodFilterLabel;
    private JLabel priceRangeFilterLabel;
    private JLabel carTypeFilterLabel;
    
    // Store active filters
    private Map<String, Object> activeFilters = new HashMap<>();
    
    // Sold cars data structure
    private List<SoldCar> soldCars;
    
    // Summary statistics
    private SoldCarsIntegration.SalesStatistics salesStats;

    /**
     * Constructor with admin ID
     * @param adminId The ID of the admin user
     */
    public SoldCarsUI(int adminId) {
        super(adminId);
        this.integration = SoldCarsIntegration.getInstance();
        this.integration.setCurrentStaffId(adminId);
    }
    
    /**
     * Default constructor
     */
    public SoldCarsUI() {
        super();
        this.integration = SoldCarsIntegration.getInstance();
        this.integration.setCurrentStaffId(1); // Default staff ID
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
     * Create the content panel for sold cars management
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
        
        JLabel headerLabel = new JLabel("Sold Cars History");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 26));
        headerLabel.setForeground(new Color(50, 50, 50));
        
        // Sub header info
        JLabel subHeaderLabel = new JLabel("Track all car sales and transactions");
        subHeaderLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subHeaderLabel.setForeground(new Color(120, 120, 120));
        
        headerPanel.add(headerLabel, BorderLayout.NORTH);
        headerPanel.add(subHeaderLabel, BorderLayout.SOUTH);
        
        // Buttons panel for top right
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
        
        // Add action listener to clear filters button
        clearFiltersButton.addActionListener(e -> clearFilters());
        
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
        
        // Add action listener to export button
        exportButton.addActionListener(e -> exportToPdf());
        
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
        
        // Load data from database
        loadSoldCarsData();
        
        // Sales statistics overview
        JPanel statsPanel = new JPanel();
        statsPanel.setOpaque(false);
        statsPanel.setLayout(new GridLayout(1, 4, 20, 0));
        statsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsPanel.setMaximumSize(new Dimension(1200, 120));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
        
        // Create overview cards using data from database
        JPanel totalSoldCard = createOverviewCard("Total Cars Sold", 
            Integer.toString(salesStats.totalSoldCount), PRIMARY_BLUE);
        JPanel totalValueCard = createOverviewCard("Total Sales Value", 
            formatCurrency(salesStats.totalSalesValue), PRIMARY_GREEN);
        JPanel avgPriceCard = createOverviewCard("Average Sale Price", 
            formatCurrency(salesStats.averagePrice), PRIMARY_YELLOW);
        JPanel monthlySalesCard = createOverviewCard("Monthly Sales", 
            Integer.toString(salesStats.monthlySalesCount), new Color(156, 39, 176)); // Purple
        
        statsPanel.add(totalSoldCard);
        statsPanel.add(totalValueCard);
        statsPanel.add(avgPriceCard);
        statsPanel.add(monthlySalesCard);
        
        // Filter section title
        JLabel filterLabel = new JLabel("SALES FILTERS");
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
        datePeriodFilterLabel = new JLabel("");
        datePeriodFilterLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        datePeriodFilterLabel.setForeground(PRIMARY_BLUE);
        
        priceRangeFilterLabel = new JLabel("");
        priceRangeFilterLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        priceRangeFilterLabel.setForeground(PRIMARY_BLUE);
        
        carTypeFilterLabel = new JLabel("");
        carTypeFilterLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        carTypeFilterLabel.setForeground(PRIMARY_BLUE);
        
        // Create filter cards
        datePeriodCard = createFilterCard("Date Period", "Filter by sales date range", datePeriodFilterLabel);
        priceRangeCard = createFilterCard("Price Range", "Filter by sale price", priceRangeFilterLabel);
        carTypeCard = createFilterCard("Car Type", "Filter by vehicle category", carTypeFilterLabel);
        
        filterCardsPanel.add(datePeriodCard);
        filterCardsPanel.add(priceRangeCard);
        filterCardsPanel.add(carTypeCard);
        
        // Sold cars list section title
        JLabel carsLabel = new JLabel("SOLD CARS HISTORY");
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
     * Load sold cars data from database
     */
   /**
 * Load sold cars data from database
 */
private void loadSoldCarsData() {
    try {
        // Make sure integration is properly initialized
        integration = SoldCarsIntegration.getInstance();
        integration.setCurrentStaffId(adminId);
        
        // Get statistics from integration
        salesStats = integration.getSalesStatistics();
        
        // Get sold cars data from integration
        List<Object[]> soldCarsData = integration.getSoldCarsData();
        soldCars = new ArrayList<>();
        
        // Convert database rows to SoldCar objects
        for (int i = 0; i < soldCarsData.size(); i++) {
            Object[] row = soldCarsData.get(i);
            
            // Create SoldCar from database row data
            // row format: [model, year, type, date, price, buyer, "View"]
            SoldCar soldCar = new SoldCar(
                i + 1, // ID
                (String) row[0], // model
                (String) row[1], // year
                (String) row[2], // type
                "N/A", // color (not in database query)
                (String) row[4], // sale price
                (String) row[3], // sale date
                (String) row[5], // buyer name
                "N/A", // buyer contact (not in database query)
                "N/A", // payment method (not in database query)
                "placeholder_car.jpg" // image path
            );
            
            soldCars.add(soldCar);
        }
        
    } catch (Exception e) {
        System.err.println("Error loading sold cars data: " + e.getMessage());
        e.printStackTrace();
        
        // Initialize empty data if error occurs
        soldCars = new ArrayList<>();
        salesStats = new SoldCarsIntegration.SalesStatistics();
        
        // Show error message
        JOptionPane.showMessageDialog(this,
            "Error loading sold cars data from database.\n" + e.getMessage(),
            "Database Error",
            JOptionPane.ERROR_MESSAGE);
    }
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
        for (SoldCar car : soldCars) {
            JPanel carCard = createCarCard(car);
            gridPanel.add(carCard);
        }
        
        // If no sold cars, show message
        if (soldCars.isEmpty()) {
            JLabel noDataLabel = new JLabel("No sold cars found in database");
            noDataLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            noDataLabel.setForeground(new Color(120, 120, 120));
            noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
            gridPanel.add(noDataLabel);
        }
        
        gridContainer.add(gridPanel);
        return gridContainer;
    }
    
    /**
     * Create a car card with image, name, and details
     */
    private JPanel createCarCard(SoldCar car) {
        RoundedPanel card = new RoundedPanel(15, Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        card.setPreferredSize(new Dimension(280, 400));
        card.setMaximumSize(new Dimension(280, 400));
        
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
        
        // Sale price
        JLabel priceLabel = new JLabel(car.getSalePrice());
        priceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        priceLabel.setForeground(PRIMARY_GREEN);
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Sale date
        JLabel dateLabel = new JLabel("Sold: " + car.getSaleDate());
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        dateLabel.setForeground(new Color(100, 100, 100));
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Buyer name
        JLabel buyerLabel = new JLabel("Buyer: " + car.getBuyerName());
        buyerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        buyerLabel.setForeground(new Color(100, 100, 100));
        buyerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Status badge (always "Sold")
        JLabel statusLabel = new JLabel("SOLD");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setOpaque(true);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setBackground(new Color(255, 236, 236));
        statusLabel.setForeground(new Color(220, 53, 69));
        
        // Action buttons panel
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        actionsPanel.setOpaque(false);
        
        JButton showButton = new JButton("Details");
        showButton.setBackground(PRIMARY_BLUE);
        showButton.setForeground(Color.WHITE);
        showButton.setFont(new Font("Arial", Font.BOLD, 11));
        showButton.setFocusPainted(false);
        showButton.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        showButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JButton receiptButton = new JButton("Receipt");
        receiptButton.setBackground(PRIMARY_YELLOW);
        receiptButton.setForeground(Color.WHITE);
        receiptButton.setFont(new Font("Arial", Font.BOLD, 11));
        receiptButton.setFocusPainted(false);
        receiptButton.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        receiptButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add action listeners
        showButton.addActionListener(e -> showSaleDetails(car));
        receiptButton.addActionListener(e -> printReceipt(car));
        
        actionsPanel.add(showButton);
        actionsPanel.add(receiptButton);
        
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(yearTypeLabel);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(priceLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(dateLabel);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(buyerLabel);
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
     * Show sale details dialog
     */
    private void showSaleDetails(SoldCar car) {
        // Try to get the actual Sale object from database
        try {
            com.example.OOP.backend.Sale sale = integration.getSaleByIndex(car.getId() - 1);
            if (sale != null) {
                // Create detailed dialog with sale information
                JDialog detailDialog = new JDialog(this, "Sale Details", true);
                detailDialog.setSize(500, 600);
                detailDialog.setLocationRelativeTo(this);
                
                JPanel mainPanel = new JPanel();
                mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
                mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                
                // Add sale details
                mainPanel.add(new JLabel("Car: " + car.getModel() + " " + car.getYear()));
                mainPanel.add(new JLabel("Buyer: " + car.getBuyerName()));
                mainPanel.add(new JLabel("Sale Price: " + car.getSalePrice()));
                mainPanel.add(new JLabel("Sale Date: " + car.getSaleDate()));
                mainPanel.add(new JLabel("Payment Method: " + (sale.getPaymentMethod() != null ? sale.getPaymentMethod() : "N/A")));
                
                JButton closeButton = new JButton("Close");
                closeButton.addActionListener(e -> detailDialog.dispose());
                mainPanel.add(closeButton);
                
                detailDialog.add(mainPanel);
                detailDialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Sale details not found", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading sale details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Print receipt for the sold car
     */
    private void printReceipt(SoldCar car) {
        JOptionPane.showMessageDialog(this,
            "Printing receipt for:\n\n" +
            "Vehicle: " + car.getModel() + " " + car.getYear() + "\n" +
            "Buyer: " + car.getBuyerName() + "\n" +
            "Sale Price: " + car.getSalePrice() + "\n" +
            "Date: " + car.getSaleDate(),
            "Print Receipt",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Handles filter card click events
     */
    private void handleFilterCardClick(String filterType) {
        switch (filterType) {
            case "Date Period":
                showDatePeriodFilterDialog();
                break;
            case "Price Range":
                showPriceRangeFilterDialog();
                break;
            case "Car Type":
                showCarTypeFilterDialog();
                break;
            default:
                // Do nothing
        }
    }
    
    /**
     * Shows date period filter dialog
     */
    private void showDatePeriodFilterDialog() {
        JDialog dateDialog = new JDialog(this, "Select Date Period", true);
        dateDialog.setSize(400, 300);
        dateDialog.setLocationRelativeTo(this);
        dateDialog.setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Select Date Range:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Start date panel
        JPanel startDatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        startDatePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel startDateLabel = new JLabel("Start Date:");
        startDateLabel.setPreferredSize(new Dimension(100, 25));
        
        Date today = new Date();
        Date threeMonthsAgo = new Date(today.getTime() - (90L * 24 * 60 * 60 * 1000));
        SpinnerDateModel startDateModel = new SpinnerDateModel(threeMonthsAgo, null, today, java.util.Calendar.DAY_OF_MONTH);
        JSpinner startDateSpinner = new JSpinner(startDateModel);
        startDateSpinner.setEditor(new JSpinner.DateEditor(startDateSpinner, "MMM dd, yyyy"));
        startDateSpinner.setPreferredSize(new Dimension(150, 25));
        
        startDatePanel.add(startDateLabel);
        startDatePanel.add(startDateSpinner);
        
        // End date panel
        JPanel endDatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        endDatePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel endDateLabel = new JLabel("End Date:");
        endDateLabel.setPreferredSize(new Dimension(100, 25));
        
SpinnerDateModel endDateModel = new SpinnerDateModel(today, null, null, java.util.Calendar.DAY_OF_MONTH);
        JSpinner endDateSpinner = new JSpinner(endDateModel);
        endDateSpinner.setEditor(new JSpinner.DateEditor(endDateSpinner, "MMM dd, yyyy"));
        endDateSpinner.setPreferredSize(new Dimension(150, 25));
        
        endDatePanel.add(endDateLabel);
        endDatePanel.add(endDateSpinner);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton applyButton = new JButton("Apply Filter");
        applyButton.setBackground(PRIMARY_BLUE);
        applyButton.setForeground(Color.WHITE);
        applyButton.setFocusPainted(false);
        applyButton.addActionListener(e -> {
            Date startDate = (Date) startDateSpinner.getValue();
            Date endDate = (Date) endDateSpinner.getValue();
            
            if (startDate.after(endDate)) {
                JOptionPane.showMessageDialog(dateDialog, "Start date must be before end date!", "Invalid Date Range", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            applyDateFilter(startDate, endDate);
            dateDialog.dispose();
        });
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dateDialog.dispose());
        
        buttonsPanel.add(applyButton);
        buttonsPanel.add(cancelButton);
        
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(startDatePanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(endDatePanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(buttonsPanel);
        
        dateDialog.add(mainPanel);
        dateDialog.setVisible(true);
    }
    
    /**
     * Shows price range filter dialog
     */
    private void showPriceRangeFilterDialog() {
        JDialog priceDialog = new JDialog(this, "Select Price Range", true);
        priceDialog.setSize(400, 250);
        priceDialog.setLocationRelativeTo(this);
        priceDialog.setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Select Price Range:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Min price panel
        JPanel minPricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        minPricePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel minPriceLabel = new JLabel("Min Price ($):");
        minPriceLabel.setPreferredSize(new Dimension(100, 25));
        
        SpinnerNumberModel minPriceModel = new SpinnerNumberModel(0, 0, 500000, 1000);
        JSpinner minPriceSpinner = new JSpinner(minPriceModel);
        minPriceSpinner.setPreferredSize(new Dimension(120, 25));
        
        minPricePanel.add(minPriceLabel);
        minPricePanel.add(minPriceSpinner);
        
        // Max price panel
        JPanel maxPricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        maxPricePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel maxPriceLabel = new JLabel("Max Price ($):");
        maxPriceLabel.setPreferredSize(new Dimension(100, 25));
        
        SpinnerNumberModel maxPriceModel = new SpinnerNumberModel(100000, 0, 1000000, 1000);
        JSpinner maxPriceSpinner = new JSpinner(maxPriceModel);
        maxPriceSpinner.setPreferredSize(new Dimension(120, 25));
        
        maxPricePanel.add(maxPriceLabel);
        maxPricePanel.add(maxPriceSpinner);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton applyButton = new JButton("Apply Filter");
        applyButton.setBackground(PRIMARY_BLUE);
        applyButton.setForeground(Color.WHITE);
        applyButton.setFocusPainted(false);
        applyButton.addActionListener(e -> {
            int minPrice = (Integer) minPriceSpinner.getValue();
            int maxPrice = (Integer) maxPriceSpinner.getValue();
            
            if (minPrice > maxPrice) {
                JOptionPane.showMessageDialog(priceDialog, "Minimum price must be less than maximum price!", "Invalid Price Range", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            applyPriceFilter(minPrice, maxPrice);
            priceDialog.dispose();
        });
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> priceDialog.dispose());
        
        buttonsPanel.add(applyButton);
        buttonsPanel.add(cancelButton);
        
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(minPricePanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(maxPricePanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(buttonsPanel);
        
        priceDialog.add(mainPanel);
        priceDialog.setVisible(true);
    }
    
    /**
     * Shows car type filter dialog
     */
    private void showCarTypeFilterDialog() {
        JDialog typeDialog = new JDialog(this, "Select Car Types", true);
        typeDialog.setSize(350, 400);
        typeDialog.setLocationRelativeTo(this);
        typeDialog.setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Select Car Types:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Get unique car types from sold cars
        List<String> carTypes = new ArrayList<>();
        for (SoldCar car : soldCars) {
            if (!carTypes.contains(car.getType())) {
                carTypes.add(car.getType());
            }
        }
        
        // Create checkboxes for each car type
        JPanel checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS));
        checkboxPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        Map<String, JCheckBox> checkboxMap = new HashMap<>();
        for (String type : carTypes) {
            JCheckBox checkbox = new JCheckBox(type);
            checkbox.setAlignmentX(Component.LEFT_ALIGNMENT);
            checkboxMap.put(type, checkbox);
            checkboxPanel.add(checkbox);
            checkboxPanel.add(Box.createVerticalStrut(5));
        }
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton applyButton = new JButton("Apply Filter");
        applyButton.setBackground(PRIMARY_BLUE);
        applyButton.setForeground(Color.WHITE);
        applyButton.setFocusPainted(false);
        applyButton.addActionListener(e -> {
            List<String> selectedTypes = new ArrayList<>();
            for (Map.Entry<String, JCheckBox> entry : checkboxMap.entrySet()) {
                if (entry.getValue().isSelected()) {
                    selectedTypes.add(entry.getKey());
                }
            }
            
            if (selectedTypes.isEmpty()) {
                JOptionPane.showMessageDialog(typeDialog, "Please select at least one car type!", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            applyCarTypeFilter(selectedTypes);
            typeDialog.dispose();
        });
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> typeDialog.dispose());
        
        buttonsPanel.add(applyButton);
        buttonsPanel.add(cancelButton);
        
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(checkboxPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(buttonsPanel);
        
        typeDialog.add(mainPanel);
        typeDialog.setVisible(true);
    }
    
    /**
     * Apply date filter to sold cars
     */
    private void applyDateFilter(Date startDate, Date endDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
        String filterText = formatter.format(startDate) + " - " + formatter.format(endDate);
        
        activeFilters.put("dateRange", new Date[]{startDate, endDate});
        datePeriodFilterLabel.setText("Active: " + filterText);
        
        refreshCarsDisplay();
    }
    
    /**
     * Apply price filter to sold cars
     */
    private void applyPriceFilter(int minPrice, int maxPrice) {
        String filterText = formatCurrency(minPrice) + " - " + formatCurrency(maxPrice);
        
        activeFilters.put("priceRange", new int[]{minPrice, maxPrice});
        priceRangeFilterLabel.setText("Active: " + filterText);
        
        refreshCarsDisplay();
    }
    
    /**
     * Apply car type filter to sold cars
     */
    private void applyCarTypeFilter(List<String> selectedTypes) {
        String filterText = String.join(", ", selectedTypes);
        if (filterText.length() > 30) {
            filterText = filterText.substring(0, 27) + "...";
        }
        
        activeFilters.put("carTypes", selectedTypes);
        carTypeFilterLabel.setText("Active: " + filterText);
        
        refreshCarsDisplay();
    }
    
    /**
     * Clear all active filters
     */
    private void clearFilters() {
        activeFilters.clear();
        datePeriodFilterLabel.setText("");
        priceRangeFilterLabel.setText("");
        carTypeFilterLabel.setText("");
        
        refreshCarsDisplay();
    }
    
    /**
     * Refresh the cars display based on active filters
     */
    private void refreshCarsDisplay() {
        // Filter sold cars based on active filters
        List<SoldCar> filteredCars = new ArrayList<>();
        
        for (SoldCar car : soldCars) {
            if (matchesFilters(car)) {
                filteredCars.add(car);
            }
        }
        
        // Update the display
        updateCarsGrid(filteredCars);
    }
    
    /**
     * Check if a car matches all active filters
     */
    private boolean matchesFilters(SoldCar car) {
        // Date range filter
        if (activeFilters.containsKey("dateRange")) {
            Date[] dateRange = (Date[]) activeFilters.get("dateRange");
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
                Date carSaleDate = formatter.parse(car.getSaleDate());
                if (carSaleDate.before(dateRange[0]) || carSaleDate.after(dateRange[1])) {
                    return false;
                }
            } catch (Exception e) {
                // If date parsing fails, exclude the car
                return false;
            }
        }
        
        // Price range filter
        if (activeFilters.containsKey("priceRange")) {
            int[] priceRange = (int[]) activeFilters.get("priceRange");
            try {
                String priceStr = car.getSalePrice().replaceAll("[^0-9.]", "");
                double carPrice = Double.parseDouble(priceStr);
                if (carPrice < priceRange[0] || carPrice > priceRange[1]) {
                    return false;
                }
            } catch (Exception e) {
                // If price parsing fails, exclude the car
                return false;
            }
        }
        
        // Car type filter
        if (activeFilters.containsKey("carTypes")) {
            @SuppressWarnings("unchecked")
            List<String> selectedTypes = (List<String>) activeFilters.get("carTypes");
            if (!selectedTypes.contains(car.getType())) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Update the cars grid with filtered cars
     */
    private void updateCarsGrid(List<SoldCar> filteredCars) {
        // Remove the current content panel and recreate it
        SwingUtilities.invokeLater(() -> {
            // Find the main content panel and update it
            JPanel contentPanel = (JPanel) ((JPanel) getContentPane().getComponent(0)).getComponent(1);
            
            // Remove the cars grid (last component)
            if (contentPanel.getComponentCount() > 3) {
                contentPanel.remove(contentPanel.getComponentCount() - 1);
            }
            
            // Create new grid with filtered cars
            JPanel newCarsGrid = createFilteredCarsGrid(filteredCars);
            contentPanel.add(newCarsGrid);
            
            // Refresh the display
            revalidate();
            repaint();
        });
    }
    
    /**
     * Create cars grid with filtered cars
     */
    private JPanel createFilteredCarsGrid(List<SoldCar> filteredCars) {
        JPanel gridContainer = new JPanel();
        gridContainer.setOpaque(false);
        gridContainer.setLayout(new BoxLayout(gridContainer, BoxLayout.Y_AXIS));
        gridContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Create grid layout (4 cars per row)
        JPanel gridPanel = new JPanel(new GridLayout(0, 4, 20, 20));
        gridPanel.setOpaque(false);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Add car cards to grid
        for (SoldCar car : filteredCars) {
            JPanel carCard = createCarCard(car);
            gridPanel.add(carCard);
        }
        
        // If no cars match filters, show message
        if (filteredCars.isEmpty()) {
            JLabel noDataLabel = new JLabel("No sold cars match the current filters");
            noDataLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            noDataLabel.setForeground(new Color(120, 120, 120));
            noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
            gridPanel.add(noDataLabel);
        }
        
        gridContainer.add(gridPanel);
        return gridContainer;
    }
    
    /**
     * Export sold cars data to PDF
     */
    private void exportToPdf() {
        JOptionPane.showMessageDialog(this,
            "PDF Export functionality will be implemented here.\n\n" +
            "This would generate a comprehensive report including:\n" +
            "- Sales statistics\n" +
            "- Individual car sale details\n" +
            "- Charts and graphs\n" +
            "- Filtered results if any filters are active",
            "Export to PDF",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Format currency value
     */
    private String formatCurrency(double amount) {
        DecimalFormat formatter = new DecimalFormat("$#,##0.00");
        return formatter.format(amount);
    }
    
    /**
     * Format currency value from integer
     */
    private String formatCurrency(int amount) {
        return formatCurrency((double) amount);
    }
    
    /**
     * Inner class to represent a sold car
     */
    public static class SoldCar {
        private int id;
        private String model;
        private String year;
        private String type;
        private String color;
        private String salePrice;
        private String saleDate;
        private String buyerName;
        private String buyerContact;
        private String paymentMethod;
        private String imagePath;
        
        public SoldCar(int id, String model, String year, String type, String color,
                       String salePrice, String saleDate, String buyerName,
                       String buyerContact, String paymentMethod, String imagePath) {
            this.id = id;
            this.model = model;
            this.year = year;
            this.type = type;
            this.color = color;
            this.salePrice = salePrice;
            this.saleDate = saleDate;
            this.buyerName = buyerName;
            this.buyerContact = buyerContact;
            this.paymentMethod = paymentMethod;
            this.imagePath = imagePath;
        }
        
        // Getters
        public int getId() { return id; }
        public String getModel() { return model; }
        public String getYear() { return year; }
        public String getType() { return type; }
        public String getColor() { return color; }
        public String getSalePrice() { return salePrice; }
        public String getSaleDate() { return saleDate; }
        public String getBuyerName() { return buyerName; }
        public String getBuyerContact() { return buyerContact; }
        public String getPaymentMethod() { return paymentMethod; }
        public String getImagePath() { return imagePath; }
        
        // Setters
        public void setId(int id) { this.id = id; }
        public void setModel(String model) { this.model = model; }
        public void setYear(String year) { this.year = year; }
        public void setType(String type) { this.type = type; }
        public void setColor(String color) { this.color = color; }
        public void setSalePrice(String salePrice) { this.salePrice = salePrice; }
        public void setSaleDate(String saleDate) { this.saleDate = saleDate; }
        public void setBuyerName(String buyerName) { this.buyerName = buyerName; }
        public void setBuyerContact(String buyerContact) { this.buyerContact = buyerContact; }
        public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
        public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    }
}