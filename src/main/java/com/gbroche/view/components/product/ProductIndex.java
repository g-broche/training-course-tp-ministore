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

    /**
     * View to display all products in a grid with the ability to filter by category
     */
    public ProductIndex() {
        super("Product index");
        categories = generateCategories();
        products = ProductDao.getInstance().getAllProducts();
        buildContent();
    }

    /**
     * Builds content when component is created
     */
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

    /**
     * Updates the dynamic content of the view back to its default state to watch
     * for new categories
     * or products.
     * used when component is loaded back into current view.
     */
    public void updateView() {
        clearCategoryData();
        categories = generateCategories();
        categorySelector.setModel(new DefaultComboBoxModel<>(categories.toArray(new String[0])));
        products = ProductDao.getInstance().getAllProducts();
        ProductTableModel model = (ProductTableModel) productTable.getModel();
        String selectedCategory = categorySelector.getSelectedItem() != null
                ? categorySelector.getSelectedItem().toString()
                : "";
        List<Product> productsToDisplay = filterProductsByCategory(selectedCategory);
        model.updateWithData(productsToDisplay);
    }

    /**
     * Updates table to display products based on the selected category
     */
    public void filterTable() {
        ProductTableModel model = (ProductTableModel) productTable.getModel();
        String selectedCategory = categorySelector.getSelectedItem() != null
                ? categorySelector.getSelectedItem().toString()
                : "";
        List<Product> productsToDisplay = filterProductsByCategory(selectedCategory);
        model.updateWithData(productsToDisplay);
    }

    /**
     * Filter products based on the selected category
     * 
     * @param category name of the category
     * @return List of Products matching the category or all products if no category
     *         selected
     */
    public List<Product> filterProductsByCategory(String category) {
        if (category.equals("All Categories")) {
            return products;
        }
        return products.stream()
                .filter(p -> p.getCategory().equals(category))
                .collect(Collectors.toList());
    }

    /**
     * Creates table and fills it with products
     * 
     * @param products
     * @return
     */
    private JTable createTable(List<Product> products) {
        ProductTableModel model = new ProductTableModel(products);
        JTable table = new JTable(model);
        return table;
    }

    /**
     * Retrieves categories from the database to get a list of category and also a
     * list of category name used to fille the category selector
     * 
     * @return a list of category names to de used for filling the category selector
     */
    private List<String> generateCategories() {
        List<Category> foundCategories = CategoryDao.getInstance().getCategories();
        List<String> categoryNames = foundCategories.stream()
                .map(Category::getName)
                .collect(Collectors.toList());
        categoryNames.add(0, "All Categories");
        return categoryNames;
    }

    private void clearCategoryData() {
        categories.clear();
        categorySelector.removeAll();
    }
}
