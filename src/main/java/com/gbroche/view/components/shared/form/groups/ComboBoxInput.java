package com.gbroche.view.components.shared.form.groups;

import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

public class ComboBoxInput implements FormInput {

    private final JComboBox<String> comboBox;

    public ComboBoxInput(String[] options) {
        comboBox = new JComboBox<>(options);
    }

    @Override
    public FormInput getInputInstance() {
        return this;
    }

    @Override
    public JComboBox<String> getComponent() {
        return comboBox;
    }

    @Override
    public String getValue() {
        Object selected = comboBox.getSelectedItem();
        return selected != null ? selected.toString() : "";
    }

    @Override
    public void setValue(String value) {
        comboBox.setSelectedItem(value);
    }

    @Override
    public void addListener(ActionListener listener) {
        comboBox.addActionListener(listener);
    }

    public void setModel(DefaultComboBoxModel<String> model) {
        comboBox.setModel(model);
    }
}
