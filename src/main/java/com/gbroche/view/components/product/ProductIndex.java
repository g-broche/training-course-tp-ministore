package com.gbroche.view.components.product;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.gbroche.dao.CategoryDao;
import com.gbroche.dao.ProductDao;
import com.gbroche.model.Category;
import com.gbroche.model.Product;
import com.gbroche.view.components.shared.ViewPanel;

public final class ProductIndex extends ViewPanel {

    private List<String> categories;
    private List<Product> products = new ArrayList<>();
    private JPanel wrapperPanel;
    private JScrollPane scrollPane;
    private JTable productTable;
    private JComboBox<String> categorySelector;

    public ProductIndex() {
        super("Product index");
        categories = generateCategories();
        products = ProductDao.getInstance().getAllProducts();
        buildContent();
    }

    @Override
    protected void buildContent() {
        wrapperPanel = new JPanel();
        wrapperPanel.setLayout(new BoxLayout(wrapperPanel, BoxLayout.Y_AXIS));
        categorySelector = new JComboBox<>(categories.toArray(new String[0]));
        categorySelector.setBackground(Color.white);
        productTable = createTable(products);
        productTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        categorySelector.addActionListener(e -> filterTable());
        wrapperPanel.add(categorySelector);
        scrollPane = new JScrollPane(productTable);
        wrapperPanel.add(scrollPane);
        changeContent(wrapperPanel);
    }

    public void updateView() {
        clearCategoryData();
        categories = generateCategories();
        categorySelector.setModel(new DefaultComboBoxModel<>(categories.toArray(new String[0])));
        products = ProductDao.getInstance().getAllProducts();
        ProductTableModel model = (ProductTableModel) productTable.getModel();
        String selectedCategory
                = categorySelector.getSelectedItem() != null
                ? categorySelector.getSelectedItem().toString()
                : "";
        List<Product> productsToDisplay = filterProductsByCategory(selectedCategory);
        model.updateWithData(productsToDisplay);
    }

    public void filterTable() {
        ProductTableModel model = (ProductTableModel) productTable.getModel();
        String selectedCategory
                = categorySelector.getSelectedItem() != null
                ? categorySelector.getSelectedItem().toString()
                : "";
        List<Product> productsToDisplay = filterProductsByCategory(selectedCategory);
        model.updateWithData(productsToDisplay);
    }

    public List<Product> filterProductsByCategory(String category) {
        if (category.equals("All Categories")) {
            return products;
        }
        return products.stream()
                .filter(p -> p.getCategory().equals(category))
                .collect(Collectors.toList());
    }

    private JTable createTable(List<Product> products) {
        ProductTableModel model = new ProductTableModel(products);
        JTable table = new JTable(model);
        return table;
    }

    private List<String> generateCategories() {
        List<Category> foundCategories = CategoryDao.getInstance().getCategories();
        List<String> categoryNames = foundCategories.stream()
                .map(Category::getName)
                .collect(Collectors.toList());
        for (String category : categoryNames) {
            System.out.println(category);
        }
        categoryNames.add(0, "All Categories");
        return categoryNames;
    }

    private void clearCategoryData() {
        categories.clear();
        categorySelector.removeAll();
    }
}
