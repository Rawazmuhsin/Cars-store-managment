package com.example.Design;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

/**
 * Coming Soon UI class that displays cars as cards with images
 */
public class ComingSoonUI extends BaseUI {
    
    // Store active filters
    private Map<String, Object> activeFilters = new HashMap<>();
    
    // Coming Soon Car data structure
    private List<ComingSoonCar> comingSoonCars;
    
    // Summary statistics
    private int totalComingCount = 0;
    private int orderedCount = 0;
    private int inTransitCount = 0;
    private int thisMonthArrivals = 0;

    /**
     * Constructor with admin ID
     * @param adminId The ID of the admin user
     */
    public ComingSoonUI(int adminId) {
        super(adminId);
    }
    
    /**
     * Default constructor
     */
    public ComingSoonUI() {
        super();
    }
    
    /**
     * Add menu items to the sidebar
     * Overrides the method in BaseUI
     * @param menuPanel The panel to add menu items to
     */
    @Override
    protected void addMenuItems(JPanel menuPanel) {
        // Add menu items - set Coming Soon as active
        addMenuItem(menuPanel, "Dashboard", false);
        addMenuItem(menuPanel, "Car Inventory", false);
        addMenuItem(menuPanel, "Add New Car", false);
        addMenuItem(menuPanel, "Coming Soon", true);
        addMenuItem(menuPanel, "Sold Cars", false);
        addMenuItem(menuPanel, "Economic", false);
        addMenuItem(menuPanel, "Audit Logs", false);
    }
    
    /**
     * Create the content panel for coming soon cars management
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
        
        JLabel headerLabel = new JLabel("Coming Soon Vehicles");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 26));
        headerLabel.setForeground(new Color(50, 50, 50));
        
        // Sub header info
        JLabel subHeaderLabel = new JLabel("Preview upcoming vehicle arrivals");
        subHeaderLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subHeaderLabel.setForeground(new Color(120, 120, 120));
        
        headerPanel.add(headerLabel, BorderLayout.NORTH);
        headerPanel.add(subHeaderLabel, BorderLayout.SOUTH);
        
        // Buttons panel for top right
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setOpaque(false);
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        
        // Add New Coming Soon button
        JButton addNewButton = new JButton("Add Coming Soon");
        addNewButton.setBackground(PRIMARY_BLUE);
        addNewButton.setForeground(Color.WHITE);
        addNewButton.setFocusPainted(false);
        addNewButton.setFont(new Font("Arial", Font.BOLD, 14));
        addNewButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        addNewButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addNewButton.setOpaque(true);
        addNewButton.setBorderPainted(false);
        
        // Add action listener to add new button
        addNewButton.addActionListener(e -> openAddComingSoonDialog());
        
        // Add hover effects
        addNewButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                addNewButton.setBackground(PRIMARY_BLUE.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                addNewButton.setBackground(PRIMARY_BLUE);
            }
        });
        
        buttonsPanel.add(addNewButton);
        
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
        
        // Initialize sample data
        initializeSampleData();
        
        // Calculate statistics
        calculateComingSoonStatistics();
        
        // Coming Soon statistics overview
        JPanel statsPanel = new JPanel();
        statsPanel.setOpaque(false);
        statsPanel.setLayout(new GridLayout(1, 4, 20, 0));
        statsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsPanel.setMaximumSize(new Dimension(1200, 120));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
        
        // Create overview cards
        JPanel totalComingCard = createOverviewCard("Total Coming Soon", Integer.toString(totalComingCount), PRIMARY_BLUE);
        JPanel orderedCard = createOverviewCard("Ordered", Integer.toString(orderedCount), PRIMARY_YELLOW);
        JPanel inTransitCard = createOverviewCard("In Transit", Integer.toString(inTransitCount), PRIMARY_GREEN);
        JPanel thisMonthCard = createOverviewCard("This Month Arrivals", Integer.toString(thisMonthArrivals), new Color(156, 39, 176)); // Purple
        
        statsPanel.add(totalComingCard);
        statsPanel.add(orderedCard);
        statsPanel.add(inTransitCard);
        statsPanel.add(thisMonthCard);
        
        // Coming Soon cars section title
        JLabel carsLabel = new JLabel("COMING SOON VEHICLES");
        carsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        carsLabel.setForeground(new Color(50, 50, 50));
        carsLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 15, 0));
        carsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Create cars grid panel
        JPanel carsGridPanel = createCarsGrid();
        
        // Create footer panel
        footerPanel = createFooterPanel();
        
        // Add components to content panel
        contentPanel.add(statsPanel);
        contentPanel.add(carsLabel);
        contentPanel.add(carsGridPanel);
        
        // Add all panels to main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    /**
     * Initialize sample data for coming soon cars
     */
    private void initializeSampleData() {
        // Initialize the list first
        comingSoonCars = new ArrayList<>();
        
        // Sample cars with placeholder images
        comingSoonCars.add(new ComingSoonCar(1, "Tesla Model Y", "2025", "White", "$55,000", "Jun 15, 2025", "Ordered", "placeholder_tesla.jpg"));
        comingSoonCars.add(new ComingSoonCar(2, "BMW X3", "2025", "Black", "$48,500", "Jun 22, 2025", "In Transit", "placeholder_bmw.jpg"));
        comingSoonCars.add(new ComingSoonCar(3, "Audi Q5", "2025", "Silver", "$52,000", "Jul 05, 2025", "Ordered", "placeholder_audi.jpg"));
        comingSoonCars.add(new ComingSoonCar(4, "Mercedes GLC", "2025", "Blue", "$58,000", "Jul 18, 2025", "In Production", "placeholder_mercedes.jpg"));
        comingSoonCars.add(new ComingSoonCar(5, "Lexus RX", "2025", "Red", "$51,500", "Aug 02, 2025", "Ordered", "placeholder_lexus.jpg"));
        comingSoonCars.add(new ComingSoonCar(6, "Porsche Cayenne", "2025", "Gray", "$75,000", "Aug 15, 2025", "In Transit", "placeholder_porsche.jpg"));
        comingSoonCars.add(new ComingSoonCar(7, "Range Rover Evoque", "2025", "Green", "$65,000", "Sep 01, 2025", "Ordered", "placeholder_range.jpg"));
        comingSoonCars.add(new ComingSoonCar(8, "Volvo XC90", "2025", "White", "$62,500", "Sep 12, 2025", "In Production", "placeholder_volvo.jpg"));
    }
    
    /**
     * Create cars grid panel with car cards
     */
    private JPanel createCarsGrid() {
        JPanel gridContainer = new JPanel();
        gridContainer.setOpaque(false);
        gridContainer.setLayout(new BoxLayout(gridContainer, BoxLayout.Y_AXIS));
        gridContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Create grid layout (4 cars per row)
        JPanel gridPanel = new JPanel(new GridLayout(0, 4, 20, 20));
        gridPanel.setOpaque(false);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Add car cards to grid
        for (ComingSoonCar car : comingSoonCars) {
            JPanel carCard = createCarCard(car);
            gridPanel.add(carCard);
        }
        
        gridContainer.add(gridPanel);
        return gridContainer;
    }
    
    /**
     * Create a car card with image, name, and model
     */
    private JPanel createCarCard(ComingSoonCar car) {
        RoundedPanel card = new RoundedPanel(15, Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setPreferredSize(new Dimension(280, 320));
        card.setMaximumSize(new Dimension(280, 320));
        
        // Car image panel
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new BorderLayout());
        imagePanel.setBackground(new Color(248, 250, 252));
        imagePanel.setPreferredSize(new Dimension(250, 180));
        imagePanel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));
        
        // Load car image (placeholder for now)
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        
        try {
            // Try to load the actual image
            ImageIcon originalIcon = new ImageIcon(car.getImagePath());
            if (originalIcon.getIconWidth() > 0) {
                Image scaledImage = originalIcon.getImage().getScaledInstance(240, 160, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
            } else {
                throw new Exception("Image not found");
            }
        } catch (Exception e) {
            // Use placeholder if image not found
            imageLabel.setText("🚗");
            imageLabel.setFont(new Font("Arial", Font.PLAIN, 60));
            imageLabel.setForeground(new Color(150, 150, 150));
        }
        
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        
        // Car info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        // Car name
        JLabel nameLabel = new JLabel(car.getModel());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setForeground(new Color(50, 50, 50));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Car year
        JLabel yearLabel = new JLabel(car.getYear());
        yearLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        yearLabel.setForeground(new Color(120, 120, 120));
        yearLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Expected price
        JLabel priceLabel = new JLabel(car.getExpectedPrice());
        priceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        priceLabel.setForeground(PRIMARY_GREEN);
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Status badge
        JLabel statusLabel = new JLabel(car.getStatus());
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setOpaque(true);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Set status color
        switch (car.getStatus()) {
            case "Ordered":
                statusLabel.setBackground(new Color(255, 250, 230));
                statusLabel.setForeground(new Color(255, 153, 0));
                break;
            case "In Transit":
                statusLabel.setBackground(new Color(232, 255, 232));
                statusLabel.setForeground(new Color(25, 135, 84));
                break;
            case "In Production":
                statusLabel.setBackground(new Color(230, 255, 250));
                statusLabel.setForeground(new Color(13, 110, 253));
                break;
            default:
                statusLabel.setBackground(new Color(240, 240, 240));
                statusLabel.setForeground(new Color(70, 70, 70));
        }
        
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(yearLabel);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(priceLabel);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(statusLabel);
        
        card.add(imagePanel, BorderLayout.NORTH);
        card.add(infoPanel, BorderLayout.CENTER);
        
        // Add click handler to open details
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openCarDetails(car);
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
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        return card;
    }
    
    /**
     * Open car details in ShowComingSoon page
     */
    private void openCarDetails(ComingSoonCar car) {
        dispose(); // Close current window
        SwingUtilities.invokeLater(() -> new ShowComingSoonUI(car, adminId).setVisible(true));
    }
    
    /**
     * Open Add Coming Soon dialog
     */
    private void openAddComingSoonDialog() {
        dispose(); // Close current window
        SwingUtilities.invokeLater(() -> new AddComingSoonUI(adminId).setVisible(true));
    }
    
    /**
     * Calculate coming soon statistics
     */
    private void calculateComingSoonStatistics() {
        // Make sure comingSoonCars is not null
        if (comingSoonCars == null) {
            totalComingCount = 0;
            orderedCount = 0;
            inTransitCount = 0;
            thisMonthArrivals = 0;
            return;
        }
        
        totalComingCount = comingSoonCars.size();
        orderedCount = 0;
        inTransitCount = 0;
        thisMonthArrivals = 0;
        
        for (ComingSoonCar car : comingSoonCars) {
            switch (car.getStatus()) {
                case "Ordered":
                    orderedCount++;
                    break;
                case "In Transit":
                    inTransitCount++;
                    break;
                case "In Production":
                    // Count as ordered for now
                    orderedCount++;
                    break;
            }
            
            // Simple check for this month arrivals (in real app, parse the date)
            if (car.getArrivalDate().contains("Jun")) {
                thisMonthArrivals++;
            }
        }
    }
    
    /**
     * Inner class to represent a Coming Soon Car
     */
    public static class ComingSoonCar {
        private int id;
        private String model;
        private String year;
        private String color;
        private String expectedPrice;
        private String arrivalDate;
        private String status;
        private String imagePath;
        
        public ComingSoonCar(int id, String model, String year, String color, String expectedPrice, 
                           String arrivalDate, String status, String imagePath) {
            this.id = id;
            this.model = model;
            this.year = year;
            this.color = color;
            this.expectedPrice = expectedPrice;
            this.arrivalDate = arrivalDate;
            this.status = status;
            this.imagePath = imagePath;
        }
        
        // Getters
        public int getId() { return id; }
        public String getModel() { return model; }
        public String getYear() { return year; }
        public String getColor() { return color; }
        public String getExpectedPrice() { return expectedPrice; }
        public String getArrivalDate() { return arrivalDate; }
        public String getStatus() { return status; }
        public String getImagePath() { return imagePath; }
        
        // Setters
        public void setId(int id) { this.id = id; }
        public void setModel(String model) { this.model = model; }
        public void setYear(String year) { this.year = year; }
        public void setColor(String color) { this.color = color; }
        public void setExpectedPrice(String expectedPrice) { this.expectedPrice = expectedPrice; }
        public void setArrivalDate(String arrivalDate) { this.arrivalDate = arrivalDate; }
        public void setStatus(String status) { this.status = status; }
        public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    }
    
    /**
     * Main method to test the Coming Soon UI
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set look and feel to system look and feel
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new ComingSoonUI().setVisible(true);
        });
    }
}