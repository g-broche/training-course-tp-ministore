package com.gbroche.view.components.shared;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

/**
 * Abstract class intended to be herited by all view creating component in order
 * to maintain
 * consistency between views
 */
public abstract class ViewPanel extends JPanel {

    private String title;
    private final JLabel titleLabel;
    private final JPanel contentPanel;

    public ViewPanel(String title) {
        setLayout(new BorderLayout());

        // title of the view
        this.title = title;
        titleLabel = new JLabel(this.title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Dynamic content
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(titleLabel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        System.out.println("generated view component with title : " + title);
    }

    protected String getTitle() {
        return title;
    }

    protected void setTitle(String newTitle) {
        this.title = newTitle;
        this.titleLabel.setText(this.title);
    }

    /**
     * Change the dynamic content to display the given JPanel
     * 
     * @param contentWrapper
     */
    protected void changeContent(JPanel contentWrapper) {
        this.contentPanel.removeAll();
        this.contentPanel.add(contentWrapper);
    }

    /**
     * Change the dynamic content to display the given JScrollPane
     * 
     * @param contentWrapper
     */
    protected void changeContent(JScrollPane contentWrapper) {
        this.contentPanel.removeAll();
        this.contentPanel.add(contentWrapper);
    }

    /**
     * Method to override by child classes to build the component content
     */
    protected abstract void buildContent();
}
