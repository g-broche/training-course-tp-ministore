package com.gbroche.view.components.product;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.gbroche.model.Product;

/**
 * Table model for displaying products data
 */
public class ProductTableModel extends AbstractTableModel {

    private List<Product> products;
    private final String[] columnNames = { "ID", "Title", "Category", "Actor", "Price", "Stock" };

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
            case 5 ->
                p.getQuantity();
            default ->
                null;
        };
    }

    /**
     * Updates the content of the table
     * 
     * @param newProducts list of products
     */
    public void updateWithData(List<Product> newProducts) {
        this.products = newProducts;
        fireTableDataChanged();
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}
