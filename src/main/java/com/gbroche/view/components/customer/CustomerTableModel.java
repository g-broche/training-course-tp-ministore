package com.gbroche.view.components.customer;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.gbroche.model.Customer;

/**
 * Model used to display a table of customers
 */
public class CustomerTableModel extends AbstractTableModel {

    private List<Customer> customers;
    private final String[] columnNames = { "ID", "Full Name", "Country", "State", "City" };

    public CustomerTableModel(List<Customer> customers) {
        this.customers = customers;
    }

    @Override
    public int getRowCount() {
        return customers.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Customer c = customers.get(rowIndex);
        return switch (columnIndex) {
            case 0 ->
                c.getId();
            case 1 ->
                c.getFullName();
            case 2 ->
                c.getCountry();
            case 3 ->
                c.getState();
            case 4 ->
                c.getCity();
            default ->
                null;
        };
    }

    /**
     * refreshes table to display new list of customers
     * 
     * @param newCustomers list of customers
     */
    public void updateWithData(List<Customer> newCustomers) {
        this.customers = newCustomers;
        fireTableDataChanged();
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}
