package com.gbroche.view.components.order;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.gbroche.dao.CustomerDao;
import com.gbroche.dao.ProductDao;
import com.gbroche.model.Customer;
import com.gbroche.model.OrderLine;
import com.gbroche.model.Product;
import com.gbroche.view.components.product.ProductOrderTableModel;
import com.gbroche.view.components.product.ProductTableModel;
import com.gbroche.view.components.shared.ViewPanel;

public class OrderPlacement extends ViewPanel {

    private List<Customer> customers;
    private List<Product> products = new ArrayList<>();
    private JPanel wrapperPanel;
    private JPanel controlPanel;
    private JScrollPane scrollPane;
    private ProductOrderTableModel tableModel;
    private JTable productTable;
    private JComboBox<String> customerSelector;
    private JButton orderButton;
    private Double tax = 21.0;

    public OrderPlacement() {
        super("Place order");
        customers = CustomerDao.getInstance().getCustomers();
        products = ProductDao.getInstance().getAllProducts();
        buildContent();
    }

    @Override
    protected void buildContent() {
        wrapperPanel = new JPanel();
        wrapperPanel.setLayout(new BoxLayout(wrapperPanel, BoxLayout.Y_AXIS));
        controlPanel = createControls();
        productTable = createTable(products);
        productTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        scrollPane = new JScrollPane(productTable);
        wrapperPanel.add(controlPanel);
        wrapperPanel.add(Box.createVerticalStrut(20));
        wrapperPanel.add(scrollPane);
        changeContent(wrapperPanel);
    }

    private JPanel createControls() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        customerSelector = new JComboBox<>(extractCustomerNames(customers));
        customerSelector.setBackground(Color.white);
        orderButton = new JButton("Place order");
        orderButton.addActionListener(e -> placeOrder());

        Dimension comboPref = customerSelector.getPreferredSize();
        customerSelector.setMaximumSize(new Dimension(200, comboPref.height)); // width is up to you

        Dimension buttonPref = orderButton.getPreferredSize();
        orderButton.setMaximumSize(new Dimension(buttonPref.width, buttonPref.height));

        customerSelector.setAlignmentY(Component.CENTER_ALIGNMENT);
        orderButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        panel.add(customerSelector);
        panel.add(Box.createHorizontalGlue());
        panel.add(orderButton);
        return panel;
    }

    public void updateView() {
        products = ProductDao.getInstance().getAllProducts();
        ProductTableModel model = (ProductTableModel) productTable.getModel();
        model.updateWithData(products);
    }

    private JTable createTable(List<Product> products) {
        ProductOrderTableModel tableModel = new ProductOrderTableModel(products);
        JTable table = new JTable(tableModel);
        return table;
    }

    private String[] extractCustomerNames(List<Customer> customers) {
        return customers.stream()
                .map(Customer::getFullName)
                .toArray(String[]::new);
    }

    private void placeOrder() {
        try {
            List<OrderLine> rawOrderLines = tableModel.getOrderLines();
            double netTotal = calculateNetTotal(rawOrderLines);
            double totalWithTax = new BigDecimal(netTotal + netTotal * tax)
                    .setScale(2, RoundingMode.FLOOR)
                    .doubleValue();
            int customerId = getSelectedCustomerId();
            /**
             * TO DO
             * Open connection and begin transaction
             * Create new order and retrieve order id
             * Add order lines using retrieved order id
             * Commit transaction
             */
        } catch (Exception e) {

        }

    }

    private double calculateNetTotal(List<OrderLine> orderLines) throws Exception{
        double netTotal = 0;
        for (OrderLine orderLine : orderLines) {
            String productTitle = orderLine.getProduct().getTitle();
            Double unitPrice = orderLine.getProduct().getPrice();
            Integer quantity = orderLine.getQuantity();
            if(unitPrice == null || unitPrice <= 0){
                throw new Exception("Invalid unit price for item '"+productTitle+"'.");
            }
            if(quantity < 1){
                throw new Exception("Invalid quantity for item '"+productTitle+"'.");
            }
            Double subTotal = unitPrice * quantity;
            netTotal += subTotal;
            System.out.println(">>> adding line for: "+productTitle+" ; u/p: "+unitPrice+" ; quantity: "+quantity+" ; line total = "+subTotal);
        }
        return new BigDecimal(netTotal)
                    .setScale(2, RoundingMode.FLOOR)
                    .doubleValue();
    }

    private int getSelectedCustomerId(){
        int indexSelected = customerSelector.getSelectedIndex();
        return customers.get(indexSelected).getId();
    }
}
