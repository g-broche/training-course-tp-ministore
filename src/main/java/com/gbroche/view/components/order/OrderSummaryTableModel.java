package com.gbroche.view.components.order;

import javax.swing.table.AbstractTableModel;

import com.gbroche.model.Order;
import com.gbroche.model.OrderLine;

public class OrderSummaryTableModel extends AbstractTableModel {

    private Order order;

    private final String[] columnNames = { "Line id", "Product id", "Title", "Quantity" };

    public OrderSummaryTableModel(Order order) {
        this.order = order;
    }

    @Override
    public int getRowCount() {
        return order.getLines().size();
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
        OrderLine line = order.getLines().get(row);
        return switch (col) {
            case 0 ->
                line.getId();
            case 1 ->
                line.getProduct().getId();
            case 2 ->
                line.getProduct().getTitle();
            case 3 ->
                line.getQuantity();
            default ->
                null;
        };
    }
}
