package com.gbroche.view.components;

import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MenuBar extends JMenuBar {

    private final JMenuItem exit;
    private final JMenuItem clientIndex;
    private final JMenuItem clientAdd;
    private final JMenuItem productIndex;
    private final JMenuItem productStock;
    private final JMenuItem orderPlacement;
    private final JMenuItem orderHistory;

    public MenuBar() {

        JMenu fileMenu = new JMenu("File");
        this.exit = new JMenuItem("Exit");
        this.exit.addActionListener(e -> System.exit(0));
        fileMenu.add(this.exit);

        JMenu clientMenu = new JMenu("Clients");
        this.clientIndex = new JMenuItem("Index");
        this.clientAdd = new JMenuItem("Add");
        clientMenu.add(this.clientIndex);
        clientMenu.add(this.clientAdd);

        JMenu productMenu = new JMenu("Products");
        this.productIndex = new JMenuItem("Index");
        this.productStock = new JMenuItem("Survey Stocks");
        productMenu.add(this.productIndex);
        productMenu.add(this.productStock);

        JMenu orderMenu = new JMenu("Orders");
        this.orderPlacement = new JMenuItem("Place order");
        this.orderHistory = new JMenuItem("Order history");
        orderMenu.add(this.orderPlacement);
        orderMenu.add(this.orderHistory);

        add(fileMenu);
        add(clientMenu);
        add(productMenu);
        add(orderMenu);
    }

    // preparing setters for listeners to trigger the desired action when a menu
    // item is clicked.

    public void setListenerClientIndex(ActionListener listener) {
        this.clientIndex.addActionListener(listener);
    }

    public void setListenerClientAdd(ActionListener listener) {
        this.clientAdd.addActionListener(listener);
    }

    public void setListenerProductIndex(ActionListener listener) {
        this.productIndex.addActionListener(listener);
    }

    public void setListenerOrderPlacement(ActionListener listener) {
        this.orderPlacement.addActionListener(listener);
    }

    public void setListenerOrderHistory(ActionListener listener) {
        this.orderHistory.addActionListener(listener);
    }

    public void setListenerSurveyStocks(ActionListener listener) {
        this.productStock.addActionListener(listener);
    }
}
