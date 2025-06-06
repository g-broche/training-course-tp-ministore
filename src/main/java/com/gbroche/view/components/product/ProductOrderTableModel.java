package com.gbroche.view.components.product;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.gbroche.model.OrderLine;
import com.gbroche.model.Product;

public class ProductOrderTableModel extends AbstractTableModel {

    private final List<Product> products;
    private final List<Integer> quantities;

    private final String[] columnNames = {"Id", "Title", "Category", "Stock", "Price", "Order amount"};

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

    @Override
    public boolean isCellEditable(int row, int col) {
        return col == 5;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        if (col == 5) {
            try {
                int quantity = Integer.parseInt(value.toString());
                quantities.set(row, Math.max(quantity, 0)); // no negatives
                fireTableCellUpdated(row, col);
            } catch (NumberFormatException ignored) {
                quantities.set(row, 0);
            }
        }
    }

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
