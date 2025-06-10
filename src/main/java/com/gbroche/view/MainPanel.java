package com.gbroche.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import com.gbroche.view.components.customer.AddCustomerForm;
import com.gbroche.view.components.customer.CustomerIndex;
import com.gbroche.view.components.order.OrderHistory;
import com.gbroche.view.components.order.OrderPlacement;
import com.gbroche.view.components.product.ProductIndex;
import com.gbroche.view.components.product.SurveyStocks;

public class MainPanel extends JPanel {

    private final JPanel dynamicContent;
    // Used to store already generated views
    private final Map<String, JPanel> views = new HashMap<>();

    public MainPanel() {
        setLayout(new BorderLayout());
        // CardLayout will allow switching between views
        dynamicContent = new JPanel(new CardLayout());
        add(dynamicContent, BorderLayout.CENTER);
    }

    /**
     * Creates a new view for the view selected in the menu and adds it to the view
     * map if the corresponding view was not previously generated.
     * If the view was previously generated, will refresh its content if
     * appropriate.
     * 
     * @param viewName name of the view to call
     */
    public void showView(String viewName) {
        System.out.println("Switching to: " + viewName);
        try {
            // If view wasn't already created, create it and add it to the map and
            // cardlayout panel
            if (!views.containsKey(viewName)) {
                JPanel newView = generateView(viewName);
                views.put(viewName, newView);
                dynamicContent.add(newView, viewName);
                // If view already was generated and it's pertinent then refresh the content
            } else if (viewName.equals("CustomerIndex")) {
                CustomerIndex customerIndexView = (CustomerIndex) views.get("CustomerIndex");
                customerIndexView.updateTable();
            } else if (viewName.equals("ProductIndex")) {
                ProductIndex productIndexView = (ProductIndex) views.get("ProductIndex");
                productIndexView.updateView();
            } else if (viewName.equals("OrderPlacement")) {
                OrderPlacement orderPlacementView = (OrderPlacement) views.get("OrderPlacement");
                orderPlacementView.updateView();
            } else if (viewName.equals("SurveyStocks")) {
                SurveyStocks surveyStocksView = (SurveyStocks) views.get("SurveyStocks");
                surveyStocksView.updateView();
            }

            // Show required view
            CardLayout cl = (CardLayout) dynamicContent.getLayout();
            cl.show(dynamicContent, viewName);

            // refresh display
            dynamicContent.revalidate();
            dynamicContent.repaint();

        } catch (Exception e) {
            System.err.println("Error generating view: " + viewName);
            e.printStackTrace();
        }
    }

    // Instantiate required component view based on view requested
    private JPanel generateView(String viewName) {
        System.out.println("generating view " + viewName);
        return switch (viewName) {
            case "CustomerIndex" ->
                new CustomerIndex();
            case "CustomerAdd" ->
                new AddCustomerForm();
            case "ProductIndex" ->
                new ProductIndex();
            case "OrderPlacement" ->
                new OrderPlacement();
            case "OrderHistory" ->
                new OrderHistory();
            case "SurveyStocks" ->
                new SurveyStocks();
            default ->
                throw new IllegalArgumentException("Unknown view: " + viewName);
        };
    }
}
