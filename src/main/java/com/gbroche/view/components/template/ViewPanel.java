package com.gbroche.view.components.template;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

public abstract class ViewPanel extends JPanel {

    private String title;
    private final JLabel titleLabel;
    private JPanel contentPanel;

    public ViewPanel(String title) {
        setLayout(new BorderLayout());

        // Title section
        this.title = title;
        titleLabel = new JLabel(this.title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Content section
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

    protected void changeContent(JPanel contentWrapper) {
        this.contentPanel.removeAll();
        this.contentPanel.add(contentWrapper);
    }

    protected void changeContent(JScrollPane contentWrapper) {
        this.contentPanel.removeAll();
        this.contentPanel.add(contentWrapper);
    }

    protected abstract void buildContent();
}
