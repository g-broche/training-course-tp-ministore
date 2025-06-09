package com.gbroche.view.components.order;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.gbroche.dao.CustomerDao;
import com.gbroche.dao.OrderDao;
import com.gbroche.dao.ProductDao;
import com.gbroche.model.Customer;
import com.gbroche.model.OrderLine;
import com.gbroche.model.Product;
import com.gbroche.service.DatabaseService;
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
        // ProductOrderTableModel model = (ProductOrderTableModel)
        // productTable.getModel();
        tableModel.updateWithData(products);
    }

    private JTable createTable(List<Product> products) {
        tableModel = new ProductOrderTableModel(products);
        JTable table = new JTable(tableModel);
        return table;
    }

    private String[] extractCustomerNames(List<Customer> customers) {
        return customers.stream()
                .map(Customer::getFullName)
                .toArray(String[]::new);
    }

    private void placeOrder() {
        if (productTable.isEditing()) {
            productTable.getCellEditor().stopCellEditing();
        }
        DatabaseService dbService = DatabaseService.getInstance();
        try {
            // preparing derived values required to add new order
            List<OrderLine> rawOrderLines = tableModel.getOrderLines();
            final double totalWithoutTax = calculateNetTotal(rawOrderLines);
            System.out.println(">>> total without taxes: " + totalWithoutTax);
            final double totalWithTax = new BigDecimal(totalWithoutTax + totalWithoutTax * tax)
                    .setScale(2, RoundingMode.FLOOR)
                    .doubleValue();
            System.out.println(">>> total with taxes: " + totalWithoutTax);
            final int customerId = getSelectedCustomerId();
            System.out.println(">>> selected customer id: " + customerId);
            final LocalDate orderDate = LocalDate.now();
            System.out.println(">>> date of order: " + orderDate);

            // creating DB connection with manual handling of transaction
            Connection connection = dbService.getConnection();
            dbService.setAutoCommit(false);

            // creating order first
            OrderDao orderDao = OrderDao.getInstance();
            final Integer orderId = orderDao.createNewOrder(customerId, orderDate, totalWithoutTax, tax,
                    totalWithTax, connection);
            if (orderId == null || orderId <= 0) {
                throw new Exception("Error : Invalid order id returned after creating a new order");
            }
            System.out.println(">>> order created during transaction: " + orderId);

            int lineId = 1;
            // if order was created, for each order line :
            for (OrderLine rawOrderLine : rawOrderLines) {
                final int productId = rawOrderLine.getProduct().getId();
                final int amountOrdered = rawOrderLine.getQuantity();
                // decrease product stock
                boolean isProductStockDecreaseSuccess = ProductDao.getInstance().decreaseStock(productId,
                        amountOrdered, connection);
                if (!isProductStockDecreaseSuccess) {
                    throw new Exception("Error : Failed to reduce stock of product id <" + productId
                            + "> by order line amount " + amountOrdered);
                }
                // add order line to order
                boolean isNewLineSuccess = orderDao.addOrderLineToOrder(lineId, orderId, rawOrderLine, orderDate,
                        connection);
                if (!isNewLineSuccess) {
                    throw new Exception("Error : Failed to add order line for order id <" + orderId
                            + "> involving product id <" + productId + "> and customer id <" + customerId + ">");
                }
                lineId++;
                System.out.println(">>> added line during transaction on order <" + orderId + "> for <" + amountOrdered
                        + "> " + rawOrderLine.getProduct().getTitle() + "> (id: " + productId + ")");
            }
            // if everything is successful commit changes with transaction
            dbService.commit();
            dbService.closeConnection();
            JOptionPane.showMessageDialog(
                    null,
                    "Successfully placed new order with id " + orderId,
                    "Order placed",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            System.err.println("Order was not successfully placed :" + e.getMessage());
            JOptionPane.showMessageDialog(
                    null,
                    "Order was not successfully placed :" + e.getMessage(),
                    "Order failed",
                    JOptionPane.ERROR_MESSAGE);
            // if any error occured and connection is open then rollback the transaction and
            // close connection
            if (dbService.isConnectionOpen()) {
                dbService.rollback();
                dbService.closeConnection();
            }
        }

    }

    private double calculateNetTotal(List<OrderLine> orderLines) throws Exception {
        double netTotal = 0;
        for (OrderLine orderLine : orderLines) {
            String productTitle = orderLine.getProduct().getTitle();
            Double unitPrice = orderLine.getProduct().getPrice();
            Integer quantity = orderLine.getQuantity();
            if (unitPrice == null || unitPrice <= 0) {
                throw new Exception("Invalid unit price for item '" + productTitle + "'.");
            }
            if (quantity < 1) {
                throw new Exception("Invalid quantity for item '" + productTitle + "'.");
            }
            Double subTotal = unitPrice * quantity;
            netTotal += subTotal;
            System.out.println(">>> adding line for: " + productTitle + " ; u/p: " + unitPrice + " ; quantity: "
                    + quantity + " ; line total = " + subTotal);
        }
        return new BigDecimal(netTotal)
                .setScale(2, RoundingMode.FLOOR)
                .doubleValue();
    }

    private int getSelectedCustomerId() {
        int indexSelected = customerSelector.getSelectedIndex();
        return customers.get(indexSelected).getId();
    }
}
