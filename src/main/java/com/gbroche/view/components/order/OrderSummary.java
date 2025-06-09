package com.gbroche.view.components.order;

import javax.swing.JPanel;

import com.gbroche.model.Order;

public class OrderSummary extends JPanel {
    private final Order order;

    OrderSummary(Order order) {
        this.order = order;
    }
}
