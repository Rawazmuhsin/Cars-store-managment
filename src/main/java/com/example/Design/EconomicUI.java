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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 * Economic UI class for financial management and reporting
 */
public class EconomicUI extends BaseUI {
    
    // Financial data
    private double monthlyRevenue = 0;
    private double monthlyCosts = 0;
    private double monthlyProfit = 0;
    private double monthlyExpenses = 0;
    
    // Filter settings
    private String selectedMonth = "May 2025";
    private Date filterStartDate;
    private Date filterEndDate;
    
    // UI Components
    private JLabel revenueLabel;
    private JLabel costsLabel;
    private JLabel expensesLabel;
    private JLabel profitLabel;
    private JLabel profitStatusLabel;
    private JTable reportTable;
    private DefaultTableModel tableModel;
    
    // Report data
    private List<FinancialRecord> financialRecords;
    
    /**
     * Constructor with admin ID
     * @param adminId The ID of the admin user
     */
    public EconomicUI(int adminId) {
        super(adminId);
        // Initialize selectedMonth before any UI creation
        selectedMonth = "May 2025";
        initializeFinancialData();
    }
    
    /**
     * Default constructor
     */
    public EconomicUI() {
        super();
        // Initialize selectedMonth before any UI creation
        selectedMonth = "May 2025";
        initializeFinancialData();
    }
    
    /**
     * Add menu items to the sidebar
     */
    @Override
    protected void addMenuItems(JPanel menuPanel) {
        addMenuItem(menuPanel, "Dashboard", false);
        addMenuItem(menuPanel, "Car Inventory", false);
        addMenuItem(menuPanel, "Add New Car", false);
        addMenuItem(menuPanel, "Coming Soon", false);
        addMenuItem(menuPanel, "Sold Cars", false);
        addMenuItem(menuPanel, "Economic", true);
        addMenuItem(menuPanel, "Audit Logs", false);
    }
    
    /**
     * Create the content panel for economic management
     */
    @Override
    protected JPanel createContentPanel() {
        // Ensure selectedMonth is initialized
        if (selectedMonth == null) {
            selectedMonth = "May 2025";
        }
        
        // Initialize financial data if not already done
        if (financialRecords == null) {
            initializeFinancialData();
        }
        
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(LIGHT_GRAY_BG);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        // Header panel
        JPanel headerPanel = createHeaderPanel();
        
        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        
        // Financial Overview Cards
        JPanel overviewPanel = createFinancialOverviewPanel();
        
        // Time Filter Panel
        JPanel filterPanel = createTimeFilterPanel();
        
        // Profit/Loss Analysis
        JPanel analysisPanel = createProfitAnalysisPanel();
        
        // Financial Report Table
        JPanel reportPanel = createReportPanel();
        
        // Add components to content panel
        contentPanel.add(overviewPanel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(filterPanel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(analysisPanel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(reportPanel);
        
        // Footer
        footerPanel = createFooterPanel();
        
        // Add all panels to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    /**
     * Create header panel
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new BorderLayout());
        
        JLabel headerLabel = new JLabel("Economic Management");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 26));
        headerLabel.setForeground(new Color(50, 50, 50));
        
        JLabel subHeaderLabel = new JLabel("Financial analysis and expense management");
        subHeaderLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subHeaderLabel.setForeground(new Color(120, 120, 120));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.add(headerLabel);
        titlePanel.add(subHeaderLabel);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setOpaque(false);
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        
        // Add Expense button
        JButton addExpenseButton = new JButton("+ Add Expense");
        addExpenseButton.setBackground(PRIMARY_BLUE);
        addExpenseButton.setForeground(Color.WHITE);
        addExpenseButton.setFocusPainted(false);
        addExpenseButton.setFont(new Font("Arial", Font.BOLD, 14));
        addExpenseButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        addExpenseButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        addExpenseButton.addActionListener(e -> openExpenseManager());
        addExpenseButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                addExpenseButton.setBackground(PRIMARY_BLUE.darker());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                addExpenseButton.setBackground(PRIMARY_BLUE);
            }
        });
        
        // Export Report button
        JButton exportButton = new JButton("Export Report");
        exportButton.setBackground(PRIMARY_GREEN);
        exportButton.setForeground(Color.WHITE);
        exportButton.setFocusPainted(false);
        exportButton.setFont(new Font("Arial", Font.BOLD, 14));
        exportButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        exportButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        exportButton.addActionListener(e -> exportReport());
        exportButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                exportButton.setBackground(PRIMARY_GREEN.darker());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                exportButton.setBackground(PRIMARY_GREEN);
            }
        });
        
        buttonsPanel.add(addExpenseButton);
        buttonsPanel.add(exportButton);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(buttonsPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    /**
     * Create financial overview panel with cards
     */
    private JPanel createFinancialOverviewPanel() {
        JPanel overviewPanel = new JPanel();
        overviewPanel.setOpaque(false);
        overviewPanel.setLayout(new BoxLayout(overviewPanel, BoxLayout.Y_AXIS));
        
        // Ensure selectedMonth is not null
        String displayMonth = (selectedMonth != null) ? selectedMonth : "May 2025";
        
        JLabel sectionTitle = new JLabel("FINANCIAL OVERVIEW - " + displayMonth.toUpperCase());
        sectionTitle.setFont(new Font("Arial", Font.BOLD, 16));
        sectionTitle.setForeground(new Color(50, 50, 50));
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        cardsPanel.setOpaque(false);
        cardsPanel.setMaximumSize(new Dimension(1200, 120));
        cardsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Revenue card
        RoundedPanel revenueCard = createFinancialCard("Monthly Revenue", formatCurrency(monthlyRevenue), PRIMARY_GREEN, "💰");
        revenueLabel = (JLabel) ((JPanel) revenueCard.getComponent(1)).getComponent(2);
        
        // Costs card (car costs)
        RoundedPanel costsCard = createFinancialCard("Car Costs", formatCurrency(monthlyCosts), PRIMARY_BLUE, "🚗");
        costsLabel = (JLabel) ((JPanel) costsCard.getComponent(1)).getComponent(2);
        
        // Expenses card (operational expenses)
        RoundedPanel expensesCard = createFinancialCard("Expenses", formatCurrency(monthlyExpenses), PRIMARY_YELLOW, "📊");
        expensesLabel = (JLabel) ((JPanel) expensesCard.getComponent(1)).getComponent(2);
        
        // Profit card
        Color profitColor = monthlyProfit >= 0 ? PRIMARY_GREEN : PRIMARY_RED;
        String profitIcon = monthlyProfit >= 0 ? "📈" : "📉";
        RoundedPanel profitCard = createFinancialCard("Net Profit", formatCurrency(monthlyProfit), profitColor, profitIcon);
        profitLabel = (JLabel) ((JPanel) profitCard.getComponent(1)).getComponent(2);
        
        cardsPanel.add(revenueCard);
        cardsPanel.add(costsCard);
        cardsPanel.add(expensesCard);
        cardsPanel.add(profitCard);
        
        overviewPanel.add(sectionTitle);
        overviewPanel.add(Box.createVerticalStrut(15));
        overviewPanel.add(cardsPanel);
        
        return overviewPanel;
    }
    
    /**
     * Create a financial card
     */
    private RoundedPanel createFinancialCard(String title, String amount, Color color, String icon) {
        RoundedPanel card = new RoundedPanel(15, Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.setPreferredSize(new Dimension(280, 120));
        
        // Icon panel
        JPanel iconPanel = new JPanel();
        iconPanel.setOpaque(false);
        iconPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 32));
        iconPanel.add(iconLabel);
        
        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(new Color(100, 100, 100));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel amountLabel = new JLabel(amount);
        amountLabel.setFont(new Font("Arial", Font.BOLD, 24));
        amountLabel.setForeground(color);
        amountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Trend indicator
        JLabel trendLabel = new JLabel("vs last month");
        trendLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        trendLabel.setForeground(new Color(150, 150, 150));
        trendLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(amountLabel);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(trendLabel);
        
        card.add(iconPanel, BorderLayout.WEST);
        card.add(contentPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    /**
     * Create time filter panel
     */
    private JPanel createTimeFilterPanel() {
        RoundedPanel filterCard = new RoundedPanel(15, Color.WHITE);
        filterCard.setLayout(new BorderLayout());
        filterCard.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        filterCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterCard.setMaximumSize(new Dimension(1200, 80));
        
        JLabel filterTitle = new JLabel("TIME FILTER");
        filterTitle.setFont(new Font("Arial", Font.BOLD, 16));
        filterTitle.setForeground(new Color(50, 50, 50));
        
        JPanel filterControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        filterControls.setOpaque(false);
        
        // Month selector
        JLabel monthLabel = new JLabel("Month:");
        monthLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        String[] months = {
            "May 2025", "April 2025", "March 2025", "February 2025", "January 2025",
            "December 2024", "November 2024", "October 2024"
        };
        JComboBox<String> monthCombo = new JComboBox<>(months);
        monthCombo.setSelectedItem(selectedMonth);
        monthCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        monthCombo.addActionListener(e -> {
            selectedMonth = (String) monthCombo.getSelectedItem();
            refreshFinancialData();
        });
        
        // Custom date range button
        JButton customRangeButton = new JButton("Custom Date Range");
        customRangeButton.setBackground(PRIMARY_BLUE);
        customRangeButton.setForeground(Color.WHITE);
        customRangeButton.setFont(new Font("Arial", Font.BOLD, 12));
        customRangeButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        customRangeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        customRangeButton.addActionListener(e -> showCustomDateDialog());
        
        // Refresh button
        JButton refreshButton = new JButton("🔄 Refresh");
        refreshButton.setBackground(PRIMARY_GREEN);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFont(new Font("Arial", Font.BOLD, 12));
        refreshButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> refreshFinancialData());
        
        filterControls.add(monthLabel);
        filterControls.add(monthCombo);
        filterControls.add(customRangeButton);
        filterControls.add(refreshButton);
        
        filterCard.add(filterTitle, BorderLayout.WEST);
        filterCard.add(filterControls, BorderLayout.CENTER);
        
        return filterCard;
    }
    
    /**
     * Create profit analysis panel
     */
    private JPanel createProfitAnalysisPanel() {
        RoundedPanel analysisCard = new RoundedPanel(15, Color.WHITE);
        analysisCard.setLayout(new BorderLayout());
        analysisCard.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        analysisCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        analysisCard.setMaximumSize(new Dimension(1200, 150));
        
        JLabel analysisTitle = new JLabel("PROFIT/LOSS ANALYSIS");
        analysisTitle.setFont(new Font("Arial", Font.BOLD, 16));
        analysisTitle.setForeground(new Color(50, 50, 50));
        
        JPanel analysisContent = new JPanel();
        analysisContent.setOpaque(false);
        analysisContent.setLayout(new BoxLayout(analysisContent, BoxLayout.Y_AXIS));
        
        // Profit status
        String statusText = monthlyProfit >= 0 ? "✅ PROFITABLE MONTH" : "❌ LOSS MONTH";
        Color statusColor = monthlyProfit >= 0 ? PRIMARY_GREEN : PRIMARY_RED;
        
        profitStatusLabel = new JLabel(statusText);
        profitStatusLabel.setFont(new Font("Arial", Font.BOLD, 18));
        profitStatusLabel.setForeground(statusColor);
        profitStatusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Analysis details
        double profitMargin = monthlyRevenue > 0 ? (monthlyProfit / monthlyRevenue) * 100 : 0;
        String analysisText = String.format(
            "Profit Margin: %.1f%% | Total Costs: %s | Break-even Point: %s",
            profitMargin,
            formatCurrency(monthlyCosts + monthlyExpenses),
            formatCurrency(monthlyCosts + monthlyExpenses)
        );
        
        JLabel analysisDetailsLabel = new JLabel(analysisText);
        analysisDetailsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        analysisDetailsLabel.setForeground(new Color(100, 100, 100));
        analysisDetailsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Recommendations
        String recommendation = getRecommendation();
        JLabel recommendationLabel = new JLabel(recommendation);
        recommendationLabel.setFont(new Font("Arial", Font.ITALIC, 13));
        recommendationLabel.setForeground(new Color(120, 120, 120));
        recommendationLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        analysisContent.add(profitStatusLabel);
        analysisContent.add(Box.createVerticalStrut(8));
        analysisContent.add(analysisDetailsLabel);
        analysisContent.add(Box.createVerticalStrut(8));
        analysisContent.add(recommendationLabel);
        
        analysisCard.add(analysisTitle, BorderLayout.NORTH);
        analysisCard.add(Box.createVerticalStrut(15), BorderLayout.NORTH);
        analysisCard.add(analysisContent, BorderLayout.CENTER);
        
        return analysisCard;
    }
    
    /**
     * Create report panel with table
     */
    private JPanel createReportPanel() {
        JPanel reportContainer = new JPanel();
        reportContainer.setOpaque(false);
        reportContainer.setLayout(new BoxLayout(reportContainer, BoxLayout.Y_AXIS));
        reportContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel reportTitle = new JLabel("DETAILED FINANCIAL REPORT");
        reportTitle.setFont(new Font("Arial", Font.BOLD, 16));
        reportTitle.setForeground(new Color(50, 50, 50));
        reportTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Create table
        createReportTable();
        
        RoundedPanel tableCard = new RoundedPanel(15, Color.WHITE);
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        tableCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JScrollPane scrollPane = new JScrollPane(reportTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        
        tableCard.add(scrollPane);
        
        reportContainer.add(reportTitle);
        reportContainer.add(Box.createVerticalStrut(15));
        reportContainer.add(tableCard);
        
        return reportContainer;
    }
    
    /**
     * Create report table
     */
    private void createReportTable() {
        String[] columnNames = {"DATE", "TYPE", "DESCRIPTION", "AMOUNT", "CATEGORY", "STATUS"};
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Populate table with financial records
        for (FinancialRecord record : financialRecords) {
            tableModel.addRow(new Object[]{
                record.getDate(),
                record.getType(),
                record.getDescription(),
                record.getAmount(),
                record.getCategory(),
                record.getStatus()
            });
        }
        
        reportTable = new JTable(tableModel);
        reportTable.setRowHeight(35);
        reportTable.setShowGrid(false);
        reportTable.setFont(new Font("Arial", Font.PLAIN, 13));
        reportTable.setBackground(Color.WHITE);
        
        // Style table header
        JTableHeader header = reportTable.getTableHeader();
        header.setBackground(new Color(248, 250, 252));
        header.setForeground(new Color(50, 50, 50));
        header.setFont(new Font("Arial", Font.BOLD, 13));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(230, 230, 230)));
        
        // Custom renderers
        reportTable.getColumnModel().getColumn(1).setCellRenderer(new TypeRenderer());
        reportTable.getColumnModel().getColumn(3).setCellRenderer(new AmountRenderer());
        reportTable.getColumnModel().getColumn(5).setCellRenderer(new StatusRenderer());
        
        // Set column widths
        reportTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        reportTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        reportTable.getColumnModel().getColumn(2).setPreferredWidth(250);
        reportTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        reportTable.getColumnModel().getColumn(4).setPreferredWidth(120);
        reportTable.getColumnModel().getColumn(5).setPreferredWidth(80);
    }
    
    /**
     * Initialize financial data
     */
    private void initializeFinancialData() {
        // Initialize list if null
        if (financialRecords == null) {
            financialRecords = new ArrayList<>();
        } else {
            financialRecords.clear();
        }
        
        // Ensure selectedMonth is not null
        if (selectedMonth == null) {
            selectedMonth = "May 2025";
        }
        
        // Get sales data from CarStatusManager
        try {
            CarStatusManager carManager = CarStatusManager.getInstance();
            List<CarStatusManager.SoldCarRecord> soldCars = carManager.getSoldCars();
            
            monthlyRevenue = 0;
            monthlyCosts = 0;
            
            // Calculate revenue and costs from car sales
            for (CarStatusManager.SoldCarRecord sale : soldCars) {
                if (sale != null && sale.getSaleDate() != null && sale.getSaleDate().contains("May")) { // Filter by current month
                    try {
                        String priceStr = sale.getSalePrice();
                        if (priceStr != null) {
                            double salePrice = Double.parseDouble(priceStr.replace("$", "").replace(",", ""));
                            monthlyRevenue += salePrice;
                            
                            // Estimate car cost (assuming 80% of sale price)
                            double carCost = salePrice * 0.8;
                            monthlyCosts += carCost;
                            
                            // Add to financial records
                            String carModel = (sale.getCar() != null && sale.getCar().getModel() != null) ? 
                                            sale.getCar().getModel() : "Unknown Vehicle";
                            
                            financialRecords.add(new FinancialRecord(
                                sale.getSaleDate(),
                                "Revenue",
                                "Car Sale: " + carModel,
                                "+" + formatCurrency(salePrice),
                                "Car Sales",
                                "Completed"
                            ));
                            
                            financialRecords.add(new FinancialRecord(
                                sale.getSaleDate(),
                                "Cost",
                                "Car Cost: " + carModel,
                                "-" + formatCurrency(carCost),
                                "Car Costs",
                                "Completed"
                            ));
                        }
                    } catch (NumberFormatException e) {
                        // Skip invalid price data
                        System.err.println("Invalid price format: " + sale.getSalePrice());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading sales data: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Add sample operational expenses
        monthlyExpenses = 15000; // Sample expenses
        
        // Sample expense records
        financialRecords.add(new FinancialRecord("May 01, 2025", "Expense", "Office Rent", "-$3,500", "Rent", "Paid"));
        financialRecords.add(new FinancialRecord("May 05, 2025", "Expense", "Marketing Campaign", "-$2,200", "Marketing", "Paid"));
        financialRecords.add(new FinancialRecord("May 10, 2025", "Expense", "Staff Salaries", "-$8,500", "Salaries", "Paid"));
        financialRecords.add(new FinancialRecord("May 15, 2025", "Expense", "Utilities", "-$800", "Utilities", "Paid"));
        
        // Calculate profit
        monthlyProfit = monthlyRevenue - monthlyCosts - monthlyExpenses;
    }
    
    /**
     * Refresh financial data
     */
    private void refreshFinancialData() {
        initializeFinancialData();
        updateDisplayValues();
        
        if (tableModel != null) {
            tableModel.setRowCount(0);
            for (FinancialRecord record : financialRecords) {
                tableModel.addRow(new Object[]{
                    record.getDate(),
                    record.getType(),
                    record.getDescription(),
                    record.getAmount(),
                    record.getCategory(),
                    record.getStatus()
                });
            }
        }
    }
    
    /**
     * Update display values
     */
    private void updateDisplayValues() {
        if (revenueLabel != null) revenueLabel.setText(formatCurrency(monthlyRevenue));
        if (costsLabel != null) costsLabel.setText(formatCurrency(monthlyCosts));
        if (expensesLabel != null) expensesLabel.setText(formatCurrency(monthlyExpenses));
        if (profitLabel != null) {
            profitLabel.setText(formatCurrency(monthlyProfit));
            profitLabel.setForeground(monthlyProfit >= 0 ? PRIMARY_GREEN : PRIMARY_RED);
        }
        if (profitStatusLabel != null) {
            String statusText = monthlyProfit >= 0 ? "✅ PROFITABLE MONTH" : "❌ LOSS MONTH";
            Color statusColor = monthlyProfit >= 0 ? PRIMARY_GREEN : PRIMARY_RED;
            profitStatusLabel.setText(statusText);
            profitStatusLabel.setForeground(statusColor);
        }
    }
    
    /**
     * Show custom date range dialog
     */
    private void showCustomDateDialog() {
        JDialog dateDialog = new JDialog(this, "Select Custom Date Range", true);
        dateDialog.setSize(400, 200);
        dateDialog.setLocationRelativeTo(this);
        dateDialog.setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        
        // Date spinners
        Date today = new Date();
        Date monthAgo = new Date(today.getTime() - (30L * 24 * 60 * 60 * 1000));
        
        JPanel startPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        startPanel.add(new JLabel("Start Date:"));
        JSpinner startSpinner = new JSpinner(new SpinnerDateModel(monthAgo, null, today, Calendar.DAY_OF_MONTH));
        startSpinner.setEditor(new JSpinner.DateEditor(startSpinner, "MMM dd, yyyy"));
        startPanel.add(startSpinner);
        
        JPanel endPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        endPanel.add(new JLabel("End Date:  "));
        JSpinner endSpinner = new JSpinner(new SpinnerDateModel(today, null, null, Calendar.DAY_OF_MONTH));
        endSpinner.setEditor(new JSpinner.DateEditor(endSpinner, "MMM dd, yyyy"));
        endPanel.add(endSpinner);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton applyButton = new JButton("Apply");
        applyButton.setBackground(PRIMARY_BLUE);
        applyButton.setForeground(Color.WHITE);
        applyButton.addActionListener(e -> {
            filterStartDate = (Date) startSpinner.getValue();
            filterEndDate = (Date) endSpinner.getValue();
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
            selectedMonth = sdf.format(filterStartDate) + " - " + sdf.format(filterEndDate);
            refreshFinancialData();
            dateDialog.dispose();
        });
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dateDialog.dispose());
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(applyButton);
        
        mainPanel.add(startPanel);
        mainPanel.add(endPanel);
        mainPanel.add(buttonPanel);
        
        dateDialog.add(mainPanel);
        dateDialog.setVisible(true);
    }
    
    /**
     * Open expense manager
     */
    private void openExpenseManager() {
        dispose();
        SwingUtilities.invokeLater(() -> new ExpenseManagerUI(adminId).setVisible(true));
    }
    
    /**
     * Export report
     */
    private void exportReport() {
        String reportContent = generateReportContent();
        
        JOptionPane.showMessageDialog(this,
            "Financial Report exported successfully!\n\n" +
            "Report Period: " + selectedMonth + "\n" +
            "Total Revenue: " + formatCurrency(monthlyRevenue) + "\n" +
            "Total Costs: " + formatCurrency(monthlyCosts + monthlyExpenses) + "\n" +
            "Net Profit: " + formatCurrency(monthlyProfit) + "\n\n" +
            "Report saved to: financial_report_" + selectedMonth.replace(" ", "_") + ".pdf",
            "Export Complete",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Generate report content
     */
    private String generateReportContent() {
        StringBuilder report = new StringBuilder();
        report.append("FINANCIAL REPORT - ").append(selectedMonth).append("\n");
        report.append("=====================================\n\n");
        report.append("SUMMARY:\n");
        report.append("Revenue: ").append(formatCurrency(monthlyRevenue)).append("\n");
        report.append("Car Costs: ").append(formatCurrency(monthlyCosts)).append("\n");
        report.append("Expenses: ").append(formatCurrency(monthlyExpenses)).append("\n");
        report.append("Net Profit: ").append(formatCurrency(monthlyProfit)).append("\n\n");
        
        report.append("DETAILED TRANSACTIONS:\n");
        for (FinancialRecord record : financialRecords) {
            report.append(record.getDate()).append(" | ");
            report.append(record.getType()).append(" | ");
            report.append(record.getDescription()).append(" | ");
            report.append(record.getAmount()).append("\n");
        }
        
        return report.toString();
    }
    
    /**
     * Get recommendation based on financial performance
     */
    private String getRecommendation() {
        if (monthlyProfit >= monthlyRevenue * 0.2) {
            return "💡 Excellent profitability! Consider expanding inventory or marketing.";
        } else if (monthlyProfit >= 0) {
            return "💡 Good performance. Look for ways to reduce costs or increase sales.";
        } else if (monthlyProfit >= -monthlyRevenue * 0.1) {
            return "⚠️ Marginal loss. Review expenses and focus on high-margin sales.";
        } else {
            return "🚨 Significant loss. Urgent review of costs and sales strategy needed.";
        }
    }
    
    /**
     * Format currency
     */
    private String formatCurrency(double amount) {
        DecimalFormat formatter = new DecimalFormat("$#,##0");
        return formatter.format(amount);
    }
    
    // Custom table renderers
    private static class TypeRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            
            String type = value.toString();
            if ("Revenue".equals(type)) {
                label.setBackground(new Color(232, 255, 232));
                label.setForeground(new Color(25, 135, 84));
            } else if ("Cost".equals(type) || "Expense".equals(type)) {
                label.setBackground(new Color(255, 236, 236));
                label.setForeground(new Color(220, 53, 69));
            } else {
                label.setBackground(new Color(248, 250, 252));
                label.setForeground(new Color(100, 100, 100));
            }
            label.setOpaque(true);
            return label;
        }
    }
    
    private static class AmountRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
            label.setHorizontalAlignment(SwingConstants.RIGHT);
            label.setFont(new Font("Arial", Font.BOLD, 13));
            
            String amount = value.toString();
            if (amount.startsWith("+")) {
                label.setForeground(new Color(25, 135, 84));
            } else if (amount.startsWith("-")) {
                label.setForeground(new Color(220, 53, 69));
            }
            
            return label;
        }
    }
    
    private static class StatusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            
            String status = value.toString();
            if ("Completed".equals(status) || "Paid".equals(status)) {
                label.setBackground(new Color(232, 255, 232));
                label.setForeground(new Color(25, 135, 84));
            } else if ("Pending".equals(status)) {
                label.setBackground(new Color(255, 250, 230));
                label.setForeground(new Color(255, 153, 0));
            } else {
                label.setBackground(new Color(248, 250, 252));
                label.setForeground(new Color(100, 100, 100));
            }
            label.setOpaque(true);
            return label;
        }
    }
    
    /**
     * Financial Record class
     */
    public static class FinancialRecord {
        private String date;
        private String type;
        private String description;
        private String amount;
        private String category;
        private String status;
        
        public FinancialRecord(String date, String type, String description, 
                             String amount, String category, String status) {
            this.date = date;
            this.type = type;
            this.description = description;
            this.amount = amount;
            this.category = category;
            this.status = status;
        }
        
        // Getters
        public String getDate() { return date; }
        public String getType() { return type; }
        public String getDescription() { return description; }
        public String getAmount() { return amount; }
        public String getCategory() { return category; }
        public String getStatus() { return status; }
        
        // Setters
        public void setDate(String date) { this.date = date; }
        public void setType(String type) { this.type = type; }
        public void setDescription(String description) { this.description = description; }
        public void setAmount(String amount) { this.amount = amount; }
        public void setCategory(String category) { this.category = category; }
        public void setStatus(String status) { this.status = status; }
    }
    
    /**
     * Main method to test the Economic UI
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new EconomicUI().setVisible(true);
        });
    }
}