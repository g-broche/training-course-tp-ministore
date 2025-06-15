package com.gbroche.view.components.shared.form.groups;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.gbroche.view.components.shared.form.validators.Validator;

/**
 * Class used to manage a form group composed of a label for the input, an input
 * herited from FormInput and a label for error display
 */
public class FormGroup extends JPanel {

    private final String fieldName;
    private final JLabel label;
    private final FormInput inputInstance;
    private final JComponent inputComponent;
    private final JLabel errorLabel;
    private final List<Validator> validators = new ArrayList<>();

    public FormGroup(String fieldName, String labelText, FormInput input) {
        this.fieldName = fieldName;
        this.inputInstance = input.getInputInstance();
        this.inputComponent = input.getComponent();
        label = new JLabel(labelText);
        this.errorLabel = new JLabel();
        this.errorLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
        this.errorLabel.setForeground(Color.RED);
    }

    public String getFieldName() {
        return fieldName;
    }

    public JLabel getLabel() {
        return label;
    }

    public JLabel getErrorLabel() {
        return errorLabel;
    }

    /**
     * returns the instance of FormInput that is used for input in this form group
     * 
     * @return FormInput class instance
     */
    public FormInput getInputInstance() {
        return inputInstance;
    }

    /**
     * returns the native Swing component used for this form group input
     * 
     * @return
     */
    public JComponent getInputComponent() {
        return inputComponent;
    }

    /**
     * adds validators to apply to this form group's input on validation
     * 
     * @param requiredValidators array of validators
     */
    public void addValidators(Validator[] requiredValidators) {
        for (Validator validator : requiredValidators) {
            validators.add(validator);
        }
    }

    /**
     * adds a validator to this form group's input on validation
     * 
     * @param validator
     */
    public void addValidator(Validator validator) {
        validators.add(validator);
    }

    /**
     * Apply all validators to validate that the input value conforms
     * 
     * @return true if input is valid, false otherwise
     */
    public boolean validateInput() {
        clearError();
        // System.out.println("***** Testing input +" + label.getText() + " *****");
        for (Validator v : validators) {
            // System.out.println("validator <" + v.getClass().toString() + "> testing value
            // : " + getValue());
            if (!v.isValid(getValue())) {
                setError(v.getErrorMessage());
                return false;
            }
        }
        // System.out.println("***** End test input +" + label.getText() + " *****");
        return true;
    }

    /**
     * gets raw value from the input
     * 
     * @return String value of the input
     */
    public String getValue() {
        return inputInstance.getValue();
    }

    /**
     * sets input value
     * 
     * @param value String value the input must have
     */
    public void setValue(String value) {
        inputInstance.setValue(value);
    }

    /**
     * sets text content for the error label
     * 
     * @param errorMessage error message to display
     */
    public void setError(String errorMessage) {
        errorLabel.setText(errorMessage);
    }

    /**
     * clear error label message
     */
    public void clearError() {
        errorLabel.setText("");
    }

}
