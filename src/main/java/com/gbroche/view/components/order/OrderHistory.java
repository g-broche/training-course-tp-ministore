package com.gbroche.view.components.order;

import java.util.ArrayList;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.sql.Connection;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;

import com.gbroche.dao.CustomerDao;
import com.gbroche.dao.OrderDao;
import com.gbroche.model.Customer;
import com.gbroche.model.Order;
import com.gbroche.service.DatabaseService;
import com.gbroche.view.components.shared.ViewPanel;

public class OrderHistory extends ViewPanel {

    private List<Customer> customers;
    private List<Order> customerOrders = new ArrayList<>();
    private JComboBox<String> customerSelector;
    private JPanel wrapperPanel;
    private JPanel controlPanel;
    private JScrollPane scrollPane;
    private JPanel orderListWrapper;

    /**
     * Component handling the view of order history for customers
     */
    public OrderHistory() {
        super("Order history");
        customers = CustomerDao.getInstance().getCustomersWithOrderHistory();
        buildContent();
    }

    /**
     * Build initial component content on creation
     */
    @Override
    protected void buildContent() {
        wrapperPanel = new JPanel();
        wrapperPanel.setLayout(new BoxLayout(wrapperPanel, BoxLayout.Y_AXIS));
        controlPanel = createControls();
        orderListWrapper = new JPanel();
        orderListWrapper.setLayout(new BoxLayout(orderListWrapper, BoxLayout.Y_AXIS));
        scrollPane = new JScrollPane(orderListWrapper);
        wrapperPanel.setLayout(new BorderLayout());
        wrapperPanel.add(controlPanel, BorderLayout.NORTH);
        wrapperPanel.add(scrollPane, BorderLayout.CENTER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        changeContent(wrapperPanel);
        displayCustomerOrderHistory();
    }

    /**
     * Creates control with customer selector and button to place order
     * 
     * @return JPanel with the two inputs
     */
    private JPanel createControls() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        customerSelector = new JComboBox<>(extractCustomerNames(customers));
        customerSelector.setBackground(Color.white);
        customerSelector.addActionListener(e -> displayCustomerOrderHistory());
        panel.add(customerSelector);
        return panel;
    }

    /**
     * Extract array of names from a list of Customer
     * 
     * @param customers
     * @return String array
     */
    private String[] extractCustomerNames(List<Customer> customers) {
        return customers.stream()
                .map(Customer::getFullName)
                .toArray(String[]::new);
    }

    private void displayCustomerOrderHistory() {
        try (Connection connection = DatabaseService.getInstance().getConnection()) {
            Customer selectedCustomer = getSelectedCustomer();
            customerOrders = OrderDao.getInstance().getOrderHistoryByViewerId(selectedCustomer.getId(), connection);
            fillHistoryPanel(selectedCustomer, customerOrders);
        } catch (Exception e) {
            throw new RuntimeException("failed to retrieve customer history");
        }
    }

    /**
     * 
     * @return Customer selected in the combo box
     */
    private Customer getSelectedCustomer() {
        int indexSelected = customerSelector.getSelectedIndex();
        return customers.get(indexSelected);
    }

    /**
     * Display data for all orders registered for a customer
     * 
     * @param customer
     * @param orders
     */
    private void fillHistoryPanel(Customer customer, List<Order> orders) {
        // clear previous content
        orderListWrapper.removeAll();
        // display user name as a heading
        JLabel userNameLabel = new JLabel(customer.getFullName());
        userNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        orderListWrapper.add(Box.createVerticalStrut(10));
        orderListWrapper.add(userNameLabel);
        orderListWrapper.add(Box.createVerticalStrut(10));

        // for all orders, create a component displaying each order data
        for (Order order : orders) {
            JPanel orderPanel = createOrderSummary(order);
            orderListWrapper.add(Box.createVerticalStrut(20));
            orderListWrapper.add(orderPanel);
        }
        // refresh UI with new data
        orderListWrapper.revalidate();
        orderListWrapper.repaint();
    }

    private JPanel createOrderSummary(Order order) {
        JPanel orderPanel = new JPanel();
        orderPanel.setLayout(new BoxLayout(orderPanel, BoxLayout.Y_AXIS));

        // Apply both padding (inner) and a colored border (outer)
        Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        Border border = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 3, true);
        orderPanel.setBorder(BorderFactory.createCompoundBorder(border, padding));

        // Date label aligned to the right
        JPanel datePanel = new JPanel();
        datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.X_AXIS));
        datePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel dateLabel = new JLabel("Order Date: " + order.getDate().toString());
        datePanel.add(Box.createHorizontalGlue());
        datePanel.add(dateLabel);
        orderPanel.add(datePanel);
        orderPanel.add(Box.createVerticalStrut(10));

        // Table + header
        OrderSummaryTableModel tableModel = new OrderSummaryTableModel(order);
        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setPreferredScrollableViewportSize(null);

        // Avoiding the standard behavior of wrapping the table inside the scroll pane
        // in order to have a more reliable vertical sizing of the order summary. A side
        // effect of table not being wrapped in a scroll pane is the removal of the
        // table header display which is compensated by adding it manually here
        orderPanel.add(table.getTableHeader());
        orderPanel.add(table);
        orderPanel.add(Box.createVerticalStrut(10));

        // Price summary panel aligned right
        JPanel orderPricePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JLabel labelPriceWithoutTax = new JLabel("Total without taxes: " + order.getNetAmount());
        JLabel labelTax = new JLabel("Tax rate: " + order.getTax());
        JLabel labelPriceWithTax = new JLabel("Total after taxes: " + order.getTotalAmount());

        orderPricePanel.add(labelPriceWithoutTax);
        orderPricePanel.add(labelTax);
        orderPricePanel.add(labelPriceWithTax);
        orderPanel.add(orderPricePanel);

        Dimension preferredSize = orderPanel.getPreferredSize();
        orderPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, preferredSize.height));
        orderPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        return orderPanel;
    }
}
