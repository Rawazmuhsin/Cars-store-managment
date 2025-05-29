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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 * Audit Log UI for tracking all system activities and user actions
 */
public class AuditLogUI extends BaseUI {
    
    private JTable auditTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> filterTypeCombo;
    private JComboBox<String> filterUserCombo;
    private JLabel totalLogsLabel;
    private JLabel todayLogsLabel;
    
    // Audit log data - Initialize here to avoid null pointer
    private List<AuditLogEntry> auditLogs = new ArrayList<>();
    private static List<AuditLogEntry> globalAuditLogs = new ArrayList<>(); // Static to persist across sessions
    
    /**
     * Constructor with admin ID
     */
    public AuditLogUI(int adminId) {
        super(adminId);
        initializeAuditData();
    }
    
    /**
     * Default constructor
     */
    public AuditLogUI() {
        super();
        initializeAuditData();
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
        addMenuItem(menuPanel, "Economic", false);
        addMenuItem(menuPanel, "Audit Logs", true);
    }
    
    /**
     * Create the content panel
     */
    @Override
    protected JPanel createContentPanel() {
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
        
        // Statistics cards
        JPanel statsPanel = createStatisticsPanel();
        
        // Filter panel
        JPanel filterPanel = createFilterPanel();
        
        // Audit logs table
        JPanel tablePanel = createAuditTablePanel();
        
        // Add components
        contentPanel.add(statsPanel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(filterPanel);
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
        
        JLabel headerLabel = new JLabel("System Audit Logs");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 26));
        headerLabel.setForeground(new Color(50, 50, 50));
        
        JLabel subHeaderLabel = new JLabel("Track all system activities and user actions");
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
        
        // Export Logs button
        JButton exportButton = new JButton("Export Logs");
        exportButton.setBackground(PRIMARY_GREEN);
        exportButton.setForeground(Color.WHITE);
        exportButton.setFocusPainted(false);
        exportButton.setFont(new Font("Arial", Font.BOLD, 14));
        exportButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        exportButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
           exportButton.setOpaque(true);  // Add this
        exportButton.setBorderPainted(false);  // Add this
        
        
        exportButton.addActionListener(e -> exportLogs());
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
        
        // Clear Old Logs button
        JButton clearButton = new JButton("Clear Old Logs");
        clearButton.setBackground(PRIMARY_RED);
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        clearButton.setFont(new Font("Arial", Font.BOLD, 14));
        clearButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        clearButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
          clearButton.setOpaque(true);  // Add this
        clearButton.setBorderPainted(false);  // Add this
        
        
        clearButton.addActionListener(e -> clearOldLogs());
        clearButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                clearButton.setBackground(PRIMARY_RED.darker());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                clearButton.setBackground(PRIMARY_RED);
            }
        });
        
        buttonsPanel.add(clearButton);
        buttonsPanel.add(exportButton);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(buttonsPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    /**
     * Create statistics panel
     */
    private JPanel createStatisticsPanel() {
        JPanel statsPanel = new JPanel();
        statsPanel.setOpaque(false);
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        
        JLabel sectionTitle = new JLabel("AUDIT LOG STATISTICS");
        sectionTitle.setFont(new Font("Arial", Font.BOLD, 16));
        sectionTitle.setForeground(new Color(50, 50, 50));
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        cardsPanel.setOpaque(false);
        cardsPanel.setMaximumSize(new Dimension(1200, 120));
        cardsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Calculate statistics - with null checks
        int totalLogs = (auditLogs != null) ? auditLogs.size() : 0;
        int todayLogs = (auditLogs != null) ? countTodayLogs() : 0;
        int loginCount = (auditLogs != null) ? countByType("Login") : 0;
        int carActions = (auditLogs != null) ? countCarActions() : 0;
        
        // Create stat cards
        RoundedPanel totalCard = createStatCard("Total Activities", String.valueOf(totalLogs), PRIMARY_BLUE, "📊");
        RoundedPanel todayCard = createStatCard("Today's Activities", String.valueOf(todayLogs), PRIMARY_GREEN, "📅");
        RoundedPanel loginCard = createStatCard("Login Sessions", String.valueOf(loginCount), PRIMARY_YELLOW, "🔐");
        RoundedPanel carCard = createStatCard("Car Actions", String.valueOf(carActions), new Color(156, 39, 176), "🚗");
        
        cardsPanel.add(totalCard);
        cardsPanel.add(todayCard);
        cardsPanel.add(loginCard);
        cardsPanel.add(carCard);
        
        statsPanel.add(sectionTitle);
        statsPanel.add(Box.createVerticalStrut(15));
        statsPanel.add(cardsPanel);
        
        return statsPanel;
    }
    
    /**
     * Create stat card
     */
    private RoundedPanel createStatCard(String title, String value, Color color, String icon) {
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
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(color);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(8));
        contentPanel.add(valueLabel);
        
        card.add(iconLabel, BorderLayout.WEST);
        card.add(contentPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    /**
     * Create filter panel
     */
    private JPanel createFilterPanel() {
        RoundedPanel filterCard = new RoundedPanel(15, Color.WHITE);
        filterCard.setLayout(new BorderLayout());
        filterCard.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        filterCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterCard.setMaximumSize(new Dimension(1200, 80));
        
        JLabel filterTitle = new JLabel("FILTER LOGS");
        filterTitle.setFont(new Font("Arial", Font.BOLD, 16));
        filterTitle.setForeground(new Color(50, 50, 50));
        
        JPanel filterControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        filterControls.setOpaque(false);
        
        // Search field
        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        searchField.setPreferredSize(new Dimension(250, 35));
        searchField.addActionListener(e -> filterLogs());
        
        // Activity type filter
        JLabel typeLabel = new JLabel("Activity Type:");
        typeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        String[] types = {"All Types", "Login", "Car Added", "Car Updated", "Car Sold", "Car Deleted", 
                         "Expense Added", "Report Generated", "Settings Changed"};
        filterTypeCombo = new JComboBox<>(types);
        filterTypeCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        filterTypeCombo.addActionListener(e -> filterLogs());
        
        // User filter
        JLabel userLabel = new JLabel("User:");
        userLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        String[] users = {"All Users", "Rawaz Muhsinn", "Admin", "System"};
        filterUserCombo = new JComboBox<>(users);
        filterUserCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        filterUserCombo.addActionListener(e -> filterLogs());
        
        // Search button
        JButton searchButton = new JButton("Search");
        searchButton.setBackground(PRIMARY_BLUE);
        searchButton.setForeground(Color.WHITE);
        searchButton.setFont(new Font("Arial", Font.BOLD, 12));
        searchButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchButton.addActionListener(e -> filterLogs());
        
        filterControls.add(searchField);
        filterControls.add(typeLabel);
        filterControls.add(filterTypeCombo);
        filterControls.add(userLabel);
        filterControls.add(filterUserCombo);
        filterControls.add(searchButton);
        
        filterCard.add(filterTitle, BorderLayout.WEST);
        filterCard.add(filterControls, BorderLayout.CENTER);
        
        return filterCard;
    }
    
    /**
     * Create audit table panel
     */
    private JPanel createAuditTablePanel() {
        JPanel tableContainer = new JPanel();
        tableContainer.setOpaque(false);
        tableContainer.setLayout(new BoxLayout(tableContainer, BoxLayout.Y_AXIS));
        tableContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel tableTitle = new JLabel("ACTIVITY LOG");
        tableTitle.setFont(new Font("Arial", Font.BOLD, 16));
        tableTitle.setForeground(new Color(50, 50, 50));
        tableTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Create table
        createAuditTable();
        
        RoundedPanel tableCard = new RoundedPanel(15, Color.WHITE);
        tableCard.setLayout(new BorderLayout());
        tableCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JScrollPane scrollPane = new JScrollPane(auditTable);
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
     * Create audit table
     */
    private void createAuditTable() {
        String[] columnNames = {"DATE & TIME", "USER", "ACTIVITY TYPE", "DESCRIPTION", "IP ADDRESS", "STATUS"};
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Populate with audit logs - with null check
        if (auditLogs != null) {
            for (AuditLogEntry log : auditLogs) {
                tableModel.addRow(new Object[]{
                    log.getDateTime(),
                    log.getUser(),
                    log.getActivityType(),
                    log.getDescription(),
                    log.getIpAddress(),
                    log.getStatus()
                });
            }
        }
        
        auditTable = new JTable(tableModel);
        auditTable.setRowHeight(40);
        auditTable.setShowGrid(false);
        auditTable.setFont(new Font("Arial", Font.PLAIN, 13));
        auditTable.setBackground(Color.WHITE);
        
        // Style header
        JTableHeader header = auditTable.getTableHeader();
        header.setBackground(new Color(248, 250, 252));
        header.setForeground(new Color(50, 50, 50));
        header.setFont(new Font("Arial", Font.BOLD, 13));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(230, 230, 230)));
        
        // Custom renderers
        auditTable.getColumnModel().getColumn(2).setCellRenderer(new ActivityTypeRenderer());
        auditTable.getColumnModel().getColumn(5).setCellRenderer(new StatusRenderer());
        
        // Set column widths
        auditTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        auditTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        auditTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        auditTable.getColumnModel().getColumn(3).setPreferredWidth(300);
        auditTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        auditTable.getColumnModel().getColumn(5).setPreferredWidth(80);
        
        // Add double-click listener for details
        auditTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = auditTable.getSelectedRow();
                    if (row >= 0) {
                        showLogDetails(row);
                    }
                }
            }
        });
    }
    
    /**
     * Initialize audit data
     */
    private void initializeAuditData() {
        if (globalAuditLogs.isEmpty()) {
            // Add sample audit logs
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
            String currentTime = dateFormat.format(new Date());
            
            globalAuditLogs.add(new AuditLogEntry(currentTime, "Rawaz Muhsinn", "Login", 
                "User logged into the system", "192.168.1.100", "Success"));
            
            globalAuditLogs.add(new AuditLogEntry("May 24, 2025 11:45:23", "Rawaz Muhsinn", "Car Added", 
                "Added new car: Toyota Camry 2023", "192.168.1.100", "Success"));
            
            globalAuditLogs.add(new AuditLogEntry("May 24, 2025 11:30:15", "Admin", "Car Sold", 
                "Sold car: BMW X5 2023 to John Smith", "192.168.1.101", "Success"));
            
            globalAuditLogs.add(new AuditLogEntry("May 24, 2025 10:15:45", "Rawaz Muhsinn", "Expense Added", 
                "Added expense: Office Rent $3,500", "192.168.1.100", "Success"));
            
            globalAuditLogs.add(new AuditLogEntry("May 24, 2025 09:30:00", "System", "Report Generated", 
                "Generated monthly financial report", "System", "Success"));
            
            globalAuditLogs.add(new AuditLogEntry("May 23, 2025 16:45:30", "Rawaz Muhsinn", "Car Updated", 
                "Updated car details: Ford F-150 2022", "192.168.1.100", "Success"));
            
            globalAuditLogs.add(new AuditLogEntry("May 23, 2025 15:20:10", "Admin", "Settings Changed", 
                "Updated system configuration", "192.168.1.101", "Success"));
            
            globalAuditLogs.add(new AuditLogEntry("May 23, 2025 14:00:00", "Rawaz Muhsinn", "Login", 
                "User logged into the system", "192.168.1.100", "Success"));
            
            globalAuditLogs.add(new AuditLogEntry("May 23, 2025 10:30:45", "System", "Car Deleted", 
                "Removed old inventory item", "System", "Success"));
            
            globalAuditLogs.add(new AuditLogEntry("May 22, 2025 17:00:00", "Rawaz Muhsinn", "Login", 
                "Failed login attempt - incorrect password", "192.168.1.100", "Failed"));
        }
        
        auditLogs = new ArrayList<>(globalAuditLogs);
    }
    
    /**
     * Add new audit log entry
     */
    public static void addAuditLog(String user, String activityType, String description, String status) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
        String currentTime = dateFormat.format(new Date());
        String ipAddress = "192.168.1.100"; // In real app, get actual IP
        
        AuditLogEntry newLog = new AuditLogEntry(currentTime, user, activityType, description, ipAddress, status);
        globalAuditLogs.add(0, newLog); // Add to beginning
    }
    
    /**
     * Filter logs based on criteria
     */
    private void filterLogs() {
        tableModel.setRowCount(0);
        
        String searchText = searchField.getText().toLowerCase();
        String selectedType = (String) filterTypeCombo.getSelectedItem();
        String selectedUser = (String) filterUserCombo.getSelectedItem();
        
        for (AuditLogEntry log : auditLogs) {
            boolean matchesSearch = searchText.isEmpty() || 
                                  log.getDescription().toLowerCase().contains(searchText);
            
            boolean matchesType = "All Types".equals(selectedType) || 
                                log.getActivityType().equals(selectedType);
            
            boolean matchesUser = "All Users".equals(selectedUser) || 
                                log.getUser().equals(selectedUser);
            
            if (matchesSearch && matchesType && matchesUser) {
                tableModel.addRow(new Object[]{
                    log.getDateTime(),
                    log.getUser(),
                    log.getActivityType(),
                    log.getDescription(),
                    log.getIpAddress(),
                    log.getStatus()
                });
            }
        }
    }
    
    /**
     * Show log details
     */
    private void showLogDetails(int row) {
        if (row >= 0 && row < tableModel.getRowCount()) {
            String dateTime = (String) tableModel.getValueAt(row, 0);
            String user = (String) tableModel.getValueAt(row, 1);
            String activity = (String) tableModel.getValueAt(row, 2);
            String description = (String) tableModel.getValueAt(row, 3);
            String ipAddress = (String) tableModel.getValueAt(row, 4);
            String status = (String) tableModel.getValueAt(row, 5);
            
            String details = "AUDIT LOG DETAILS\n" +
                           "==================\n\n" +
                           "Date & Time: " + dateTime + "\n" +
                           "User: " + user + "\n" +
                           "Activity Type: " + activity + "\n" +
                           "Description: " + description + "\n" +
                           "IP Address: " + ipAddress + "\n" +
                           "Status: " + status + "\n\n" +
                           "Additional Information:\n" +
                           "- Session ID: " + generateSessionId() + "\n" +
                           "- Browser: Chrome 125.0\n" +
                           "- Operating System: Windows 10\n" +
                           "- Location: Erbil, Iraq";
            
            JOptionPane.showMessageDialog(this, details, "Log Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Export logs
     */
    private void exportLogs() {
        String exportInfo = "Audit logs exported successfully!\n\n" +
                          "Total Records: " + tableModel.getRowCount() + "\n" +
                          "Export File: audit_logs_" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".csv\n" +
                          "Location: Downloads/AuditLogs/";
        
        JOptionPane.showMessageDialog(this, exportInfo, "Export Complete", JOptionPane.INFORMATION_MESSAGE);
        
        // Log this action
        addAuditLog(managerName, "Report Generated", "Exported audit logs", "Success");
    }
    
    /**
     * Clear old logs
     */
    private void clearOldLogs() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "This will remove all logs older than 30 days.\nAre you sure you want to continue?",
            "Clear Old Logs",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // In real app, filter and remove old logs
            JOptionPane.showMessageDialog(this,
                "Old logs cleared successfully!\nRemoved 45 logs older than 30 days.",
                "Logs Cleared",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Log this action
            addAuditLog(managerName, "Settings Changed", "Cleared old audit logs", "Success");
        }
    }
    
    /**
     * Count today's logs
     */
    private int countTodayLogs() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        String today = dateFormat.format(new Date());
        int count = 0;
        
        for (AuditLogEntry log : auditLogs) {
            if (log.getDateTime().startsWith(today)) {
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * Count logs by type
     */
    private int countByType(String type) {
        int count = 0;
        for (AuditLogEntry log : auditLogs) {
            if (log.getActivityType().equals(type)) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Count car-related actions
     */
    private int countCarActions() {
        int count = 0;
        for (AuditLogEntry log : auditLogs) {
            if (log.getActivityType().startsWith("Car ")) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Generate session ID
     */
    private String generateSessionId() {
        return "SES" + System.currentTimeMillis() % 1000000;
    }
    
    // Custom table renderers
    private static class ActivityTypeRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            
            String type = value.toString();
            if (type.equals("Login")) {
                label.setBackground(new Color(230, 255, 250));
                label.setForeground(new Color(13, 110, 253));
            } else if (type.startsWith("Car ")) {
                label.setBackground(new Color(255, 250, 230));
                label.setForeground(new Color(255, 153, 0));
            } else if (type.contains("Expense")) {
                label.setBackground(new Color(255, 236, 236));
                label.setForeground(new Color(220, 53, 69));
            } else if (type.contains("Report")) {
                label.setBackground(new Color(232, 255, 232));
                label.setForeground(new Color(25, 135, 84));
            } else {
                label.setBackground(new Color(248, 250, 252));
                label.setForeground(new Color(100, 100, 100));
            }
            label.setOpaque(true);
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
            if ("Success".equals(status)) {
                label.setBackground(new Color(232, 255, 232));
                label.setForeground(new Color(25, 135, 84));
            } else if ("Failed".equals(status)) {
                label.setBackground(new Color(255, 236, 236));
                label.setForeground(new Color(220, 53, 69));
            } else {
                label.setBackground(new Color(255, 250, 230));
                label.setForeground(new Color(255, 153, 0));
            }
            label.setOpaque(true);
            return label;
        }
    }
    
    /**
     * Audit Log Entry class
     */
    public static class AuditLogEntry {
        private String dateTime;
        private String user;
        private String activityType;
        private String description;
        private String ipAddress;
        private String status;
        
        public AuditLogEntry(String dateTime, String user, String activityType, 
                           String description, String ipAddress, String status) {
            this.dateTime = dateTime;
            this.user = user;
            this.activityType = activityType;
            this.description = description;
            this.ipAddress = ipAddress;
            this.status = status;
        }
        
        // Getters
        public String getDateTime() { return dateTime; }
        public String getUser() { return user; }
        public String getActivityType() { return activityType; }
        public String getDescription() { return description; }
        public String getIpAddress() { return ipAddress; }
        public String getStatus() { return status; }
        
        // Setters
        public void setDateTime(String dateTime) { this.dateTime = dateTime; }
        public void setUser(String user) { this.user = user; }
        public void setActivityType(String activityType) { this.activityType = activityType; }
        public void setDescription(String description) { this.description = description; }
        public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
        public void setStatus(String status) { this.status = status; }
    }
    
    /**
     * Main method to test the Audit Log UI
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new AuditLogUI().setVisible(true);
        });
    }
}