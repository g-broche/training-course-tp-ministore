package com.gbroche.view.components.product;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.gbroche.dao.ProductDao;
import com.gbroche.model.Product;
import com.gbroche.view.components.shared.ViewPanel;

/**
 * Visual component allowing for reviewing the products below a specified
 * threshold
 */
public class SurveyStocks extends ViewPanel {
    private List<Product> products = new ArrayList<>();
    private Integer threshold = 10;
    private JTextField thresholdInput;
    private JButton submitButton;
    private JPanel controlPanel;
    private JPanel wrapperPanel;
    private JScrollPane scrollPane;
    private JTable productTable;

    public SurveyStocks() {
        super("Survey stocks");
        products = ProductDao.getInstance().getAllProductsBelowStockThreshold(threshold);
        buildContent();
    }

    /**
     * creates visual component
     */
    @Override
    protected void buildContent() {
        wrapperPanel = new JPanel();
        wrapperPanel.setLayout(new BoxLayout(wrapperPanel, BoxLayout.Y_AXIS));
        controlPanel = createControls();
        productTable = createTable(products);
        productTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        scrollPane = new JScrollPane(productTable);
        wrapperPanel.setLayout(new BorderLayout());
        wrapperPanel.add(controlPanel, BorderLayout.NORTH);
        wrapperPanel.add(scrollPane, BorderLayout.CENTER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        wrapperPanel.add(scrollPane);
        changeContent(wrapperPanel);
    }

    /**
     * Updates table of products based on default or inputed threshold.
     */
    public void updateView() {
        if (!isThresholdInputValid()) {
            return;
        }
        threshold = Integer.parseInt(thresholdInput.getText());
        products = ProductDao.getInstance().getAllProductsBelowStockThreshold(threshold);
        ProductTableModel model = (ProductTableModel) productTable.getModel();
        model.updateWithData(products);
    }

    /**
     * Creates the JPanel containing the various user controls
     * 
     * @return JPanel containing user inputs
     */
    private JPanel createControls() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel thresholdLabel = new JLabel("Enter threshold");

        thresholdInput = new JTextField(threshold.toString(), 5);

        panel.add(thresholdLabel);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(thresholdInput);
        panel.add(Box.createHorizontalStrut(10));

        submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> updateView());
        panel.add(submitButton);

        return panel;
    }

    /**
     * Creates the table listing product informations
     * 
     * @param products list of products
     * @return created table
     */
    private JTable createTable(List<Product> products) {
        ProductTableModel model = new ProductTableModel(products);
        JTable table = new JTable(model);
        return table;
    }

    /**
     * Validate if the inputed threshold is a valid value
     * 
     * @return
     */
    private boolean isThresholdInputValid() {
        try {
            String valueInputed = thresholdInput.getText();
            Integer intValue = Integer.parseInt(valueInputed);
            return intValue >= 0;
        } catch (NumberFormatException e) {
            thresholdInput.setText(threshold.toString());
            return false;
        }
    }
}
