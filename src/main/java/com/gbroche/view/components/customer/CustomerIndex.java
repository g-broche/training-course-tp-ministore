package com.gbroche.view.components.customer;

import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.gbroche.dao.CustomerDao;
import com.gbroche.model.Customer;
import com.gbroche.view.components.shared.ViewPanel;

/**
 * Component used to display a view listing all customers
 */
public final class CustomerIndex extends ViewPanel {

    private List<Customer> customers;
    private final CustomerDao customerDao;
    private JScrollPane scrollPane;
    private JTable customerTable;

    public CustomerIndex() {
        super("Customer index");
        customerDao = CustomerDao.getInstance();
        buildContent();
    }

    /**
     * Building component's content on creation
     */
    @Override
    protected void buildContent() {
        customers = customerDao.getCustomers();
        customerTable = createTable(customers);
        scrollPane = new JScrollPane(customerTable);
        changeContent(scrollPane);
    }

    /**
     * Updates table of customers with all currently existing customers
     */
    public void updateTable() {
        CustomerTableModel model = (CustomerTableModel) customerTable.getModel();
        customers = customerDao.getCustomers();
        model.updateWithData(customers);
    }

    /**
     * Creates table listing all customers
     * 
     * @param customers list of customers
     * @return created JTable
     */
    private JTable createTable(List<Customer> customers) {
        CustomerTableModel model = new CustomerTableModel(customers);
        JTable table = new JTable(model);
        return table;
    }

}
