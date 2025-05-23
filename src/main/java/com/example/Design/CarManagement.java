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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 * Car Management UI class that extends BaseUI to inherit common UI elements
 */
public class CarManagement extends BaseUI {
    
    private DefaultTableModel tableModel;
    private JTable carsTable;
    
    // Filter panels with active state indicators
    private RoundedPanel priceRangeCard;
    private RoundedPanel carTypeCard;
    private RoundedPanel statusCard;
    
    // Labels for active filter indicators
    private JLabel priceRangeFilterLabel;
    private JLabel carTypeFilterLabel;
    private JLabel statusFilterLabel;
    
    // Store active filters
    private Map<String, Object> activeFilters = new HashMap<>();

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
        
        statusFilterLabel = new JLabel("");
        statusFilterLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        statusFilterLabel.setForeground(PRIMARY_BLUE);
        
        // Create filter cards
        priceRangeCard = createFilterCard("Price Range", "Filter by minimum and maximum price", priceRangeFilterLabel);
        carTypeCard = createFilterCard("Car Type", "Filter by sedan, SUV, truck, etc.", carTypeFilterLabel);
        statusCard = createFilterCard("Status", "Filter by available, sold, reserved", statusFilterLabel);
        
        filterCardsPanel.add(priceRangeCard);
        filterCardsPanel.add(carTypeCard);
        filterCardsPanel.add(statusCard);
        
        // Car list section title
        JLabel carsLabel = new JLabel("CAR INVENTORY");
        carsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        carsLabel.setForeground(new Color(50, 50, 50));
        carsLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 15, 0));
        carsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Create table with sample data
        String[] columnNames = {"MODEL", "YEAR", "TYPE", "COLOR", "PRICE", "STATUS", "ACTIONS"};
        
        // Create table model for dynamic updates
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Only make Actions column editable
            }
        };

        // Add sample data
        tableModel.addRow(new Object[]{"Toyota Camry", "2023", "Sedan", "Silver", "$28,500", "Available", "View/Edit"});
        tableModel.addRow(new Object[]{"Honda CR-V", "2023", "SUV", "White", "$32,000", "Available", "View/Edit"});
        tableModel.addRow(new Object[]{"Ford F-150", "2022", "Truck", "Black", "$45,000", "Available", "View/Edit"});
        tableModel.addRow(new Object[]{"BMW X5", "2023", "SUV", "Blue", "$62,000", "Sold", "View"});
        tableModel.addRow(new Object[]{"Chevrolet Malibu", "2022", "Sedan", "Red", "$26,000", "Reserved", "View/Edit"});
        tableModel.addRow(new Object[]{"Tesla Model 3", "2023", "Electric", "Black", "$47,000", "Available", "View/Edit"});
        tableModel.addRow(new Object[]{"Jeep Wrangler", "2023", "SUV", "Green", "$38,000", "Available", "View/Edit"});
        tableModel.addRow(new Object[]{"Nissan Altima", "2022", "Sedan", "Gray", "$25,000", "Available", "View/Edit"});
        tableModel.addRow(new Object[]{"Audi Q7", "2023", "SUV", "Black", "$58,000", "Available", "View/Edit"});
        tableModel.addRow(new Object[]{"Kia Sorento", "2022", "SUV", "White", "$33,000", "Sold", "View"});

        carsTable = new JTable(tableModel);
        carsTable.setRowHeight(40);
        carsTable.setShowGrid(false);
        carsTable.setIntercellSpacing(new Dimension(0, 0));
        carsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        carsTable.setBackground(Color.WHITE);
        
        // Style table header
        JTableHeader header = carsTable.getTableHeader();
        header.setBackground(new Color(248, 250, 252));
        header.setForeground(new Color(50, 50, 50));
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(230, 230, 230)));
        header.setReorderingAllowed(false);
        
        // Custom renderers for different columns
        carsTable.getColumnModel().getColumn(2).setCellRenderer(new CarTypeRenderer());
        carsTable.getColumnModel().getColumn(4).setCellRenderer(new PriceRenderer());
        carsTable.getColumnModel().getColumn(5).setCellRenderer(new StatusRenderer());
        carsTable.getColumnModel().getColumn(6).setCellRenderer(new ActionRenderer());
        
        // Set column widths
        carsTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        carsTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        carsTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        carsTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        carsTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        carsTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        carsTable.getColumnModel().getColumn(6).setPreferredWidth(100);
        
        // Add action listener for the action buttons
        carsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = carsTable.rowAtPoint(e.getPoint());
                int col = carsTable.columnAtPoint(e.getPoint());
                
                if (col == 6 && row >= 0) {
                    // Get car details and show action dialog
                    String status = (String) tableModel.getValueAt(row, 5);
                    if (status.equals("Sold")) {
                        showCarDetails(row);
                    } else {
                        showCarEditDialog(row);
                    }
                }
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                int col = carsTable.columnAtPoint(e.getPoint());
                if (col == 6) {
                    carsTable.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                carsTable.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        
        // Create a rounded panel for the table
        RoundedPanel tablePanel = new RoundedPanel(15, Color.WHITE);
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        // Create a scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(carsTable);
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
        for (int i = 1; i <= 5; i++) {
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
        
        // Create footer panel
        footerPanel = createFooterPanel();
        
        // Add components to content panel
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
            case "Status":
                showStatusFilterDialog();
                break;
            default:
                // Do nothing
        }
    }
    
    /**
     * Shows price range filter dialog
     */
    private void showPriceRangeFilterDialog() {
        // Create a custom dialog for price range selection
        JDialog dialog = new JDialog(this, "Select Price Range", true);
        dialog.setSize(400, 250);
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
        
        JButton budgetButton = new JButton("Budget (<$25k)");
        JButton midRangeButton = new JButton("Mid-Range ($25k-$50k)");
        JButton luxuryButton = new JButton("Luxury (>$50k)");
        
        // Style the quick buttons
        Font quickButtonFont = new Font("Arial", Font.PLAIN, 12);
        budgetButton.setFont(quickButtonFont);
        midRangeButton.setFont(quickButtonFont);
        luxuryButton.setFont(quickButtonFont);
        
        budgetButton.addActionListener(e -> {
            minPriceSpinner.setValue(0);
            maxPriceSpinner.setValue(25000);
        });
        
        midRangeButton.addActionListener(e -> {
            minPriceSpinner.setValue(25000);
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
            // Get selected prices
            int minPrice = (int) minPriceSpinner.getValue();
            int maxPrice = (int) maxPriceSpinner.getValue();
            
            // Apply price filter (in a real implementation)
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
     * Shows status filter dialog
     */
    private void showStatusFilterDialog() {
        JDialog dialog = new JDialog(this, "Select Car Status", true);
        dialog.setSize(350, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Select Car Status:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Create radio buttons for status
        JRadioButton allRadio = new JRadioButton("All Statuses");
        allRadio.setFont(new Font("Arial", Font.PLAIN, 14));
        allRadio.setAlignmentX(Component.LEFT_ALIGNMENT);
        allRadio.setSelected(true);
        
        JRadioButton availableRadio = new JRadioButton("Available Only");
        availableRadio.setFont(new Font("Arial", Font.PLAIN, 14));
        availableRadio.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JRadioButton soldRadio = new JRadioButton("Sold Only");
        soldRadio.setFont(new Font("Arial", Font.PLAIN, 14));
        soldRadio.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JRadioButton reservedRadio = new JRadioButton("Reserved Only");
        reservedRadio.setFont(new Font("Arial", Font.PLAIN, 14));
        reservedRadio.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Group radio buttons
        ButtonGroup statusGroup = new ButtonGroup();
        statusGroup.add(allRadio);
        statusGroup.add(availableRadio);
        statusGroup.add(soldRadio);
        statusGroup.add(reservedRadio);
        
        // Add components to main panel
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(allRadio);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(availableRadio);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(soldRadio);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(reservedRadio);
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
            // Get selected status
            String selectedStatus = null;
            
            if (availableRadio.isSelected()) {
                selectedStatus = "Available";
                statusFilterLabel.setText("Filter: Available Only");
            } else if (soldRadio.isSelected()) {
                selectedStatus = "Sold";
                statusFilterLabel.setText("Filter: Sold Only");
            } else if (reservedRadio.isSelected()) {
                selectedStatus = "Reserved";
                statusFilterLabel.setText("Filter: Reserved Only");
            } else {
                statusFilterLabel.setText("");
            }
            
            // Close dialog
            dialog.dispose();
        });
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(applyButton);
        
        mainPanel.add(buttonPanel);
        
        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
    
    /**
     * Shows car details dialog
     */
    private void showCarDetails(int row) {
        // Get car details from the table
        String model = (String) tableModel.getValueAt(row, 0);
        String year = (String) tableModel.getValueAt(row, 1);
        String type = (String) tableModel.getValueAt(row, 2);
        String color = (String) tableModel.getValueAt(row, 3);
        String price = (String) tableModel.getValueAt(row, 4);
        String status = (String) tableModel.getValueAt(row, 5);
        
        // Create car details dialog
        JDialog dialog = new JDialog(this, "Car Details", true);
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Car Details");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Create details panels
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        // Add field rows
        addDetailRow(detailsPanel, "Model:", model);
        addDetailRow(detailsPanel, "Year:", year);
        addDetailRow(detailsPanel, "Type:", type);
        addDetailRow(detailsPanel, "Color:", color);
        addDetailRow(detailsPanel, "Price:", price);
        addDetailRow(detailsPanel, "Status:", status);
        addDetailRow(detailsPanel, "VIN:", "ABC" + (1000 + row) + "XYZ");
        addDetailRow(detailsPanel, "Engine:", "2.5L 4-Cylinder");
        addDetailRow(detailsPanel, "Transmission:", "Automatic");
        addDetailRow(detailsPanel, "Mileage:", "0 mi (New)");
        
        // Add components to main panel
        mainPanel.add(titleLabel);
        mainPanel.add(detailsPanel);
        mainPanel.add(Box.createVerticalGlue());
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(closeButton);
        
        mainPanel.add(buttonPanel);
        
        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
    
    /**
     * Shows car edit dialog
     */
    private void showCarEditDialog(int row) {
        // Get car details from the table
        String model = (String) tableModel.getValueAt(row, 0);
        String year = (String) tableModel.getValueAt(row, 1);
        String type = (String) tableModel.getValueAt(row, 2);
        String color = (String) tableModel.getValueAt(row, 3);
        String price = (String) tableModel.getValueAt(row, 4);
        String status = (String) tableModel.getValueAt(row, 5);
        
        // Create car edit dialog
        JDialog dialog = new JDialog(this, "Edit Car", true);
        dialog.setSize(450, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Edit Car Information");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Create form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        // Create form fields
        JPanel modelPanel = createFormField("Model:", model);
        JTextField modelField = (JTextField) ((JPanel)modelPanel.getComponent(1)).getComponent(0);
        
        JPanel yearPanel = createFormField("Year:", year);
        JTextField yearField = (JTextField) ((JPanel)yearPanel.getComponent(1)).getComponent(0);
        
        JPanel typePanel = createFormField("Type:", type);
        JTextField typeField = (JTextField) ((JPanel)typePanel.getComponent(1)).getComponent(0);
        
        JPanel colorPanel = createFormField("Color:", color);
        JTextField colorField = (JTextField) ((JPanel)colorPanel.getComponent(1)).getComponent(0);
        
        JPanel pricePanel = createFormField("Price:", price.replace("$", ""));
        JTextField priceField = (JTextField) ((JPanel)pricePanel.getComponent(1)).getComponent(0);
        
        // Status dropdown field
        JPanel statusPanel = new JPanel(new BorderLayout(10, 0));
        statusPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        statusPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusLabel.setPreferredSize(new Dimension(100, 25));
        
        String[] statusOptions = {"Available", "Reserved", "Sold"};
        JPanel statusFieldPanel = new JPanel(new BorderLayout());
        JTextField statusField = new JTextField(status);
        statusField.setFont(new Font("Arial", Font.PLAIN, 14));
        statusFieldPanel.add(statusField);
        
        statusPanel.add(statusLabel, BorderLayout.WEST);
        statusPanel.add(statusFieldPanel, BorderLayout.CENTER);
        
        // Engine
        JPanel enginePanel = createFormField("Engine:", "2.5L 4-Cylinder");
        
        // Transmission
        JPanel transmissionPanel = createFormField("Transmission:", "Automatic");
        
        // Mileage
        JPanel mileagePanel = createFormField("Mileage:", "0");
        
        // Add fields to form panel
        formPanel.add(modelPanel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(yearPanel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(typePanel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(colorPanel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(pricePanel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(statusPanel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(enginePanel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(transmissionPanel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(mileagePanel);
        formPanel.add(Box.createVerticalStrut(20));
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dialog.dispose());
        
        JButton saveButton = new JButton("Save Changes");
        saveButton.setBackground(PRIMARY_GREEN);
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveButton.addActionListener(e -> {
            // In a real application, save changes to database
            JOptionPane.showMessageDialog(dialog,
                "Car information updated successfully!",
                "Update Successful",
                JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        
        // Add components to main panel
        mainPanel.add(titleLabel);
        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(buttonPanel);
        
        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
    
    /**
     * Creates a form field with label and text field
     */
    private JPanel createFormField(String label, String value) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Arial", Font.BOLD, 14));
        labelComponent.setPreferredSize(new Dimension(100, 25));
        
        JPanel fieldPanel = new JPanel(new BorderLayout());
        JTextField field = new JTextField(value);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        fieldPanel.add(field);
        
        panel.add(labelComponent, BorderLayout.WEST);
        panel.add(fieldPanel, BorderLayout.CENTER);
        
        return panel;
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
        
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Arial", Font.PLAIN, 14));
        
        rowPanel.add(labelComponent, BorderLayout.WEST);
        rowPanel.add(valueComponent, BorderLayout.CENTER);
        
        panel.add(rowPanel);
        panel.add(Box.createVerticalStrut(10));
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

    // Custom renderer for Status column
    private static class StatusRenderer extends DefaultTableCellRenderer {
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
            
            String status = value.toString();
            if (status.equalsIgnoreCase("Available")) {
                label.setBackground(new Color(230, 255, 250)); // #e6fffa
                label.setForeground(new Color(13, 110, 253)); // #0d6efd
            } else if (status.equalsIgnoreCase("Reserved")) {
                label.setBackground(new Color(255, 250, 230));
                label.setForeground(new Color(255, 153, 0));
            } else {
                label.setBackground(new Color(255, 236, 236)); // #ffecec
                label.setForeground(new Color(220, 53, 69)); // #dc3545
            }
            label.setBorder(BorderFactory.createLineBorder(label.getBackground()));
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

    private void showCarTypeFilterDialog() {
        JDialog dialog = new JDialog(this, "Select Car Types", true);
        dialog.setSize(350, 300);
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
        cancelButton.addActionListener(e -> dialog.dispose());
        
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
            } else {
                carTypeFilterLabel.setText("Filter: " + String.join(", ", selectedTypes));
            }
            
            // Close dialog
            dialog.dispose();
        });
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(applyButton);
        
        mainPanel.add(buttonPanel);
        
        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.setVisible(true);}}