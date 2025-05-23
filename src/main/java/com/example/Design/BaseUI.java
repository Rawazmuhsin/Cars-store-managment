package com.example.Design;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * BaseUI class that provides common UI elements and functionality for the Car Store Management system.
 * Other UI classes like DashboardUI and CarManagementUI will extend this class.
 */
public abstract class BaseUI extends JFrame {
    
    // Common color scheme for all UI screens
    protected static final Color DARK_BLUE = new Color(20, 30, 70);
    protected static final Color LIGHT_GRAY_BG = new Color(245, 248, 250);
    protected static final Color PRIMARY_BLUE = new Color(33, 150, 243);
    protected static final Color PRIMARY_GREEN = new Color(76, 175, 80);
    protected static final Color PRIMARY_RED = new Color(239, 68, 68);
    protected static final Color PRIMARY_YELLOW = new Color(255, 170, 0);
    
    // Common UI elements
    protected JPanel sidebarPanel;
    protected JPanel contentPanel;
    protected JPanel footerPanel;
    
    // User information
    protected int adminId = 0;
    protected String managerName = "Rawaz Muhsinn";
    protected String lastLogin = "May 21 2025 11:57 AM";
    
    /**
     * Constructor for BaseUI
     */
    public BaseUI() {
        this(0);
    }
    
    /**
     * Constructor with admin ID
     * @param adminId The ID of the admin user
     */
    public BaseUI(int adminId) {
        this.adminId = adminId;
        initialize();
    }
    
    /**
     * Initialize the UI components
     */
    private void initialize() {
        setTitle("CSM Manager Dashboard");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Create sidebar panel
        sidebarPanel = createSidebar();
        add(sidebarPanel, BorderLayout.WEST);
        
        // Create content panel
        contentPanel = createContentPanel();
        add(contentPanel, BorderLayout.CENTER);
        
        // Set application icon - try multiple possible paths
        setApplicationIcon();
    }
    
    /**
     * Set the application icon with multiple fallback options
     */
   private void setApplicationIcon() {
    String[] possiblePaths = {
        "src/main/java/com/example/logo/ChatGPT Image May 23, 2025, 08_41_35 PM.png", // Your exact file
        "src/main/java/com/example/logo/car_logo.png",     // Standard name
        "logo/car_logo.png",                              // Standard path
        "src/logo/car_logo.png",                          // Alternative path
        "Logo/car_logo.png",                              // Capitalized Logo folder
        "src/Logo/car_logo.png",                          // Capitalized in src
        "resources/logo/car_logo.png",                    // Resources folder
        "src/main/resources/logo/car_logo.png",           // Maven structure
        "car_logo.png"                                    // Root directory
    };
    
    boolean iconLoaded = false;
    
    for (String path : possiblePaths) {
        try {
            java.io.File iconFile = new java.io.File(path);
            if (iconFile.exists()) {
                ImageIcon icon = new ImageIcon(path);
                if (icon.getIconWidth() > 0) {
                    setIconImage(icon.getImage());
                    System.out.println("Successfully loaded icon from: " + path);
                    iconLoaded = true;
                    break;
                }
            }
        } catch (Exception e) {
            // Continue to next path
        }
    }
    
    if (!iconLoaded) {
        System.err.println("Could not load application icon from any of the expected locations.");
        System.err.println("Please ensure the logo file exists in one of these locations:");
        for (String path : possiblePaths) {
            System.err.println("  - " + path);
        }
        
        // Create a simple default icon as fallback
        createDefaultIcon();
    }
}

    /**
     * Create a simple default icon if no image file is found
     */
    private void createDefaultIcon() {
        try {
            // Create a simple 32x32 icon with CSM text
            java.awt.image.BufferedImage iconImage = new java.awt.image.BufferedImage(32, 32, java.awt.image.BufferedImage.TYPE_INT_RGB);
            java.awt.Graphics2D g2d = iconImage.createGraphics();
            
            // Set rendering hints for better quality
            g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            // Fill background with dark blue
            g2d.setColor(DARK_BLUE);
            g2d.fillRect(0, 0, 32, 32);
            
            // Add CSM text
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 10));
            java.awt.FontMetrics fm = g2d.getFontMetrics();
            String text = "CSM";
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getHeight();
            int x = (32 - textWidth) / 2;
            int y = (32 - textHeight) / 2 + fm.getAscent();
            g2d.drawString(text, x, y);
            
            g2d.dispose();
            setIconImage(iconImage);
            System.out.println("Created default CSM icon");
            
        } catch (Exception e) {
            System.err.println("Could not create default icon: " + e.getMessage());
        }
    }
    
    /**
     * Create the sidebar panel
     * @return JPanel containing the sidebar
     */
    protected JPanel createSidebar() {
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
        
        // Create logo container - try to load image first, fallback to circle
        JPanel logoContainer = createLogoContainer();
        
        JLabel titleLabel = new JLabel("CSM Manager");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Administration Panel");
        subtitleLabel.setForeground(new Color(200, 200, 200));
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        logoPanel.add(Box.createVerticalStrut(20));
        logoPanel.add(logoContainer);
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
        
        // Add menu items - these will be handled by the subclasses
        addMenuItems(menuPanel);
        
        sidebar.add(logoPanel);
        sidebar.add(menuPanel);
        sidebar.add(Box.createVerticalGlue()); // Push user panel to bottom
        
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
        
        // Add action listener to logout button
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(
                    BaseUI.this,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION
                );
                
                if (response == JOptionPane.YES_OPTION) {
                    dispose();
                    // Redirect to login screen if you have one
                    // SwingUtilities.invokeLater(() -> new LoginUI().setVisible(true));
                }
            }
        });
        
        userPanel.add(nameLabel);
        userPanel.add(Box.createVerticalStrut(5));
        userPanel.add(roleLabel);
        userPanel.add(Box.createVerticalStrut(15));
        userPanel.add(logoutButton);
        
        sidebar.add(userPanel);
        
        return sidebar;
    }
    
    /**
     * Create logo container - either image or fallback circle
     */
   private JPanel createLogoContainer() {
    JPanel container = new JPanel();
    container.setBackground(DARK_BLUE);
    container.setPreferredSize(new Dimension(100, 100));
    container.setMaximumSize(new Dimension(100, 100));
    container.setAlignmentX(Component.CENTER_ALIGNMENT);
    container.setLayout(new BorderLayout());
    
    // Try to load logo image
    JLabel logoLabel = new JLabel();
    logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
    logoLabel.setVerticalAlignment(SwingConstants.CENTER);
    
    boolean imageLoaded = false;
    String[] possiblePaths = {
        "src/main/java/com/example/logo/ChatGPT Image May 23, 2025, 08_41_35 PM.png", // Your exact file
        "src/main/java/com/example/logo/car_logo.png",     // Standard name
        "logo/car_logo.png",
        "src/logo/car_logo.png",
        "Logo/car_logo.png",
        "src/Logo/car_logo.png",
        "resources/logo/car_logo.png",
        "car_logo.png"
    };
    
    for (String path : possiblePaths) {
        try {
            java.io.File logoFile = new java.io.File(path);
            if (logoFile.exists()) {
                ImageIcon originalIcon = new ImageIcon(path);
                if (originalIcon.getIconWidth() > 0) {
                    // Scale the image to fit the container
                    Image scaledImage = originalIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                    logoLabel.setIcon(new ImageIcon(scaledImage));
                    imageLoaded = true;
                    System.out.println("Successfully loaded sidebar logo from: " + path);
                    break;
                }
            }
        } catch (Exception e) {
            // Continue to next path
        }
    }
    
    if (!imageLoaded) {
        // Fallback to the original circular design with CSM text
        container.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        logoLabel.setText("CSM");
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
    }
    
    container.add(logoLabel, BorderLayout.CENTER);
    return container;
}
    
    /**
     * Add menu items to the sidebar
     * This method will be implemented by subclasses
     * @param menuPanel The panel to add menu items to
     */
    protected abstract void addMenuItems(JPanel menuPanel);
    
    /**
     * Create the content panel
     * This method will be implemented by subclasses
     * @return JPanel containing the content
     */
    protected abstract JPanel createContentPanel();
    
    /**
     * Helper method to add menu items to sidebar
     * @param menuPanel Panel to add menu items to
     * @param text Label text for the menu item
     * @param isActive Whether this menu item is active
     */
    protected void addMenuItem(JPanel menuPanel, String text, boolean isActive) {
        JPanel menuItemPanel = new JPanel(new BorderLayout());
        menuItemPanel.setBackground(DARK_BLUE);
        menuItemPanel.setMaximumSize(new Dimension(250, 50));
        menuItemPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Active indicator (yellow bar)
        JPanel indicator = new JPanel();
        indicator.setPreferredSize(new Dimension(5, 30));
        if (isActive) {
            indicator.setBackground(PRIMARY_YELLOW);
        } else {
            indicator.setBackground(DARK_BLUE);
        }
        
        JLabel menuLabel = new JLabel(text);
        menuLabel.setForeground(Color.WHITE);
        menuLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        menuLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        
        menuItemPanel.add(indicator, BorderLayout.WEST);
        menuItemPanel.add(menuLabel, BorderLayout.CENTER);
        
        // Add hover effect
        menuItemPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isActive) {
                    indicator.setBackground(new Color(100, 100, 100));
                    menuLabel.setFont(new Font("Arial", Font.BOLD, 16));
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (!isActive) {
                    indicator.setBackground(DARK_BLUE);
                    menuLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                }
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                navigateToScreen(text);
            }
        });
        
        menuPanel.add(menuItemPanel);
        menuPanel.add(Box.createVerticalStrut(5));
    }
    
    /**
     * Create footer panel with version info
     * @return JPanel containing the footer
     */
    protected JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(LIGHT_GRAY_BG);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));
        
        JLabel copyrightLabel = new JLabel("Car Store Management Portal v1.0 | © 2025 CSM. All rights reserved.");
        copyrightLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        copyrightLabel.setForeground(new Color(130, 130, 130));
        
        footerPanel.add(copyrightLabel);
        
        return footerPanel;
    }
    
    /**
     * Create an overview card for dashboard
     */
    protected JPanel createOverviewCard(String title, String count, Color accentColor) {
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
    
    /**
     * Create a tool card for dashboard
     */
    protected JPanel createToolCard(String title, String description, Color accentColor) {
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
    
    /**
     * Navigate to different screens based on menu item clicks
     * @param screenName Name of the screen to navigate to
     */
    protected void navigateToScreen(String screenName) {
        dispose(); // Close the current window
        
        switch (screenName) {
            case "Dashboard":
                SwingUtilities.invokeLater(() -> new DashboardUI(adminId).setVisible(true));
                break;
            case "Cars Available":
            case "Car Inventory":
                SwingUtilities.invokeLater(() -> new CarManagement(adminId).setVisible(true));
                break;
            case "Add New Car":
                SwingUtilities.invokeLater(() -> new AddCarUI(adminId).setVisible(true));
                break;
            case "Sold Cars":
            case "Sales History":
                SwingUtilities.invokeLater(() -> new SoldCarsUI(adminId).setVisible(true));
                break;
            case "Coming Soon":
            case "Coming Soon Cars":
                SwingUtilities.invokeLater(() -> new ComingSoonUI(adminId).setVisible(true));
                break;
            case "Economic":
            case "Economic Reports":
                // SwingUtilities.invokeLater(() -> new EconomicUI(adminId).setVisible(true));
                break;
            case "Audit Logs":
                // SwingUtilities.invokeLater(() -> new AuditLogsUI(adminId).setVisible(true));
                break;
            default:
                SwingUtilities.invokeLater(() -> new DashboardUI(adminId).setVisible(true));
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
}