package com.gbroche.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import com.gbroche.view.components.customer.AddCustomerForm;
import com.gbroche.view.components.customer.CustomerIndex;
import com.gbroche.view.components.product.ProductIndex;

public class MainPanel extends JPanel {

    private final JPanel dynamicContent;
    private final Map<String, JPanel> views = new HashMap<>();

    public MainPanel() {
        setLayout(new BorderLayout());
        dynamicContent = new JPanel(new CardLayout());
        add(dynamicContent, BorderLayout.CENTER);
    }

    public void showView(String viewName) {
        System.out.println("Switching to: " + viewName);
        try {
            if (!views.containsKey(viewName)) {
                JPanel newView = generateView(viewName);
                views.put(viewName, newView);
                dynamicContent.add(newView, viewName);
            } else if (viewName.equals("CustomerIndex")) {
                CustomerIndex customerIndexView = (CustomerIndex) views.get("CustomerIndex");
                customerIndexView.updateTable();
            } else if (viewName.equals("ProductIndex")) {
                ProductIndex productIndexView = (ProductIndex) views.get("ProductIndex");
                productIndexView.updateView();
            }

            CardLayout cl = (CardLayout) dynamicContent.getLayout();
            cl.show(dynamicContent, viewName);

            dynamicContent.revalidate();
            dynamicContent.repaint();

        } catch (Exception e) {
            System.err.println("Error generating view: " + viewName);
            e.printStackTrace();
        }
    }

    private JPanel generateView(String viewName) {
        System.out.println("generating view " + viewName);
        return switch (viewName) {
            case "CustomerIndex" ->
                new CustomerIndex();
            case "CustomerAdd" ->
                new AddCustomerForm();
            case "ProductIndex" ->
                new ProductIndex();
            default ->
                throw new IllegalArgumentException("Unknown view: " + viewName);
        };
    }
}
