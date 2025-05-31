package com.gbroche.view.components.shared.form.groups;

import java.awt.Color;
import java.awt.Font;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.gbroche.view.components.shared.form.validators.Validator;

public class FormGroup extends JPanel {

    private final JLabel label;
    private final FormInput inputInstance;
    private final JComponent inputComponent;
    private final JLabel errorLabel;
    private final Set<Validator> validators = new HashSet<>();

    public FormGroup(String labelText, FormInput input) {
        this.inputInstance = input.getInputInstance();
        this.inputComponent = input.getComponent();
        label = new JLabel(labelText);
        this.errorLabel = new JLabel();
        this.errorLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
        this.errorLabel.setForeground(Color.RED);
    }

    public JLabel getLabel() {
        return label;
    }

    public JLabel getErrorLabel() {
        return errorLabel;
    }

    public FormInput getInputInstance() {
        return inputInstance;
    }

    public JComponent getInputComponent() {
        return inputComponent;
    }

    public void addValidators(Validator[] requiredValidators) {
        for (Validator validator : requiredValidators) {
            validators.add(validator);
        }
    }

    public void addValidator(Validator validator) {
        validators.add(validator);
    }

    public boolean validateInput() {
        clearError();
        for (Validator v : validators) {
            if (!v.isValid(getValue())) {
                setError(v.getErrorMessage());
                return false;
            }
        }
        return true;
    }

    public String getValue() {
        return inputInstance.getValue();
    }

    public void setValue(String value) {
        inputInstance.setValue(value);
    }

    public void setError(String errorMessage) {
        errorLabel.setText(errorMessage);
    }

    public void clearError() {
        errorLabel.setText("");
    }

}
