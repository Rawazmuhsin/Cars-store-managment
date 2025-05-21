package com.example.Design;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class CarStoreDashboard extends JFrame {
    // Define colors based on KOB dashboard
    private static final Color DARK_BLUE = new Color(20, 30, 70);
    private static final Color LIGHT_GRAY_BG = new Color(245, 248, 250);
    private static final Color PRIMARY_BLUE = new Color(33, 150, 243);
    private static final Color PRIMARY_GREEN = new Color(76, 175, 80);
    private static final Color PRIMARY_RED = new Color(239, 68, 68);
    private static final Color PRIMARY_YELLOW = new Color(255, 170, 0);
    
    private String managerName = "Rawaz Muhsinn";
    private String lastLogin = "May 21 2025 11:57 AM";

    public CarStoreDashboard() {
        setTitle("CSM Manager Dashboard");
        setSize(1200, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Sidebar (now with centered styling)
        JPanel sidebar = createSidebar();
        
        // Main content
        JPanel mainContent = createMainContent();
        
        // Combine all components
        add(sidebar, BorderLayout.WEST);
        add(mainContent, BorderLayout.CENTER);

        setVisible(true);
    }
    
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(DARK_BLUE);
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        
        // Logo Panel
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(DARK_BLUE);
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoPanel.setMaximumSize(new Dimension(250, 200));
        
        // Create circular logo
        JPanel circlePanel = new JPanel();
        circlePanel.setPreferredSize(new Dimension(100, 100));
        circlePanel.setMaximumSize(new Dimension(100, 100));
        circlePanel.setBackground(DARK_BLUE);
        circlePanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        circlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel logoText = new JLabel("CSM");
        logoText.setForeground(Color.WHITE);
        logoText.setFont(new Font("Arial", Font.BOLD, 24));
        circlePanel.setLayout(new BorderLayout());
        circlePanel.add(logoText, BorderLayout.CENTER);
        
        JLabel titleLabel = new JLabel("CSM Manager");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Administration Panel");
        subtitleLabel.setForeground(new Color(200, 200, 200));
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        logoPanel.add(Box.createVerticalStrut(20));
        logoPanel.add(circlePanel);
        logoPanel.add(Box.createVerticalStrut(15));
        logoPanel.add(titleLabel);
        logoPanel.add(Box.createVerticalStrut(5));
        logoPanel.add(subtitleLabel);
        logoPanel.add(Box.createVerticalStrut(30));
        
        // Menu Items with active indicator
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(DARK_BLUE);
        menuPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Menu items - using your requested changes
        String[] menuItems = {"Dashboard", "Cars Available", "Sold Cars", "Coming Soon", "Economic", "Audit Logs"};
        for (int i = 0; i < menuItems.length; i++) {
            JPanel menuItemPanel = new JPanel(new BorderLayout());
            menuItemPanel.setBackground(DARK_BLUE);
            menuItemPanel.setMaximumSize(new Dimension(250, 50));
            
            // Active indicator (yellow bar)
            JPanel indicator = new JPanel();
            indicator.setPreferredSize(new Dimension(5, 30));
            if (menuItems[i].equals("Dashboard")) {
                indicator.setBackground(PRIMARY_YELLOW);
            } else {
                indicator.setBackground(DARK_BLUE);
            }
            
            JLabel menuLabel = new JLabel(menuItems[i]);
            menuLabel.setForeground(Color.WHITE);
            menuLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            menuLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
            
            menuItemPanel.add(indicator, BorderLayout.WEST);
            menuItemPanel.add(menuLabel, BorderLayout.CENTER);
            
            menuPanel.add(menuItemPanel);
            menuPanel.add(Box.createVerticalStrut(5));
        }
        
        // User info at bottom
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.setBackground(new Color(30, 40, 80));
        userPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        userPanel.setMaximumSize(new Dimension(250, 120));
        userPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel nameLabel = new JLabel(managerName);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel roleLabel = new JLabel("Manager");
        roleLabel.setForeground(new Color(200, 200, 200));
        roleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(PRIMARY_RED);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutButton.setMaximumSize(new Dimension(120, 35));
        
        userPanel.add(nameLabel);
        userPanel.add(Box.createVerticalStrut(5));
        userPanel.add(roleLabel);
        userPanel.add(Box.createVerticalStrut(15));
        userPanel.add(logoutButton);
        
        // Add components to sidebar
        sidebar.add(logoPanel);
        sidebar.add(menuPanel);
        sidebar.add(Box.createVerticalGlue()); // Push user panel to bottom
        sidebar.add(userPanel);
        
        return sidebar;
    }
    
    private JPanel createMainContent() {
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
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(refreshButton, BorderLayout.EAST);
        
        // Today's Overview Section
        JPanel overviewPanel = new JPanel(new BorderLayout());
        overviewPanel.setBackground(LIGHT_GRAY_BG);
        overviewPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));
        
        JLabel overviewLabel = new JLabel("TODAY'S OVERVIEW");
        overviewLabel.setFont(new Font("Arial", Font.BOLD, 16));
        overviewLabel.setForeground(Color.DARK_GRAY);
        
        JPanel overviewCardsPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        overviewCardsPanel.setBackground(LIGHT_GRAY_BG);
        overviewCardsPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        // Per your request: Keep only "Coming Soon" and "New Cars" cards
        overviewCardsPanel.add(createOverviewCard("Coming Soon", "8", PRIMARY_BLUE));
        overviewCardsPanel.add(createOverviewCard("New Cars", "12", PRIMARY_YELLOW));
        
        overviewPanel.add(overviewLabel, BorderLayout.NORTH);
        overviewPanel.add(overviewCardsPanel, BorderLayout.CENTER);
        
        // Management Tools Section
        JPanel managementPanel = new JPanel(new BorderLayout());
        managementPanel.setBackground(LIGHT_GRAY_BG);
        managementPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));
        
        JLabel managementLabel = new JLabel("MANAGEMENT TOOLS");
        managementLabel.setFont(new Font("Arial", Font.BOLD, 16));
        managementLabel.setForeground(Color.DARK_GRAY);
        
        JPanel toolsGridPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        toolsGridPanel.setBackground(LIGHT_GRAY_BG);
        toolsGridPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        // Per your request: Rename the management tool cards
        toolsGridPanel.add(createToolCard("Cars", "Search and manage all available cars", PRIMARY_BLUE));
        toolsGridPanel.add(createToolCard("Sold Cars", "Review and modify sold car records", PRIMARY_GREEN));
        toolsGridPanel.add(createToolCard("See Detail of Cars in Way", "Authorize pending car shipments", PRIMARY_RED));
        toolsGridPanel.add(createToolCard("Economic Management", "Generate financial and sales reports", PRIMARY_YELLOW));
        
        managementPanel.add(managementLabel, BorderLayout.NORTH);
        managementPanel.add(toolsGridPanel, BorderLayout.CENTER);
        
        // Footer
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(LIGHT_GRAY_BG);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));
        
        JLabel copyrightLabel = new JLabel("Car Store Management Portal v1.0 | © 2025 CSM. All rights reserved.");
        copyrightLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        copyrightLabel.setForeground(new Color(130, 130, 130));
        
        footerPanel.add(copyrightLabel);
        
        // Add all sections to main panel
        mainPanel.add(headerPanel);
        mainPanel.add(overviewPanel);
        mainPanel.add(managementPanel);
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(footerPanel);
        
        return mainPanel;
    }
    
    private JPanel createOverviewCard(String title, String count, Color accentColor) {
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Top accent bar
        JPanel accentBar = new JPanel();
        accentBar.setPreferredSize(new Dimension(cardPanel.getWidth(), 5));
        accentBar.setBackground(accentColor);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        JLabel countLabel = new JLabel(count);
        countLabel.setFont(new Font("Arial", Font.BOLD, 28));
        
        cardPanel.add(accentBar, BorderLayout.NORTH);
        cardPanel.add(titleLabel, BorderLayout.CENTER);
        cardPanel.add(countLabel, BorderLayout.SOUTH);
        
        return cardPanel;
    }
    
    private JPanel createToolCard(String title, String description, Color accentColor) {
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Left accent bar
        JPanel accentBar = new JPanel();
        accentBar.setPreferredSize(new Dimension(5, cardPanel.getHeight()));
        accentBar.setBackground(accentColor);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descLabel.setForeground(new Color(100, 100, 100));
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton accessButton = new JButton("Access");
        accessButton.setBackground(accentColor);
        accessButton.setForeground(Color.WHITE);
        accessButton.setFocusPainted(false);
        accessButton.setFont(new Font("Arial", Font.BOLD, 14));
        accessButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        accessButton.setMaximumSize(new Dimension(200, 40));
        accessButton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(descLabel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(accessButton);
        
        cardPanel.add(accentBar, BorderLayout.WEST);
        cardPanel.add(contentPanel, BorderLayout.CENTER);
        
        return cardPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CarStoreDashboard::new);
    }
}