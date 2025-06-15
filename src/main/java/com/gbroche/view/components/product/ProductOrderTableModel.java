package com.gbroche.view.components.product;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.gbroche.model.OrderLine;
import com.gbroche.model.Product;

/**
 * Table model for displaying products data and also inputing a quantity for
 * ordering
 */
public class ProductOrderTableModel extends AbstractTableModel {

    private List<Product> products;
    private List<Integer> quantities;

    private final String[] columnNames = { "Id", "Title", "Category", "Stock", "Price", "Order amount" };

    public ProductOrderTableModel(List<Product> products) {
        this.products = products;
        this.quantities = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            quantities.add(0);
        }
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
    public String getColumnName(int col) {
        return columnNames[col];
    }

    /**
     * Fill table content
     */
    @Override
    public Object getValueAt(int row, int col) {
        Product product = products.get(row);
        return switch (col) {
            case 0 ->
                product.getId();
            case 1 ->
                product.getTitle();
            case 2 ->
                product.getCategory();
            case 3 ->
                product.getQuantity();
            case 4 ->
                product.getPrice();
            case 5 ->
                quantities.get(row);
            default ->
                null;
        };
    }

    /**
     * Set column for quantity to order as editable
     */
    @Override
    public boolean isCellEditable(int row, int col) {
        return col == 5;
    }

    /**
     * Extra constraint and parsing on table editable row
     */
    @Override
    public void setValueAt(Object value, int row, int col) {
        if (col == 5) {
            try {
                int quantity = Integer.parseInt(value.toString());
                quantities.set(row, Math.max(quantity, 0));
                fireTableCellUpdated(row, col);
            } catch (NumberFormatException ignored) {
                quantities.set(row, 0);
            }
        }
    }

    /**
     * Updates table with list of products to display
     * 
     * @param newProducts
     */
    public void updateWithData(List<Product> newProducts) {
        this.products = newProducts;
        this.quantities = new ArrayList<>();
        for (int i = 0; i < newProducts.size(); i++) {
            quantities.add(0);
        }
        fireTableDataChanged();
    }

    /**
     * returns orderlines with products and the corresponding order quantity based
     * on the
     * table rows with a quantity greater than 0
     * 
     * @return List of OrderLine instances
     */
    public List<OrderLine> getOrderLines() {
        List<OrderLine> orderLines = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            int quantity = quantities.get(i);
            if (quantity > 0) {
                orderLines.add(new OrderLine(products.get(i), quantity));
            }
        }
        return orderLines;
    }
}
