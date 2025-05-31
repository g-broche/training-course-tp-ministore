package com.gbroche.view.components.shared.form.groups;

import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JTextField;

public class TextFieldInput implements FormInput {

    private JTextField field;

    public TextFieldInput() {
        field = new JTextField();
    }

    @Override
    public FormInput getInputInstance() {
        return this;
    }

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

    @Override
    public void addListener(ActionListener listener) {
        field.addActionListener(listener);
    }
}
