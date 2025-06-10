package com.gbroche.view.components.shared.form.groups;

import java.awt.event.ActionListener;

import javax.swing.JTextField;

/**
 * Class used to manage and valide text field inputs.
 */
public class TextFieldInput implements FormInput {

    private final JTextField field;

    public TextFieldInput() {
        field = new JTextField();
    }

    @Override
    public FormInput getInputInstance() {
        return this;
    }

    /**
     * gives direct access to this class JTextField
     */
    @Override
    public JTextField getComponent() {
        return field;
    }

    @Override
    public String getValue() {
        return field.getText().trim();
    }

    @Override
    public void setValue(String value) {
        field.setText(value);
    }

    /**
     * Provides access to the related text field to add listener
     */
    @Override
    public void addListener(ActionListener listener) {
        field.addActionListener(listener);
    }
}
