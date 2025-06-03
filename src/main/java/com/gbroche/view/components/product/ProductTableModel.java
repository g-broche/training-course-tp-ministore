package com.gbroche.view.components.product;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.gbroche.model.Product;

public class ProductTableModel extends AbstractTableModel {

    private List<Product> products;
    private final String[] columnNames = {"ID", "Title", "Category", "Actor", "price"};

    public ProductTableModel(List<Product> products) {
        this.products = products;
    }

    @Override
    public int getRowCount() {
        return products.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Product p = products.get(rowIndex);
        return switch (columnIndex) {
            case 0 ->
                p.getId();
            case 1 ->
                p.getTitle();
            case 2 ->
                p.getCategory();
            case 3 ->
                p.getActor();
            case 4 ->
                p.getPrice();
            default ->
                null;
        };
    }

    public void updateWithData(List<Product> newproducts) {
        this.products = newproducts;
        fireTableDataChanged(); // Notifies the JTable to refresh its view
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}
