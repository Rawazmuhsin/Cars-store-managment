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
        addMenuItem(menuPanel, "Cars Available", false);
        addMenuItem(menuPanel, "Sold Cars", false);
        addMenuItem(menuPanel, "Coming Soon", false);
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
        
        JPanel overviewCardsPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        overviewCardsPanel.setBackground(LIGHT_GRAY_BG);
        overviewCardsPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        // Add overview cards
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
        
        // Add tool cards with action listeners
        JPanel carsCard = createToolCard("Cars", "Search and manage all available cars", PRIMARY_BLUE);
        JButton carsButton = getAccessButtonFromToolCard(carsCard);
        carsButton.addActionListener(e -> navigateToScreen("Cars Available"));
        
        JPanel soldCarsCard = createToolCard("Sold Cars", "Review and modify sold car records", PRIMARY_GREEN);
        JButton soldCarsButton = getAccessButtonFromToolCard(soldCarsCard);
        soldCarsButton.addActionListener(e -> navigateToScreen("Sold Cars"));
        
        JPanel comingSoonCard = createToolCard("See Detail of Cars in Way", "Authorize pending car shipments", PRIMARY_RED);
        JButton comingSoonButton = getAccessButtonFromToolCard(comingSoonCard);
        comingSoonButton.addActionListener(e -> navigateToScreen("Coming Soon"));
        
        JPanel economicCard = createToolCard("Economic Management", "Generate financial and sales reports", PRIMARY_YELLOW);
        JButton economicButton = getAccessButtonFromToolCard(economicCard);
        economicButton.addActionListener(e -> navigateToScreen("Economic"));
        
        toolsGridPanel.add(carsCard);
        toolsGridPanel.add(soldCarsCard);
        toolsGridPanel.add(comingSoonCard);
        toolsGridPanel.add(economicCard);
        
        managementPanel.add(managementLabel, BorderLayout.NORTH);
        managementPanel.add(toolsGridPanel, BorderLayout.CENTER);
        
        // Footer
        JPanel footerPanel = createFooterPanel();
        
        // Add all sections to main panel
        mainPanel.add(headerPanel);
        mainPanel.add(overviewPanel);
        mainPanel.add(managementPanel);
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(footerPanel);
        
        return mainPanel;
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