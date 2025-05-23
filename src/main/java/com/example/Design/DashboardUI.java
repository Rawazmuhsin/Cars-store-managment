package com.example.Design;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Dashboard UI for the Car Store Management system.
 * Extends BaseUI to inherit common functionality.
 */
public class DashboardUI extends BaseUI {
    
    /**
     * Default constructor
     */
    public DashboardUI() {
        super();
    }
    
    /**
     * Constructor with admin ID
     * @param adminId ID of the admin user
     */
    public DashboardUI(int adminId) {
        super(adminId);
        setVisible(true);
    }
    
    @Override
    protected void addMenuItems(JPanel menuPanel) {
        // Add menu items - set Dashboard as active
        addMenuItem(menuPanel, "Dashboard", true);
        addMenuItem(menuPanel, "Car Inventory", false);
        addMenuItem(menuPanel, "Add New Car", false);
        addMenuItem(menuPanel, "Coming Soon", false);
        addMenuItem(menuPanel, "Sold Cars", false);
        addMenuItem(menuPanel, "Economic", false);
        addMenuItem(menuPanel, "Audit Logs", false);
    }
    
    @Override
    protected JPanel createContentPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(LIGHT_GRAY_BG);
        
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(LIGHT_GRAY_BG);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(LIGHT_GRAY_BG);
        
        JLabel dashboardTitle = new JLabel("Manager Dashboard");
        dashboardTitle.setFont(new Font("Arial", Font.BOLD, 24));
        dashboardTitle.setForeground(Color.DARK_GRAY);
        
        JLabel welcomeLabel = new JLabel("Welcome back, " + managerName + " | Last login: " + lastLogin);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        welcomeLabel.setForeground(new Color(100, 100, 100));
        
        titlePanel.add(dashboardTitle);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(welcomeLabel);
        
        JButton refreshButton = new JButton("Refresh Dashboard");
        refreshButton.setBackground(PRIMARY_BLUE);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.setFont(new Font("Arial", Font.BOLD, 14));
        refreshButton.setPreferredSize(new Dimension(200, 40));
        
        // Add action listener to refresh button
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code to refresh dashboard data would go here
                // For now, just recreate the dashboard
                dispose();
                SwingUtilities.invokeLater(() -> new DashboardUI(adminId).setVisible(true));
            }
        });
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(refreshButton, BorderLayout.EAST);
        
        // Today's Overview Section
        JPanel overviewPanel = new JPanel(new BorderLayout());
        overviewPanel.setBackground(LIGHT_GRAY_BG);
        overviewPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));
        
        JLabel overviewLabel = new JLabel("TODAY'S OVERVIEW");
        overviewLabel.setFont(new Font("Arial", Font.BOLD, 16));
        overviewLabel.setForeground(Color.DARK_GRAY);
        
        JPanel overviewCardsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        overviewCardsPanel.setBackground(LIGHT_GRAY_BG);
        overviewCardsPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        // Add overview cards with updated statistics
        overviewCardsPanel.add(createOverviewCard("Available Cars", "24", PRIMARY_BLUE));
        overviewCardsPanel.add(createOverviewCard("Coming Soon", "8", PRIMARY_YELLOW));
        overviewCardsPanel.add(createOverviewCard("Sold Today", "3", PRIMARY_GREEN));
        overviewCardsPanel.add(createOverviewCard("Total Revenue", "$95,400", PRIMARY_RED));
        
        overviewPanel.add(overviewLabel, BorderLayout.NORTH);
        overviewPanel.add(overviewCardsPanel, BorderLayout.CENTER);
        
        // Management Tools Section
        JPanel managementPanel = new JPanel(new BorderLayout());
        managementPanel.setBackground(LIGHT_GRAY_BG);
        managementPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));
        
        JLabel managementLabel = new JLabel("MANAGEMENT TOOLS");
        managementLabel.setFont(new Font("Arial", Font.BOLD, 16));
        managementLabel.setForeground(Color.DARK_GRAY);
        
        JPanel toolsGridPanel = new JPanel(new GridLayout(2, 3, 15, 15));
        toolsGridPanel.setBackground(LIGHT_GRAY_BG);
        toolsGridPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        // Add tool cards with action listeners
        JPanel carsCard = createToolCard("Car Inventory", "Search and manage all available cars", PRIMARY_BLUE);
        JButton carsButton = getAccessButtonFromToolCard(carsCard);
        carsButton.addActionListener(e -> navigateTo("Car Inventory"));
        
        JPanel addCarCard = createToolCard("Add New Car", "Add new vehicles to inventory", new Color(76, 175, 80));
        JButton addCarButton = getAccessButtonFromToolCard(addCarCard);
        addCarButton.addActionListener(e -> navigateTo("Add New Car"));
        
        JPanel soldCarsCard = createToolCard("Sold Cars", "Review and modify sold car records", PRIMARY_GREEN);
        JButton soldCarsButton = getAccessButtonFromToolCard(soldCarsCard);
        soldCarsButton.addActionListener(e -> navigateTo("Sold Cars"));
        
        JPanel comingSoonCard = createToolCard("Coming Soon Cars", "Authorize pending car shipments", PRIMARY_RED);
        JButton comingSoonButton = getAccessButtonFromToolCard(comingSoonCard);
        comingSoonButton.addActionListener(e -> navigateTo("Coming Soon"));
        
        JPanel economicCard = createToolCard("Economic Reports", "Generate financial and sales reports", PRIMARY_YELLOW);
        JButton economicButton = getAccessButtonFromToolCard(economicCard);
        economicButton.addActionListener(e -> navigateTo("Economic"));
        
        JPanel auditCard = createToolCard("Audit Logs", "View system activities and user actions", new Color(156, 39, 176));
        JButton auditButton = getAccessButtonFromToolCard(auditCard);
        auditButton.addActionListener(e -> navigateTo("Audit Logs"));
        
        toolsGridPanel.add(carsCard);
        toolsGridPanel.add(addCarCard);
        toolsGridPanel.add(soldCarsCard);
        toolsGridPanel.add(comingSoonCard);
        toolsGridPanel.add(economicCard);
        toolsGridPanel.add(auditCard);
        
        managementPanel.add(managementLabel, BorderLayout.NORTH);
        managementPanel.add(toolsGridPanel, BorderLayout.CENTER);
        
        // Quick Actions Section
        JPanel quickActionsPanel = new JPanel(new BorderLayout());
        quickActionsPanel.setBackground(LIGHT_GRAY_BG);
        quickActionsPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));
        
        JLabel quickActionsLabel = new JLabel("QUICK ACTIONS");
        quickActionsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        quickActionsLabel.setForeground(Color.DARK_GRAY);
        
        JPanel quickButtonsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        quickButtonsPanel.setBackground(LIGHT_GRAY_BG);
        quickButtonsPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        // Quick action buttons
        JButton viewInventoryButton = createQuickActionButton("View Inventory", PRIMARY_BLUE);
        viewInventoryButton.addActionListener(e -> navigateTo("Car Inventory"));
        
        JButton addNewCarButton = createQuickActionButton("Add New Car", PRIMARY_GREEN);
        addNewCarButton.addActionListener(e -> navigateTo("Add New Car"));
        
        JButton viewReportsButton = createQuickActionButton("View Reports", PRIMARY_YELLOW);
        viewReportsButton.addActionListener(e -> navigateTo("Economic"));
        
        JButton viewComingSoonButton = createQuickActionButton("Coming Soon", PRIMARY_RED);
        viewComingSoonButton.addActionListener(e -> navigateTo("Coming Soon"));
        
        quickButtonsPanel.add(viewInventoryButton);
        quickButtonsPanel.add(addNewCarButton);
        quickButtonsPanel.add(viewReportsButton);
        quickButtonsPanel.add(viewComingSoonButton);
        
        quickActionsPanel.add(quickActionsLabel, BorderLayout.NORTH);
        quickActionsPanel.add(quickButtonsPanel, BorderLayout.CENTER);
        
        // Footer
        JPanel footerPanel = createFooterPanel();
        
        // Add all sections to main panel
        mainPanel.add(headerPanel);
        mainPanel.add(overviewPanel);
        mainPanel.add(managementPanel);
        mainPanel.add(quickActionsPanel);
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(footerPanel);
        
        return mainPanel;
    }
    
    /**
     * Navigate to different screens from dashboard
     * @param screenName Name of the screen to navigate to
     */
    private void navigateTo(String screenName) {
        dispose(); // Close the current window
        
        switch (screenName) {
            case "Car Inventory":
                SwingUtilities.invokeLater(() -> new CarManagement(adminId).setVisible(true));
                break;
            case "Add New Car":
                SwingUtilities.invokeLater(() -> new AddCarUI(adminId).setVisible(true));
                break;
            case "Sold Cars":
                SwingUtilities.invokeLater(() -> new SoldCarsUI(adminId).setVisible(true));
                break;
            case "Coming Soon":
                SwingUtilities.invokeLater(() -> new ComingSoonUI(adminId).setVisible(true));
                break;
            case "Economic":
                // SwingUtilities.invokeLater(() -> new EconomicUI(adminId).setVisible(true));
                break;
            case "Audit Logs":
                // SwingUtilities.invokeLater(() -> new AuditLogsUI(adminId).setVisible(true));
                break;
            default:
                // Stay on dashboard or show error
                System.err.println("Unknown navigation target: " + screenName);
                break;
        }
    }
    
    
    /**
     * Create a quick action button
     * @param text Button text
     * @param color Button color
     * @return JButton configured as quick action button
     */
    private JButton createQuickActionButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        button.setPreferredSize(new Dimension(200, 50));
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(color.darker());
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    /**
     * Helper method to get the Access button from a tool card
     * @param toolCard The tool card panel
     * @return The Access button
     */
    private JButton getAccessButtonFromToolCard(JPanel toolCard) {
        // The button is the last component in the content panel
        // Content panel is at BorderLayout.CENTER of the tool card
        JPanel contentPanel = (JPanel) toolCard.getComponent(1);
        return (JButton) contentPanel.getComponent(4); // The button is the 5th component (index 4)
    }
    
    /**
     * Override the navigateToScreen method from BaseUI to use local navigation
     * @param screenName Name of the screen to navigate to
     */
    @Override
    protected void navigateToScreen(String screenName) {
        // Use the local navigateTo method for consistency
        navigateTo(screenName);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set look and feel to system look and feel
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new DashboardUI().setVisible(true);
        });
    }
}