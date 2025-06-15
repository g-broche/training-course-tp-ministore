package com.gbroche.view.components.shared.form.groups;

import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

/**
 * Class used to manage and valide ComboBox inputs.
 */
public class ComboBoxInput implements FormInput {

    private final JComboBox<String> comboBox;

    public ComboBoxInput(String[] options) {
        comboBox = new JComboBox<>(options);
    }

    @Override
    public FormInput getInputInstance() {
        return this;
    }

    /**
     * gives direct access to this class ComboBox
     */
    @Override
    public JComboBox<String> getComponent() {
        return comboBox;
    }

    /**
     * Get selected value
     */
    @Override
    public String getValue() {
        Object selected = comboBox.getSelectedItem();
        return selected != null ? selected.toString() : "";
    }

    @Override
    public void setValue(String value) {
        comboBox.setSelectedItem(value);
    }

    /**
     * Provides access to the related ComboBox to add listener
     */
    @Override
    public void addListener(ActionListener listener) {
        comboBox.addActionListener(listener);
    }

    /**
     * Sets the model of the combo box
     * 
     * @param model
     */
    public void setModel(DefaultComboBoxModel<String> model) {
        comboBox.setModel(model);
    }
}
