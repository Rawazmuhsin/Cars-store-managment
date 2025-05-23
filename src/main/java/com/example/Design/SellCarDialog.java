package com.example.Design;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

/**
 * Sell Car Dialog for processing car sales
 */
public class SellCarDialog extends JDialog {
    
    private CarManagement.Car car;
    private JTextField buyerNameField;
    private JTextField buyerContactField;
    private JTextField buyerEmailField;
    private JSpinner salePriceSpinner;
    private JSpinner saleDateSpinner;
    private JComboBox<String> paymentMethodCombo;
    private JTextArea notesArea;
    private JLabel taxAmountLabel;
    private JLabel totalAmountLabel;
    
    // Colors from BaseUI
    private static final Color PRIMARY_BLUE = new Color(33, 150, 243);
    private static final Color PRIMARY_GREEN = new Color(76, 175, 80);
    private static final Color PRIMARY_RED = new Color(239, 68, 68);
    private static final Color LIGHT_GRAY_BG = new Color(245, 248, 250);
    
    private boolean saleCompleted = false;
    
    /**
     * Constructor
     * @param parent Parent window
     * @param car Car to sell
     */
    public SellCarDialog(JFrame parent, CarManagement.Car car) {
        super(parent, "Sell Vehicle - " + car.getModel() + " " + car.getYear(), true);
        this.car = car;
        
        initializeDialog();
        createComponents();
        layoutComponents();
        addEventListeners();
        
        setSize(600, 700);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
    
    /**
     * Initialize dialog settings
     */
    private void initializeDialog() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(LIGHT_GRAY_BG);
    }
    
    /**
     * Create all UI components
     */
    private void createComponents() {
        // Buyer name field
        buyerNameField = new JTextField(25);
        buyerNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        buyerNameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        // Buyer contact field
        buyerContactField = new JTextField(25);
        buyerContactField.setFont(new Font("Arial", Font.PLAIN, 14));
        buyerContactField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        // Buyer email field
        buyerEmailField = new JTextField(25);
        buyerEmailField.setFont(new Font("Arial", Font.PLAIN, 14));
        buyerEmailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        // Sale price spinner
        int currentPrice = Integer.parseInt(car.getPrice().replace("$", "").replace(",", ""));
        SpinnerNumberModel priceModel = new SpinnerNumberModel(currentPrice, 1000, 1000000, 100);
        salePriceSpinner = new JSpinner(priceModel);
        salePriceSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Sale date spinner
        SpinnerDateModel dateModel = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH);
        saleDateSpinner = new JSpinner(dateModel);
        saleDateSpinner.setEditor(new JSpinner.DateEditor(saleDateSpinner, "MMM dd, yyyy"));
        saleDateSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Payment method combo
        String[] paymentMethods = {"Cash", "Financing", "Credit Card", "Bank Transfer", "Check"};
        paymentMethodCombo = new JComboBox<>(paymentMethods);
        paymentMethodCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        paymentMethodCombo.setBackground(Color.WHITE);
        
        // Notes area
        notesArea = new JTextArea(4, 30);
        notesArea.setFont(new Font("Arial", Font.PLAIN, 14));
        notesArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        notesArea.setText("Vehicle sold in excellent condition. All paperwork completed and signed.");
        
        // Tax and total labels
        taxAmountLabel = new JLabel("$0.00");
        taxAmountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        taxAmountLabel.setForeground(PRIMARY_BLUE);
        
        totalAmountLabel = new JLabel("$0.00");
        totalAmountLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalAmountLabel.setForeground(PRIMARY_GREEN);
        
        // Update calculations initially
        updateCalculations();
    }
    
    /**
     * Layout all components
     */
    private void layoutComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(LIGHT_GRAY_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        
        // Form
        JPanel formPanel = createFormPanel();
        
        // Buttons
        JPanel buttonsPanel = createButtonsPanel();
        
        mainPanel.add(headerPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(buttonsPanel);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    /**
     * Create header panel
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel titleLabel = new JLabel("Complete Vehicle Sale");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(50, 50, 50));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel vehicleLabel = new JLabel(car.getModel() + " " + car.getYear() + " • " + car.getColor() + " • " + car.getType());
        vehicleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        vehicleLabel.setForeground(new Color(100, 100, 100));
        vehicleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(vehicleLabel);
        
        return headerPanel;
    }
    
    /**
     * Create form panel
     */
    private JPanel createFormPanel() {
        JPanel formCard = new JPanel();
        formCard.setBackground(Color.WHITE);
        formCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        formCard.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        
        // Buyer Information Section
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 15, 0);
        JLabel buyerSectionLabel = new JLabel("Buyer Information");
        buyerSectionLabel.setFont(new Font("Arial", Font.BOLD, 18));
        buyerSectionLabel.setForeground(new Color(50, 50, 50));
        formCard.add(buyerSectionLabel, gbc);
        row++;
        
        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 8, 8, 8);
        
        // Buyer Name
        gbc.gridx = 0; gbc.gridy = row;
        formCard.add(new JLabel("Buyer Name:"), gbc);
        gbc.gridx = 1;
        formCard.add(buyerNameField, gbc);
        row++;
        
        // Buyer Contact
        gbc.gridx = 0; gbc.gridy = row;
        formCard.add(new JLabel("Contact Phone:"), gbc);
        gbc.gridx = 1;
        formCard.add(buyerContactField, gbc);
        row++;
        
        // Buyer Email
        gbc.gridx = 0; gbc.gridy = row;
        formCard.add(new JLabel("Email Address:"), gbc);
        gbc.gridx = 1;
        formCard.add(buyerEmailField, gbc);
        row++;
        
        // Sale Information Section
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 0, 15, 0);
        JLabel saleSectionLabel = new JLabel("Sale Information");
        saleSectionLabel.setFont(new Font("Arial", Font.BOLD, 18));
        saleSectionLabel.setForeground(new Color(50, 50, 50));
        formCard.add(saleSectionLabel, gbc);
        row++;
        
        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 8, 8, 8);
        
        // Sale Price
        gbc.gridx = 0; gbc.gridy = row;
        formCard.add(new JLabel("Sale Price:"), gbc);
        gbc.gridx = 1;
        formCard.add(salePriceSpinner, gbc);
        row++;
        
        // Sale Date
        gbc.gridx = 0; gbc.gridy = row;
        formCard.add(new JLabel("Sale Date:"), gbc);
        gbc.gridx = 1;
        formCard.add(saleDateSpinner, gbc);
        row++;
        
        // Payment Method
        gbc.gridx = 0; gbc.gridy = row;
        formCard.add(new JLabel("Payment Method:"), gbc);
        gbc.gridx = 1;
        formCard.add(paymentMethodCombo, gbc);
        row++;
        
        // Tax Amount
        gbc.gridx = 0; gbc.gridy = row;
        formCard.add(new JLabel("Tax Amount (8%):"), gbc);
        gbc.gridx = 1;
        formCard.add(taxAmountLabel, gbc);
        row++;
        
        // Total Amount
        gbc.gridx = 0; gbc.gridy = row;
        JLabel totalLabel = new JLabel("Total Amount:");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formCard.add(totalLabel, gbc);
        gbc.gridx = 1;
        formCard.add(totalAmountLabel, gbc);
        row++;
        
        // Notes
        gbc.gridx = 0; gbc.gridy = row;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formCard.add(new JLabel("Notes:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        JScrollPane notesScrollPane = new JScrollPane(notesArea);
        notesScrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        formCard.add(notesScrollPane, gbc);
        
        return formCard;
    }
    
    /**
     * Create buttons panel
     */
    private JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonsPanel.setOpaque(false);
        
        // Cancel button
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(108, 117, 125));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Complete Sale button
        JButton completeSaleButton = new JButton("Complete Sale");
        completeSaleButton.setBackground(PRIMARY_GREEN);
        completeSaleButton.setForeground(Color.WHITE);
        completeSaleButton.setFocusPainted(false);
        completeSaleButton.setFont(new Font("Arial", Font.BOLD, 14));
        completeSaleButton.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        completeSaleButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        buttonsPanel.add(cancelButton);
        buttonsPanel.add(completeSaleButton);
        
        return buttonsPanel;
    }
    
    /**
     * Add event listeners
     */
    private void addEventListeners() {
        // Update calculations when price changes
        salePriceSpinner.addChangeListener(e -> updateCalculations());
        
        // Cancel button
        JPanel mainPanel = (JPanel) getContentPane().getComponent(0);
        JPanel buttonsPanel = (JPanel) mainPanel.getComponent(4); // 0:header, 1:strut, 2:form, 3:strut, 4:buttons
        JButton cancelButton = (JButton) buttonsPanel.getComponent(0);
        JButton completeSaleButton = (JButton) buttonsPanel.getComponent(1);

        cancelButton.addActionListener(e -> {
            dispose();
        });

        completeSaleButton.addActionListener(e -> {
            completeSale();
        });
    }
    
    /**
     * Update tax and total calculations
     */
    private void updateCalculations() {
        int salePrice = (int) salePriceSpinner.getValue();
        double taxRate = 0.08; // 8% tax
        double taxAmount = salePrice * taxRate;
        double totalAmount = salePrice + taxAmount;
        
        DecimalFormat formatter = new DecimalFormat("$#,##0.00");
        taxAmountLabel.setText(formatter.format(taxAmount));
        totalAmountLabel.setText(formatter.format(totalAmount));
    }
    
    /**
     * Complete the sale
     */
    private void completeSale() {
        // Validate required fields
        if (buyerNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the buyer's name.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            buyerNameField.requestFocus();
            return;
        }
        
        if (buyerContactField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the buyer's contact information.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            buyerContactField.requestFocus();
            return;
        }
        
        // Confirm sale
        int result = JOptionPane.showConfirmDialog(this,
            "Complete the sale of this vehicle?\n\n" +
            "Vehicle: " + car.getModel() + " " + car.getYear() + "\n" +
            "Buyer: " + buyerNameField.getText() + "\n" +
            "Sale Price: " + totalAmountLabel.getText() + "\n\n" +
            "This action cannot be undone.",
            "Confirm Sale",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (result == JOptionPane.YES_OPTION) {
            // Process the sale
            CarStatusManager carManager = CarStatusManager.getInstance();
            
            String salePrice = new DecimalFormat("$#,##0").format((int) salePriceSpinner.getValue());
            boolean success = carManager.sellCar(
                car.getId(),
                buyerNameField.getText().trim(),
                buyerContactField.getText().trim(),
                salePrice,
                (String) paymentMethodCombo.getSelectedItem()
            );
            
            if (success) {
                saleCompleted = true;
                
                JOptionPane.showMessageDialog(this,
                    "Sale completed successfully!\n\n" +
                    "The vehicle has been moved to the Sold Cars section.",
                    "Sale Complete",
                    JOptionPane.INFORMATION_MESSAGE);
                
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Error completing sale. Please try again.",
                    "Sale Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Check if sale was completed
     */
    public boolean isSaleCompleted() {
        return saleCompleted;
    }
    
    /**
     * Test the dialog
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // Create a sample car for testing
            CarManagement.Car sampleCar = new CarManagement.Car(
                1, "Toyota Camry", "2023", "Sedan", "Silver", "$28,500", "Available", "May 15, 2025", "placeholder_toyota.jpg"
            );
            
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            
            SellCarDialog dialog = new SellCarDialog(frame, sampleCar);
            dialog.setVisible(true);
        });
    }
}