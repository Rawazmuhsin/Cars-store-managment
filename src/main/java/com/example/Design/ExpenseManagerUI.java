package com.example.Design;

import javax.swing.SwingUtilities;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 * Expense Manager UI for recording and managing business expenses
 */
public class ExpenseManagerUI extends BaseUI {
    
    // Form components
    private JTextField descriptionField;
    private JSpinner amountSpinner;
    private JSpinner dateSpinner;
    private JComboBox<String> categoryCombo;
    private JComboBox<String> statusCombo;
    private JTextArea notesArea;
    
    // Table components
    private JTable expensesTable;
    private DefaultTableModel tableModel;
    
    // Data
    private List<Expense> expenses;
    private double totalExpenses = 0;
    private double monthlyBudget = 20000; // $20,000 monthly budget
    
    /**
     * Constructor with admin ID
     */
    public ExpenseManagerUI(int adminId) {
        super(adminId);
    }
    
    /**
     * Default constructor
     */
    public ExpenseManagerUI() {
        super();
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
     * Create the content panel
     */
    @Override
    protected JPanel createContentPanel() {
        // Initialize data
        initializeExpenseData();
        
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
        
        // Budget overview
        JPanel budgetPanel = createBudgetOverviewPanel();
        
        // Add expense form
        JPanel formPanel = createExpenseFormPanel();
        
        // Expenses table
        JPanel tablePanel = createExpensesTablePanel();
        
        // Add components
        contentPanel.add(budgetPanel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(formPanel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(tablePanel);
        
        // Footer
        footerPanel = createFooterPanel();
        
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
        
        JLabel headerLabel = new JLabel("Expense Manager");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 26));
        headerLabel.setForeground(new Color(50, 50, 50));
        
        JLabel subHeaderLabel = new JLabel("Record and track business expenses");
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
        
        // Back to Economic button
        JButton backButton = new JButton("← Back to Economic");
        backButton.setBackground(new Color(108, 117, 125));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        backButton.addActionListener(e -> goBackToEconomic());
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
        
        // Export Expenses button
        JButton exportButton = new JButton("Export Expenses");
        exportButton.setBackground(PRIMARY_GREEN);
        exportButton.setForeground(Color.WHITE);
        exportButton.setFocusPainted(false);
        exportButton.setFont(new Font("Arial", Font.BOLD, 14));
        exportButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        exportButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        exportButton.addActionListener(e -> exportExpenses());
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
        
        buttonsPanel.add(backButton);
        buttonsPanel.add(exportButton);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(buttonsPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    /**
     * Create budget overview panel
     */
    private JPanel createBudgetOverviewPanel() {
        JPanel overviewPanel = new JPanel();
        overviewPanel.setOpaque(false);
        overviewPanel.setLayout(new BoxLayout(overviewPanel, BoxLayout.Y_AXIS));
        
        JLabel sectionTitle = new JLabel("EXPENSE OVERVIEW - MAY 2025");
        sectionTitle.setFont(new Font("Arial", Font.BOLD, 16));
        sectionTitle.setForeground(new Color(50, 50, 50));
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        cardsPanel.setOpaque(false);
        cardsPanel.setMaximumSize(new Dimension(1200, 120));
        cardsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Calculate budget usage
        double budgetUsed = (totalExpenses / monthlyBudget) * 100;
        double remaining = monthlyBudget - totalExpenses;
        
        // Budget cards
        RoundedPanel totalCard = createBudgetCard("Total Expenses", formatCurrency(totalExpenses), PRIMARY_RED, "💸");
        RoundedPanel budgetCard = createBudgetCard("Monthly Budget", formatCurrency(monthlyBudget), PRIMARY_BLUE, "💰");
        RoundedPanel remainingCard = createBudgetCard("Remaining", formatCurrency(remaining), 
                                                    remaining >= 0 ? PRIMARY_GREEN : PRIMARY_RED, "📊");
        RoundedPanel usageCard = createBudgetCard("Budget Used", String.format("%.1f%%", budgetUsed), 
                                                budgetUsed <= 80 ? PRIMARY_GREEN : PRIMARY_YELLOW, "📈");
        
        cardsPanel.add(totalCard);
        cardsPanel.add(budgetCard);
        cardsPanel.add(remainingCard);
        cardsPanel.add(usageCard);
        
        overviewPanel.add(sectionTitle);
        overviewPanel.add(Box.createVerticalStrut(15));
        overviewPanel.add(cardsPanel);
        
        return overviewPanel;
    }
    
    /**
     * Create budget card
     */
    private RoundedPanel createBudgetCard(String title, String amount, Color color, String icon) {
        RoundedPanel card = new RoundedPanel(15, Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.setPreferredSize(new Dimension(280, 120));
        
        // Icon
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 32));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Content
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(new Color(100, 100, 100));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel amountLabel = new JLabel(amount);
        amountLabel.setFont(new Font("Arial", Font.BOLD, 20));
        amountLabel.setForeground(color);
        amountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(8));
        contentPanel.add(amountLabel);
        
        card.add(iconLabel, BorderLayout.WEST);
        card.add(contentPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    /**
     * Create expense form panel
     */
    private JPanel createExpenseFormPanel() {
        RoundedPanel formCard = new RoundedPanel(15, Color.WHITE);
        formCard.setLayout(new BorderLayout());
        formCard.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        formCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.setMaximumSize(new Dimension(1200, 300));
        
        JLabel formTitle = new JLabel("ADD NEW EXPENSE");
        formTitle.setFont(new Font("Arial", Font.BOLD, 18));
        formTitle.setForeground(new Color(50, 50, 50));
        
        JPanel formContent = new JPanel(new GridBagLayout());
        formContent.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Description field
        gbc.gridx = 0; gbc.gridy = 0;
        formContent.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        descriptionField = createStyledTextField();
        formContent.add(descriptionField, gbc);
        
        // Amount field
        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formContent.add(new JLabel("Amount:"), gbc);
        gbc.gridx = 3;
        amountSpinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 100000.0, 1.0));
        amountSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        amountSpinner.setPreferredSize(new Dimension(120, 32));
        formContent.add(amountSpinner, gbc);
        
        // Date field
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formContent.add(new JLabel("Date:"), gbc);
        gbc.gridx = 1;
        dateSpinner = new JSpinner(new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH));
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "MMM dd, yyyy"));
        dateSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        dateSpinner.setPreferredSize(new Dimension(150, 32));
        formContent.add(dateSpinner, gbc);
        
        // Category field
        gbc.gridx = 2;
        formContent.add(new JLabel("Category:"), gbc);
        gbc.gridx = 3;
        String[] categories = {"Rent", "Utilities", "Marketing", "Salaries", "Maintenance", "Insurance", 
                              "Office Supplies", "Travel", "Legal", "Other"};
        categoryCombo = new JComboBox<>(categories);
        categoryCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        categoryCombo.setPreferredSize(new Dimension(120, 32));
        formContent.add(categoryCombo, gbc);
        
        // Status field
        gbc.gridx = 0; gbc.gridy = 2;
        formContent.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        String[] statuses = {"Pending", "Paid", "Overdue"};
        statusCombo = new JComboBox<>(statuses);
        statusCombo.setSelectedItem("Paid");
        statusCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        statusCombo.setPreferredSize(new Dimension(150, 32));
        formContent.add(statusCombo, gbc);
        
        // Notes field
        gbc.gridx = 2; gbc.gridwidth = 2;
        formContent.add(new JLabel("Notes:"), gbc);
        gbc.gridx = 2; gbc.gridy = 3; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0; gbc.weighty = 1.0;
        notesArea = new JTextArea(3, 30);
        notesArea.setFont(new Font("Arial", Font.PLAIN, 14));
        notesArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        JScrollPane notesScroll = new JScrollPane(notesArea);
        notesScroll.setPreferredSize(new Dimension(300, 80));
        formContent.add(notesScroll, gbc);
        
        // Buttons
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 4; gbc.fill = GridBagConstraints.NONE; 
        gbc.weightx = 0; gbc.weighty = 0; gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = createFormButtonsPanel();
        formContent.add(buttonPanel, gbc);
        
        formCard.add(formTitle, BorderLayout.NORTH);
        formCard.add(Box.createVerticalStrut(20), BorderLayout.NORTH);
        formCard.add(formContent, BorderLayout.CENTER);
        
        return formCard;
    }
    
    /**
     * Create styled text field
     */
    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setPreferredSize(new Dimension(200, 32));
        return field;
    }
    
    /**
     * Create form buttons panel
     */
    private JPanel createFormButtonsPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);
        
        JButton clearButton = new JButton("Clear Form");
        clearButton.setBackground(new Color(108, 117, 125));
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        clearButton.setFont(new Font("Arial", Font.BOLD, 14));
        clearButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        clearButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearButton.addActionListener(e -> clearForm());
        
        JButton addButton = new JButton("Add Expense");
        addButton.setBackground(PRIMARY_GREEN);
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.addActionListener(e -> addExpense());
        
        buttonPanel.add(clearButton);
        buttonPanel.add(addButton);
        
        return buttonPanel;
    }
    
    /**
     * Create expenses table panel
     */
    private JPanel createExpensesTablePanel() {
        JPanel tableContainer = new JPanel();
        tableContainer.setOpaque(false);
        tableContainer.setLayout(new BoxLayout(tableContainer, BoxLayout.Y_AXIS));
        tableContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel tableTitle = new JLabel("EXPENSE RECORDS");
        tableTitle.setFont(new Font("Arial", Font.BOLD, 16));
        tableTitle.setForeground(new Color(50, 50, 50));
        tableTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Create table
        createExpensesTable();
        
        RoundedPanel tableCard = new RoundedPanel(15, Color.WHITE);
        tableCard.setLayout(new BorderLayout());
        tableCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JScrollPane scrollPane = new JScrollPane(expensesTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        
        tableCard.add(scrollPane);
        
        tableContainer.add(tableTitle);
        tableContainer.add(Box.createVerticalStrut(15));
        tableContainer.add(tableCard);
        
        return tableContainer;
    }
    
    /**
     * Create expenses table
     */
    private void createExpensesTable() {
        String[] columnNames = {"DATE", "DESCRIPTION", "AMOUNT", "CATEGORY", "STATUS", "ACTIONS"};
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only actions column
            }
        };
        
        // Populate with existing expenses
        for (Expense expense : expenses) {
            tableModel.addRow(new Object[]{
                expense.getDate(),
                expense.getDescription(),
                "-" + formatCurrency(expense.getAmount()),
                expense.getCategory(),
                expense.getStatus(),
                "Edit/Delete"
            });
        }
        
        expensesTable = new JTable(tableModel);
        expensesTable.setRowHeight(40);
        expensesTable.setShowGrid(false);
        expensesTable.setFont(new Font("Arial", Font.PLAIN, 13));
        expensesTable.setBackground(Color.WHITE);
        
        // Style header
        JTableHeader header = expensesTable.getTableHeader();
        header.setBackground(new Color(248, 250, 252));
        header.setForeground(new Color(50, 50, 50));
        header.setFont(new Font("Arial", Font.BOLD, 13));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(230, 230, 230)));
        
        // Custom renderers
        expensesTable.getColumnModel().getColumn(2).setCellRenderer(new AmountRenderer());
        expensesTable.getColumnModel().getColumn(4).setCellRenderer(new StatusRenderer());
        expensesTable.getColumnModel().getColumn(5).setCellRenderer(new ActionRenderer());
        
        // Set column widths
        expensesTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        expensesTable.getColumnModel().getColumn(1).setPreferredWidth(250);
        expensesTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        expensesTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        expensesTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        expensesTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        
        // Add click listener for actions
        expensesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = expensesTable.rowAtPoint(e.getPoint());
                int col = expensesTable.columnAtPoint(e.getPoint());
                
                if (col == 5 && row >= 0) {
                    showExpenseActions(row);
                }
            }
        });
    }
    
    /**
     * Initialize expense data
     */
    private void initializeExpenseData() {
        expenses = new ArrayList<>();
        
        // Sample expense data
        expenses.add(new Expense("May 01, 2025", "Office Rent", 3500.0, "Rent", "Paid", "Monthly office rent payment"));
        expenses.add(new Expense("May 05, 2025", "Marketing Campaign", 2200.0, "Marketing", "Paid", "Social media advertising"));
        expenses.add(new Expense("May 10, 2025", "Staff Salaries", 8500.0, "Salaries", "Paid", "Monthly employee salaries"));
        expenses.add(new Expense("May 15, 2025", "Utilities", 800.0, "Utilities", "Paid", "Electricity and water bills"));
        expenses.add(new Expense("May 20, 2025", "Office Supplies", 350.0, "Office Supplies", "Paid", "Stationery and equipment"));
        expenses.add(new Expense("May 22, 2025", "Insurance", 1200.0, "Insurance", "Paid", "Business insurance premium"));
        
        // Calculate total
        calculateTotalExpenses();
    }
    
    /**
     * Calculate total expenses
     */
    private void calculateTotalExpenses() {
        totalExpenses = 0;
        for (Expense expense : expenses) {
            if ("Paid".equals(expense.getStatus())) {
                totalExpenses += expense.getAmount();
            }
        }
    }
    
    /**
     * Add new expense
     */
    private void addExpense() {
        // Validate form
        if (!validateForm()) {
            return;
        }
        
        try {
            // Get form values
            String description = descriptionField.getText().trim();
            double amount = (Double) amountSpinner.getValue();
            Date date = (Date) dateSpinner.getValue();
            String category = (String) categoryCombo.getSelectedItem();
            String status = (String) statusCombo.getSelectedItem();
            String notes = notesArea.getText().trim();
            
            // Format date
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
            String dateStr = sdf.format(date);
            
            // Create new expense
            Expense newExpense = new Expense(dateStr, description, amount, category, status, notes);
            expenses.add(newExpense);
            
            // Add to table
            tableModel.addRow(new Object[]{
                dateStr,
                description,
                "-" + formatCurrency(amount),
                category,
                status,
                "Edit/Delete"
            });
            
            // Recalculate totals
            calculateTotalExpenses();
            
            // Show success message
            JOptionPane.showMessageDialog(this,
                "Expense added successfully!\n\n" +
                "Description: " + description + "\n" +
                "Amount: " + formatCurrency(amount) + "\n" +
                "Category: " + category + "\n" +
                "Status: " + status,
                "Expense Added",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Clear form
            clearForm();
            
            // Refresh display (in a real app, this would update the UI)
            refreshDisplay();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error adding expense: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Validate form
     */
    private boolean validateForm() {
        if (descriptionField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a description.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            descriptionField.requestFocus();
            return false;
        }
        
        double amount = (Double) amountSpinner.getValue();
        if (amount <= 0) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount greater than 0.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            amountSpinner.requestFocus();
            return false;
        }
        
        return true;
    }
    
    /**
     * Clear form
     */
    private void clearForm() {
        descriptionField.setText("");
        amountSpinner.setValue(0.0);
        dateSpinner.setValue(new Date());
        categoryCombo.setSelectedIndex(0);
        statusCombo.setSelectedItem("Paid");
        notesArea.setText("");
    }
    
    /**
     * Show expense actions dialog
     */
    private void showExpenseActions(int row) {
        String[] options = {"Edit", "Delete", "Cancel"};
        int choice = JOptionPane.showOptionDialog(this,
            "What would you like to do with this expense?",
            "Expense Actions",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[2]);
        
        switch (choice) {
            case 0: // Edit
                editExpense(row);
                break;
            case 1: // Delete
                deleteExpense(row);
                break;
            default: // Cancel
                break;
        }
    }
    
    /**
     * Edit expense
     */
    private void editExpense(int row) {
        if (row >= 0 && row < expenses.size()) {
            Expense expense = expenses.get(row);
            
            // Fill form with expense data
            descriptionField.setText(expense.getDescription());
            amountSpinner.setValue(expense.getAmount());
            categoryCombo.setSelectedItem(expense.getCategory());
            statusCombo.setSelectedItem(expense.getStatus());
            notesArea.setText(expense.getNotes());
            
            // Remove the old expense
            expenses.remove(row);
            tableModel.removeRow(row);
            calculateTotalExpenses();
            refreshDisplay();
            
            JOptionPane.showMessageDialog(this,
                "Expense loaded for editing. Modify the details and click 'Add Expense' to save changes.",
                "Edit Mode",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Delete expense
     */
    private void deleteExpense(int row) {
        if (row >= 0 && row < expenses.size()) {
            Expense expense = expenses.get(row);
            
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this expense?\n\n" +
                "Description: " + expense.getDescription() + "\n" +
                "Amount: " + formatCurrency(expense.getAmount()),
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                expenses.remove(row);
                tableModel.removeRow(row);
                calculateTotalExpenses();
                refreshDisplay();
                
                JOptionPane.showMessageDialog(this,
                    "Expense deleted successfully.",
                    "Deleted",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    /**
     * Refresh display
     */
    private void refreshDisplay() {
        // In a real application, this would update the budget overview cards
        // For now, just show a message
        System.out.println("Total expenses updated: " + formatCurrency(totalExpenses));
    }
    
    /**
     * Export expenses
     */
    private void exportExpenses() {
        StringBuilder export = new StringBuilder();
        export.append("EXPENSE REPORT - MAY 2025\n");
        export.append("==========================\n\n");
        export.append("Total Expenses: ").append(formatCurrency(totalExpenses)).append("\n");
        export.append("Monthly Budget: ").append(formatCurrency(monthlyBudget)).append("\n");
        export.append("Remaining Budget: ").append(formatCurrency(monthlyBudget - totalExpenses)).append("\n\n");
        
        export.append("DETAILED EXPENSES:\n");
        export.append("Date\t\tDescription\t\tAmount\t\tCategory\t\tStatus\n");
        export.append("----\t\t-----------\t\t------\t\t--------\t\t------\n");
        
        for (Expense expense : expenses) {
            export.append(expense.getDate()).append("\t");
            export.append(expense.getDescription()).append("\t\t");
            export.append(formatCurrency(expense.getAmount())).append("\t\t");
            export.append(expense.getCategory()).append("\t\t");
            export.append(expense.getStatus()).append("\n");
        }
        
        JOptionPane.showMessageDialog(this,
            "Expense report exported successfully!\n\n" +
            "Total Records: " + expenses.size() + "\n" +
            "Total Amount: " + formatCurrency(totalExpenses) + "\n" +
            "Export saved to: expense_report_may_2025.csv",
            "Export Complete",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Go back to Economic UI
     */
    private void goBackToEconomic() {
        dispose();
        SwingUtilities.invokeLater(() -> new EconomicUI(adminId).setVisible(true));
    }
    
    /**
     * Format currency
     */
    private String formatCurrency(double amount) {
        DecimalFormat formatter = new DecimalFormat("$#,##0.00");
        return formatter.format(amount);
    }
    
    // Custom table renderers
    private static class AmountRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
            label.setHorizontalAlignment(SwingConstants.RIGHT);
            label.setFont(new Font("Arial", Font.BOLD, 13));
            label.setForeground(new Color(220, 53, 69)); // Red for expenses
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
            if ("Paid".equals(status)) {
                label.setBackground(new Color(232, 255, 232));
                label.setForeground(new Color(25, 135, 84));
            } else if ("Pending".equals(status)) {
                label.setBackground(new Color(255, 250, 230));
                label.setForeground(new Color(255, 153, 0));
            } else if ("Overdue".equals(status)) {
                label.setBackground(new Color(255, 236, 236));
                label.setForeground(new Color(220, 53, 69));
            }
            label.setOpaque(true);
            return label;
        }
    }
    
    private static class ActionRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 12));
            label.setBackground(new Color(33, 150, 243));
            label.setForeground(Color.WHITE);
            label.setOpaque(true);
            label.setText("Actions");
            return label;
        }
    }
    
    /**
     * Expense class
     */
    public static class Expense {
        private String date;
        private String description;
        private double amount;
        private String category;
        private String status;
        private String notes;
        
        public Expense(String date, String description, double amount, String category, String status, String notes) {
            this.date = date;
            this.description = description;
            this.amount = amount;
            this.category = category;
            this.status = status;
            this.notes = notes;
        }
        
        // Getters
        public String getDate() { return date; }
        public String getDescription() { return description; }
        public double getAmount() { return amount; }
        public String getCategory() { return category; }
        public String getStatus() { return status; }
        public String getNotes() { return notes; }
        
        // Setters
        public void setDate(String date) { this.date = date; }
        public void setDescription(String description) { this.description = description; }
        public void setAmount(double amount) { this.amount = amount; }
        public void setCategory(String category) { this.category = category; }
        public void setStatus(String status) { this.status = status; }
        public void setNotes(String notes) { this.notes = notes; }
    }
    
    /**
     * Main method to test the Expense Manager UI
     */
      public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set look and feel to system look and feel
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // Launch Expense Manager directly
            new ExpenseManagerUI().setVisible(true);
        });
    }
}