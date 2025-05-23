package com.example.Design;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 * Sold Cars UI class that extends BaseUI to inherit common UI elements
 * and provides functionality for managing sold cars
 */
public class SoldCarsUI extends BaseUI {
    
    private DefaultTableModel tableModel;
    private JTable soldCarsTable;
    
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
    
    // Summary statistics
    private int totalSoldCount = 0;
    private double totalSalesValue = 0;
    private double averagePrice = 0;
    private int monthlySalesCount = 0;

    /**
     * Constructor with admin ID
     * @param adminId The ID of the admin user
     */
    public SoldCarsUI(int adminId) {
        super(adminId);
    }
    
    /**
     * Default constructor
     */
    public SoldCarsUI() {
        super();
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
        
        // FIRST: Create and initialize the table model and data
        createTableAndData();
        
        // THEN: Calculate statistics after table is created
        calculateSalesStatistics();
        
        // Sales statistics overview
        JPanel statsPanel = new JPanel();
        statsPanel.setOpaque(false);
        statsPanel.setLayout(new GridLayout(1, 4, 20, 0));
        statsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsPanel.setMaximumSize(new Dimension(1200, 120));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
        
        // Create overview cards
        JPanel totalSoldCard = createOverviewCard("Total Cars Sold", Integer.toString(totalSoldCount), PRIMARY_BLUE);
        JPanel totalValueCard = createOverviewCard("Total Sales Value", formatCurrency(totalSalesValue), PRIMARY_GREEN);
        JPanel avgPriceCard = createOverviewCard("Average Sale Price", formatCurrency(averagePrice), PRIMARY_YELLOW);
        JPanel monthlySalesCard = createOverviewCard("Monthly Sales", Integer.toString(monthlySalesCount), new Color(156, 39, 176)); // Purple
        
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
        
        // Create table UI components
        createTableUI();
        
        // Create a rounded panel for the table
        RoundedPanel tablePanel = new RoundedPanel(15, Color.WHITE);
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        // Create a scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(soldCarsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBackground(Color.WHITE);
        
        tablePanel.add(scrollPane);
        
        // Add box for padding around table
        Box tableBox = Box.createVerticalBox();
        tableBox.setOpaque(false);
        tableBox.add(tablePanel);
        tableBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Pagination panel
        JPanel paginationPanel = createPaginationPanel();
        
        // Create footer panel
        footerPanel = createFooterPanel();
        
        // Add components to content panel
        contentPanel.add(statsPanel);
        contentPanel.add(filterLabel);
        contentPanel.add(filterCardsPanel);
        contentPanel.add(carsLabel);
        contentPanel.add(tableBox);
        contentPanel.add(paginationPanel);
        
        // Add all panels to main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    /**
     * Create and initialize the table model with data
     */
    private void createTableAndData() {
        // Create table with sample data
        String[] columnNames = {"MODEL", "YEAR", "TYPE", "SALE DATE", "SALE PRICE", "BUYER", "ACTIONS"};
        
        // Create table model for dynamic updates
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Only make Actions column editable
            }
        };

        // Add sample data - Note: In a real app, this would be from a database
        tableModel.addRow(new Object[]{"Toyota Camry", "2023", "Sedan", "Jan 15, 2025", "$29,500", "John Smith", "View"});
        tableModel.addRow(new Object[]{"Honda CR-V", "2023", "SUV", "Jan 23, 2025", "$32,800", "Emma Johnson", "View"});
        tableModel.addRow(new Object[]{"Ford F-150", "2022", "Truck", "Feb 02, 2025", "$47,200", "Michael Brown", "View"});
        tableModel.addRow(new Object[]{"BMW X5", "2023", "SUV", "Feb 14, 2025", "$65,000", "Sarah Davis", "View"});
        tableModel.addRow(new Object[]{"Chevrolet Malibu", "2022", "Sedan", "Mar 05, 2025", "$27,500", "Robert Wilson", "View"});
        tableModel.addRow(new Object[]{"Tesla Model 3", "2023", "Electric", "Mar 18, 2025", "$49,800", "Jennifer Lee", "View"});
        tableModel.addRow(new Object[]{"Jeep Wrangler", "2023", "SUV", "Apr 02, 2025", "$39,500", "David Garcia", "View"});
        tableModel.addRow(new Object[]{"Audi Q7", "2023", "SUV", "Apr 15, 2025", "$61,200", "Lisa Martinez", "View"});
        tableModel.addRow(new Object[]{"Kia Sorento", "2022", "SUV", "Apr 27, 2025", "$34,300", "James Thompson", "View"});
        tableModel.addRow(new Object[]{"Mercedes-Benz C-Class", "2023", "Sedan", "May 10, 2025", "$52,800", "Patricia Anderson", "View"});
    }
    
    /**
     * Create table UI components and styling
     */
    private void createTableUI() {
        soldCarsTable = new JTable(tableModel);
        soldCarsTable.setRowHeight(40);
        soldCarsTable.setShowGrid(false);
        soldCarsTable.setIntercellSpacing(new Dimension(0, 0));
        soldCarsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        soldCarsTable.setBackground(Color.WHITE);
        
        // Style table header
        JTableHeader header = soldCarsTable.getTableHeader();
        header.setBackground(new Color(248, 250, 252));
        header.setForeground(new Color(50, 50, 50));
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(230, 230, 230)));
        header.setReorderingAllowed(false);
        
        // Custom renderers for different columns
        soldCarsTable.getColumnModel().getColumn(2).setCellRenderer(new CarTypeRenderer());
        soldCarsTable.getColumnModel().getColumn(4).setCellRenderer(new PriceRenderer());
        soldCarsTable.getColumnModel().getColumn(6).setCellRenderer(new ActionRenderer());
        
        // Set column widths
        soldCarsTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        soldCarsTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        soldCarsTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        soldCarsTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        soldCarsTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        soldCarsTable.getColumnModel().getColumn(5).setPreferredWidth(150);
        soldCarsTable.getColumnModel().getColumn(6).setPreferredWidth(80);
        
        // Add action listener for the action buttons
        soldCarsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = soldCarsTable.rowAtPoint(e.getPoint());
                int col = soldCarsTable.columnAtPoint(e.getPoint());
                
                if (col == 6 && row >= 0) {
                    showSaleDetails(row);
                }
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                int col = soldCarsTable.columnAtPoint(e.getPoint());
                if (col == 6) {
                    soldCarsTable.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                soldCarsTable.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }
    
    /**
     * Create pagination panel
     */
    private JPanel createPaginationPanel() {
        JPanel paginationPanel = new JPanel();
        paginationPanel.setOpaque(false);
        paginationPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));
        paginationPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Previous page button
        JButton prevButton = new JButton("← Previous");
        prevButton.setFont(new Font("Arial", Font.PLAIN, 14));
        prevButton.setBackground(new Color(248, 250, 252));
        prevButton.setForeground(new Color(50, 50, 50));
        prevButton.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        prevButton.setFocusPainted(false);
        
        // Page number buttons
        for (int i = 1; i <= 3; i++) {
            JButton pageButton = new JButton(String.valueOf(i));
            pageButton.setFont(new Font("Arial", Font.PLAIN, 14));
            
            if (i == 1) { // First page is active by default
                pageButton.setBackground(PRIMARY_BLUE);
                pageButton.setForeground(Color.WHITE);
                pageButton.setBorder(BorderFactory.createLineBorder(PRIMARY_BLUE));
            } else {
                pageButton.setBackground(new Color(248, 250, 252));
                pageButton.setForeground(new Color(50, 50, 50));
                pageButton.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
            }
            
            pageButton.setFocusPainted(false);
            paginationPanel.add(pageButton);
        }
        
        // Next page button
        JButton nextButton = new JButton("Next →");
        nextButton.setFont(new Font("Arial", Font.PLAIN, 14));
        nextButton.setBackground(new Color(248, 250, 252));
        nextButton.setForeground(new Color(50, 50, 50));
        nextButton.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        nextButton.setFocusPainted(false);
        
        paginationPanel.add(prevButton);
        paginationPanel.add(nextButton);
        
        return paginationPanel;
    }
    
    /**
     * Create a filter card with title, description, and indicator
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
        
        // Date spinner for start date
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
        
        // Date spinner for end date
        SpinnerDateModel endDateModel = new SpinnerDateModel(today, null, null, java.util.Calendar.DAY_OF_MONTH);
        JSpinner endDateSpinner = new JSpinner(endDateModel);
        endDateSpinner.setEditor(new JSpinner.DateEditor(endDateSpinner, "MMM dd, yyyy"));
        endDateSpinner.setPreferredSize(new Dimension(150, 25));
        
        endDatePanel.add(endDateLabel);
        endDatePanel.add(endDateSpinner);
        
        // Quick period selection panel
        JPanel quickPeriodPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        quickPeriodPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel quickPeriodLabel = new JLabel("Quick Select:");
        quickPeriodLabel.setPreferredSize(new Dimension(100, 25));
        
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        
        JButton lastMonthButton = new JButton("Last Month");
        JButton last3MonthsButton = new JButton("Last 3 Months");
        JButton yearToDateButton = new JButton("Year to Date");
        
        // Style the quick buttons
        Font quickButtonFont = new Font("Arial", Font.PLAIN, 12);
        lastMonthButton.setFont(quickButtonFont);
        last3MonthsButton.setFont(quickButtonFont);
        yearToDateButton.setFont(quickButtonFont);
        
        // Add actions to quick period buttons
        lastMonthButton.addActionListener(e -> {
            Date now = new Date();
            Date oneMonthAgo = new Date(now.getTime() - (30L * 24 * 60 * 60 * 1000));
            startDateSpinner.setValue(oneMonthAgo);
            endDateSpinner.setValue(now);
        });
        
        last3MonthsButton.addActionListener(e -> {
            Date now = new Date();
            Date threeMonthsAgoDate = new Date(now.getTime() - (90L * 24 * 60 * 60 * 1000));
            startDateSpinner.setValue(threeMonthsAgoDate);
            endDateSpinner.setValue(now);
        });
        
        yearToDateButton.addActionListener(e -> {
            Date now = new Date();
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.set(java.util.Calendar.DAY_OF_YEAR, 1);
            startDateSpinner.setValue(cal.getTime());
            endDateSpinner.setValue(now);
        });
        
        buttonsPanel.add(lastMonthButton);
        buttonsPanel.add(last3MonthsButton);
        buttonsPanel.add(yearToDateButton);
        
        quickPeriodPanel.add(quickPeriodLabel);
        quickPeriodPanel.add(buttonsPanel);
        
        // Add panels to main panel
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(startDatePanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(endDatePanel);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(quickPeriodPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dateDialog.dispose());
        
        JButton applyButton = new JButton("Apply Filter");
        applyButton.setBackground(PRIMARY_BLUE);
        applyButton.setForeground(Color.WHITE);
        applyButton.addActionListener(e -> {
            // Get selected dates
            Date startDate = (Date) startDateSpinner.getValue();
            Date endDate = (Date) endDateSpinner.getValue();
            
            // Apply date filter
            if (startDate.before(endDate) || startDate.equals(endDate)) {
                SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
                datePeriodFilterLabel.setText("Filter: " + formatter.format(startDate) + " - " + formatter.format(endDate));
                dateDialog.dispose();
                
                // Store filter values for later use
                activeFilters.put("startDate", startDate);
                activeFilters.put("endDate", endDate);
                
                // Apply filter to data (in a real app this would filter the actual data)
                applyFilters();
            } else {
                JOptionPane.showMessageDialog(dateDialog, 
                    "End date must be after or equal to start date", 
                    "Invalid Date Range", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(applyButton);
        
        mainPanel.add(buttonPanel);
        
        dateDialog.add(mainPanel, BorderLayout.CENTER);
        dateDialog.setVisible(true);
    }
    
    /**
     * Shows sale details dialog
     */
    private void showSaleDetails(int row) {
        // Get sale details from the table
        String model = (String) tableModel.getValueAt(row, 0);
        String year = (String) tableModel.getValueAt(row, 1);
        String type = (String) tableModel.getValueAt(row, 2);
        String saleDate = (String) tableModel.getValueAt(row, 3);
        String salePrice = (String) tableModel.getValueAt(row, 4);
        String buyer = (String) tableModel.getValueAt(row, 5);
        
        // Create sale details dialog
        JDialog saleDialog = new JDialog(this, "Sale Transaction Details", true);
        saleDialog.setSize(500, 500);
        saleDialog.setLocationRelativeTo(this);
        saleDialog.setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Sale Transaction Details");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Create details panels
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        // Vehicle information section
        JLabel vehicleInfoLabel = new JLabel("Vehicle Information");
        vehicleInfoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        vehicleInfoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Add field rows for vehicle info
        addDetailRow(detailsPanel, "Model:", model);
        addDetailRow(detailsPanel, "Year:", year);
        addDetailRow(detailsPanel, "Type:", type);
        addDetailRow(detailsPanel, "VIN:", "ABC" + (1000 + row) + "XYZ");
        addDetailRow(detailsPanel, "Engine:", "2.5L 4-Cylinder");
        addDetailRow(detailsPanel, "Transmission:", "Automatic");
        addDetailRow(detailsPanel, "Color:", row % 2 == 0 ? "Black" : "Silver");
        
        // Transaction information section
        JLabel transactionInfoLabel = new JLabel("Transaction Information");
        transactionInfoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        transactionInfoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Add field rows for transaction info
        addDetailRow(detailsPanel, "Sale Date:", saleDate);
        addDetailRow(detailsPanel, "Sale Price:", salePrice);
        addDetailRow(detailsPanel, "Tax:", "$" + (Integer.parseInt(salePrice.replace("$", "").replace(",", "")) * 0.08 / 1000) + "00");
        addDetailRow(detailsPanel, "Total Amount:", "$" + (Integer.parseInt(salePrice.replace("$", "").replace(",", "")) * 1.08 / 1000) + "00");
        addDetailRow(detailsPanel, "Payment Method:", "Financing");
        
        // Buyer information section
        JLabel buyerInfoLabel = new JLabel("Buyer Information");
        buyerInfoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        buyerInfoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Add field rows for buyer info
        addDetailRow(detailsPanel, "Buyer Name:", buyer);
        addDetailRow(detailsPanel, "Contact:", "(555) " + (100 + row) + "-" + (2000 + row));
        addDetailRow(detailsPanel, "Email:", buyer.toLowerCase().replace(" ", ".") + "@example.com");
        addDetailRow(detailsPanel, "Address:", "123 Main St, City, State");
        
        // Add components to main panel
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(vehicleInfoLabel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(detailsPanel);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(transactionInfoLabel);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(buyerInfoLabel);
        mainPanel.add(Box.createVerticalGlue());
        
        // Buttons panel
        JPanel dialogButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        dialogButtonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton printButton = new JButton("Print Receipt");
        printButton.setBackground(PRIMARY_GREEN);
        printButton.setForeground(Color.WHITE);
        printButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(saleDialog, 
                "Printing receipt for " + buyer + "...", 
                "Print Receipt", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> saleDialog.dispose());
        
        dialogButtonPanel.add(printButton);
        dialogButtonPanel.add(closeButton);
        
        mainPanel.add(dialogButtonPanel);
        
        saleDialog.add(mainPanel, BorderLayout.CENTER);
        saleDialog.setVisible(true);
    }
    
    /**
     * Adds a detail row to the details panel
     */
    private void addDetailRow(JPanel panel, String label, String value) {
        JPanel rowPanel = new JPanel(new BorderLayout(10, 0));
        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        rowPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Arial", Font.BOLD, 14));
        labelComponent.setPreferredSize(new Dimension(120, 25));
        
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Arial", Font.PLAIN, 14));
        
        rowPanel.add(labelComponent, BorderLayout.WEST);
        rowPanel.add(valueComponent, BorderLayout.CENTER);
        
        panel.add(rowPanel);
        panel.add(Box.createVerticalStrut(10));
    }
    
    /**
     * Shows price range filter dialog
     */
    private void showPriceRangeFilterDialog() {
        // Create a custom dialog for price range selection
        JDialog priceDialog = new JDialog(this, "Select Price Range", true);
        priceDialog.setSize(400, 250);
        priceDialog.setLocationRelativeTo(this);
        priceDialog.setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Min price
        JPanel minPricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        minPricePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel minPriceLabel = new JLabel("Minimum Price:");
        minPriceLabel.setPreferredSize(new Dimension(100, 25));
        
        // Use JSpinner with NumberModel for price selection
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
        
        // Quick price ranges panel
        JPanel quickRangesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        quickRangesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel quickRangeLabel = new JLabel("Quick Select:");
        quickRangeLabel.setPreferredSize(new Dimension(100, 25));
        
        JButton budgetButton = new JButton("Budget (<$30k)");
        JButton midRangeButton = new JButton("Mid-Range ($30k-$50k)");
        JButton luxuryButton = new JButton("Luxury (>$50k)");
        
        // Style the quick buttons
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
        cancelButton.addActionListener(e -> priceDialog.dispose());
        
        JButton applyButton = new JButton("Apply Filter");
        applyButton.setBackground(PRIMARY_BLUE);
        applyButton.setForeground(Color.WHITE);
        applyButton.addActionListener(e -> {
            // Get selected prices
            int minPrice = (int) minPriceSpinner.getValue();
            int maxPrice = (int) maxPriceSpinner.getValue();
            
            // Apply price filter (in a real implementation)
            if (maxPrice >= minPrice) {
                DecimalFormat formatter = new DecimalFormat("$#,###");
                priceRangeFilterLabel.setText("Filter: " + formatter.format(minPrice) + " - " + formatter.format(maxPrice));
                
                // Store filter values for later use
                activeFilters.put("minPrice", minPrice);
                activeFilters.put("maxPrice", maxPrice);
                
                // Apply filter to data
                applyFilters();
                
                priceDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(priceDialog, 
                    "Maximum price must be greater than or equal to minimum price", 
                    "Invalid Price Range", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(applyButton);
        
        mainPanel.add(buttonPanel);
        
        priceDialog.add(mainPanel, BorderLayout.CENTER);
        priceDialog.setVisible(true);
    }
    
    /**
     * Shows car type filter dialog
     */
    private void showCarTypeFilterDialog() {
        JDialog typeDialog = new JDialog(this, "Select Car Types", true);
        typeDialog.setSize(350, 300);
        typeDialog.setLocationRelativeTo(this);
        typeDialog.setLayout(new BorderLayout());
        
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
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> typeDialog.dispose());
        
        JButton applyButton = new JButton("Apply Filter");
        applyButton.setBackground(PRIMARY_BLUE);
        applyButton.setForeground(Color.WHITE);
        applyButton.addActionListener(e -> {
            // Create list of selected car types
            List<String> selectedTypes = new ArrayList<>();
            
            if (sedanCheckBox.isSelected()) {
                selectedTypes.add("Sedan");
            }
            
            if (suvCheckBox.isSelected()) {
                selectedTypes.add("SUV");
            }
            
            if (truckCheckBox.isSelected()) {
                selectedTypes.add("Truck");
            }
            
            if (electricCheckBox.isSelected()) {
                selectedTypes.add("Electric");
            }
            
            if (luxuryCheckBox.isSelected()) {
                selectedTypes.add("Luxury");
            }
            
            // Apply filter label
            if (selectedTypes.size() == 5 || selectedTypes.isEmpty()) {
                carTypeFilterLabel.setText("");
                activeFilters.remove("carTypes");
            } else {
                carTypeFilterLabel.setText("Filter: " + String.join(", ", selectedTypes));
                activeFilters.put("carTypes", selectedTypes);
            }
            
            // Apply filter to data
            applyFilters();
            
            // Close dialog
            typeDialog.dispose();
        });
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(applyButton);
        
        mainPanel.add(buttonPanel);
        
        typeDialog.add(mainPanel, BorderLayout.CENTER);
        typeDialog.setVisible(true);
    }
    
    /**
     * Apply all active filters to the table data
     */
    private void applyFilters() {
        // In a real application, this would query a database with filters
        // For this example, we'll just show a message
        JOptionPane.showMessageDialog(this, 
            "Filters applied. In a real application, this would filter the data.", 
            "Filters Applied", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Clear all active filters
     */
    private void clearFilters() {
        activeFilters.clear();
        datePeriodFilterLabel.setText("");
        priceRangeFilterLabel.setText("");
        carTypeFilterLabel.setText("");
        
        // In a real application, this would refresh the data without filters
        JOptionPane.showMessageDialog(this, 
            "All filters cleared. Data would be refreshed in a real application.", 
            "Filters Cleared", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Export the current table data to PDF
     */
    private void exportToPdf() {
        JOptionPane.showMessageDialog(this, 
            "Exporting sales data to PDF...\nIn a real application, this would create a PDF file.", 
            "Export to PDF", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Calculate sales statistics
     * In a real application, this would come from a database
     */
    private void calculateSalesStatistics() {
        // Make sure tableModel is not null before using it
        if (tableModel == null) {
            // Set default values if table is not initialized
            totalSoldCount = 0;
            totalSalesValue = 0;
            averagePrice = 0;
            monthlySalesCount = 0;
            return;
        }
        
        // Simulate calculating statistics from the table data
        totalSoldCount = tableModel.getRowCount();
        totalSalesValue = 0;
        
        // Calculate the total sales value and average price
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String priceStr = (String) tableModel.getValueAt(i, 4);
            int price = Integer.parseInt(priceStr.replace("$", "").replace(",", ""));
            totalSalesValue += price;
        }
        
        if (totalSoldCount > 0) {
            averagePrice = totalSalesValue / totalSoldCount;
        } else {
            averagePrice = 0;
        }
        
        // Count sales in the current month
        monthlySalesCount = 3; // Example value
    }
    
    /**
     * Format a number as currency
     * @param amount The amount to format
     * @return Formatted currency string
     */
    private String formatCurrency(double amount) {
        DecimalFormat formatter = new DecimalFormat("$#,###,###");
        return formatter.format(amount);
    }
    
    // Custom renderer for Car Type column
    private static class CarTypeRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setOpaque(true);

            if (value == null) {
                return label;
            }
            
            String type = value.toString();
            if (type.equalsIgnoreCase("Sedan")) {
                label.setBackground(new Color(230, 255, 250)); // #e6fffa
                label.setForeground(new Color(13, 110, 253)); // #0d6efd
            } else if (type.equalsIgnoreCase("SUV")) {
                label.setBackground(new Color(255, 250, 230));
                label.setForeground(new Color(255, 153, 0));
            } else if (type.equalsIgnoreCase("Truck")) {
                label.setBackground(new Color(240, 240, 255));
                label.setForeground(new Color(102, 16, 242));
            } else if (type.equalsIgnoreCase("Electric")) {
                label.setBackground(new Color(232, 255, 232));
                label.setForeground(new Color(25, 135, 84));
            } else {
                label.setBackground(new Color(240, 240, 240));
                label.setForeground(new Color(70, 70, 70));
            }
            label.setBorder(BorderFactory.createLineBorder(label.getBackground()));
            return label;
        }
    }

    // Custom renderer for Price column
    private static class PriceRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
            label.setHorizontalAlignment(SwingConstants.RIGHT);
            return label;
        }
    }

    // Custom renderer for Action column
    private class ActionRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            // Create a label that looks like a button
            JLabel buttonLabel = new JLabel(value.toString());
            buttonLabel.setHorizontalAlignment(SwingConstants.CENTER);
            buttonLabel.setFont(new Font("Arial", Font.BOLD, 12));
            buttonLabel.setBackground(PRIMARY_BLUE);
            buttonLabel.setForeground(Color.WHITE);
            buttonLabel.setOpaque(true);
            buttonLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            
            // Add a darker border to make it look more like a button
            buttonLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_BLUE.darker(), 1),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)
            ));
            
            return buttonLabel;
        }
    }
    
    /**
     * Main method to test the Sold Cars UI
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set look and feel to system look and feel
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new SoldCarsUI().setVisible(true);
        });
    }
}