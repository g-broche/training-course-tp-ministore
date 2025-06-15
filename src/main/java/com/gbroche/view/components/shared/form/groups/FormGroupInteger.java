package com.gbroche.view.components.shared.form.groups;

import com.gbroche.view.components.shared.form.validators.IntegerValidator;

/**
 * Class used to manage a form group composed of a label for the input, an input
 * herited from FormInput and a label for error display.
 * This variant is specific to handling inputs intended to return an integer
 * value and comes with a built in IntegerValidator
 */
public class FormGroupInteger extends FormGroup {

    public FormGroupInteger(String fieldName, String labelText, FormInput input) {
        super(fieldName, labelText, input);
        addValidator(new IntegerValidator());
    }

    /**
     * Parse the raw string value of the input into an integer
     * 
     * @return integer value of the input text
     */
    public Integer getValueAsInteger() {
        try {
            int value = Integer.parseInt(getValue());
            return value;
        } catch (NumberFormatException e) {
            return null;
        }

    }
}
