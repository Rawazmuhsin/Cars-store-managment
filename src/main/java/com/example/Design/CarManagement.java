package com.example.Design;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;

import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
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

public class CarManagement extends JFrame {
    
    private static final Color PRIMARY_COLOR = new Color(20, 30, 70);
    private static final Color SECONDARY_COLOR = new Color(30, 144, 255);
    private static final Color ACCENT_COLOR = new Color(255, 165, 0);
    private static final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    private static final Color TEXT_COLOR = new Color(50, 50, 50);
    private static final Color LIGHT_TEXT_COLOR = new Color(120, 120, 120);
    private static final Color HIGHLIGHT_COLOR = new Color(255, 220, 120);
    
    private DefaultTableModel tableModel;
    private JTable carsTable;
    private int adminId = 0;
    private String adminName = "Administrator";
    
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

    public CarManagement() {
        initialize();
    }
    
    public CarManagement(int adminId) {
        this.adminId = adminId;
        initialize();
    }
    
    private void initialize() {
        setTitle("Car Store Management (CSM)");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Create sidebar panel
        JPanel sidebarPanel = createSidebarPanel();
        add(sidebarPanel, BorderLayout.WEST);

        // Create main content panel
        JPanel mainContentPanel = createCarContentPanel();
        add(mainContentPanel, BorderLayout.CENTER);
        
        // Try to set icon
        try {
            setIconImage(new ImageIcon("Logo/car_logo.png").getImage());
        } catch (Exception e) {
            System.err.println("Error loading icon: " + e.getMessage());
        }

        setVisible(true);
    }

    private JPanel createSidebarPanel() {
        JPanel sidebar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, PRIMARY_COLOR, 
                    0, getHeight(), new Color(10, 20, 50)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        
        sidebar.setPreferredSize(new Dimension(260, getHeight()));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(30, 25, 30, 25));
        
        JLabel logoLabel = new JLabel();
        try {
            ImageIcon logoIcon = new ImageIcon("Logo/car_logo.png");
            java.awt.Image image = logoIcon.getImage().getScaledInstance(120, 80, java.awt.Image.SCALE_SMOOTH);
            logoIcon = new ImageIcon(image);
            logoLabel.setIcon(logoIcon);
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        } catch (Exception e) {
            System.err.println("Error loading logo: " + e.getMessage());
        }
        
        // App name 
        JLabel appNameLabel = new JLabel("Car Store Management");
        appNameLabel.setForeground(Color.WHITE);
        appNameLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        appNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Admin panel label
        JLabel adminLabel = new JLabel("Administration Panel");
        adminLabel.setForeground(new Color(180, 180, 180));
        adminLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
        adminLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add sidebar header
        sidebar.add(logoLabel);
        sidebar.add(appNameLabel);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(adminLabel);
        sidebar.add(Box.createVerticalStrut(40));
        
        // Menu items panel
        JPanel menuPanel = new JPanel();
        menuPanel.setOpaque(false);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Add menu items - set Car Inventory as active
        addMenuItem(menuPanel, "Dashboard", false);
        addMenuItem(menuPanel, "Car Inventory", true);
        addMenuItem(menuPanel, "Add New Car", false);
        addMenuItem(menuPanel, "Coming Soon Cars", false);
        addMenuItem(menuPanel, "Sales History", false);
        addMenuItem(menuPanel, "Economic Reports", false);
        
        sidebar.add(menuPanel);
        sidebar.add(Box.createVerticalGlue());
        
        // Add admin profile at bottom of sidebar
        JPanel profilePanel = new RoundedPanel(15, new Color(40, 50, 90));
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
        profilePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        profilePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        profilePanel.setMaximumSize(new Dimension(250, 120));
        
        JLabel nameLabel = new JLabel(adminName);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel roleLabel = new JLabel("Manager");
        roleLabel.setForeground(new Color(200, 200, 200));
        roleLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(255, 100, 100));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        logoutButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setOpaque(true); 
        logoutButton.setBorderPainted(false);
        
        // Add action listener to logout button
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(
                    CarManagement.this,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION
                );
                
                if (response == JOptionPane.YES_OPTION) {
                    dispose();
                    // Redirect to login screen if you have one
                }
            }
        });
        
        profilePanel.add(nameLabel);
        profilePanel.add(Box.createVerticalStrut(5));
        profilePanel.add(roleLabel);
        profilePanel.add(Box.createVerticalStrut(15));
        profilePanel.add(logoutButton);
        
        sidebar.add(profilePanel);
        
        return sidebar;
    }
    
    /**
     * Helper method to add menu items
     */
    private void addMenuItem(JPanel menuPanel, String text, boolean isActive) {
        JPanel menuItem = new JPanel();
        menuItem.setLayout(new BorderLayout());
        menuItem.setOpaque(false);
        menuItem.setMaximumSize(new Dimension(250, 50));
        menuItem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add highlight for active item
        if (isActive) {
            menuItem.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 3, 0, 0, ACCENT_COLOR),
                BorderFactory.createEmptyBorder(10, 15, 10, 10)
            ));
        } else {
            menuItem.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 10));
        }
        
        JLabel label = new JLabel(text);
        label.setForeground(isActive ? Color.WHITE : new Color(200, 200, 200));
        label.setFont(new Font("SansSerif", isActive ? Font.BOLD : Font.PLAIN, 16));
        
        menuItem.add(label, BorderLayout.CENTER);
        
        // Add hover effect
        menuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isActive) {
                    menuItem.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 3, 0, 0, new Color(100, 100, 100)),
                        BorderFactory.createEmptyBorder(10, 15, 10, 10)
                    ));
                    label.setForeground(new Color(230, 230, 230));
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (!isActive) {
                    menuItem.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 10));
                    label.setForeground(new Color(200, 200, 200));
                }
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                // Navigation would go here
            }
        });
        
        menuPanel.add(menuItem);
        menuPanel.add(Box.createVerticalStrut(5));
    }

    private JPanel createCarContentPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new BorderLayout());
        
        JLabel headerLabel = new JLabel("Car Inventory Management");
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        headerLabel.setForeground(TEXT_COLOR);
        
        // Sub header info
        JLabel subHeaderLabel = new JLabel("View and manage your car inventory");
        subHeaderLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subHeaderLabel.setForeground(LIGHT_TEXT_COLOR);
        
        headerPanel.add(headerLabel, BorderLayout.NORTH);
        headerPanel.add(subHeaderLabel, BorderLayout.SOUTH);
        
        // Create buttons panel for top right
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setOpaque(false);
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        
        // Clear filters button
        JButton clearFiltersButton = new JButton("Clear Filters");
        clearFiltersButton.setBackground(new Color(220, 53, 69)); // Red color
        clearFiltersButton.setForeground(Color.WHITE);
        clearFiltersButton.setFocusPainted(false);
        clearFiltersButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        clearFiltersButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        clearFiltersButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearFiltersButton.setOpaque(true);
        clearFiltersButton.setBorderPainted(false);
        
        // Add hover effects
        clearFiltersButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                clearFiltersButton.setBackground(new Color(200, 35, 51));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                clearFiltersButton.setBackground(new Color(220, 53, 69));
            }
        });
        
        // Export button
        JButton exportButton = new JButton("Export PDF");
        exportButton.setBackground(ACCENT_COLOR);
        exportButton.setForeground(Color.WHITE);
        exportButton.setFocusPainted(false);
        exportButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        exportButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        exportButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exportButton.setOpaque(true);
        exportButton.setBorderPainted(false);
        
        // Add hover effects
        exportButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                exportButton.setBackground(ACCENT_COLOR.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                exportButton.setBackground(ACCENT_COLOR);
            }
        });
        
        // Add Car button
        JButton addCarButton = new JButton("Add New Car");
        addCarButton.setBackground(SECONDARY_COLOR);
        addCarButton.setForeground(Color.WHITE);
        addCarButton.setFocusPainted(false);
        addCarButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        addCarButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        addCarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addCarButton.setOpaque(true);
        addCarButton.setBorderPainted(false);
        
        // Add hover effects
        addCarButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                addCarButton.setBackground(new Color(25, 118, 210));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                addCarButton.setBackground(SECONDARY_COLOR);
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
        filterLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        filterLabel.setForeground(TEXT_COLOR);
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
        priceRangeFilterLabel.setFont(new Font("SansSerif", Font.ITALIC, 11));
        priceRangeFilterLabel.setForeground(SECONDARY_COLOR);
        
        carTypeFilterLabel = new JLabel("");
        carTypeFilterLabel.setFont(new Font("SansSerif", Font.ITALIC, 11));
        carTypeFilterLabel.setForeground(SECONDARY_COLOR);
        
        statusFilterLabel = new JLabel("");
        statusFilterLabel.setFont(new Font("SansSerif", Font.ITALIC, 11));
        statusFilterLabel.setForeground(SECONDARY_COLOR);
        
        // Create filter cards
        priceRangeCard = createFilterCard("Price Range", "Filter by minimum and maximum price", priceRangeFilterLabel);
        carTypeCard = createFilterCard("Car Type", "Filter by sedan, SUV, truck, etc.", carTypeFilterLabel);
        statusCard = createFilterCard("Status", "Filter by available, sold, reserved", statusFilterLabel);
        
        filterCardsPanel.add(priceRangeCard);
        filterCardsPanel.add(carTypeCard);
        filterCardsPanel.add(statusCard);
        
        // Car list section title
        JLabel carsLabel = new JLabel("CAR INVENTORY");
        carsLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        carsLabel.setForeground(TEXT_COLOR);
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
        carsTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        carsTable.setBackground(Color.WHITE);
        
        // Style table header
        JTableHeader header = carsTable.getTableHeader();
        header.setBackground(new Color(248, 250, 252));
        header.setForeground(TEXT_COLOR);
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
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
        prevButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        prevButton.setBackground(new Color(248, 250, 252));
        prevButton.setForeground(TEXT_COLOR);
        prevButton.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        prevButton.setFocusPainted(false);
        
        // Page number buttons
        for (int i = 1; i <= 5; i++) {
            JButton pageButton = new JButton(String.valueOf(i));
            pageButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
            
            if (i == 1) { // First page is active by default
                pageButton.setBackground(SECONDARY_COLOR);
                pageButton.setForeground(Color.WHITE);
                pageButton.setBorder(BorderFactory.createLineBorder(SECONDARY_COLOR));
            } else {
                pageButton.setBackground(new Color(248, 250, 252));
                pageButton.setForeground(TEXT_COLOR);
                pageButton.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
            }
            
            pageButton.setFocusPainted(false);
            paginationPanel.add(pageButton);
        }
        
        // Next page button
        JButton nextButton = new JButton("Next →");
        nextButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        nextButton.setBackground(new Color(248, 250, 252));
        nextButton.setForeground(TEXT_COLOR);
        nextButton.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        nextButton.setFocusPainted(false);
        
        paginationPanel.add(prevButton);
        paginationPanel.add(nextButton);
        
        // Footer panel
        JPanel footerPanel = new JPanel();
        footerPanel.setOpaque(false);
        footerPanel.setLayout(new BorderLayout());
        footerPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        
        JLabel versionLabel = new JLabel("Car Store Management System v1.0 | © 2025 CSM. All rights reserved.");
        versionLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        versionLabel.setForeground(LIGHT_TEXT_COLOR);
        
        footerPanel.add(versionLabel, BorderLayout.WEST);
        
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
        colorBar.setBackground(SECONDARY_COLOR);
        colorBar.setPreferredSize(new Dimension(50, 5));
        
        // Title label
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_COLOR);
        
        // Description label
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        descLabel.setForeground(LIGHT_TEXT_COLOR);
        
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
        Font quickButtonFont = new Font("SansSerif", Font.PLAIN, 12);
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
        applyButton.setBackground(SECONDARY_COLOR);
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
     * Shows car type filter dialog
     */
    private void showCarTypeFilterDialog() {
        JDialog dialog = new JDialog(this, "Select Car Types", true);
        dialog.setSize(350, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Select Car Types:");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Create checkboxes for car types
        JCheckBox sedanCheckBox = new JCheckBox("Sedan");
        sedanCheckBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        sedanCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        sedanCheckBox.setSelected(true);
        
        JCheckBox suvCheckBox = new JCheckBox("SUV");
        suvCheckBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        suvCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        suvCheckBox.setSelected(true);
        
        JCheckBox truckCheckBox = new JCheckBox("Truck");
        truckCheckBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        truckCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        truckCheckBox.setSelected(true);
        
        JCheckBox electricCheckBox = new JCheckBox("Electric");
        electricCheckBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        electricCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        electricCheckBox.setSelected(true);
        
        JCheckBox luxuryCheckBox = new JCheckBox("Luxury");
        luxuryCheckBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
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
        applyButton.setBackground(SECONDARY_COLOR);
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
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Create radio buttons for status
        JRadioButton allRadio = new JRadioButton("All Statuses");
        allRadio.setFont(new Font("SansSerif", Font.PLAIN, 14));
        allRadio.setAlignmentX(Component.LEFT_ALIGNMENT);
        allRadio.setSelected(true);
        
        JRadioButton availableRadio = new JRadioButton("Available Only");
        availableRadio.setFont(new Font("SansSerif", Font.PLAIN, 14));
        availableRadio.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JRadioButton soldRadio = new JRadioButton("Sold Only");
        soldRadio.setFont(new Font("SansSerif", Font.PLAIN, 14));
        soldRadio.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JRadioButton reservedRadio = new JRadioButton("Reserved Only");
        reservedRadio.setFont(new Font("SansSerif", Font.PLAIN, 14));
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
        applyButton.setBackground(SECONDARY_COLOR);
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
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
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
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
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
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        statusLabel.setPreferredSize(new Dimension(100, 25));
        
        String[] statusOptions = {"Available", "Reserved", "Sold"};
        JPanel statusFieldPanel = new JPanel(new BorderLayout());
        JTextField statusField = new JTextField(status);
        statusField.setFont(new Font("SansSerif", Font.PLAIN, 14));
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
        saveButton.setBackground(new Color(40, 167, 69)); // Green
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("SansSerif", Font.BOLD, 14));
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
        labelComponent.setFont(new Font("SansSerif", Font.BOLD, 14));
        labelComponent.setPreferredSize(new Dimension(100, 25));
        
        JPanel fieldPanel = new JPanel(new BorderLayout());
        JTextField field = new JTextField(value);
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
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
        labelComponent.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
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
            buttonLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
            buttonLabel.setBackground(SECONDARY_COLOR);
            buttonLabel.setForeground(Color.WHITE);
            buttonLabel.setOpaque(true);
            buttonLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            
            // Add a darker border to make it look more like a button
            buttonLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SECONDARY_COLOR.darker(), 1),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)
            ));
            
            return buttonLabel;
        }
    }
    
    /**
     * Custom rounded panel with background color
     */
    class RoundedPanel extends JPanel {
        private int cornerRadius;
        private Color backgroundColor;
        
        public RoundedPanel(int radius, Color bgColor) {
            super();
            this.cornerRadius = radius;
            this.backgroundColor = bgColor;
            setOpaque(false);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(backgroundColor);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
            g2d.dispose();
        }
    }

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