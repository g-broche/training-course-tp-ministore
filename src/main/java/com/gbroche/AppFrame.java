package com.gbroche;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.gbroche.view.MainPanel;
import com.gbroche.view.components.MenuBar;

public class AppFrame extends JFrame {

    private MainPanel mainPanel;
    private final MenuBar menu;

    public AppFrame() {
        setTitle("MiniStore ERP");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        menu = new MenuBar();
        mainPanel = new MainPanel();
        setJMenuBar(menu);
        add(mainPanel, BorderLayout.CENTER);

        // Use listeners defined in MenuBar to generate and display the requested view
        // depending on which menu item was clicked
        menu.setListenerClientIndex(e -> mainPanel.showView("CustomerIndex"));
        menu.setListenerClientAdd(e -> mainPanel.showView("CustomerAdd"));
        menu.setListenerProductIndex(e -> mainPanel.showView("ProductIndex"));
        menu.setListenerOrderPlacement(e -> mainPanel.showView("OrderPlacement"));
        menu.setListenerOrderHistory(e -> mainPanel.showView("OrderHistory"));
        menu.setListenerSurveyStocks(e -> mainPanel.showView("SurveyStocks"));
    }
}
