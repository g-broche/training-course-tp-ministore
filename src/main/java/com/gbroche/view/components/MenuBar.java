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
    private final JMenuItem orderIndex;
    private final JMenuItem resuplyIndex;

    public MenuBar() {
        JMenuBar menuBar = new JMenuBar();

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
        productMenu.add(this.productIndex);

        JMenu orderMenu = new JMenu("Orders");
        this.orderIndex = new JMenuItem("Index");
        orderMenu.add(this.orderIndex);

        JMenu resupplyMenu = new JMenu("Resupply");
        this.resuplyIndex = new JMenuItem("Required");
        resupplyMenu.add(this.resuplyIndex);

        add(fileMenu);
        add(clientMenu);
        add(productMenu);
        add(orderMenu);
        add(resupplyMenu);
    }

    public void setListenerClientIndex(ActionListener listener) {
        this.clientIndex.addActionListener(listener);
    }

    public void setListenerClientAdd(ActionListener listener) {
        this.clientAdd.addActionListener(listener);
    }

    public void setListenerProductIndex(ActionListener listener) {
        this.productIndex.addActionListener(listener);
    }

    public void setListenerOrderIndex(ActionListener listener) {
        this.orderIndex.addActionListener(listener);
    }

    public void setListenerResuplyIndex(ActionListener listener) {
        this.resuplyIndex.addActionListener(listener);
    }
}
